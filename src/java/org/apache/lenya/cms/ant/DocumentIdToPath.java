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

/* $Id$  */

package org.apache.lenya.cms.ant;

import org.apache.lenya.cms.publication.DocumentIdToPathMapper;
import org.apache.lenya.cms.publication.Publication;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Target;

/**
 * Ant task to get the directory path of the xml files of a document with document id.
 * The path is given from the {area} directory.
 */
public class DocumentIdToPath extends PublicationTask {
    private String area;
    private String documentid;
    private String propertyname;

    /**
     * Creates a new instance of DocumentIdToPath
     */
    public DocumentIdToPath() {
        super();
    }

    /**
     * @return Sting The area.
     */
    public String getArea() {
        return this.area;
    }

    /**
     * @return string The document id 
     */
    protected String getDocumentid() {
        return this.documentid;
    }

    /**
     * @return propertyname. The name of the property for the directory path.
     */
    public String getPropertyname() {
        return this.propertyname;
    }

    /**
     * @param string The area.
     */
    public void setArea(String string) {
        this.area = string;
    }

    /**
     * @param string The name of the property.
     */
    public void setPropertyname(String string) {
        this.propertyname = string;
    }

    /**
     * Set the value of the document id.
     * @param string The document id. 
     */
    public void setDocumentid(String string) {
        this.documentid = string;
    }

    /**
     * Gets the directory path from the document id and sets this value in the 
     * property of the project with the name propertyname.   
     * @param _area The area (ex authoring)
     * @param _documentid  The document id.
     * @param _propertyname The name of the property
     */
    public void compute(String _area, String _documentid, String _propertyname) {

        Publication publication = getPublication();
        DocumentIdToPathMapper pathMapper = publication.getPathMapper();
        String path = pathMapper.getPath(_documentid, "");
        log("path " + path);

        int index = path.lastIndexOf("/");
        String dir = path.substring(0, index);
        log("dir " + dir);

        Target _target = getOwningTarget();
        Project _project = _target.getProject();
        _project.setProperty(_propertyname, dir);
    }

    /** 
     * @see org.apache.tools.ant.Task#execute()
     **/
    public void execute() throws BuildException {
        log("document-id " + getDocumentid());
        log("area " + getArea());
        log("property: " + getPropertyname());
        compute(getArea(), getDocumentid(), getPropertyname());
    }

}
