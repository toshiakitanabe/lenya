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
package org.apache.lenya.cms.jcr;

import javax.jcr.Repository;
import javax.jcr.RepositoryException;

/**
 * Factory for JCR repositories.
 */
public interface RepositoryFactory {

    /**
     * @param webappPath The absolute path to the Lenya web application.
     * @return A repository.
     * @throws RepositoryException if an error occurs.
     */
    Repository getRepository(String webappPath) throws RepositoryException;
    
}
