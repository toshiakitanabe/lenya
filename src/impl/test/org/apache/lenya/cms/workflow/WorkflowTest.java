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

package org.apache.lenya.cms.workflow;

import org.apache.lenya.ac.Identity;
import org.apache.lenya.ac.User;
import org.apache.lenya.ac.impl.AccessControlTest;
import org.apache.lenya.cms.publication.Document;
import org.apache.lenya.cms.publication.DocumentFactory;
import org.apache.lenya.cms.publication.Publication;
import org.apache.lenya.cms.publication.PublicationUtil;
import org.apache.lenya.cms.repository.RepositoryUtil;
import org.apache.lenya.cms.repository.Session;
import org.apache.lenya.workflow.Workflowable;

/**
 * To change the template for this generated type comment go to Window>Preferences>Java>Code
 * Generation>Code and Comments
 */
public class WorkflowTest extends AccessControlTest {

    private static final String variableName = "is_live";
    protected static final String URL = "/authoring/index.html";

    protected String getWebappUrl() {
        return "/test" + URL;
    }

    /**
     * Tests the workflow.
     * @throws Exception when something went wrong.
     */
    public void testWorkflow() throws Exception {
        Publication publication = PublicationUtil.getPublication(getManager(), "test");
        String url = "/" + publication.getId() + URL;
        DocumentFactory map = getIdentityMap();
        Document document = map.getFromURL(url);

        document.getRepositoryNode().lock();

        for (int situationIndex = 0; situationIndex < situations.length; situationIndex++) {

            login(situations[situationIndex].getUser());

            Identity identity = (Identity) getRequest().getSession()
                    .getAttribute(Identity.class.getName());
            User user = identity.getUser();
            getLogger().info("User: [" + user + "]");

            Session session = RepositoryUtil.createSession(getManager(), identity);
            Workflowable instance = new DocumentWorkflowable(getManager(),
                    session,
                    document,
                    getLogger());
            assertNotNull(instance);

            if (situationIndex > 0) {
                getLogger().info("Current state: " + instance.getLatestVersion().getState());
            }

            String event = situations[situationIndex].getEvent();

            getLogger().info("Event: " + event);

            WorkflowUtil.invoke(getManager(), session, getLogger(), document, event);

            boolean value = instance.getLatestVersion().getValue(variableName);

            getLogger().info("Variable: " + variableName + " = " + value);
            getLogger().info("------------------------------------------------------");

            assertEquals(value, situations[situationIndex].getValue());

        }

        document.getRepositoryNode().unlock();

        getLogger().info("Test completed.");
    }

    private static final TestSituation[] situations = {
            new TestSituation("lenya", "submit", false),
            new TestSituation("alice", "reject", false),
            new TestSituation("lenya", "submit", false),
            new TestSituation("alice", "publish", true),
            new TestSituation("alice", "deactivate", false) };

    /**
     * A test situation.
     */
    public static class TestSituation {
        private String user;
        private String event;
        private boolean value;

        /**
         * Creates a new test situation.
         * @param _user The user.
         * @param _event The event.
         * @param _value The variable value.
         */
        public TestSituation(String _user, String _event, boolean _value) {
            this.user = _user;
            this.event = _event;
            this.value = _value;
        }

        /**
         * Returns the event.
         * @return An event.
         */
        public String getEvent() {
            return this.event;
        }

        /**
         * Returns the user.
         * @return A string.
         */
        public String getUser() {
            return this.user;
        }

        /**
         * Returns the value.
         * @return A value.
         */
        public boolean getValue() {
            return this.value;
        }
    }

}