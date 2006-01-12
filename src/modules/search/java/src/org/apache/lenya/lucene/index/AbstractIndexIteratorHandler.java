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

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;

/**
 * Abstact base class for Index handling
 */
public abstract class AbstractIndexIteratorHandler implements IndexIteratorHandler {
    /** Creates a new instance of AbstractIndexIteratorHandler */
    public AbstractIndexIteratorHandler() {
        // do nothing
    }

    /** Handles a stale document.
     * @param reader The index reader
     * @param term The term
     */
    public void handleStaleDocument(IndexReader reader, Term term) {
        // do nothing
    }

    /** Handles a stale document.
     * @param reader The reader
     * @param term The term
     * @param file The file
     */
    public void handleUnmodifiedDocument(IndexReader reader, Term term, File file) {
        // do nothing
    }

    /** Handles an unmodified document and the file that represents it.
     * @param reader The reader
     * @param term The term
     * @param file The file
     */
    public void handleNewDocument(IndexReader reader, Term term, File file) {
        // do nothing
    }

    /** Handles a file. This is called for every file and mainly used for creating a new index.
     * @param reader The reader
     * @param file The file
     */
    public void handleFile(IndexReader reader, File file) {
        // do nothing
    }
}