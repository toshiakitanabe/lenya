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

/* $Id$  */

package org.apache.lenya.cms.metadata.dublincore;

import org.apache.lenya.cms.publication.Document;
import org.apache.lenya.cms.publication.DocumentException;

/**
 * A proxy to the dublin core meta implementation so that meta data is 
 * only read from file when it is actually requested.
 */
public class DublinCoreProxy implements DublinCore {

    private DublinCoreImpl dcCore;
    private Document cmsDocument;


    /** 
     * Creates a new instance of Dublin Core
     * 
     * @param aDocument the document for which the Dublin Core instance is created.
     */
    public DublinCoreProxy(Document aDocument) {
        this.cmsDocument = aDocument;
    }

    /**
     * Instanciate a dublin core implementation object
     * 
     * @return a real dublin core object
     * @throws DocumentException when an error occurs.
     */
    protected DublinCoreImpl instance() throws DocumentException {
        if (dcCore == null) {
            dcCore = new DublinCoreImpl(this.cmsDocument);
        }
        return dcCore;
    }

    /**
     * @see org.apache.lenya.cms.metadata.dublincore.DublinCore#save()
     */
    public void save() throws DocumentException {
        instance().save();
    }

    /**
     * @see org.apache.lenya.cms.metadata.dublincore.DublinCore#getValues(java.lang.String)
     */
    public String[] getValues(String key) throws DocumentException {
        return instance().getValues(key);
    }

    /**
     * @see org.apache.lenya.cms.metadata.dublincore.DublinCore#getFirstValue(java.lang.String)
     */
    public String getFirstValue(String key) throws DocumentException {
        return instance().getFirstValue(key);
    }

    /**
     * @see org.apache.lenya.cms.metadata.dublincore.DublinCore#addValue(java.lang.String, java.lang.String)
     */
    public void addValue(String key, String value) throws DocumentException {
        instance().addValue(key, value);

    }

    /**
     * @see org.apache.lenya.cms.metadata.dublincore.DublinCore#removeValue(java.lang.String, java.lang.String)
     */
    public void removeValue(String key, String value) throws DocumentException {
        instance().removeValue(key, value);
    }

    /**
     * @see org.apache.lenya.cms.metadata.dublincore.DublinCore#removeAllValues(java.lang.String)
     */
    public void removeAllValues(String key) throws DocumentException {
        instance().removeAllValues(key);
    }
    
	/**
	 * @see org.apache.lenya.cms.metadata.dublincore.DublinCore#replaceBy(org.apache.lenya.cms.metadata.dublincore.DublinCore)
	 */
	public void replaceBy(DublinCore other) throws DocumentException {
		instance().replaceBy(other);

	}

    /**
     * @see org.apache.lenya.cms.metadata.dublincore.DublinCore#addValues(java.lang.String, java.lang.String[])
     */
    public void addValues(String key, String[] values) throws DocumentException {
        instance().addValues(key, values);
        
    }

    /**
     * @see org.apache.lenya.cms.metadata.dublincore.DublinCore#setValue(java.lang.String, java.lang.String)
     */
    public void setValue(String key, String value) throws DocumentException {
        instance().setValue(key, value);
    }

}
