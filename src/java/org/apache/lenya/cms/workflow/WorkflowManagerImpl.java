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
package org.apache.lenya.cms.workflow;

import org.apache.avalon.framework.logger.AbstractLogEnabled;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.avalon.framework.service.Serviceable;
import org.apache.lenya.cms.publication.Document;
import org.apache.lenya.cms.publication.util.DocumentSet;
import org.apache.lenya.workflow.Situation;
import org.apache.lenya.workflow.WorkflowException;
import org.apache.lenya.workflow.WorkflowInstance;

/**
 * Workflow manager implementation.
 * 
 * @version $Id:$
 */
public class WorkflowManagerImpl extends AbstractLogEnabled implements WorkflowManager, Serviceable {

    /**
     * @see org.apache.lenya.cms.workflow.WorkflowManager#invoke(org.apache.lenya.cms.publication.Document,
     *      java.lang.String, boolean)
     */
    public void invoke(Document document, String event, boolean force) throws WorkflowException {
        WorkflowResolver resolver = null;
        try {
            resolver = (WorkflowResolver) this.manager.lookup(WorkflowResolver.ROLE);
            if (resolver.hasWorkflow(document)) {
                WorkflowInstance instance = resolver.getWorkflowInstance(document);
                Situation situation = resolver.getSituation();
                if (force && !instance.canInvoke(situation, event)) {
                    throw new WorkflowException("The event [" + event
                            + "] cannot be invoked on the document [" + document
                            + "] in the situation [" + situation + "]");
                }
                instance.invoke(situation, event);
            }
        } catch (ServiceException e) {
            throw new WorkflowException(e);
        } finally {
            if (resolver != null) {
                this.manager.release(resolver);
            }
        }
    }

    /**
     * @see org.apache.lenya.cms.workflow.WorkflowManager#invoke(org.apache.lenya.cms.publication.Document,
     *      java.lang.String)
     */
    public void invoke(Document document, String event) throws WorkflowException {
        invoke(document, event, true);
    }

    /**
     * @see org.apache.lenya.cms.workflow.WorkflowManager#invokeOnAll(org.apache.lenya.cms.publication.util.DocumentSet,
     *      java.lang.String, boolean)
     */
    public void invokeOnAll(DocumentSet documentSet, String event, boolean force)
            throws WorkflowException {
        WorkflowResolver resolver = null;
        try {
            resolver = (WorkflowResolver) this.manager.lookup(WorkflowResolver.ROLE);
            Document[] documents = documentSet.getDocuments();
            for (int i = 0; i < documents.length; i++) {
                invoke(documents[i], event, force);
            }
        } catch (ServiceException e) {
            throw new WorkflowException(e);
        } finally {
            if (resolver != null) {
                this.manager.release(resolver);
            }
        }
    }

    /**
     * @see org.apache.lenya.cms.workflow.WorkflowManager#canInvoke(org.apache.lenya.cms.publication.Document,
     *      java.lang.String)
     */
    public boolean canInvoke(Document document, String event) {
        WorkflowResolver resolver = null;
        boolean canInvoke = true;
        try {
            resolver = (WorkflowResolver) this.manager.lookup(WorkflowResolver.ROLE);
            if (resolver.hasWorkflow(document)) {
                WorkflowInstance instance = resolver.getWorkflowInstance(document);
                Situation situation = resolver.getSituation();
                canInvoke = instance.canInvoke(situation, event);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (resolver != null) {
                this.manager.release(resolver);
            }
        }
        return canInvoke;
    }

    /**
     * @see org.apache.lenya.cms.workflow.WorkflowManager#canInvokeOnAll(org.apache.lenya.cms.publication.util.DocumentSet,
     *      java.lang.String)
     */
    public boolean canInvokeOnAll(DocumentSet documents, String event) {
        WorkflowResolver resolver = null;
        boolean canInvoke = true;
        try {
            resolver = (WorkflowResolver) this.manager.lookup(WorkflowResolver.ROLE);
            Document[] documentArray = documents.getDocuments();
            for (int i = 0; i < documentArray.length; i++) {
                canInvoke = canInvoke && canInvoke(documentArray[i], event);
            }
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        } finally {
            if (resolver != null) {
                this.manager.release(resolver);
            }
        }
        return canInvoke;
    }

    protected ServiceManager manager;

    /**
     * @see org.apache.avalon.framework.service.Serviceable#service(org.apache.avalon.framework.service.ServiceManager)
     */
    public void service(ServiceManager manager) throws ServiceException {
        this.manager = manager;
    }

}