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

package org.apache.lenya.workflow.impl;

import org.apache.lenya.workflow.BooleanVariableAssignment;
import org.apache.lenya.workflow.WorkflowException;
import org.apache.lenya.workflow.WorkflowInstance;


/**
 * Implementation of a boolean variable assignment.
 */
public class BooleanVariableAssignmentImpl implements BooleanVariableAssignment {
    
    /**
     * Ctor.
     * @param _variable The variable.
     * @param _value The value.
     */
    protected BooleanVariableAssignmentImpl(BooleanVariableImpl _variable, boolean _value) {
        this.variable = _variable;
        this.value = _value;
    }

    private BooleanVariableImpl variable;
    private boolean value;

    /**
     * @see org.apache.lenya.workflow.BooleanVariableAssignment#execute(org.apache.lenya.workflow.WorkflowInstance instance)
     */
    public void execute(WorkflowInstance instance) throws WorkflowException {
        WorkflowInstanceImpl impl = (WorkflowInstanceImpl) instance;
        impl.getVariableInstance(getVariable()).setValue(getValue());
    }

    /**
     * Returns the value of this assignment.
     * @return A boolean value.
     */
    public boolean getValue() {
        return this.value;
    }

    /**
     * Returns the variable of this assignment.
     * @return A variable.
     */
    public BooleanVariableImpl getVariable() {
        return this.variable;
    }
}
