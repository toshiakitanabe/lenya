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
package org.apache.lenya.cms.admin;

import org.apache.lenya.ac.User;

/**
 * Usecase to edit a user's profile.
 */
public class UserProfile extends AccessControlUsecase {

    protected static final String USER_ID = "userId";
    protected static final String FULL_NAME = "fullName";
    protected static final String EMAIL = "email";
    protected static final String DESCRIPTION = "description";
    
    /**
     * Ctor.
     */
    public UserProfile() {
        super();
    }

    /**
     * @see org.apache.lenya.cms.usecase.AbstractUsecase#doCheckExecutionConditions()
     */
    protected void doCheckExecutionConditions() throws Exception {
        
        String email = getParameter(UserProfile.EMAIL);
        if (email.length() == 0) {
            addErrorMessage("Please enter an e-mail address.");
        }
    }
    
    /**
     * @see org.apache.lenya.cms.usecase.AbstractUsecase#doExecute()
     */
    protected void doExecute() throws Exception {
        super.doExecute();
        
        String fullName = getParameter(UserProfile.FULL_NAME);
        String description = getParameter(UserProfile.DESCRIPTION);
        String email = getParameter(UserProfile.EMAIL);
        
        getUser().setEmail(email);
        getUser().setName(fullName);
        getUser().setDescription(description);
        getUser().save();
        
    }
    
    private User user;
    
    /**
     * Returns the currently edited user.
     * @return A user.
     */
    protected User getUser() {
        return this.user;
    }
    
    /**
     * @see org.apache.lenya.cms.usecase.Usecase#setParameter(java.lang.String, java.lang.String)
     */
    public void setParameter(String name, String value) {
        super.setParameter(name, value);
        
        if (name.equals(USER_ID)) {
            String userId = value;
            this.user = getUserManager().getUser(userId);
            if (this.user == null) {
                throw new RuntimeException("User [" + userId + "] not found.");
            }
            
            setParameter(EMAIL, this.user.getEmail());
            setParameter(DESCRIPTION, this.user.getDescription());
            setParameter(FULL_NAME, this.user.getName());
        }
    }

}
