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
package org.apache.lenya.cms.repo.avalon;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.logger.AbstractLogEnabled;
import org.apache.lenya.cms.repo.metadata.Element;
import org.apache.lenya.cms.repo.metadata.ElementSet;
import org.apache.lenya.cms.repo.metadata.impl.ElementImpl;

/**
 * Avalon-based element set.
 */
public class ConfigurableElementSet extends AbstractLogEnabled implements ElementSet, Configurable {

    private String namespaceUri;
    private Map elements = new HashMap();

    protected String getNamespaceURI() {
        return this.namespaceUri;
    }

    public void configure(Configuration config) throws ConfigurationException {

        this.namespaceUri = config.getAttribute("namespace");

        Configuration[] attributeConfigs = config.getChildren("attribute");
        for (int i = 0; i < attributeConfigs.length; i++) {
            String name = attributeConfigs[i].getAttribute("name");
            boolean isMultiple = attributeConfigs[i].getAttributeAsBoolean("multiple", false);
            this.elements.put(name, new ElementImpl(name, isMultiple));
        }

    }

    public Element[] getElements() {
        Collection values = this.elements.values();
        return (Element[]) values.toArray(new Element[values.size()]);
    }

    public Element getElement(String name) {
        return (Element) this.elements.get(name);
    }

}
