/*
$Id: InitWorkflowHistoryTask.java,v 1.6 2003/08/13 17:21:57 egli Exp $
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

import org.apache.lenya.cms.publication.DefaultDocumentBuilder;
import org.apache.lenya.cms.publication.Document;
import org.apache.lenya.cms.publication.DocumentBuildException;
import org.apache.lenya.cms.publication.DocumentType;
import org.apache.lenya.cms.publication.DocumentTypeBuilder;
import org.apache.lenya.cms.publication.Publication;
import org.apache.lenya.cms.workflow.WorkflowFactory;

import org.apache.tools.ant.BuildException;


/**
 * @author andreas
 *
 */
public class InitWorkflowHistoryTask extends PublicationTask {
    private String documentId;
    private String documentType;
    private String language;

    /**
     * @see org.apache.tools.ant.Task#execute()
     */
    public void execute() throws BuildException {
        // FIXME: URL as parameter
        String url =
            "/"
                + getPublication().getId()
                + "/"
                + Publication.AUTHORING_AREA
                + getDocumentId()
                + "_"
                + getLanguage()
                + ".html";
        Document document;

        try {
            document =
                DefaultDocumentBuilder.getInstance().buildDocument(
                    getPublication(),
                    url);
        } catch (DocumentBuildException e) {
            throw new BuildException(e);
        }

        try {
            DocumentType type =
                DocumentTypeBuilder.buildDocumentType(
                    getDocumentType(),
                    getPublication());
            WorkflowFactory.initHistory(document, type.getWorkflowFileName());
        } catch (Exception e) {
            throw new BuildException(e);
        }
    }

    /**
     * Get the document-id.
     * 
     * @return the document-id
     */
    public String getDocumentId() {
        assertString(documentId);

        return documentId;
    }

    /**
     * Set the document-id.
     * 
     * @param aDocumentId the document-id
     */
    public void setDocumentId(String aDocumentId) {
        assertString(aDocumentId);
        documentId = aDocumentId;
    }

    /**
     * Get the document type.
     * 
     * @return the document type
     */
    public String getDocumentType() {
        assertString(documentType);

        return documentType;
    }

    /**
     * Set the document type.
     * 
     * @param aDocumentType the document type
     */
    public void setDocumentType(String aDocumentType) {
        assertString(aDocumentType);
        documentType = aDocumentType;
    }
    
    /**
     * Get the language.
     *  
     * @return the language
     */
    public String getLanguage() {
        return language;
    }

    /**
     * Set the language.
     * 
     * @param string the language
     */
    public void setLanguage(String string) {
        language = string;
    }

}
