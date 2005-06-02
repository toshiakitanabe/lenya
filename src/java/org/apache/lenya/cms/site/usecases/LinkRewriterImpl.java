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

import java.util.Map;

import org.apache.avalon.framework.context.Context;
import org.apache.avalon.framework.context.ContextException;
import org.apache.avalon.framework.context.Contextualizable;
import org.apache.avalon.framework.logger.AbstractLogEnabled;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.avalon.framework.service.ServiceSelector;
import org.apache.avalon.framework.service.Serviceable;
import org.apache.cocoon.components.ContextHelper;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Request;
import org.apache.lenya.cms.cocoon.source.SourceUtil;
import org.apache.lenya.cms.publication.Document;
import org.apache.lenya.cms.publication.DocumentBuilder;
import org.apache.lenya.cms.publication.DocumentIdentityMap;
import org.apache.lenya.cms.publication.DocumentType;
import org.apache.lenya.cms.publication.Publication;
import org.apache.lenya.cms.site.SiteManager;
import org.apache.lenya.transaction.Transactionable;
import org.apache.xpath.XPathAPI;
import org.w3c.dom.Attr;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Rewrite the links in a publication. This is used after renaming / moving a document.
 * 
 * @version $Id$
 */
public class LinkRewriterImpl extends AbstractLogEnabled implements LinkRewriter, Serviceable,
        Contextualizable {

    /**
     * Ctor.
     */
    public LinkRewriterImpl() {
    }

    /**
     * @see org.apache.lenya.cms.site.usecases.LinkRewriter#rewriteLinks(org.apache.lenya.cms.publication.Document,
     *      org.apache.lenya.cms.publication.Document)
     */
    public void rewriteLinks(Document originalTargetDocument, Document newTargetDocument) {

        Publication publication = originalTargetDocument.getPublication();
        String area = originalTargetDocument.getArea();
        DocumentIdentityMap identityMap = originalTargetDocument.getIdentityMap();

        Document[] documents;

        ServiceSelector selector = null;
        SiteManager siteManager = null;
        try {
            selector = (ServiceSelector) this.manager.lookup(SiteManager.ROLE + "Selector");
            siteManager = (SiteManager) selector.select(publication.getSiteManagerHint());
            documents = siteManager.getDocuments(identityMap, publication, area);
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

        Request request = ObjectModelHelper.getRequest(this.objectModel);
        String contextPath = request.getContextPath();

        try {
            for (int documentIndex = 0; documentIndex < documents.length; documentIndex++) {

                Document examinedDocument = documents[documentIndex];
                if (examinedDocument.exists()) {

                    if (getLogger().isDebugEnabled()) {
                        getLogger().debug("Rewriting links in document [" + examinedDocument + "]");
                    }

                    boolean linksRewritten = false;

                    DocumentType doctype = examinedDocument.getResourceType();
                    String[] xPaths = doctype.getLinkAttributeXPaths();

                    try {

                        org.w3c.dom.Document xmlDocument = SourceUtil.readDOM(examinedDocument.getSourceURI(), this.manager);

                        for (int xPathIndex = 0; xPathIndex < xPaths.length; xPathIndex++) {
                            NodeList nodes = XPathAPI.selectNodeList(xmlDocument,
                                    xPaths[xPathIndex]);
                            for (int nodeIndex = 0; nodeIndex < nodes.getLength(); nodeIndex++) {
                                Node node = nodes.item(nodeIndex);
                                if (node.getNodeType() != Node.ATTRIBUTE_NODE) {
                                    throw new RuntimeException("The XPath [" + xPaths[xPathIndex]
                                            + "] may only match attribute nodes!");
                                }
                                Attr attribute = (Attr) node;
                                final String url = attribute.getValue();

                                if (url.startsWith(contextPath + "/" + publication.getId())) {
                                    final String webappUrl = url.substring(contextPath.length());

                                    if (identityMap.isDocument(webappUrl)) {
                                        Document targetDocument = identityMap.getFromURL(webappUrl);

                                        if (matches(targetDocument, originalTargetDocument)) {
                                            String newTargetUrl = getNewTargetURL(targetDocument,
                                                    originalTargetDocument,
                                                    newTargetDocument);
                                            attribute.setValue(contextPath + newTargetUrl);
                                            linksRewritten = true;
                                        }
                                    }
                                }
                            }
                        }

                        if (linksRewritten) {
                            Transactionable nodes[] = examinedDocument.getRepositoryNodes();
                            for (int i = 0; i < nodes.length; i++) {
                                nodes[i].lock();
                            }
                            SourceUtil.writeDOM(xmlDocument, examinedDocument.getSourceURI(), this.manager);
                        }

                    } finally {
                    }

                }
            }
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Checks if targetDocument refers to originalTargetDocument, to one of its language versions,
     * to one of its descendants, or to a language version of one of the descendants.
     * @param targetDocument The target document.
     * @param originalTargetDocument The original target document.
     * @return A boolean value.
     */
    protected boolean matches(Document targetDocument, Document originalTargetDocument) {
        String matchString = originalTargetDocument.getId() + "/";
        String testString = targetDocument.getId() + "/";
        return testString.startsWith(matchString);
    }

    /**
     * Rewrites a document.
     * @param targetDocument The target document to rewrite.
     * @param originalTargetDocument The original target document.
     * @param newTargetDocument The new target document.
     * @return A string.
     */
    protected String getNewTargetURL(Document targetDocument, Document originalTargetDocument,
            Document newTargetDocument) {
        String originalId = originalTargetDocument.getId();
        String targetId = targetDocument.getId();
        String childString = targetId.substring(originalId.length());

        ServiceSelector selector = null;
        DocumentBuilder builder = null;
        try {
            selector = (ServiceSelector) this.manager.lookup(DocumentBuilder.ROLE + "Selector");
            builder = (DocumentBuilder) selector.select(originalTargetDocument.getPublication()
                    .getDocumentBuilderHint());

            String newTargetUrl = builder.buildCanonicalUrl(newTargetDocument.getPublication(),
                    newTargetDocument.getArea(),
                    newTargetDocument.getId() + childString,
                    targetDocument.getLanguage());
            return newTargetUrl;
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        } finally {
            if (selector != null) {
                if (builder != null) {
                    selector.release(builder);
                }
                this.manager.release(selector);
            }
        }
    }

    private ServiceManager manager;

    /**
     * @see org.apache.avalon.framework.service.Serviceable#service(org.apache.avalon.framework.service.ServiceManager)
     */
    public void service(ServiceManager manager) throws ServiceException {
        this.manager = manager;
    }

    private Map objectModel;

    /**
     * @see org.apache.avalon.framework.context.Contextualizable#contextualize(org.apache.avalon.framework.context.Context)
     */
    public void contextualize(Context context) throws ContextException {
        this.objectModel = ContextHelper.getObjectModel(context);

    }

}