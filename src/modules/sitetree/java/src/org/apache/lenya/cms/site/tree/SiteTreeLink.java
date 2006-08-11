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
package org.apache.lenya.cms.site.tree;

import org.apache.lenya.cms.publication.DocumentFactory;
import org.apache.lenya.cms.site.Label;
import org.apache.lenya.cms.site.SiteNode;

/**
 * SiteTree link.
 */
public class SiteTreeLink extends Label {

    protected SiteTreeLink(DocumentFactory factory, SiteNode node, String _label, String _language) {
        super(factory, node, _label, _language);
    }

    public void delete() {
        SiteTreeNode node = (SiteTreeNode) getNode();
        node.removeLabel(getLanguage());
    }

    /**
     * Set the actual label of the label object.
     * 
     * @param label The label.
     */
    public void setLabel(String label) {
        SiteTreeNode node = (SiteTreeNode) getNode();
        node.setLabel(getLanguage(), label);
    }

}
