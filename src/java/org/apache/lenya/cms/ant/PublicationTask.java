/*
$Id: PublicationTask.java,v 1.9 2004/02/02 02:50:40 stefano Exp $
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

import java.io.File;
import java.io.IOException;

import org.apache.lenya.cms.publication.Publication;
import org.apache.lenya.cms.publication.PublicationException;
import org.apache.lenya.cms.publication.PublicationFactory;
import org.apache.lenya.cms.task.AntTask;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

/**
 * Abstract base class for publication-dependent Ant tasks.
 * It requires some project parameters that are set by the AntTask.
 * @author  <a href="mailto:andreas@apache.org"/>
 */
public abstract class PublicationTask extends Task {
    /** Creates a new instance of PublicationTask */
    public PublicationTask() {}

    /**
     * Returns the publication directory.
     * 
     * @return a the path to the publication directory as a <code>File</code>
     */
    protected File getPublicationDirectory() {
        return new File(
            getProject().getProperty(AntTask.PUBLICATION_DIRECTORY));
    }
    
    /**
     * Return the context prefix.
     * 
     * @return the context-prefix
     */
    protected String getContextPrefix() {
    	return getProject().getProperty(AntTask.CONTEXT_PREFIX);
    }

    /**
     * Returns the publication ID.
     * 
     * @return the publication-id
     */
    protected String getPublicationId() {
        return getProject().getProperty(AntTask.PUBLICATION_ID);
    }

    /**
     * Returns the servlet context (e.g., <code>tomcat/webapp/lenya</code>)
     * 
     * @return the servlet-context
     */
    protected File getServletContext() {
        return new File(getProject().getProperty(AntTask.SERVLET_CONTEXT_PATH));
    }

    /**
     * Get the publication
     * 
     * @return the publication
     *  
     * @throws BuildException if the publication could not be found
     */
    protected Publication getPublication() throws BuildException {
        try {
            return PublicationFactory.getPublication(
                getPublicationId(),
                getServletContext().getCanonicalPath());
        } catch (IOException e) {
            throw new BuildException(e);
        } catch (PublicationException e) {
            throw new BuildException(e);
        }
    }

    /**
     * Utility method for assertion that a string is != null and != ""
     * 
     * @param string the string to check
     */
    protected void assertString(String string) {
        assert(string != null) && !string.equals("");
    }
}
