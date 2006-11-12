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

import org.apache.lenya.xml.Schema;

/**
 * @version $Id:$
 */
public interface AssetType {

    /**
     * Returns the name of this document type.
     * @return A string value.
     */
    String getName();

    /**
     * @return The source URI of the RelaxNG schema.
     */
    Schema getSchema();

    /**
     * @return If documents belonging to this document type should be validated upon writing.
     */
    boolean isValidating();
    
}