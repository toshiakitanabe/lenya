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
package org.apache.lenya.cms.site;

/**
 * A node in the site structure.
 */
public interface SiteNode {
    
    /**
     * @return The site structure this node belongs to.
     */
    SiteStructure getStructure();

    /**
     * @return The path.
     */
    String getPath();

    /**
     * @return The parent node.
     * @throws SiteException If the node has no parent.
     */
    SiteNode getParent() throws SiteException;
    
    /**
     * @return The languages of this node.
     */
    String[] getLanguages();
    
    /**
     * @param language The language.
     * @return The link for the language.
     * @throws SiteException if no link is contained for the language.
     */
    Link getLink(String language) throws SiteException;

    /**
     * @return The UUID of this node.
     */
    String getUuid();

    /**
     * Checks if a link for a certain language is contained.
     * @param language The language.
     * @return A boolean value.
     */
    boolean hasLink(String language);

    /**
     * @return The name, i.e. the last path element.
     */
    String getName();
    
    /**
     * @return if the node is visible in the navigation.
     */
    boolean isVisible();

    /**
     * Sets the node visibility in the navigation.
     * @param visibleInNav if the node should be visible.
     */
    void setVisible(boolean visibleInNav);

}
