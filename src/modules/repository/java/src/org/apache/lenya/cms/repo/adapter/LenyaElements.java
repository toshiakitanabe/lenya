/*
 * Copyright  1999-2005 The Apache Software Foundation
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
package org.apache.lenya.cms.repo.adapter;

import org.apache.lenya.cms.metadata.LenyaMetaData;
import org.apache.lenya.cms.repo.metadata.Element;
import org.apache.lenya.cms.repo.metadata.impl.ElementImpl;

public interface LenyaElements {

    String ELEMENT_SET = LenyaElements.class.getName();

    Element[] ELEMENTS = { new ElementImpl(LenyaMetaData.ELEMENT_RESOURCE_TYPE, false),
            new ElementImpl(LenyaMetaData.ELEMENT_CONTENT_TYPE, false),
            new ElementImpl(LenyaMetaData.ELEMENT_WORKFLOW_VERSION, true),
            new ElementImpl(LenyaMetaData.ELEMENT_PLACEHOLDER, false),
            new ElementImpl(LenyaMetaData.ELEMENTE_HEIGHT, false),
            new ElementImpl(LenyaMetaData.ELEMENT_WIDTH, false) };

}