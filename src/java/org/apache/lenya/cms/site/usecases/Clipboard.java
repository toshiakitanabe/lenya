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

import org.apache.avalon.framework.logger.ConsoleLogger;
import org.apache.lenya.cms.publication.Document;
import org.apache.lenya.cms.publication.DocumentBuildException;
import org.apache.lenya.cms.publication.DocumentIdentityMap;
import org.apache.lenya.cms.publication.Publication;
import org.apache.lenya.cms.publication.PublicationException;
import org.apache.lenya.cms.publication.PublicationFactory;

/**
 * Clipboard for cut/copy/paste of documents. The clipping method is either {@link #METHOD_CUT}or
 * {@link #METHOD_COPY}.
 * 
 * @version $Id:$
 */
public class Clipboard {

    private String publicationId;
    private String servletContextPath;
    private String area;
    private String documentId;
    private String language;
    private int method;

    protected static final int METHOD_CUT = 0;
    protected static final int METHOD_COPY = 1;

    /**
     * Ctor.
     * @param document The document to put on the clipboard.
     * @param method The clipping method.
     */
    public Clipboard(Document document, int method) {
        this.publicationId = document.getPublication().getId();
        this.servletContextPath = document.getPublication().getServletContext().getAbsolutePath();
        this.area = document.getArea();
        this.documentId = document.getId();
        this.language = document.getLanguage();
        this.method = method;
    }

    /**
     * Returns the document for the current identity map.
     * @param identityMap The identity map.
     * @return A document.
     * @throws DocumentBuildException if the document could not be built.
     */
    public Document getDocument(DocumentIdentityMap identityMap) throws DocumentBuildException {

        PublicationFactory factory = PublicationFactory.getInstance(new ConsoleLogger());
        Publication publication;
        try {
            publication = factory.getPublication(this.publicationId, this.servletContextPath);
        } catch (PublicationException e) {
            throw new RuntimeException(e);
        }
        Document document = identityMap.get(publication, this.area, this.documentId, this.language);
        return document;
    }

    /**
     * @return The ID of the publication the document belongs to.
     */
    public String getPublicationId() {
        return this.publicationId;
    }

    /**
     * Returns the method of this clipboard.
     * @return An integer.
     */
    public int getMethod() {
        return this.method;
    }
}