/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
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
package org.apache.lenya.cms.repo;

/**
 * The content of an area.
 */
public interface Content {

    /**
     * @param id The node ID.
     * @return A node with a specific ID.
     * @throws RepositoryException if an error occurs.
     */
    Asset getAsset(String id) throws RepositoryException;

    /**
     * @return All content nodes in this area.
     * @throws RepositoryException if an error occurs.
     */
    Asset[] getAssets() throws RepositoryException;

    /**
     * @param type The asset type.
     * @return The added node.
     * @throws RepositoryException if a node with this ID already exists or the document type is not
     *             registered.
     */
    Asset addAsset(AssetType type) throws RepositoryException;
    
    /**
     * @return The area this content belongs to.
     * @throws RepositoryException if an error occurs.
     */
    Area getArea() throws RepositoryException;

}