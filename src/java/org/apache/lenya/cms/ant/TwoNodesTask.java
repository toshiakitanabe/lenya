/*
$Id: TwoNodesTask.java,v 1.6 2004/02/02 02:50:40 stefano Exp $
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

import org.apache.lenya.cms.publication.SiteTreeException;
import org.apache.tools.ant.BuildException;


/**
 * Abstract base class for Ant tasks, which manipulates two nodes.
 * The first node corresponds to the document with id firstdocumentid
 * and the area firstarea.
 * The 2nd node corresponds to the document with id secdocumentid 
 * and the area secarea.
 * @author edith
 */
public abstract class TwoNodesTask extends PublicationTask {
	private String firstarea;
	private String firstdocumentid;
	private String secarea;
	private String secdocumentid;

    /**
     * Creates a new instance of TwoNodesTask
     */
    public TwoNodesTask() {
        super();
    }

	/**
	 * @return String The area of the document of the first node.
	 */
	public String getFirstarea() {
		return firstarea;
	}

	/**
	 * @return String The document-id corresponding to the first node.
	 */
	public String getFirstdocumentid() {
		return firstdocumentid;
	}

	/**
	 * @return String The area of the document of the second node.
	 */
	public String getSecarea() {
		return secarea;
	}

	/**
	 * @return String The document-id corresponding to the second node.
	 */
	public String getSecdocumentid() {
		return secdocumentid;
	}

	/**
	 * @param string The area of the document of the first node.
	 */
	public void setFirstarea(String string) {
		firstarea = string;
	}

	/**
	 * @param string The document-id corresponding to the first node.
	 */
	public void setFirstdocumentid(String string) {
		firstdocumentid = string;
	}

	/**
	 * @param string The area of the document of the second node.
	 */
	public void setSecarea(String string) {
		secarea = string;
	}

	/**
	 * @param string The document-id corresponding to the second node.
	 */
	public void setSecdocumentid(String string) {
		secdocumentid = string;
	}

    /**
     * To be overriden.
     * Manipulation of two nodes . 
     * @param firstdocumentid : id of the first document
     * @param secdocumentid : id of the second document
     * @param firstarea : area of the tree of the first node
     * @param secarea : area of the tree of the 2nd node
     * 
     * @throws SiteTreeException if an error occurs
     */
    public abstract void manipulateTree(String firstdocumentid, String secdocumentid,
        String firstarea, String secarea)
        throws SiteTreeException;

    /** (non-Javadoc)
     * @see org.apache.tools.ant.Task#execute()
     */
    public void execute() throws BuildException {
        try {
            log("document-id corresponding to the first node: " + this.getFirstdocumentid());
            log("document-id corresponding to the second node: " + this.getSecdocumentid());
            log("area corresponding to the first node: " + this.getFirstarea());
			log("area corresponding to the second node: " + this.getSecarea());
            manipulateTree(getFirstdocumentid(), getSecdocumentid(), getFirstarea(), getSecarea());
        } catch (Exception e) {
            throw new BuildException(e);
        }
    }
}
