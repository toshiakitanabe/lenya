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
package org.apache.lenya.cms.publication.templating;

import org.apache.lenya.cms.publication.Publication;

/**
 * Object to create an instance of a templating-enabled publication.
 * 
 * @version $Id:$
 */
public interface Instantiator {

    /**
     * The Avalon role.
     */
    String ROLE = Instantiator.class.getName();

    /**
     * Instantiate a publication.
     * @param templatePublication The template publication.
     * @param newPublicationId The ID of the new publication instance.
     * @param newPublicationName The name of the new publication.
     * @throws Exception if an error occurs.
     */
    void instantiate(Publication templatePublication, String newPublicationId,
            String newPublicationName) throws Exception;

}