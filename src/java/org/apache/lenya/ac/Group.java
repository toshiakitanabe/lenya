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

package org.apache.lenya.ac;

import org.apache.lenya.ac.attr.AttributeOwner;
import org.apache.lenya.ac.attr.AttributeRule;

/**
 * A group.
 * @version $Id: Group.java 473841 2006-11-12 00:46:38Z gregor $
 */
public interface Group extends Accreditable, Item {
    
    /**
     * Returns the members of this group.
     * @return An array of {@link Groupable}s.
     */
    Groupable[] getMembers();
    
    /**
     * Adds a member to this group.
     * @param member The member to add.
     */
    void add(Groupable member);
    
    /**
     * Removes a member from this group.
     * @param member The member to remove.
     */
    void remove(Groupable member);
    
    /**
     * Removes all members from this group.
     */
    void removeAllMembers();
    
    /**
     * Returns if this group explicitly contains this member. The rule is not considered.
     * @param member The member to check.
     * @return A boolean value.
     */
    boolean contains(Groupable member);
    
    /**
     * Delete a group.
     * @throws AccessControlException if the delete failed
     */
    void delete() throws AccessControlException;
    
    /**
     * Saves this group.
     * @throws AccessControlException when saving failed.
     */
    void save() throws AccessControlException;
    
    /**
     * @param rule The rule. A <code>null</code> value means that no rule should be used.
     */
    void setRule(AttributeRule rule);
    
    /**
     * @return The rule or <code>null</code> if no rule is set.
     */
    AttributeRule getRule();
    
    /**
     * @param user The user.
     * @return if the group's rule matches this user.
     */
    boolean matches(AttributeOwner user);
    
}