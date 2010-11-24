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

/**
 * A DocumentIdentityMap avoids the multiple instanciation of a document object.
 * 
 * @version $Id$
 */
public interface DocumentFactory {

    /**
     * Returns a document.
     * @param identifier The identifier of the document.
     * @return A document.
     * @throws ResourceNotFoundException if the document does not exist.
     */
    Document get(DocumentIdentifier identifier) throws ResourceNotFoundException;
    
    /**
     * Returns a document.
     * @param publication The publication.
     * @param area The area.
     * @param uuid The document ID.
     * @param language The language.
     * @return A document.
     * @throws ResourceNotFoundException if the document does not exist.
     */
    Document get(Publication publication, String area, String uuid, String language)
            throws ResourceNotFoundException;

    /**
     * Returns a revision of a document.
     * @param publication The publication.
     * @param area The area.
     * @param uuid The document ID.
     * @param language The language.
     * @param revision The revision..
     * @return A document.
     * @throws ResourceNotFoundException if the document does not exist.
     */
    Document get(Publication publication, String area, String uuid, String language, int revision)
            throws ResourceNotFoundException;

    /**
     * Returns the document identified by a certain web application URL.
     * @param webappUrl The web application URL.
     * @return A document.
     * @throws ResourceNotFoundException if an error occurs.
     */
    Document getFromURL(String webappUrl) throws ResourceNotFoundException;

    /**
     * Builds a document for the default language.
     * @param publication The publication.
     * @param area The area.
     * @param uuid The document UUID.
     * @return A document.
     * @throws ResourceNotFoundException if an error occurs.
     */
    Document get(Publication publication, String area, String uuid)
            throws ResourceNotFoundException;

    /**
     * Checks if a webapp URL represents a document.
     * @param webappUrl A web application URL.
     * @return A boolean value.
     */
    boolean isDocument(String webappUrl);
    
    /**
     * @return The session.
     */
    Session getSession();
    
    /**
     * @param id The publication ID.
     * @return A publication.
     * @throws PublicationException if the publication does not exist.
     */
    Publication getPublication(String id) throws PublicationException;
    
    /**
     * @return All publication IDs.
     */
    String[] getPublicationIds();
    
    /**
     * @param id The publication ID.
     * @return If a publication with this ID exists.
     */
    boolean existsPublication(String id);
}
