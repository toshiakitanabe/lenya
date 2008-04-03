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

/* $Id: Item.java 473841 2006-11-12 00:46:38Z gregor $  */
package org.apache.lenya.ac.file;

import java.io.File;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.lenya.ac.Item;

/**
 * An item which is stored in a file.
 */
public interface FileItem extends Item {

    /**
     * Sets the configuration directory of this item.
     * @param configurationDirectory The configuration directory.
     */
    void setConfigurationDirectory(File configurationDirectory);

    /**
     * Configures this item.
     * @param configuration The configuration.
     * @throws ConfigurationException when something went wrong.
     */
    void configure(Configuration configuration) throws ConfigurationException;
    
}