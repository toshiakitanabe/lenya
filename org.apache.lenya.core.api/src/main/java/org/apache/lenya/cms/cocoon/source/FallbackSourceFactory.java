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
package org.apache.lenya.cms.cocoon.source;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Map;

import org.apache.avalon.framework.context.ContextException;
import org.apache.avalon.framework.context.Contextualizable;
import org.apache.cocoon.components.ContextHelper;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.util.AbstractLogEnabled;
import org.apache.excalibur.source.Source;
import org.apache.excalibur.source.SourceFactory;
import org.apache.excalibur.source.SourceResolver;
import org.apache.excalibur.source.SourceUtil;
import org.apache.excalibur.source.URIAbsolutizer;
import org.apache.excalibur.store.impl.MRUMemoryStore;
import org.apache.lenya.cms.module.ModuleManager;
import org.apache.lenya.cms.publication.DocumentFactory;
import org.apache.lenya.cms.publication.DocumentFactoryBuilder;
import org.apache.lenya.cms.publication.Publication;
import org.apache.lenya.cms.publication.URLInformation;
import org.apache.lenya.cms.publication.templating.ExistingSourceResolver;
import org.apache.lenya.cms.publication.templating.PublicationTemplateManager;
import org.apache.lenya.cms.publication.templating.VisitingSourceResolver;
import org.apache.lenya.cms.repository.RepositoryManager;
import org.apache.lenya.cms.repository.RepositoryUtil;
import org.apache.lenya.cms.repository.Session;
import org.apache.lenya.util.ServletHelper;
import org.springframework.util.Assert;

/**
 * <p>
 * Source factory following the fallback principle.
 * </p>
 * <p>
 * The ID of the current publication can be passed in the URL (<code>fallback:pub://path</code),
 * this is necessary as a workaround for bug 40564.
 * </p>
 * 
 * @version $Id$
 */
public class FallbackSourceFactory extends AbstractLogEnabled implements SourceFactory,
        Contextualizable, URIAbsolutizer {

    protected MRUMemoryStore store;
    private SourceResolver resolver;
    private RepositoryManager repositoryManager;
    private DocumentFactoryBuilder documentFactoryBuilder;
    private PublicationTemplateManager templateManager;
    private ModuleManager moduleManager;
    
    /**
     * Configure the spring bean accordingly if you want to use a store.
     * @param store The store.
     */
    public void setStore(MRUMemoryStore store) {
        Assert.notNull(store, "store");
        this.store = store;
    }

    protected boolean useCache() {
        return this.store != null;
    }

    protected MRUMemoryStore getStore() {
        return this.store;
    }
    
    public void setSourceResolver(SourceResolver resolver) {
        this.resolver = resolver;
    }
    
    protected SourceResolver getSourceResolver() {
        return this.resolver;
    }
    
    public void setRepositoryManager(RepositoryManager repoMgr) {
        this.repositoryManager = repoMgr;
    }
    
    protected RepositoryManager getRepositoryManager() {
        return this.repositoryManager;
    }
    
    public void setDocumentFactoryBuilder(DocumentFactoryBuilder builder) {
        this.documentFactoryBuilder = builder;
    }
    
    protected DocumentFactoryBuilder getDocumentFactoryBuilder() {
        return this.documentFactoryBuilder;
    }
    
    public void setTemplateManager(PublicationTemplateManager mgr) {
        this.templateManager = mgr;
    }
    
    protected PublicationTemplateManager getTemplateManager() {
        return this.templateManager;
    }
    
    public void setModuleManager(ModuleManager mgr) {
        this.moduleManager = mgr;
    }
    
    protected ModuleManager getModuleManager() {
        return this.moduleManager;
    }

    /**
     * @see org.apache.excalibur.source.SourceFactory#getSource(java.lang.String, java.util.Map)
     */
    public Source getSource(final String location, Map parameters) throws IOException,
            MalformedURLException {

        Source source;

        if (useCache()) {
            MRUMemoryStore store = getStore();
            final String pubId = getPublicationId();
            final String cacheKey = getCacheKey(pubId, location);
            final String cachedSourceUri = (String) store.get(cacheKey);

            if (cachedSourceUri == null) {
                source = findSource(location, parameters);
                final String resolvedSourceUri = source.getURI();
                store.hold(cacheKey, resolvedSourceUri);
                if (getLogger().isDebugEnabled()) {
                    getLogger().debug(
                            "No cached source URI for key " + cacheKey + ", caching URI "
                                    + resolvedSourceUri);
                }
            } else {
                source = this.resolver.resolveURI(cachedSourceUri);
                if (getLogger().isDebugEnabled()) {
                    getLogger().debug(
                            "Using cached source URI " + cachedSourceUri + " for key " + cacheKey);
                }
            }

        } else {
            source = findSource(location, parameters);
        }

        return source;
    }

    /**
     * @param pubId The publication ID.
     * @param fallbackUri The fallback:// (or template-fallback:// etc.) URI.
     * @return A string.
     */
    public static String getCacheKey(final String pubId, final String fallbackUri) {
        String cacheKey = pubId == null ? fallbackUri : pubId + ":" + fallbackUri;
        return cacheKey;
    }

    protected String getPublicationId() {
        Request request = ContextHelper.getRequest(this.context);
        String webappUri = ServletHelper.getWebappURI(request);
        URLInformation info = new URLInformation(webappUri);
        String pubId = null;
        try {
            Session session = RepositoryUtil.getSession(this.repositoryManager, request);
            DocumentFactory factory = this.documentFactoryBuilder.createDocumentFactory(session);
            String pubIdCandidate = info.getPublicationId();
            if (pubIdCandidate != null && factory.existsPublication(pubIdCandidate)) {
                pubId = pubIdCandidate;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return pubId;
    }
    
    protected Source findSource(final String location, Map parameters) throws IOException,
            MalformedURLException {
        
        FallbackUri uri = new FallbackUri(location);

        String pubId = uri.getPubId();
        String path = uri.getPath();

        Source source = null;
        try {

            Request request = ContextHelper.getRequest(this.context);

            if (pubId == null) {
                String webappUrl = request.getRequestURI().substring(
                        request.getContextPath().length());

                URLInformation info = new URLInformation(webappUrl);
                pubId = info.getPublicationId();
            }

            Session session = RepositoryUtil.getSession(this.repositoryManager, request);
            DocumentFactory factory = this.documentFactoryBuilder.createDocumentFactory(session);
            if (factory.existsPublication(pubId)) {
                Publication pub = factory.getPublication(pubId);
                VisitingSourceResolver resolver = getSourceVisitor();
                this.templateManager.visit(pub, path, resolver);
                source = resolver.getSource();
            }

            if (source == null) {
                if (path.startsWith("lenya/modules/")) {
                    final String moduleShortcut = path.split("/")[2];
                    String baseUri = this.moduleManager.getBaseURI(moduleShortcut);
                    final String modulePath = path
                            .substring(("lenya/modules/" + moduleShortcut).length());
                    source = this.resolver.resolveURI(baseUri + modulePath);
                } else {
                    String contextUri = "context://" + path;
                    source = this.resolver.resolveURI(contextUri);
                }
            }

            if (getLogger().isDebugEnabled()) {
                getLogger().debug("Resolved source:  [" + source.getURI() + "]");
            }

        } catch (Exception e) {
            throw new RuntimeException("Resolving path [" + location + "] failed: ", e);
        }

        return source;
    }

    protected VisitingSourceResolver getSourceVisitor() {
        return new ExistingSourceResolver();
    }

    protected org.apache.avalon.framework.context.Context context;

    /**
     * @see org.apache.avalon.framework.context.Contextualizable#contextualize(org.apache.avalon.framework.context.Context)
     */
    public void contextualize(org.apache.avalon.framework.context.Context _context)
            throws ContextException {
        this.context = _context;
    }

    /**
     * @see org.apache.excalibur.source.SourceFactory#release(org.apache.excalibur.source.Source)
     */
    public void release(Source source) {
        // In fact, this method should never be called as this factory
        // returns a source object from a different factory. So that
        // factory should release the source
        if (null != source) {
            if (this.getLogger().isDebugEnabled()) {
                this.getLogger().debug("Releasing source " + source.getURI());
            }
            this.resolver.release(source);
        }
    }

    /**
     * @see org.apache.excalibur.source.URIAbsolutizer#absolutize(java.lang.String,
     *      java.lang.String)
     */
    public String absolutize(String baseURI, String location) {
        return SourceUtil.absolutize(baseURI, location, true);
    }

}