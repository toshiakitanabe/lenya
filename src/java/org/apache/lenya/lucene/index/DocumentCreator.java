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

package org.apache.lenya.lucene.index;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.document.Document;


/**
 * An object of a class implementing this interface creates Lucene documents
 * from files.
 */
public interface DocumentCreator {
    /**
     * Create a document from a file.
     * @param file The file
     * @param htdocsDumpDir The directory to hold the dumps from spidering
     * @return The document
     * @throws IOException
     */
    public Document getDocument(File file, File htdocsDumpDir)
        throws IOException;
}
