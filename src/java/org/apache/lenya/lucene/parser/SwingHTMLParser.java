/*
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
package org.apache.lenya.lucene.parser;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URLConnection;

import javax.swing.text.html.parser.ParserDelegator;


/**
 * @author Andreas Hartmann
 * @author Michael Wechner
 * @version $Id: SwingHTMLParser.java,v 1.8 2004/02/02 02:50:38 stefano Exp $
 */
public class SwingHTMLParser extends AbstractHTMLParser {
    /** Creates a new instance of SwingHTMLParser */
    public SwingHTMLParser() {
    }

    /**
     * Parses a URI.
     */
    public void parse(URI uri) throws ParseException {
        try {
            ParserDelegator delagator = new ParserDelegator();
            handler = new SwingHTMLHandler();

            Reader reader = new PreParser().parse(getReader(uri));
            delagator.parse(reader, handler, true);
        } catch (IOException e) {
            throw new ParseException(e);
        }
    }

    private SwingHTMLHandler handler;

    protected SwingHTMLHandler getHandler() {
        return handler;
    }

    /**
     * Get title
     *
     * @return DOCUMENT ME!
     */
    public String getTitle() {
        return getHandler().getTitle();
    }

    /**
     * Get keywords
     *
     * @return DOCUMENT ME!
     */
    public String getKeywords() {
        return getHandler().getKeywords();
    }

    private Reader reader;

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Reader getReader() {
        return getHandler().getReader();
    }

    protected Reader getReader(URI uri) throws IOException, MalformedURLException {
        if (uri.toString().startsWith("http:")) {
            // uri is url
            URLConnection connection = uri.toURL().openConnection();

            return new InputStreamReader(connection.getInputStream());
        } else {
            // uri is file
            return new FileReader(new File(uri));
        }
    }
}
