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
package org.apache.lenya.cms.export;

import java.io.File;

import org.apache.lenya.ac.impl.AbstractAccessControlTest;
import org.apache.lenya.cms.publication.Area;
import org.apache.lenya.cms.publication.Publication;

/**
 * Import example content into test publication.
 */
public class ImportTest extends AbstractAccessControlTest {

    /**
     * @throws Exception if an error occurs.
     */
    public void testImport() throws Exception {
        
        login("lenya");
        
        Publication pub = getPublication("test");
        Area area = pub.getArea("authoring");
        
        if (area.getDocuments().length == 0) {
            Publication defaultPub = getPublication("default");
            Area defaultArea = defaultPub.getArea("authoring");
            String pubPath = defaultArea.getPublication().getDirectory().getAbsolutePath();
            String path = pubPath.replace(File.separatorChar, '/') + "/example-content";
            Importer importer = new Importer(getManager(), getLogger());
            importer.importContent(area, path);
            getFactory().getSession().commit();
        }
    }
    
}
