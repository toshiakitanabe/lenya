/*
$Id: MoveNode.java,v 1.8 2004/02/02 02:50:40 stefano Exp $
<License>

 ============================================================================
                   The Apache Software License, Version 1.1
 ============================================================================

 Copyright (C) 1999-2003 The Apache Software Foundation. All rights reserved.

 Redistribution and use in source and binary forms, with or without modifica-
 tion, are permitted provided that the following conditions are met:

 1. Redistributions of  source code must  retain the above copyright  notice,
    this list of conditions and the following disclaimer.

 2. Redistributions in binary form must reproduce the above copyright notice,
    this list of conditions and the following disclaimer in the documentation
    and/or other materials provided with the distribution.

 3. The end-user documentation included with the redistribution, if any, must
    include  the following  acknowledgment:  "This product includes  software
    developed  by the  Apache Software Foundation  (http://www.apache.org/)."
    Alternately, this  acknowledgment may  appear in the software itself,  if
    and wherever such third-party acknowledgments normally appear.

 4. The names "Apache Lenya" and  "Apache Software Foundation"  must  not  be
    used to  endorse or promote  products derived from  this software without
    prior written permission. For written permission, please contact
    apache@apache.org.

 5. Products  derived from this software may not  be called "Apache", nor may
    "Apache" appear  in their name,  without prior written permission  of the
    Apache Software Foundation.

 THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 FITNESS  FOR A PARTICULAR  PURPOSE ARE  DISCLAIMED.  IN NO  EVENT SHALL  THE
 APACHE SOFTWARE  FOUNDATION  OR ITS CONTRIBUTORS  BE LIABLE FOR  ANY DIRECT,
 INDIRECT, INCIDENTAL, SPECIAL,  EXEMPLARY, OR CONSEQUENTIAL  DAMAGES (INCLU-
 DING, BUT NOT LIMITED TO, PROCUREMENT  OF SUBSTITUTE GOODS OR SERVICES; LOSS
 OF USE, DATA, OR  PROFITS; OR BUSINESS  INTERRUPTION)  HOWEVER CAUSED AND ON
 ANY  THEORY OF LIABILITY,  WHETHER  IN CONTRACT,  STRICT LIABILITY,  OR TORT
 (INCLUDING  NEGLIGENCE OR  OTHERWISE) ARISING IN  ANY WAY OUT OF THE  USE OF
 THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 This software  consists of voluntary contributions made  by many individuals
 on  behalf of the Apache Software  Foundation and was  originally created by
 Michael Wechner <michi@apache.org>. For more information on the Apache Soft-
 ware Foundation, please see <http://www.apache.org/>.

 Lenya includes software developed by the Apache Software Foundation, W3C,
 DOM4J Project, BitfluxEditor, Xopus, and WebSHPINX.
</License>
*/
package org.apache.lenya.cms.ant;

import java.util.StringTokenizer;

import org.apache.lenya.cms.publication.Publication;
import org.apache.lenya.cms.publication.SiteTree;
import org.apache.lenya.cms.publication.SiteTreeException;
import org.apache.lenya.cms.publication.SiteTreeNode;

/**
 * @author edith
 *
 * Ant task that moves a node in a tree.
 */
public class MoveNode extends TwoNodesTask {
	private String refdocumentid;
	/**
	 *
	 */
	public MoveNode() {
		super();
	}

	/**
	 * Move a node.
	 * 
	 * @param firstdocumentid The document-id of the document corresponding to the source node.
	 * @param secdocumentid  The document-id of the document corresponding to the destination node.
	 * @param firstarea The area of the document corresponding to the source node.
	 * @param secarea The area of the document corresponding to the destination node.
	 * @throws SiteTreeException if there are problems with creating or saving the site tree.  
	 */
	public void manipulateTree(
		String firstdocumentid,
		String secdocumentid,
		String firstarea,
		String secarea)
		throws SiteTreeException {

		Publication publication = getPublication();
		SiteTree firsttree = publication.getSiteTree(firstarea);
		SiteTree sectree = publication.getSiteTree(secarea);

		String parentid = "";
		StringTokenizer st = new StringTokenizer(secdocumentid, "/");
		int length = st.countTokens();

		for (int i = 0; i < (length - 1); i++) {
			parentid = parentid + "/" + st.nextToken();
		}
		String newid = st.nextToken();

		SiteTreeNode node = firsttree.removeNode(firstdocumentid);
		if (node != null) {
			SiteTreeNode parentNode = sectree.getNode(parentid);
			if (parentNode != null) {
				sectree.importSubtree(parentNode, node, newid, this.getRefdocumentid());
			} else {
				throw new SiteTreeException(
					"The parent node "
						+ parentNode
						+ " where the removed node shall be inserted not found");
			}
		} else {
			throw new SiteTreeException(
				"Node " + node + " couldn't be removed");
		}

		if (firstarea.equals(secarea)) {
			firsttree.save();
		} else {
			firsttree.save();
			sectree.save();
		}
	}
	/**
	 * @return string The document-id corresponding to the reference node, before which 
	 * the moved node shoul be inserted. If null, the node is inserted at the end. 
	 */
	public String getRefdocumentid() {
		return refdocumentid;
	}

	/**
	 * @param string The document-id corresponding to the reference node, before which 
	 * the moved node shoul be inserted. If null, the node is inserted at the end.
	 */
	public void setRefdocumentid(String string) {
		refdocumentid = string;
	}

}
