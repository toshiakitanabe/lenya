/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.lenya.modules.resource;

import java.io.IOException;
import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.generation.AbstractGenerator;
import org.apache.lenya.cms.cocoon.source.RepositorySource;
import org.apache.lenya.cms.repository.ContentHolder;
import org.pdfbox.pdfparser.PDFParser;
import org.pdfbox.pdmodel.PDDocument;
import org.pdfbox.util.PDFTextStripper;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

public class PdfToTextGenerator extends AbstractGenerator {

    private static final String PREFIX = "pdf";
    private static final String NAMESPACE = "http://apache.org/lenya/pdf/1.0";
    private ContentHolder content;

    public void setup(SourceResolver resolver, Map objectModel, String src, Parameters par)
            throws ProcessingException, SAXException, IOException {

        super.setup(resolver, objectModel, src, par);

        RepositorySource source = null;
        try {
            source = (RepositorySource) resolver.resolveURI(src);
            this.content = source.getContent();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (source != null) {
                resolver.release(source);
            }
        }
    }

    public void generate() throws IOException, SAXException, ProcessingException {
        this.contentHandler.startDocument();
        this.contentHandler.startPrefixMapping(PREFIX, NAMESPACE);
        this.contentHandler.startElement(NAMESPACE, "document", PREFIX + ":document",
                new AttributesImpl());

        try {
            PDFTextStripper stripper = new PDFTextStripper();
            PDFParser parser = new PDFParser(this.content.getInputStream());
            parser.parse();
            PDDocument doc = parser.getPDDocument();
            String text = stripper.getText(doc);
            doc.close();
            char[] chars = text.toCharArray();
            this.contentHandler.characters(chars, 0, chars.length);
        } catch (Exception e) {
            throw new ProcessingException(e);
        }

        this.contentHandler.endElement(NAMESPACE, "document", PREFIX + ":document");
        this.contentHandler.endDocument();

    }

}
