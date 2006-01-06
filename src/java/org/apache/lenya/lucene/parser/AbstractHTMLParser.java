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

/* $Id: AbstractHTMLParser.java,v 1.5 2004/03/01 16:18:26 gregor Exp $  */

package org.apache.lenya.lucene.parser;

import java.io.File;

/**
 * Abstract base class for HTML parsers.
 */
public abstract class AbstractHTMLParser implements HTMLParser {
    /** Creates a new instance of AbstractHTMLParser */
    public AbstractHTMLParser() {
    }

    /**
     * Parses a file.
     */
    public void parse(File file) throws ParseException {
        parse(file.toURI());
    }
}