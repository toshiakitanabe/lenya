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

package org.apache.lenya.workflow;

/**
 * <p>A workflow schema.</p>
 * <p>
 * A workflow schema defines a state machine (deterministic finite
 * automaton - DFA), consisting of
 * </p>
 * <ul>
 * <li>states, including a marked initial state,</li>
 * <li>transitions, and</li>
 * <li>state variables.</li>
 */
public interface Workflow {
    /**
     * <code>NAMESPACE</code> Workflow namespace URI
     */
    String NAMESPACE = "http://apache.org/cocoon/lenya/workflow/1.0";
    /**
     * <code>DEFAULT_PREFIX</code> Default workflow namespace prefix
     */
    String DEFAULT_PREFIX = "wf";
    
    /**
     * Returns the initial state of this workflow.
     * @return The initial state
     */
    State getInitialState();

    /**
     * Returns the transitions that leave a state.
     * This method is used, e.g., to disable menu items.
     * @param state A state.
     * @return The transitions that leave the state.
     */
    Transition[] getLeavingTransitions(State state);
    
    /**
     * Returns the variable names.
     * @return A string array.
     */
    String[] getVariableNames();
    
    /**
     * @return The name of this workflow.
     */
    String getName();
}
