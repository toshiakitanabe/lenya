/*
$Id: DefaultTaskWrapper.java,v 1.2 2003/08/25 15:40:55 andreas Exp $
<License>

 ============================================================================
                   The Apache Software License, Version 1.1
 ============================================================================

 Copyright (C) 1999-2003 The Apache Software Foundation. All rights reserved.

 Redistribution and use in source and binary forms, with or without modifica-
 tion, are permitted provided that the following conditions are met:

 1. Redistributions of  source code must  retain the above copyright  notice,
    this list of conditions and the following disclaimer.

 2. Redistributions in binary form must reproduce the above copyright notice,
    this list of conditions and the following disclaimer in the documentation
    and/or other materials provided with the distribution.

 3. The end-user documentation included with the redistribution, if any, must
    include  the following  acknowledgment:  "This product includes  software
    developed  by the  Apache Software Foundation  (http://www.apache.org/)."
    Alternately, this  acknowledgment may  appear in the software itself,  if
    and wherever such third-party acknowledgments normally appear.

 4. The names "Apache Lenya" and  "Apache Software Foundation"  must  not  be
    used to  endorse or promote  products derived from  this software without
    prior written permission. For written permission, please contact
    apache@apache.org.

 5. Products  derived from this software may not  be called "Apache", nor may
    "Apache" appear  in their name,  without prior written permission  of the
    Apache Software Foundation.

 THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 FITNESS  FOR A PARTICULAR  PURPOSE ARE  DISCLAIMED.  IN NO  EVENT SHALL  THE
 APACHE SOFTWARE  FOUNDATION  OR ITS CONTRIBUTORS  BE LIABLE FOR  ANY DIRECT,
 INDIRECT, INCIDENTAL, SPECIAL,  EXEMPLARY, OR CONSEQUENTIAL  DAMAGES (INCLU-
 DING, BUT NOT LIMITED TO, PROCUREMENT  OF SUBSTITUTE GOODS OR SERVICES; LOSS
 OF USE, DATA, OR  PROFITS; OR BUSINESS  INTERRUPTION)  HOWEVER CAUSED AND ON
 ANY  THEORY OF LIABILITY,  WHETHER  IN CONTRACT,  STRICT LIABILITY,  OR TORT
 (INCLUDING  NEGLIGENCE OR  OTHERWISE) ARISING IN  ANY WAY OUT OF THE  USE OF
 THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 This software  consists of voluntary contributions made  by many individuals
 on  behalf of the Apache Software  Foundation and was  originally created by
 Michael Wechner <michi@apache.org>. For more information on the Apache Soft-
 ware Foundation, please see <http://www.apache.org/>.

 Lenya includes software developed by the Apache Software Foundation, W3C,
 DOM4J Project, BitfluxEditor, Xopus, and WebSHPINX.
</License>
*/
package org.apache.lenya.cms.task;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.environment.Request;
import org.apache.lenya.cms.ac.Role;
import org.apache.lenya.cms.ac2.Identity;
import org.apache.lenya.cms.publication.Publication;
import org.apache.lenya.util.NamespaceMap;
import org.apache.lenya.xml.NamespaceHelper;
import org.apache.log4j.Category;
import org.w3c.dom.Element;

/**
 * @author andreas
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class DefaultTaskWrapper implements TaskWrapper {
    
    private static Category log = Category.getInstance(DefaultTaskWrapper.class);

    private Map parameters = new HashMap();
    private TaskWrapperParameters wrapperParameters = new TaskWrapperParameters(parameters);
    private WorkflowInvoker workflowInvoker = null;
    private TaskParameters taskParameters = new TaskParameters(parameters);

    /**
     * Default ctor for subclasses.
     */
    protected DefaultTaskWrapper() {
    }

    /**
     * Ctor to be called when all task wrapper parameters are known.
     * @param parameters The prefixed parameters.
     */
    public DefaultTaskWrapper(Map parameters) {
        log.debug("Creating");
        this.parameters.putAll(parameters);
        
        if (log.isDebugEnabled()) {
            for (Iterator i = parameters.keySet().iterator(); i.hasNext();) {
                String key = (String) i.next();
                String value = (String) parameters.get(key);
                log.debug("Setting parameter: [" + key + "] = [" + value + "]");
            }
        }
    }

    /**
     * Ctor.
     * Restores the wrapper parameters from an XML element.
     * @param parent The parent of the task wrapper element.
     * @param helper The namespace helper of the document.
     */
    public DefaultTaskWrapper(NamespaceHelper helper, Element parent) {
        log.debug("Creating");
        restore(helper, parent);
    }

    /**
     * Initializes the task wrapper.
     * @param taskId The task ID.
     * @param publication The publication.
     * @param webappUrl The webapp URL.
     * @param parameters The task parameters.
     * @throws ExecutionException when the task ID is null.
     */
    protected void initialize(
        String taskId,
        Publication publication,
        String webappUrl,
        Parameters parameters)
        throws ExecutionException {
        log.debug("Initializing");

        getTaskParameters().setPublication(publication);
        getWrapperParameters().setWebappUrl(webappUrl);

        if (taskId == null) {
            throw new ExecutionException("No task id provided!");
        }
        getWrapperParameters().setTaskId(taskId);
        getTaskParameters().parameterize(parameters);
    }

    /**
     * Extracts the task parameters from the given objects.
     * @param parameters A parameters object.
     * @param publication A publication.
     * @param request A request.
     * @return A parameters object.
     */
    protected Parameters extractTaskParameters(
        Parameters parameters,
        Publication publication,
        Request request) {
        Parameters taskParameters = new Parameters();
        taskParameters.setParameter(
            Task.PARAMETER_SERVLET_CONTEXT,
            publication.getServletContext().getAbsolutePath());
        taskParameters.setParameter(Task.PARAMETER_CONTEXT_PREFIX, request.getContextPath() + "/");
        taskParameters.setParameter(
            Task.PARAMETER_SERVER_PORT,
            Integer.toString(request.getServerPort()));
        taskParameters.setParameter(Task.PARAMETER_SERVER_URI, "http://" + request.getServerName());
        taskParameters.setParameter(Task.PARAMETER_PUBLICATION_ID, publication.getId());

        for (Enumeration e = request.getParameterNames(); e.hasMoreElements();) {
            String key = (String) e.nextElement();
            String value = request.getParameter(key);
            if (value != null) {
                taskParameters.setParameter(key, value);
            }
        }

        String[] names = parameters.getNames();
        for (int i = 0; i < names.length; i++) {
            String name = names[i];
            String value = parameters.getParameter(name, "");
            if (value != null) {
                taskParameters.setParameter(name, value);
            }
        }
        return taskParameters;
    }

    /**
     * Enables workflow transition invocation.
     * @param eventName The event name.
     * @param identity The identity that executes the task.
     * @param roles The roles of the identity.
     */
    public void setWorkflowAware(String eventName, Identity identity, Role[] roles) {
        this.workflowInvoker = new WorkflowInvoker(getParameterMap(), eventName, identity, roles);
    }

    /**
     * Executes the task.
     * @throws ExecutionException when something went wrong.
     */
    public void execute() throws ExecutionException {

        String taskId = getWrapperParameters().getTaskId();
        log.debug("-----------------------------------");
        log.debug(" Executing task [" + taskId + "]");
        log.debug("-----------------------------------");

        if (!wrapperParameters.isComplete()) {

            String[] missingKeys = getWrapperParameters().getMissingKeys();
            String keyString = "";
            for (int i = 0; i < missingKeys.length; i++) {
                if (i > 0) {
                    keyString += ", ";
                }
                keyString += missingKeys[i];
            }
            throw new ExecutionException("Parameters missing: [" + keyString + "]");
        }

        TaskManager manager;

        Publication publication = getTaskParameters().getPublication();

        if (workflowInvoker != null) {
            workflowInvoker.setup(publication, getWrapperParameters().getWebappUrl());
        }

        Task task;
        try {
            manager = new TaskManager(publication.getDirectory().getAbsolutePath());
            task = manager.getTask(taskId);
            
            Properties properties = new Properties();
            properties.putAll(getTaskParameters().getMap());
            Parameters parameters = Parameters.fromProperties(properties);
            
            task.parameterize(parameters);
        } catch (Exception e) {
            throw new ExecutionException(e);
        }

        //FIXME The new workflow is set before the end of the transition because the document id
        // and so the document are sometimes changing during the transition (ex archiving , ...) 
        if (workflowInvoker != null) {
            workflowInvoker.invokeTransition();
        }

        task.execute(publication.getServletContext().getAbsolutePath());

        Notifier notifier = new Notifier(manager, getParameterMap());
        notifier.sendNotification(getTaskParameters());

    }

    /**
     * Returns the task wrapper parameters.
     * @return A task wrapper parameters object.
     */
    protected TaskWrapperParameters getWrapperParameters() {
        return wrapperParameters;
    }
    
    /**
     * Returns the task parameters.
     * @return A task parameters object.
     */
    protected TaskParameters getTaskParameters() {
        return taskParameters;
    }

    protected static final String ELEMENT_TASK = "task";
    protected static final String ELEMENT_PARAMETER = "parameter";
    protected static final String ATTRIBUTE_NAME = "name";
    protected static final String ATTRIBUTE_VALUE = "value";

    /**
     * Saves the wrapper parameters to an XML element.
     * @param helper The namespace helper of the document.
     * @return An XML element.
     */
    public Element save(NamespaceHelper helper) {
        org.w3c.dom.Document document = helper.getDocument();
        NamespaceHelper taskHelper =
            new NamespaceHelper(Task.NAMESPACE, Task.DEFAULT_PREFIX, document);
        Element element = taskHelper.createElement(ELEMENT_TASK);

        for (Iterator i = getParameterMap().keySet().iterator(); i.hasNext(); ) {
            String key = (String) i.next();
            Element parameterElement = taskHelper.createElement(ELEMENT_PARAMETER);
            parameterElement.setAttribute(ATTRIBUTE_NAME, key);
            parameterElement.setAttribute(ATTRIBUTE_VALUE, (String) getParameterMap().get(key));
        }

        return element;
    }

    /**
     * Restores the wrapper parameters from an XML element.
     * @param parent The parent of the task wrapper element.
     * @param helper The namespace helper of the document.
     */
    protected void restore(NamespaceHelper helper, Element parent) {
        org.w3c.dom.Document document = helper.getDocument();
        NamespaceHelper taskHelper =
            new NamespaceHelper(Task.NAMESPACE, Task.DEFAULT_PREFIX, document);
        Element taskElement = taskHelper.getFirstChild(parent, ELEMENT_TASK);
        Element[] parameterElements = taskHelper.getChildren(taskElement, ELEMENT_PARAMETER);
        for (int i = 0; i < parameterElements.length; i++) {
            String key = parameterElements[i].getAttribute(ATTRIBUTE_NAME);
            String value = parameterElements[i].getAttribute(ATTRIBUTE_VALUE);
            getWrapperParameters().put(key, value);
        }
    }

    /**
     * Returns all prefixed parameters.
     * @return A parameter object.
     */
    public Parameters getParameters() {
        Properties properties = new Properties();
        properties.putAll(getParameterMap());
        Parameters params = Parameters.fromProperties(properties);
        return params;
    }

    /**
     * Returns all prefixed parameters.
     * @return A map.
     */
    public Map getParameterMap() {
        return parameters;
    }

    /**
     * Sets the notification parameters.
     * @param notificationParameters The notification parameters.
     */
    protected void setNotifying(NamespaceMap notificationParameters) {
        log.info("Enabling notification");
        getParameterMap().putAll(notificationParameters.getPrefixedMap());
    }

}
