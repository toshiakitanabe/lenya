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

package org.apache.lenya.cms.ant;

import org.apache.lenya.cms.site.Label;
import org.apache.lenya.cms.site.SiteException;
import org.apache.lenya.cms.site.tree.SiteTree;
import org.apache.lenya.cms.site.tree.SiteTreeNode;
import org.apache.tools.ant.BuildException;

/**
 * Ant task to remove the language from the node corresponding to the document 
 * with document id <documentid> and area <area>. The node is also deleted if no 
 * other language is available.
 */
public class DeleteLanguageNodeTask extends DeleteNodeTask {
    private String language;

    /**
     * Creates a new instance of DeleteLanguageNodeTask
     */
	public DeleteLanguageNodeTask() {
		super();
	}

    /**
     * Remove the language from the node corresponding to the document 
     * with document id <documentid> and area <area>. When no more language
     * is available, then the node will be deleted fom the tree. 
     * 
     * @param _language The language of the document.
     * @param documentid The id of the document.
     * @param area The area of the document.
     * 
     * @throws SiteException if an error occurs
     */
    public void deleteLanguageNode(String _language, String documentid, String area)
        throws SiteException {
        SiteTree tree = null;

        try {
            tree = getSiteTree(area);
            SiteTreeNode node = tree.getNode(documentid);
            node.removeLabel(node.getLabel(_language));
            Label[] labels = node.getLabels();
            if (labels.length < 1 ){
                deleteNode(documentid,area);   
            } else {
                tree.save();
            }
        } catch (Exception e) {
            throw new SiteException(e);
        }
    }   
    /** (non-Javadoc)
     * @see org.apache.tools.ant.Task#execute()
     */
    public void execute() throws BuildException {
        try {
            log("document-id : " + getDocumentid());
            log("area: " + getArea());
            log("language : " + getLanguage());
            deleteLanguageNode(getLanguage(), getDocumentid(), getArea());
        } catch (Exception e) {
            throw new BuildException(e);
        }
    }

	/**
     * Get the value of the language to be removed.
     * 
	 * @return The language
	 */
	public String getLanguage() {
		return this.language;
	}

    /**
     * Set the value of the language to be removed.
     * 
     * @param string The language.
     */
	public void setLanguage(String string) {
		this.language = string;
	}

}
