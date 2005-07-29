/*
 * Copyright  1999-2005 The Apache Software Foundation
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import org.apache.avalon.framework.service.ServiceSelector;
import org.apache.lenya.cms.publication.Document;
import org.apache.lenya.cms.publication.DocumentIdentityMap;
import org.apache.lenya.cms.publication.DocumentManager;
import org.apache.lenya.cms.publication.Publication;
import org.apache.lenya.cms.publication.PublicationException;
import org.apache.lenya.cms.publication.util.DocumentVisitor;
import org.apache.lenya.cms.publication.util.DocumentSet;
import org.apache.lenya.cms.site.Node;
import org.apache.lenya.cms.site.NodeFactory;
import org.apache.lenya.cms.site.SiteManager;
import org.apache.lenya.cms.site.SiteUtil;
import org.apache.lenya.cms.site.usecases.AssetUtil;
import org.apache.lenya.cms.usecase.DocumentUsecase;
import org.apache.lenya.cms.usecase.UsecaseException;
import org.apache.lenya.cms.usecase.scheduling.UsecaseScheduler;
import org.apache.lenya.cms.workflow.WorkflowUtil;
import org.apache.lenya.transaction.Transactionable;
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
    protected static final String SCHEDULE = "schedule";
    protected static final String SCHEDULE_TIME = "schedule.time";

    /**
     * @see org.apache.lenya.cms.usecase.AbstractUsecase#initParameters()
     */
    protected void initParameters() {
        super.initParameters();

        Date now = new GregorianCalendar().getTime();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        setParameter(SCHEDULE_TIME, format.format(now));
    }

    /**
     * @see org.apache.lenya.cms.usecase.AbstractUsecase#getObjectsToLock()
     */
    protected Transactionable[] getObjectsToLock() throws UsecaseException {
        try {
            List nodes = new ArrayList();
            DocumentSet set = new DocumentSet();

            Document doc = getSourceDocument();
            set.addAll(SiteUtil.getSubSite(this.manager, doc));
            Map targets = SiteUtil.getTransferedSubSite(this.manager, doc, Publication.LIVE_AREA,
                    SiteUtil.MODE_REPLACE);
            Document[] docs = set.getDocuments();
            for (int i = 0; i < docs.length; i++) {
                nodes.addAll(Arrays.asList(docs[i].getRepositoryNodes()));
                Document target = (Document) targets.get(docs[i]);
                nodes.addAll(Arrays.asList(target.getRepositoryNodes()));
                nodes.addAll(AssetUtil.getCopiedAssetNodes(docs[i], target, this.manager,
                        getLogger()));
            }

            nodes.add(SiteUtil.getSiteStructure(this.manager, getDocumentIdentityMap(),
                    doc.getPublication(), Publication.LIVE_AREA).getRepositoryNode());
            return (Transactionable[]) nodes.toArray(new Transactionable[nodes.size()]);

        } catch (Exception e) {
            throw new UsecaseException(e);
        }
    }

    /**
     * Checks if the workflow event is supported and the parent of the document exists in the live
     * area.
     * 
     * @see org.apache.lenya.cms.usecase.AbstractUsecase#doCheckPreconditions()
     */
    protected void doCheckPreconditions() throws Exception {
        super.doCheckPreconditions();
        if (!hasErrors()) {

            String event = getEvent();
            Document document = getSourceDocument();

            if (!document.getArea().equals(Publication.AUTHORING_AREA)) {
                addErrorMessage("This usecase can only be invoked from the authoring area.");
                return;
            }

            if (!WorkflowUtil.canInvoke(this.manager, getLogger(), getSourceDocument(), event)) {
                setParameter(ALLOW_SINGLE_DOCUMENT, Boolean.toString(false));
                addInfoMessage("The single document cannot be published because the workflow event cannot be invoked.");
            } else {
                setParameter(ALLOW_SINGLE_DOCUMENT, Boolean.toString(true));
            }

            Publication publication = document.getPublication();
            DocumentIdentityMap map = document.getIdentityMap();

            Document liveDocument = map.getAreaVersion(document, Publication.LIVE_AREA);

            List missingDocuments = new ArrayList();

            ServiceSelector selector = null;
            SiteManager siteManager = null;
            try {
                selector = (ServiceSelector) this.manager.lookup(SiteManager.ROLE + "Selector");
                siteManager = (SiteManager) selector.select(publication.getSiteManagerHint());

                Node liveNode = NodeFactory.getNode(liveDocument);
                Node[] requiredNodes = siteManager.getRequiredResources(map, liveNode);

                for (int i = 0; i < requiredNodes.length; i++) {

                    DocumentSet liveDocs = SiteUtil.getExistingDocuments(map, requiredNodes[i]);
                    if (liveDocs.isEmpty()) {
                        Document authoringDoc = map.get(requiredNodes[i].getPublication(),
                                Publication.AUTHORING_AREA, requiredNodes[i].getDocumentId());
                        if (authoringDoc.exists()) {
                            missingDocuments.add(authoringDoc);
                        } else {
                            missingDocuments.add(map.getLanguageVersion(authoringDoc, authoringDoc
                                    .getPublication().getDefaultLanguage()));
                        }
                    }

                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                if (selector != null) {
                    if (siteManager != null) {
                        selector.release(siteManager);
                    }
                    this.manager.release(selector);
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

        boolean schedule = Boolean.valueOf(getBooleanCheckboxParameter(SCHEDULE)).booleanValue();
        if (schedule) {
            deleteParameter(SCHEDULE);
            String dateString = getParameterAsString(SCHEDULE_TIME);
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            UsecaseScheduler scheduler = null;
            try {
                Date date = null;
                try {
                    date = format.parse(dateString);
                } catch (ParseException e) {
                    addErrorMessage("The scheduler date must be of the form 'yyyy-MM-dd hh:mm:ss'.");
                }
                if (date != null) {
                    scheduler = (UsecaseScheduler) this.manager.lookup(UsecaseScheduler.ROLE);
                    scheduler.schedule(this, date);
                }
            } finally {
                if (scheduler != null) {
                    this.manager.release(scheduler);
                }
            }
        } else {
            super.doExecute();
            if (isSubtreeEnabled()) {
                publishAll(getSourceDocument());
            } else {
                publish(getSourceDocument());
            }
        }
    }

    /**
     * Publishes a document.
     * 
     * @param authoringDocument The authoring document.
     */
    protected void publish(Document authoringDocument) {

        DocumentManager documentManager = null;
        try {
            documentManager = (DocumentManager) this.manager.lookup(DocumentManager.ROLE);
            documentManager.copyToArea(authoringDocument, Publication.LIVE_AREA);
            WorkflowUtil.invoke(this.manager, getLogger(), authoringDocument, getEvent());
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (documentManager != null) {
                this.manager.release(documentManager);
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
     * Publishes a document or the subtree below a document, based on the parameter SUBTREE.
     * 
     * @param document The document.
     */
    protected void publishAll(Document document) {

        if (getLogger().isDebugEnabled()) {
            getLogger().debug("Publishing document [" + document + "]");
            getLogger().debug("Subtree publishing: [" + isSubtreeEnabled() + "]");
        }

        try {
            DocumentSet set = SiteUtil.getSubSite(this.manager, document);
            SiteUtil.sortAscending(this.manager, set);
            set.visit(this);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (getLogger().isDebugEnabled()) {
            getLogger().debug("Publishing completed.");
        }
    }

    /**
     * Returns whether subtree publishing is enabled.
     * 
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
            Document parent = document.getIdentityMap().getParent(document);
            boolean publish = true;
            if (parent != null) {
                Document liveParent = parent.getIdentityMap().getAreaVersion(parent,
                        Publication.LIVE_AREA);
                if (!liveParent.exists()) {
                    publish = false;
                }
            }
            if (publish) {
                publishAllLanguageVersions(document);
            }
        } catch (WorkflowException e) {
            throw new PublicationException(e);
        }
    }

    /**
     * Publishes all existing language versions of a document.
     * 
     * @param document The document.
     * @throws PublicationException if an error occurs.
     * @throws WorkflowException
     */
    protected void publishAllLanguageVersions(Document document) throws PublicationException,
            WorkflowException {
        String[] languages = document.getPublication().getLanguages();

        try {
            for (int i = 0; i < languages.length; i++) {
                Document version = document.getIdentityMap().getLanguageVersion(document,
                        languages[i]);
                if (version.exists()) {
                    if (WorkflowUtil.canInvoke(this.manager, getLogger(), version, getEvent())) {
                        publish(version);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}