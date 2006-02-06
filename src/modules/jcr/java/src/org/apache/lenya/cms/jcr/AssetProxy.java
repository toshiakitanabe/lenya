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
package org.apache.lenya.cms.jcr;

import org.apache.lenya.cms.jcr.mapping.AbstractNodeProxy;
import org.apache.lenya.cms.jcr.mapping.NamePathElement;
import org.apache.lenya.cms.jcr.mapping.Path;
import org.apache.lenya.cms.jcr.mapping.PathElement;
import org.apache.lenya.cms.repo.Asset;
import org.apache.lenya.cms.repo.Content;
import org.apache.lenya.cms.repo.Translation;
import org.apache.lenya.cms.repo.AssetType;
import org.apache.lenya.cms.repo.RepositoryException;

/**
 * Content node proxy.
 */
public class AssetProxy extends AbstractNodeProxy implements Asset {

    protected static final String NODE_TYPE = "lnt:contentNode";
    protected static final String NODE_NAME = "lenya:contentNode";
    protected static final String ID_PROPERTY = "jcr:uuid";
    protected static final String DOCUMENT_TYPE_PROPERTY = "lenya:documentType";
    protected static final String VISIBLE_IN_NAV_PROPERTY = "lenya:visibleInNav";

    public Translation[] getTranslations() throws RepositoryException {
        ContentProxy contentProxy = (ContentProxy) getParentProxy();
        Path path = contentProxy.getAbsolutePath()
                .append(new NamePathElement(TranslationProxy.NODE_NAME));
        return (Translation[]) getRepository().getProxies(path);
    }

    public Translation addTranslation(String language, String label, String mimeType)
            throws RepositoryException {

        TranslationProxy proxy = (TranslationProxy) getRepository().addByProperty(getAbsolutePath(),
                TranslationProxy.NODE_TYPE,
                TranslationProxy.class.getName(),
                TranslationProxy.NODE_NAME,
                TranslationProxy.LANGUAGE_PROPERTY,
                language);
        proxy.setLabel(label);

        ResourceProxy resourceProxy = (ResourceProxy) getRepository().addByName(proxy.getAbsolutePath(),
                ResourceProxy.NODE_TYPE,
                ResourceProxy.class.getName(),
                ResourceProxy.NODE_NAME);
        resourceProxy.init(mimeType);

        return proxy;
    }

    public void removeTranslation(Translation document) throws RepositoryException {
        // TODO Auto-generated method stub

    }

    public Translation getTranslation(String language) throws RepositoryException {
        Path path = TranslationProxy.getPath(this, language);
        return (Translation) getRepository().getProxy(path);
    }

    public AssetType getAssetType() throws RepositoryException {
        String name = getPropertyString(DOCUMENT_TYPE_PROPERTY);
        return getRepository().getDocumentTypeRegistry().getDocumentType(name);
    }

    public String getAssetId() throws RepositoryException {
        try {
            return getNode().getUUID();
        } catch (javax.jcr.RepositoryException e) {
            throw new RepositoryException(e);
        }
    }

    public Path getAbsolutePath() throws RepositoryException {
        return AssetProxy.getPath((ContentProxy) getParentProxy(), getAssetId());
    }

    protected static Path getPath(ContentProxy contentProxy, String nodeId)
            throws RepositoryException {
        return contentProxy.getAbsolutePath()
                .append(getPathElement(NODE_NAME, ID_PROPERTY, nodeId));
    }

    public PathElement getPathElement() throws RepositoryException {
        return getPathElement(NODE_NAME, ID_PROPERTY, getAssetId());
    }

    public boolean isVisibleInNav() throws RepositoryException {
        return getPropertyBoolean(VISIBLE_IN_NAV_PROPERTY);
    }

    public void setVisibleInNav(boolean visible) throws RepositoryException {
        setProperty(VISIBLE_IN_NAV_PROPERTY, visible);
    }

    /**
     * @param documentType The document type's name.
     * @throws RepositoryException if an error occurs.
     */
    public void setDocumentType(String documentType) throws RepositoryException {
        setProperty(DOCUMENT_TYPE_PROPERTY, documentType);
    }

    public Content getContent() throws RepositoryException {
        return (Content) getParentProxy();
    }

}
