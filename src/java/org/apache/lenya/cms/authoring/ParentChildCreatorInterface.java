/*
 * $Id: ParentChildCreatorInterface.java,v 1.2 2003/02/27 15:59:35 egli Exp $

 * The Apache Software License
 *
 * Copyright (c) 2002 wyona. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this
 *    list of conditions and the following disclaimer in the documentation and/or
 *    other materials provided with the distribution.
 *
 * 3. All advertising materials mentioning features or use of this software must
 *    display the following acknowledgment: "This product includes software developed
 *    by wyona (http://www.wyona.org)"
 *
 * 4. The name "wyona" must not be used to endorse or promote products derived from
 *    this software without prior written permission. For written permission, please
 *    contact contact@wyona.org
 *
 * 5. Products derived from this software may not be called "wyona" nor may "wyona"
 *    appear in their names without prior written permission of wyona.
 *
 * 6. Redistributions of any form whatsoever must retain the following acknowledgment:
 *    "This product includes software developed by wyona (http://www.wyona.org)"
 *
 * THIS SOFTWARE IS PROVIDED BY wyona "AS IS" WITHOUT ANY WARRANTY EXPRESS OR IMPLIED,
 * INCLUDING THE WARRANTY OF NON-INFRINGEMENT AND THE IMPLIED WARRANTIES OF MERCHANTI-
 * BILITY AND FITNESS FOR A PARTICULAR PURPOSE. wyona WILL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY YOU AS A RESULT OF USING THIS SOFTWARE. IN NO EVENT WILL wyona BE LIABLE
 * FOR ANY SPECIAL, INDIRECT OR CONSEQUENTIAL DAMAGES OR LOST PROFITS EVEN IF wyona HAS
 * BEEN ADVISED OF THE POSSIBILITY OF THEIR OCCURRENCE. wyona WILL NOT BE LIABLE FOR ANY
 * THIRD PARTY CLAIMS AGAINST YOU.
 *
 * Wyona includes software developed by the Apache Software Foundation, W3C,
 * DOM4J Project, BitfluxEditor and Xopus.
 */

package org.wyona.cms.authoring;

import java.io.File;
import java.util.Map;

import org.apache.avalon.framework.configuration.Configuration;
/**
 * DOCUMENT ME!
 *
 * @author <a href="mailto:christian.egli@wyona.org">Christian Egli</a>
 */
public interface ParentChildCreatorInterface {

    /**
     * Constant for a branch node. Branch nodes are somewhat related
     * to the concept of collections in WebDAV. They are not the same
     * however. 
     *
     */
    public final short BRANCH_NODE = 1;

    /**
     * Constant for a leaf node. Leaf nodes are somewhat related to
     * the concept of resources in WebDAV. They are not the same
     * however.
     *
     */
    public final short LEAF_NODE = 0;

    public void init(Configuration doctypeConf);

    /**
     * Return the type of node this creator will create. It can be
     * either <code>BRANCH_NODE</code> or <code>LEAF_NODE</code>. An
     * implementation can simply return the input parameter (which can
     * be used to pass in a request parameter) or choose to ignore it.
     *
     * @param childType a <code>short</code> value
     * @return a <code>short</code> value (either <code>BRANCH_NODE</code> or <code>LEAF_NODE</code>)
     * @exception Exception if an error occurs
     */
    short getChildType(short childType) throws Exception;

    /**
     * Describe <code>getChildName</code> method here.
     *
     * @param childname a <code>String</code> value
     * @return a <code>String</code> value
     * @exception Exception if an error occurs
     */
    String getChildName(String childname) throws Exception;

    /**
     * Describe <code>generateTreeId</code> method here.
     *
     * @param childId a <code>String</code> value
     * @param childType a <code>short</code> value
     * @return a <code>String</code> value
     * @exception Exception if an error occurs
     */
    String generateTreeId(String childId, short childType) throws Exception;

    /**
     * Describe <code>create</code> method here.
     *
     * @param samplesDir a <code>File</code> value
     * @param parentDir a <code>File</code> value
     * @param childId a <code>String</code> value
     * @param childType a <code>short</code> value
     * @param childName a <code>String</code> value
     * @exception Exception if an error occurs
     */
    void create(File samplesDir, File parentDir,
		String childId, short childType, String childName,
		Map parameters)
	throws Exception;
}
