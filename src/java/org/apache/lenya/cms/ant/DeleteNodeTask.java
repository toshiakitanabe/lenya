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

import org.apache.lenya.cms.site.SiteException;
import org.apache.lenya.cms.site.tree.SiteTree;
import org.apache.lenya.cms.site.tree.SiteTreeNode;
import org.apache.tools.ant.BuildException;


/**
 * Ant task to delete a node of a tree.
 */
public class DeleteNodeTask extends PublicationTask {
    private String area;
    private String documentid;

    /**
     * Creates a new instance of DeleteNodeTask
     */
    public DeleteNodeTask() {
        super();
    }

    /**
     * Get the area.
     * @return the area.
     */
    public String getArea() {
        return this.area;
    }

    /**
     * Set the area.
     * @param _area the area
     */
    public void setArea(String _area) {
        this.area = _area;
    }

    /**
     * return the document-id corresponding to the node to delete
     * @return string The document-id.
     */
    protected String getDocumentid() {
        return this.documentid;
    }

    /**
     * Set the value of the document-id corresponding to the node to delete
     * @param string The document-id.
     */
    public void setDocumentid(String string) {
        this.documentid = string;
    }

    /**
     * Delete a node of a tree.
     * 
     * @param _documentid The id of the document corresponding to the node to delete.
     * @param _area the areaof the tree
     * 
     * @throws SiteException if an error occurs
     */
    public void deleteNode(String _documentid, String _area)
        throws SiteException {
		SiteTree tree = null;

	  	try {
			tree = getSiteTree(_area);
			SiteTreeNode node = tree.removeNode(_documentid);
			if (node == null) {
				throw new SiteException("Node " + node + " couldn't be removed");
			} 
			tree.save();
		} catch (Exception e) {
			throw new SiteException(e);
		}
    }   
    /**
     * @see org.apache.tools.ant.Task#execute()
     */
    public void execute() throws BuildException {
        try {
            log("document-id corresponding to the node: " + getDocumentid());
            log("area: " + getArea());
			deleteNode(getDocumentid(), getArea());
        } catch (Exception e) {
            throw new BuildException(e);
        }
    }
}
