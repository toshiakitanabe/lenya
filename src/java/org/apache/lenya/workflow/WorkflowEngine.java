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
package org.apache.lenya.workflow;

/**
 * Workflow engine.
 * 
 * @version $Id$
 */
public interface WorkflowEngine {

    /**
     * Checks if an event can be invoked.
     * @param workflowable The workflowable.
     * @param workflow The workflow schema.
     * @param event The event.
     * @return A boolean value.
     * @throws WorkflowException if an error occurs.
     */
    boolean canInvoke(Workflowable workflowable, Workflow workflow, String event)
            throws WorkflowException;

    /**
     * Invokes an event.
     * @param workflowable The workflowable.
     * @param workflow The workflow.
     * @param event The event.
     * @throws WorkflowException if an error occurs.
     */
    void invoke(Workflowable workflowable, Workflow workflow, String event)
            throws WorkflowException;

}