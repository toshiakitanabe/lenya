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

/* $Id$  */

package org.apache.lenya.ac.impl;

import org.apache.avalon.framework.logger.Logger;
import org.apache.lenya.ac.Role;


/**
 * A Role embodies the privilege to do certain things.
 */
public abstract class AbstractRole extends AbstractItem implements Role {
    
    /**
     * Creates a new instance of Role.
     */
    public AbstractRole() {
	    // do nothing
    }

    /**
     * Creates a new instance of Role.
     * @param name The role name.
     * @param logger The logger.
     */
    public AbstractRole(String name, Logger logger) {
        assert name != null;
        setName(name);
    }

}
