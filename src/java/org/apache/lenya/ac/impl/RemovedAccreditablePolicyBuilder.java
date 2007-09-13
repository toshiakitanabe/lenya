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

/* $Id: RemovedAccreditablePolicyBuilder.java 473841 2006-11-12 00:46:38Z gregor $  */

package org.apache.lenya.ac.impl;

import org.apache.lenya.ac.AccessControlException;
import org.apache.lenya.ac.Accreditable;
import org.apache.lenya.ac.AccreditableManager;
import org.apache.lenya.ac.Group;
import org.apache.lenya.ac.IPRange;
import org.apache.lenya.ac.User;

/**
 * A PolicyBuilder which can be used after an accreditable was removed.
 */
public class RemovedAccreditablePolicyBuilder extends PolicyBuilder {

    /**
     * Ctor.
     * @param accreditableManager The accreditable manager.
     */
    public RemovedAccreditablePolicyBuilder(AccreditableManager accreditableManager) {
        super(accreditableManager);
    }

    private Accreditable removedAccreditable;

    /**
     * Sets the removed accreditable.
     * 
     * @param accreditable An accreditable.
     */
    public void setRemovedAccreditable(Accreditable accreditable) {
        this.removedAccreditable = accreditable;
    }

    /**
     * @see org.apache.lenya.ac.impl.PolicyBuilder#getAccreditable(java.lang.String, java.lang.String)
     */
    protected Accreditable getAccreditable(String elementName, String id)
        throws AccessControlException {

        Accreditable accreditable;

        if (removedAccreditable instanceof User
            && elementName.equals(USER_ELEMENT)
            && ((User) removedAccreditable).getId().equals(id)) {
            accreditable = removedAccreditable;
        } else if (
            removedAccreditable instanceof Group
                && elementName.equals(GROUP_ELEMENT)
                && ((Group) removedAccreditable).getId().equals(id)) {
            accreditable = removedAccreditable;
        } else if (
            removedAccreditable instanceof IPRange
                && elementName.equals(IP_RANGE_ELEMENT)
                && ((IPRange) removedAccreditable).getId().equals(id)) {
            accreditable = removedAccreditable;
        } else {

            accreditable = super.getAccreditable(elementName, id);
        }
        return accreditable;
    }

}
