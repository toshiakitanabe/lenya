/*

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

 4. The names "Apache Cocoon" and  "Apache Software Foundation" must  not  be
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
 Stefano Mazzocchi  <stefano@apache.org>. For more  information on the Apache
 Software Foundation, please see <http://www.apache.org/>.

*/
package org.apache.cocoon.components.resolver.test;

import java.io.File;
import java.io.InputStream;
import java.util.Properties;

import junit.swingui.TestRunner;

import org.apache.avalon.excalibur.testcase.ExcaliburTestCase;
import org.apache.avalon.framework.activity.Disposable;
import org.apache.avalon.framework.activity.Initializable;
import org.apache.avalon.framework.context.DefaultContext;
import org.apache.cocoon.Constants;
import org.apache.lenya.cms.ac.Identity;
import org.apache.cocoon.environment.commandline.CommandlineContext;
import org.apache.cocoon.util.IOUtils;
import org.apache.excalibur.xml.EntityResolver;
import org.xml.sax.InputSource;

/**
 * A test case for cms/ac/Identity
 *
 * @author <a href="mailto:gregor@apache.org">Gregor J. Rothfuss</a>
 * @author <a href="mailto:michi@apache.org">Michael Wechner</a>
 * @version CVS $Id: IdentityTestCase.java,v 1.1 2003/04/16 14:14:29 gregor Exp $
 */
public final class IdentityTestCase
         extends ExcaliburTestCase
         implements Initializable, Disposable
{

    /**
     * Constructor for the IdentityTestCase object
     *
     * @since
     */
    public IdentityTestCase() {
        this("IdentityTestCase");
    }


    /**
     * The main program for the IdentityTestCase class.
     *
     * @param  args           The command line arguments
     * @exception  Exception  Description of Exception
     * @since
     */
    public static void main(final String[] args) throws Exception {
        final String[] testCaseName = {IdentityTestCase.class.getName()};
        TestRunner.main(testCaseName);
    }

    /**
     * The JUnit setup method. Lookup the resolver role.
     *
     * @exception  Exception  Description of Exception
     * @since
     */
    public void setUp() throws Exception {
        super.setUp();
    }

    /**
     * Invoked before components are created.
     *
     * @exception  Exception  Description of Exception
     * @since
     */
    public void initialize() throws Exception {
    }

    /**
     * Invoked after components have been disposed.
     *
     * @since
     */
    public void dispose() {
    }

    /**
     * The teardown method for JUnit
     *
     * @exception  Exception  Description of Exception
     * @since
     */
    public void tearDown() throws Exception {
        super.tearDown();

        if (Identity != null) {
//            manager.release(Identity);
            Identity = null;
        }
    }


    /**
     * JUnit test case:
     * Ask for an entity using deliberately non-existent publicId and systemId
     *
     * @exception  Exception  Description of Exception
     * @since
     */
    public void testGetPassword() throws Exception {
    }
}

