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
package org.apache.lenya.cms.site.usecases;

import org.apache.avalon.framework.service.ServiceException;
import org.apache.lenya.cms.publication.Document;
import org.apache.lenya.cms.publication.DocumentBuildException;
import org.apache.lenya.cms.publication.DocumentException;
import org.apache.lenya.cms.publication.DocumentFactory;
import org.apache.lenya.cms.publication.DocumentIdentityMap;
import org.apache.lenya.cms.publication.DocumentManager;
import org.apache.lenya.cms.publication.Publication;
import org.apache.lenya.cms.publication.PublicationException;
import org.apache.lenya.cms.usecase.DocumentUsecase;

/**
 * Change the node ID of a document.
 * 
 * @version $Id: ChangeLabel.java 152682 2005-02-08 18:13:39Z gregor $
 */
public class ChangeNodeID extends DocumentUsecase {

    protected static final String NODE_ID = "nodeId";

    /**
     * @see org.apache.lenya.cms.usecase.AbstractUsecase#initParameters()
     */
    protected void initParameters() {
        super.initParameters();
        Document document = getSourceDocument();
        if (document != null) {
            setParameter(NODE_ID, document.getName());
        }
    }

    /**
     * @see org.apache.lenya.cms.usecase.AbstractUsecase#doCheckPreconditions()
     */
    protected void doCheckPreconditions() throws Exception {
        super.doCheckPreconditions();
        if (!getErrorMessages().isEmpty()) {
            return;
        }

        if (!getSourceDocument().getArea().equals(Publication.AUTHORING_AREA)) {
            addErrorMessage("This usecase can only be invoked in the authoring area!");
        } else {
            DocumentFactory factory = getUnitOfWork().getIdentityMap().getFactory();
            Document liveVersion = factory.getAreaVersion(getSourceDocument(),
                    Publication.LIVE_AREA);
            if (liveVersion.exists()) {
                addErrorMessage("This usecase cannot be invoked when the live version exists!");
            }
        }
    }

    /**
     * @see org.apache.lenya.cms.usecase.AbstractUsecase#doCheckExecutionConditions()
     */
    protected void doCheckExecutionConditions() throws Exception {
        super.doCheckExecutionConditions();

        DocumentIdentityMap identityMap = getSourceDocument().getIdentityMap();

        String nodeId = getParameterAsString(NODE_ID);
        Document parent = identityMap.getFactory().getParent(getSourceDocument());
        DocumentManager documentManager = null;
        try {
            documentManager = (DocumentManager) this.manager.lookup(DocumentManager.ROLE);
            String[] messages = documentManager.canCreate(identityMap, getSourceDocument()
                    .getArea(), parent, nodeId, getSourceDocument().getLanguage());
            addErrorMessages(messages);
        } finally {
            if (documentManager != null) {
                this.manager.release(documentManager);
            }
        }
    }

    /**
     * @see org.apache.lenya.cms.usecase.AbstractUsecase#doExecute()
     */
    protected void doExecute() throws Exception {
        super.doExecute();

        Document document = getSourceDocument();
        Document newDocument = moveAllLanguageVersions(document);

        LinkRewriter rewriter = null;
        try {
            rewriter = (LinkRewriter) this.manager.lookup(LinkRewriter.ROLE);
            rewriter.rewriteLinks(document, newDocument);
        } finally {
            if (rewriter != null) {
                this.manager.release(rewriter);
            }
        }

        setTargetDocument(newDocument);
    }

    /**
     * Moves all language versions of a document.
     * @param document The document.
     * @return The moved version of the document.
     * @throws DocumentException if an error occurs.
     * @throws DocumentBuildException if an error occurs.
     * @throws PublicationException if an error occurs.
     * @throws ServiceException if an access error to a an Avalon service occurs
     */
    protected Document moveAllLanguageVersions(Document document) throws DocumentException,
            DocumentBuildException, PublicationException, ServiceException {
        Document newDocument = null;

        DocumentIdentityMap identityMap = document.getIdentityMap();
        String newDocumentId = getNewDocumentId();

        String[] availableLanguages = document.getLanguages();

        DocumentManager documentManager = null;
        try {
            documentManager = (DocumentManager) this.manager.lookup(DocumentManager.ROLE);
            for (int i = 0; i < availableLanguages.length; i++) {
                Document languageVersion = identityMap.getFactory().get(document.getPublication(),
                        document.getArea(),
                        document.getId(),
                        availableLanguages[i]);

                Document newLanguageVersion = identityMap.getFactory().get(document.getPublication(),
                        document.getArea(),
                        newDocumentId,
                        availableLanguages[i]);

                documentManager.moveDocument(languageVersion, newLanguageVersion);

                if (availableLanguages[i].equals(document.getLanguage())) {
                    newDocument = newLanguageVersion;
                }
            }
        } finally {
            if (documentManager != null) {
                this.manager.release(documentManager);
            }
        }
        
        return newDocument;
    }

    /**
     * Returns the resulting document when the node ID would be changed.
     * @return A document.
     */
    protected String getNewDocumentId() {
        String nodeId = getParameterAsString(NODE_ID);

        Document document = getSourceDocument();

        String oldDocumentId = document.getId();
        int lastSlashIndex = oldDocumentId.lastIndexOf("/");
        String strippedDocumentId = oldDocumentId.substring(0, lastSlashIndex + 1);
        String newDocumentId = strippedDocumentId + nodeId;

        return newDocumentId;
    }
}