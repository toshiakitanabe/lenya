/*
 * Copyright  1999-2004 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package org.apache.lenya.defaultpub.cms.usecases;

import java.util.ArrayList;
import java.util.List;

import org.apache.lenya.cms.publication.Document;
import org.apache.lenya.cms.publication.DocumentFactory;
import org.apache.lenya.cms.publication.Publication;
import org.apache.lenya.cms.publication.PublicationException;
import org.apache.lenya.cms.publication.util.DocumentVisitor;
import org.apache.lenya.cms.publication.util.OrderedDocumentSet;
import org.apache.lenya.cms.site.SiteManager;
import org.apache.lenya.cms.usecase.DocumentUsecase;
import org.apache.lenya.cms.workflow.WorkflowManager;
import org.apache.lenya.workflow.WorkflowException;

/**
 * Publish usecase handler.
 * 
 * @version $Id$
 */
public class Publish extends DocumentUsecase implements DocumentVisitor {

    protected static final String MISSING_DOCUMENTS = "missingDocuments";
    protected static final String SUBTREE = "subtree";
    protected static final String ALLOW_SINGLE_DOCUMENT = "allowSingleDocument";

    /**
     * Checks if the workflow event is supported and the parent of the document
     * exists in the live area.
     * 
     * @see org.apache.lenya.cms.usecase.AbstractUsecase#doCheckPreconditions()
     */
    protected void doCheckPreconditions() throws Exception {
        super.doCheckPreconditions();
        if (getErrorMessages().isEmpty()) {

            String event = getEvent();
            Document document = getSourceDocument();

            if (!document.getArea().equals(Publication.AUTHORING_AREA)) {
                addErrorMessage("This usecase can only be invoked from the authoring area.");
                return;
            }

            WorkflowManager wfManager = null;
            try {
                wfManager = (WorkflowManager) this.manager.lookup(WorkflowManager.ROLE);
                if (!wfManager.canInvoke(getSourceDocument(), event)) {
                    setParameter(ALLOW_SINGLE_DOCUMENT, Boolean.toString(false));
                    addInfoMessage("The single document cannot be published because the workflow event cannot be invoked.");
                } else {
                    setParameter(ALLOW_SINGLE_DOCUMENT, Boolean.toString(true));
                }
            } finally {
                if (wfManager != null) {
                    this.manager.release(wfManager);
                }
            }

            Publication publication = document.getPublication();

            Document liveDocument = publication.getAreaVersion(document, Publication.LIVE_AREA);

            List missingDocuments = new ArrayList();

            SiteManager manager = publication.getSiteManager(document.getIdentityMap());
            Document[] requiredDocuments = manager.getRequiredResources(liveDocument);
            for (int i = 0; i < requiredDocuments.length; i++) {
                if (!manager.containsInAnyLanguage(requiredDocuments[i])) {
                    Document authoringVersion = publication.getAreaVersion(requiredDocuments[i],
                            Publication.AUTHORING_AREA);
                    missingDocuments.add(authoringVersion);
                }
            }

            if (!missingDocuments.isEmpty()) {
                addErrorMessage("Cannot publish document unless the following documents are published:");
                setParameter(MISSING_DOCUMENTS, missingDocuments);
            }
        }
    }

    /**
     * @see org.apache.lenya.cms.usecase.AbstractUsecase#doExecute()
     */
    protected void doExecute() throws Exception {
        super.doExecute();
        if (isSubtreeEnabled()) {
            publishAll(getSourceDocument());
        } else {
            publish(getSourceDocument());
        }
    }

    /**
     * Publishes a document.
     * @param authoringDocument The authoring document.
     */
    protected void publish(Document authoringDocument) {

        WorkflowManager wfManager = null;
        try {
            wfManager = (WorkflowManager) this.manager.lookup(WorkflowManager.ROLE);
            getDocumentManager().copyDocumentToArea(authoringDocument, Publication.LIVE_AREA);
            wfManager.invoke(authoringDocument, getEvent());
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (wfManager != null) {
                this.manager.release(wfManager);
            }
        }
    }

    /**
     * @return The event to invoke.
     */
    private String getEvent() {
        return "publish";
    }

    /**
     * Publishes a document or the subtree below a document, based on the
     * parameter SUBTREE.
     * @param document The document.
     */
    protected void publishAll(Document document) {

        if (getLogger().isDebugEnabled()) {
            getLogger().debug("Publishing document [" + document + "]");
            getLogger().debug("Subtree publishing: [" + isSubtreeEnabled() + "]");
        }

        try {

            OrderedDocumentSet set = new OrderedDocumentSet();
            SiteManager manager = document.getPublication().getSiteManager(document
                    .getIdentityMap());
            Document[] descendants = manager.getRequiringResources(document);

            set = new OrderedDocumentSet(descendants);
            set.add(document);
            set.visitAscending(this);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (getLogger().isDebugEnabled()) {
            getLogger().debug("Publishing completed.");
        }
    }

    /**
     * Returns whether subtree publishing is enabled.
     * @return A boolean value.
     */
    protected boolean isSubtreeEnabled() {
        String value = getParameterAsString(SUBTREE);
        return value != null;
    }

    /**
     * @throws PublicationException
     * @see org.apache.lenya.cms.publication.util.DocumentVisitor#visitDocument(org.apache.lenya.cms.publication.Document)
     */
    public void visitDocument(Document document) throws PublicationException {

        if (getLogger().isDebugEnabled()) {
            getLogger().debug("Visiting resource [" + document + "]");
        }

        try {
            publishAllLanguageVersions(document);
        } catch (WorkflowException e) {
            throw new PublicationException(e);
        }
    }

    /**
     * Publishes all existing language versions of a document.
     * @param document The document.
     * @throws PublicationException if an error occurs.
     * @throws WorkflowException
     */
    protected void publishAllLanguageVersions(Document document) throws PublicationException,
            WorkflowException {
        String[] languages = document.getPublication().getLanguages();
        DocumentFactory factory = document.getIdentityMap().getFactory();

        WorkflowManager wfManager = null;
        try {
            wfManager = (WorkflowManager) this.manager.lookup(WorkflowManager.ROLE);
            for (int i = 0; i < languages.length; i++) {
                Document version = factory.getLanguageVersion(document, languages[i]);
                if (version.exists()) {
                    if (wfManager.canInvoke(version, getEvent())) {
                        publish(version);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (wfManager != null) {
                this.manager.release(wfManager);
            }
        }

    }

}