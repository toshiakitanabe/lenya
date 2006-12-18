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
package org.apache.lenya.defaultpub.cms.usecases;

import org.apache.cocoon.components.ContextHelper;
import org.apache.cocoon.environment.Request;
import org.apache.lenya.ac.AccessControlException;
import org.apache.lenya.ac.User;
import org.apache.lenya.cms.ac.PolicyUtil;
import org.apache.lenya.cms.observation.RepositoryEvent;
import org.apache.lenya.cms.observation.RepositoryEventFactory;
import org.apache.lenya.cms.publication.Document;
import org.apache.lenya.cms.publication.DocumentException;
import org.apache.lenya.cms.publication.Proxy;
import org.apache.lenya.cms.publication.Publication;
import org.apache.lenya.notification.NotificationException;
import org.apache.lenya.cms.workflow.usecases.InvokeWorkflow;
import org.apache.lenya.notification.Message;
import org.apache.lenya.notification.NotificationEventDescriptor;

/**
 * Submit usecase handler.
 */
public class Submit extends InvokeWorkflow {

    protected static final String MESSAGE_SUBJECT = "notification-message";
    protected static final String MESSAGE_DOCUMENT_SUBMITTED = "document-submitted";
    protected static final String SEND_NOTIFICATION = "sendNotification";

    /**
     * @see org.apache.lenya.cms.usecase.AbstractUsecase#doExecute()
     */
    protected void doExecute() throws Exception {

        super.doExecute();
        
        if (Boolean.valueOf(getBooleanCheckboxParameter(SEND_NOTIFICATION)).booleanValue()) {
            sendNotification(getSourceDocument());
        }
    }

    protected void sendNotification(Document authoringDocument) throws NotificationException,
            DocumentException, AccessControlException {

        User sender = getSession().getIdentity().getUser();

        User[] recipients = PolicyUtil.getUsersWithRole(this.manager, authoringDocument
                .getCanonicalWebappURL(), "review", getLogger());

        // check to see if current user can review their own submission
        for (int i = 0; i < recipients.length; i++) {
            if (recipients[i].equals(sender))
                return;
        }

        String url;
        Document authoringVersion = authoringDocument.getAreaVersion(Publication.AUTHORING_AREA);
        Proxy proxy = authoringVersion.getPublication().getProxy(authoringVersion, false);

        if (proxy != null) {
            url = proxy.getURL(authoringVersion);
        } else {
            Request request = ContextHelper.getRequest(this.context);
            final String serverUrl = "http://" + request.getServerName() + ":"
                    + request.getServerPort();
            final String webappUrl = authoringVersion.getCanonicalWebappURL();
            url = serverUrl + request.getContextPath() + webappUrl;
        }

        String[] params = { url };

        Message message = new Message(MESSAGE_SUBJECT, new String[0], MESSAGE_DOCUMENT_SUBMITTED,
                params, sender, recipients);
        NotificationEventDescriptor descriptor = new NotificationEventDescriptor(message);
        RepositoryEvent event = RepositoryEventFactory.createEvent(this.manager, getSession(),
                getLogger(), descriptor);
        getSession().enqueueEvent(event);
    }
}
