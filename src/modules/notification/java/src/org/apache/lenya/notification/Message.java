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
package org.apache.lenya.notification;

import java.util.Arrays;
import java.util.Date;

import org.apache.lenya.ac.Identifiable;

/**
 * A notification message.
 */
public class Message {

    private String subject;
    private String[] subjectParams;
    private String body;
    private String[] bodyParams;
    private Identifiable sender;
    private Identifiable[] recipients;
    private Date time;

    /**
     * Ctor.
     * @param subject The subject.
     * @param subjectParams The subject parameters.
     * @param body The body.
     * @param bodyParams The body parameters.
     * @param sender The sender.
     * @param recipients The recipients.
     */
    public Message(String subject, String[] subjectParams, String body, String[] bodyParams,
            Identifiable sender, Identifiable[] recipients) {
        this.subject = subject;
        this.subjectParams = subjectParams;
        this.body = body;
        this.bodyParams = bodyParams;
        this.sender = sender;
        this.recipients = recipients;
        this.time = new Date();
    }

    /**
     * Determine if this message has parameters
     * @return true if the message has parameters
     */
    public boolean hasBodyParameters() {
        return bodyParams != null && bodyParams.length > 0;
    }

    /**
     * Retrieve the message content
     * @return the message
     */
    public String getBody() {
        return body;
    }

    /**
     * Retrieve the parameters for this message
     * @return the parameters
     */
    public String[] getBodyParameters() {
        return bodyParams;
    }

    /**
     * Determine if this message has parameters
     * @return true if the message has parameters
     */
    public boolean hasSubjectParameters() {
        return subjectParams != null && subjectParams.length > 0;
    }

    /**
     * Retrieve the message subject
     * @return the subject
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Retrieve the parameters for this message
     * @return the parameters
     */
    public String[] getSubjectParameters() {
        return subjectParams;
    }
    
    /**
     * @return The sender.
     */
    public Identifiable getSender() {
        return this.sender;
    }
    
    /**
     * @return The recipients.
     */
    public Identifiable[] getRecipients() {
        // don't expose the internal array
        return (Identifiable[]) Arrays.asList(this.recipients).toArray(
                new Identifiable[this.recipients.length]);
    }
    
    /**
     * @return The time when the message was sent.
     */
    public Date getTime() {
        return this.time;
    }

}