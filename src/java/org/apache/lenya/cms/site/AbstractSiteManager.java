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

package org.apache.lenya.cms.site;

import org.apache.avalon.framework.logger.AbstractLogEnabled;
import org.apache.lenya.cms.publication.DocumentIdentityMap;

/**
 * Abstract base class for site managers.
 * 
 * @version $Id$
 */
public abstract class AbstractSiteManager extends AbstractLogEnabled implements SiteManager {
    
    private DocumentIdentityMap map;
    
    /**
     * Ctor.
     */
    public AbstractSiteManager() {
	    // do nothing
    }
    
    /**
     * @see org.apache.lenya.cms.site.SiteManager#setIdentityMap(org.apache.lenya.cms.publication.DocumentIdentityMap)
     */
    public void setIdentityMap(DocumentIdentityMap _map) {
        this.map = _map;
    }

    /**
     * Returns the identity map.
     * @return A resource identity map.
     */
    public DocumentIdentityMap getIdentityMap() {
        return this.map;
    }

}
