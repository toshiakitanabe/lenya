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

import java.util.StringTokenizer;

import org.apache.lenya.cms.site.SiteException;
import org.apache.lenya.cms.site.tree.SiteTree;
import org.apache.lenya.cms.site.tree.SiteTreeNode;

/**
 * Ant task that copies a node of a tree and inserts it in tree
 */
public class InsertCopyNode extends TwoNodesTask {
    /**
     * Creates a new instance of InsertCopyNode
     */
    public InsertCopyNode() {
        super();
    }

    /**
     * copies a node corresponding to a document with id firstdocumentid and area firstarea and
     * inserts it like a node corresponding to a document with id secdocumentid and area secarea.
     * @param firstdocumentid The document-id of the document corresponding to the source node.
     * @param secdocumentid The document-id of the document corresponding to the destination node.
     * @param firstarea The area of the document corresponding to the source node.
     * @param secarea The area of the document corresponding to the destination node.
     * @throws SiteException if there are problems with creating or saving the site tree.
     */
    public void manipulateTree(String firstdocumentid, String secdocumentid, String firstarea,
            String secarea) throws SiteException {

        SiteTree firsttree = getSiteTree(firstarea);
        SiteTree sectree = getSiteTree(secarea);

		StringBuffer buf = new StringBuffer();
        StringTokenizer st = new StringTokenizer(secdocumentid, "/");
        int length = st.countTokens();

        for (int i = 0; i < (length - 1); i++) {
            buf.append("/" + st.nextToken());
        }
        String parentid = buf.toString();
        String newid = st.nextToken();

        SiteTreeNode node = firsttree.getNode(firstdocumentid);

        if (node != null) {
            SiteTreeNode parentNode = sectree.getNode(parentid);
            if (parentNode != null) {
                sectree.importSubtree(parentNode, node, newid, null);
            } else {
                throw new SiteException("The parent node " + parentNode
                        + " where the copied node shall be inserted not found");
            }
        } else {
            throw new SiteException("Node " + node + " couldn't be found");
        }
        if (firstarea.equals(secarea)) {
            firsttree.save();
        } else {
            firsttree.save();
            sectree.save();
        }
    }
}