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

package org.apache.lenya.ac.impl;

import java.io.File;

import org.apache.lenya.ac.AccessControlException;
import org.apache.lenya.ac.file.FileGroupManager;

/**
 * Group manager test.
 *
 * @version $Id$
 */
public class GroupManagerTest extends AccessControlTest {

    /**
     * The test
     * @throws AccessControlException if an error occurs
     */
    public final void testInstance() throws AccessControlException {
        FileGroupManager _manager = null;
        File configDir = getAccreditablesDirectory();
        _manager = FileGroupManager.instance(configDir, getLogger());
        assertNotNull(_manager);

        FileGroupManager anotherManager = null;
        anotherManager = FileGroupManager.instance(configDir, getLogger());
        assertNotNull(anotherManager);
        assertEquals(_manager, anotherManager);
    }
}