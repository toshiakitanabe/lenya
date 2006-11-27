/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
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

package org.apache.lenya.cms.publication;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.avalon.framework.container.ContainerUtil;
import org.apache.avalon.framework.logger.AbstractLogEnabled;
import org.apache.avalon.framework.logger.Logger;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.avalon.framework.service.ServiceSelector;
import org.apache.lenya.cms.cocoon.source.SourceUtil;
import org.apache.lenya.cms.metadata.MetaData;
import org.apache.lenya.cms.metadata.MetaDataException;
import org.apache.lenya.cms.publication.util.DocumentVisitor;
import org.apache.lenya.cms.repository.Node;
import org.apache.lenya.cms.repository.NodeFactory;
import org.apache.lenya.cms.repository.RepositoryException;
import org.apache.lenya.cms.repository.Session;
import org.apache.lenya.cms.site.Link;
import org.apache.lenya.cms.site.SiteException;
import org.apache.lenya.cms.site.SiteStructure;

/**
 * A typical CMS document.
 * @version $Id$
 */
public class DocumentImpl extends AbstractLogEnabled implements Document {

    private DocumentIdentifier identifier;
    private DocumentFactory factory;
    protected ServiceManager manager;

    /**
     * The meta data namespace.
     */
    public static final String METADATA_NAMESPACE = "http://apache.org/lenya/metadata/document/1.0";

    /**
     * The name of the resource type attribute. A resource has a resource type; this information can
     * be used e.g. for different rendering of different types.
     */
    public static final String METADATA_RESOURCE_TYPE = "resourceType";

    /**
     * The name of the mime type attribute.
     */
    public static final String METADATA_MIME_TYPE = "mimeType";

    /**
     * The name of the content type attribute. Any content managed by Lenya has a type; this
     * information can be used e.g. to provide an appropriate management interface.
     */
    public static final String METADATA_CONTENT_TYPE = "contentType";

    /**
     * The number of seconds from the request that a document can be cached before it expires
     */
    public static final String METADATA_EXPIRES = "expires";

    /**
     * The extension to use for the document source.
     */
    public static final String METADATA_EXTENSION = "extension";

    /**
     * Creates a new instance of DefaultDocument.
     * @param manager The service manager.
     * @param map The identity map the document belongs to.
     * @param identifier The identifier.
     * @param _logger a logger
     */
    protected DocumentImpl(ServiceManager manager, DocumentFactory map,
            DocumentIdentifier identifier, Logger _logger) {

        ContainerUtil.enableLogging(this, _logger);
        if (getLogger().isDebugEnabled()) {
            getLogger().debug("DefaultDocument() creating new instance with id ["
                    + identifier.getUUID() + "], language [" + identifier.getLanguage() + "]");
        }

        this.manager = manager;
        this.identifier = identifier;
        if (identifier.getUUID() == null) {
            throw new IllegalArgumentException("The document ID must not be null!");
        }

        this.factory = map;

        if (getLogger().isDebugEnabled()) {
            getLogger().debug("DefaultDocument() done building instance with _id ["
                    + identifier.getUUID() + "], _language [" + identifier.getLanguage() + "]");
        }
    }

    /**
     * @see org.apache.lenya.cms.publication.Document#getExpires()
     */
    public Date getExpires() throws DocumentException {
        Date expires = null;
        long secs = 0;

        MetaData metaData = null;
        String expiresMeta = null;
        try {
            metaData = this.getMetaData(METADATA_NAMESPACE);
            expiresMeta = metaData.getFirstValue("expires");
        } catch (MetaDataException e) {
            throw new DocumentException(e);
        }
        if (expiresMeta != null) {
            secs = Long.parseLong(expiresMeta);
        } else {
            secs = -1;
        }

        if (secs != -1) {
            Date date = new Date();
            date.setTime(date.getTime() + secs * 1000l);
            expires = date;
        } else {
            expires = this.getResourceType().getExpires();
        }

        return expires;
    }

    /**
     * @see org.apache.lenya.cms.publication.Document#getId()
     */
    public String getId() {
        // throw new IllegalStateException("Use getUUID() or getPath() instead");
        return this.identifier.getUUID();
    }

    /**
     * @see org.apache.lenya.cms.publication.Document#getName()
     */
    public String getName() {
        try {
            return getLink().getNode().getName();
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @see org.apache.lenya.cms.publication.Document#getPublication()
     */
    public Publication getPublication() {
        return this.identifier.getPublication();
    }

    /**
     * @see org.apache.lenya.cms.publication.Document#getLastModified()
     */
    public long getLastModified() throws DocumentException {
        try {
            return getRepositoryNode().getLastModified();
        } catch (RepositoryException e) {
            throw new DocumentException(e);
        }
    }

    public File getFile() {
        return getPublication().getPathMapper().getFile(getPublication(),
                getArea(),
                getUUID(),
                getLanguage());
    }

    /**
     * @see org.apache.lenya.cms.publication.Document#getLanguage()
     */
    public String getLanguage() {
        return this.identifier.getLanguage();
    }

    /**
     * @see org.apache.lenya.cms.publication.Document#getLanguages()
     */
    public String[] getLanguages() throws DocumentException {

        List documentLanguages = new ArrayList();
        String[] allLanguages = getPublication().getLanguages();

        if (getLogger().isDebugEnabled()) {
            getLogger().debug("Number of languages of this publication: " + allLanguages.length);
        }

        for (int i = 0; i < allLanguages.length; i++) {
            if (existsTranslation(allLanguages[i])) {
                documentLanguages.add(allLanguages[i]);
            }
        }

        return (String[]) documentLanguages.toArray(new String[documentLanguages.size()]);
    }

    /**
     * @see org.apache.lenya.cms.publication.Document#getArea()
     */
    public String getArea() {
        return this.identifier.getArea();
    }

    private String extension = null;
    private String defaultExtension = "html";

    /**
     * @see org.apache.lenya.cms.publication.Document#getExtension()
     */
    public String getExtension() {
        if (extension == null) {
            getLogger().info("Default extension will be used: " + defaultExtension);
            return defaultExtension;
        }
        return this.extension;
    }

    /**
     * @see org.apache.lenya.cms.publication.Document#getUUID()
     */
    public String getUUID() {
        return getIdentifier().getUUID();
    }

    private String defaultSourceExtension = "xml";

    /**
     * @see org.apache.lenya.cms.publication.Document#getSourceExtension()
     */
    public String getSourceExtension() {
        String sourceExtension;
        try {
            sourceExtension = getMetaData(METADATA_NAMESPACE).getFirstValue(METADATA_EXTENSION);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (sourceExtension == null) {
            getLogger().warn("No source extension for document [" + this + "]. The extension \""
                    + defaultSourceExtension + "\" will be used as default!");
            sourceExtension = defaultSourceExtension;
        }
        return sourceExtension;
    }

    /**
     * Sets the extension of the file in the URL.
     * @param _extension A string.
     */
    protected void setExtension(String _extension) {
        assert _extension != null;
        this.extension = _extension;
    }

    /**
     * @see org.apache.lenya.cms.publication.Document#exists()
     */
    public boolean exists() throws DocumentException {
        try {
            return getRepositoryNode().exists();
        } catch (RepositoryException e) {
            throw new DocumentException(e);
        }
    }

    /**
     * @see org.apache.lenya.cms.publication.Document#existsInAnyLanguage()
     */
    public boolean existsInAnyLanguage() throws DocumentException {
        String[] languages = getLanguages();

        if (languages.length > 0) {
            if (getLogger().isDebugEnabled()) {
                getLogger().debug("Document (" + this + ") exists in at least one language: "
                        + languages.length);
            }
            String[] allLanguages = getPublication().getLanguages();
            if (languages.length == allLanguages.length)
                // TODO: This is not entirely true, because the publication could assume the
                // languages EN and DE, but the document could exist for the languages DE and FR!
                if (getLogger().isDebugEnabled()) {
                    getLogger().debug("Document (" + this
                            + ") exists even in all languages of this publication");
                }
            return true;
        } else {
            if (getLogger().isDebugEnabled()) {
                getLogger().debug("Document (" + this + ") does NOT exist in any language");
            }
            return false;
        }

    }

    public DocumentIdentifier getIdentifier() {
        return this.identifier;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object object) {
        if (getClass().isInstance(object)) {
            DocumentImpl document = (DocumentImpl) object;
            return document.getIdentifier().equals(getIdentifier());
        }
        return false;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {

        String key = getPublication().getId() + ":" + getPublication().getServletContext() + ":"
                + getArea() + ":" + getId() + ":" + getLanguage();

        return key.hashCode();
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return getIdentifier().toString();
    }

    /**
     * @see org.apache.lenya.cms.publication.Document#getFactory()
     */
    public DocumentFactory getFactory() {
        return this.factory;
    }

    /**
     * @see org.apache.lenya.cms.publication.Document#getCanonicalWebappURL()
     */
    public String getCanonicalWebappURL() {
        return "/" + getPublication().getId() + "/" + getArea() + getCanonicalDocumentURL();
    }

    /**
     * @see org.apache.lenya.cms.publication.Document#getCanonicalDocumentURL()
     */
    public String getCanonicalDocumentURL() {
        DocumentBuilder builder = null;
        ServiceSelector selector = null;
        try {
            selector = (ServiceSelector) this.manager.lookup(DocumentBuilder.ROLE + "Selector");
            builder = (DocumentBuilder) selector.select(getPublication().getDocumentBuilderHint());
            String webappUrl = builder.buildCanonicalUrl(getFactory(), getLocator());
            String prefix = "/" + getPublication().getId() + "/" + getArea();
            return webappUrl.substring(prefix.length());
        } catch (Exception e) {
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

    /**
     * @see org.apache.lenya.cms.publication.Document#accept(org.apache.lenya.cms.publication.util.DocumentVisitor)
     */
    public void accept(DocumentVisitor visitor) throws PublicationException {
        visitor.visitDocument(this);
    }

    /**
     * @see org.apache.lenya.cms.publication.Document#delete()
     */
    public void delete() throws DocumentException {
        try {
            SourceUtil.delete(getSourceURI(), this.manager);
        } catch (Exception e) {
            throw new DocumentException(e);
        }
    }

    protected static final String IDENTIFIABLE_TYPE = "document";

    private ResourceType resourceType;

    /**
     * Convenience method to read the document's resource type from the meta-data.
     * @see Document#getResourceType()
     */
    public ResourceType getResourceType() throws DocumentException {
        
        if (!exists()) {
            throw new DocumentException("The document [" + this + "] doesn't exist!");
        }
        
        if (this.resourceType == null) {
            ServiceSelector selector = null;
            try {
                String name = getMetaData(METADATA_NAMESPACE).getFirstValue(METADATA_RESOURCE_TYPE);
                if (name == null) {
                    throw new DocumentException("No resource type defined for document [" + this
                            + "]!");
                }
                selector = (ServiceSelector) this.manager.lookup(ResourceType.ROLE + "Selector");
                this.resourceType = (ResourceType) selector.select(name);
            } catch (Exception e) {
                throw new DocumentException(e);
            }
        }
        return this.resourceType;
    }

    public MetaData getMetaData(String namespaceUri) throws MetaDataException {
        return getRepositoryNode().getMetaData(namespaceUri);
    }

    public String[] getMetaDataNamespaceUris() throws MetaDataException {
        return getRepositoryNode().getMetaDataNamespaceUris();
    }

    public String getMimeType() throws DocumentException {
        try {
            String mimeType = getMetaData(METADATA_NAMESPACE).getFirstValue(METADATA_MIME_TYPE);
            if (mimeType == null) {
                mimeType = "";
            }
            return mimeType;
        } catch (MetaDataException e) {
            throw new DocumentException(e);
        }
    }

    public long getContentLength() throws DocumentException {
        try {
            return getRepositoryNode().getContentLength();
        } catch (RepositoryException e) {
            throw new DocumentException(e);
        }
    }

    public void setMimeType(String mimeType) throws DocumentException {
        try {
            getMetaData(METADATA_NAMESPACE).setValue(METADATA_MIME_TYPE, mimeType);
        } catch (MetaDataException e) {
            throw new DocumentException(e);
        }
    }

    public DocumentLocator getLocator() {
        SiteStructure structure = area().getSite();
        if (!structure.containsByUuid(getUUID(), getLanguage())) {
            throw new RuntimeException("The document [" + this
                    + "] is not referenced in the site structure.");
        }
        try {
            return DocumentLocator.getLocator(getPublication().getId(),
                    getArea(),
                    structure.getByUuid(getUUID(), getLanguage()).getNode().getPath(),
                    getLanguage());
        } catch (SiteException e) {
            throw new RuntimeException(e);
        }
    }

    public String getPath() throws DocumentException {
        return getLink().getNode().getPath();
    }

    public boolean existsAreaVersion(String area) {
        String sourceUri = getSourceURI(getPublication(), area, getUUID(), getLanguage());
        try {
            return SourceUtil.exists(sourceUri, this.manager);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean existsTranslation(String language) {
        String sourceUri = getSourceURI(getPublication(), getArea(), getUUID(), language);
        try {
            return SourceUtil.exists(sourceUri, this.manager);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Document getAreaVersion(String area) throws DocumentException {
        try {
            return getFactory().get(getPublication(), area, getUUID(), getLanguage());
        } catch (DocumentBuildException e) {
            throw new DocumentException(e);
        }
    }

    public Document getTranslation(String language) throws DocumentException {
        try {
            return getFactory().get(getPublication(), getArea(), getUUID(), language);
        } catch (DocumentBuildException e) {
            throw new DocumentException(e);
        }
    }

    /**
     * @see org.apache.lenya.cms.publication.Document#getRepositoryNode()
     */
    public Node getRepositoryNode() {
        return getRepositoryNode(this.manager, getFactory(), getSourceURI());
    }

    protected static Node getRepositoryNode(ServiceManager manager, DocumentFactory docFactory,
            String sourceUri) {
        Session session = docFactory.getSession();
        NodeFactory factory = null;
        try {
            factory = (NodeFactory) manager.lookup(NodeFactory.ROLE);
            factory.setSession(session);
            return (Node) session.getRepositoryItem(factory, sourceUri);
        } catch (Exception e) {
            throw new RuntimeException("Creating repository node failed: ", e);
        } finally {
            if (factory != null) {
                manager.release(factory);
            }
        }
    }

    /**
     * @see org.apache.lenya.cms.publication.Document#getSourceURI()
     */
    public String getSourceURI() {
        return getSourceURI(getPublication(), getArea(), getUUID(), getLanguage());
    }

    protected static String getSourceURI(Publication pub, String area, String uuid, String language) {
        String path = pub.getPathMapper().getPath(uuid, language);
        return pub.getSourceURI() + "/content/" + area + "/" + path;
    }

    public boolean existsVersion(String area, String language) {
        String sourceUri = getSourceURI(getPublication(), area, getUUID(), language);
        try {
            return SourceUtil.exists(sourceUri, this.manager);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Document getVersion(String area, String language) throws DocumentException {
        try {
            return getFactory().get(getPublication(), area, getUUID(), language);
        } catch (DocumentBuildException e) {
            throw new DocumentException(e);
        }
    }

    public Link getLink() throws DocumentException {
        SiteStructure structure = area().getSite();
        if (structure.containsByUuid(getUUID(), getLanguage())) {
            try {
                return structure.getByUuid(getUUID(), getLanguage());
            } catch (SiteException e) {
                throw new DocumentException(e);
            }
        }
        throw new DocumentException("The document [" + this
                + "] is not referenced in the site structure!");
    }

    public boolean hasLink() {
        return area().getSite().containsByUuid(getUUID(), getLanguage());
    }

    private Area area;

    public Area area() {
        if (this.area == null) {
            this.area = new AreaImpl(this.manager, getFactory(), getPublication(), getArea());
        }
        return this.area;
    }

}