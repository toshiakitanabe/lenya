/*
 * <License>
 * The Apache Software License
 *
 * Copyright (c) 2002 lenya. All rights reserved.
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
 *    by lenya (http://www.lenya.org)"
 *
 * 4. The name "lenya" must not be used to endorse or promote products derived from
 *    this software without prior written permission. For written permission, please
 *    contact contact@lenya.org
 *
 * 5. Products derived from this software may not be called "lenya" nor may "lenya"
 *    appear in their names without prior written permission of lenya.
 *
 * 6. Redistributions of any form whatsoever must retain the following acknowledgment:
 *    "This product includes software developed by lenya (http://www.lenya.org)"
 *
 * THIS SOFTWARE IS PROVIDED BY lenya "AS IS" WITHOUT ANY WARRANTY EXPRESS OR IMPLIED,
 * INCLUDING THE WARRANTY OF NON-INFRINGEMENT AND THE IMPLIED WARRANTIES OF MERCHANTI-
 * BILITY AND FITNESS FOR A PARTICULAR PURPOSE. lenya WILL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY YOU AS A RESULT OF USING THIS SOFTWARE. IN NO EVENT WILL lenya BE LIABLE
 * FOR ANY SPECIAL, INDIRECT OR CONSEQUENTIAL DAMAGES OR LOST PROFITS EVEN IF lenya HAS
 * BEEN ADVISED OF THE POSSIBILITY OF THEIR OCCURRENCE. lenya WILL NOT BE LIABLE FOR ANY
 * THIRD PARTY CLAIMS AGAINST YOU.
 *
 * Lenya includes software developed by the Apache Software Foundation, W3C,
 * DOM4J Project, BitfluxEditor and Xopus.
 * </License>
 */
package org.apache.lenya.cms.task;

import java.io.File;
import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.lenya.cms.PublicationHelper;
import org.apache.lenya.cms.publication.Publication;
import org.apache.lenya.cms.publication.SiteTree;
import org.apache.lenya.cms.publication.SiteTreeNode;


/**
 * Class for testing the task to deactivate a document.
 * Extend the AntTask class.
 * @author edith
 */
public class DocumentDeactivateTaskTest extends AntTaskTest {

	private long time = 0;

	/**
     * Creates a new DocumentDeactivateTaskTest object.
	 * @param test The test.
	 */
	public DocumentDeactivateTaskTest(String test) {
		super(test);
	}

	/** 
	 * Creates a test suite.
	 * @return Test 
	 **/
	public static Test getSuite() {
		return new TestSuite(DocumentDeactivateTaskTest.class);
	}

	/**
	 * The main program for the DocumentDeactivateTaskTest class
	 *
	 * @param args The command line arguments
	 */
	public static void main(String[] args) {
		AntTaskTest.initialize(args);
		TestRunner.run(getSuite());
	}

	public static final String DOCUMENT_ID = "tutorial";
	public static final String AUTHORING_PATH = "content/authoring".replace('/', File.separatorChar);
	public static final String LIVE_PATH = "content/live".replace('/', File.separatorChar);
	
	/**
	 * @see org.apache.lenya.cms.task.AntTaskTest#getTaskParameters()
	 **/
	protected Parameters getTaskParameters() {
		Parameters parameters = super.getTaskParameters();
		parameters.setParameter("properties.node.firstdocumentid", DOCUMENT_ID);
		return parameters;
	}
    
	/**
	 * Returns the target test.
	 * @return target.
	 */
	protected String getTarget() {
		return "deactivateDocument";
	}

	/** (non-Javadoc)
     * @see org.apache.lenya.cms.task.AntTaskTest#prepareTest()
     */
    protected void prepareTest() throws Exception {
		File publicationDirectory = PublicationHelper.getPublication().getDirectory();
        String publicationPath = publicationDirectory.getAbsolutePath()+ File.separator; 


		// TODO generate the resources  
}

	/** (non-Javadoc)
     * @see org.apache.lenya.cms.task.AntTaskTest#evaluateTest()
     */
    protected void evaluateTest() throws Exception {
		File publicationDirectory = PublicationHelper.getPublication().getDirectory();
		String publicationPath = publicationDirectory.getAbsolutePath(); 
        File authoringDirectory = new File(publicationPath, AUTHORING_PATH);
		File liveDirectory = new File(publicationPath, LIVE_PATH);
        
		String filepath = DOCUMENT_ID + File.separator + "index.xml";  

		File authoringDocumentFile = new File(authoringDirectory, filepath);
        assertTrue(authoringDocumentFile.exists());
		System.out.println("Document exists already in authoring: " + authoringDocumentFile.getAbsolutePath());
		File liveDocumentFile = new File(liveDirectory, filepath);
		assertFalse(liveDocumentFile.exists());
		System.out.println("Document was deleted from live: " + liveDocumentFile.getAbsolutePath());

        //TODO evaluation of resources, meta, workflow
        
		SiteTree authoringSitetree = PublicationHelper.getPublication().getSiteTree(Publication.AUTHORING_AREA);
		SiteTreeNode node = authoringSitetree.getNode(DOCUMENT_ID);
		assertNotNull(node);
		System.out.println("Sitetree node with id "+node.getId()+"is always in authoring");
		SiteTree liveSitetree = PublicationHelper.getPublication().getSiteTree(Publication.LIVE_AREA);
		SiteTreeNode livenode = liveSitetree.getNode(DOCUMENT_ID);
		assertNull(livenode);
		System.out.println("Sitetree node for document id "+DOCUMENT_ID+" was deleted from the live tree");
	}
}
