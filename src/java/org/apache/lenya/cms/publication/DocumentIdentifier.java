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
package org.apache.lenya.cms.publication;

/**
 * Value object to identify documents.
 */
public class DocumentIdentifier {

    private Publication publication;
    private String area;
    private String id;
    private String language;
    
    /**
     * Ctor.
     * @param publication The publication.
     * @param area The area.
     * @param id The document ID.
     * @param language The language.
     */
    public DocumentIdentifier(Publication publication, String area, String id, String language) {
        this.publication = publication;
        this.area = area;
        this.id = id;
        this.language = language;
    }

    /**
     * @return The area.
     */
    public String getArea() {
        return area;
    }

    /**
     * @return The document ID.
     */
    public String getId() {
        return id;
    }

    /**
     * @return The language.
     */
    public String getLanguage() {
        return language;
    }

    /**
     * @return The publication.
     */
    public Publication getPublication() {
        return publication;
    }
    
    public boolean equals(Object obj) {
        return (obj instanceof DocumentIdentifier) && obj.hashCode() == hashCode();
    }
    
    public int hashCode() {
        return getKey().hashCode();
    }
    
    protected String getKey() {
        return this.publication.getId() + ":" + this.area + ":" + this.id + ":" + this.language;
    }
}