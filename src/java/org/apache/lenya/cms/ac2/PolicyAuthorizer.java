/*
 * $Id
 * <License>
 * The Apache Software License
 *
 * Copyright (c) 2002 lenya. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this
 *    list of conditions and the following disclaimer in the documentation and/or
 *    other materials provided with the distribution.
 *
 * 3. All advertising materials mentioning features or use of this software must
 *    display the following acknowledgment: "This product includes software developed
 *    by lenya (http://www.lenya.org)"
 *
 * 4. The name "lenya" must not be used to endorse or promote products derived from
 *    this software without prior written permission. For written permission, please
 *    contact contact@lenya.org
 *
 * 5. Products derived from this software may not be called "lenya" nor may "lenya"
 *    appear in their names without prior written permission of lenya.
 *
 * 6. Redistributions of any form whatsoever must retain the following acknowledgment:
 *    "This product includes software developed by lenya (http://www.lenya.org)"
 *
 * THIS SOFTWARE IS PROVIDED BY lenya "AS IS" WITHOUT ANY WARRANTY EXPRESS OR IMPLIED,
 * INCLUDING THE WARRANTY OF NON-INFRINGEMENT AND THE IMPLIED WARRANTIES OF MERCHANTI-
 * BILITY AND FITNESS FOR A PARTICULAR PURPOSE. lenya WILL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY YOU AS A RESULT OF USING THIS SOFTWARE. IN NO EVENT WILL lenya BE LIABLE
 * FOR ANY SPECIAL, INDIRECT OR CONSEQUENTIAL DAMAGES OR LOST PROFITS EVEN IF lenya HAS
 * BEEN ADVISED OF THE POSSIBILITY OF THEIR OCCURRENCE. lenya WILL NOT BE LIABLE FOR ANY
 * THIRD PARTY CLAIMS AGAINST YOU.
 *
 * Lenya includes software developed by the Apache Software Foundation, W3C,
 * DOM4J Project, BitfluxEditor and Xopus.
 * </License>
 */
package org.apache.lenya.cms.ac2;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.cocoon.environment.Request;
import org.apache.lenya.cms.ac.AccessControlException;
import org.apache.lenya.cms.ac.Role;
import org.apache.lenya.cms.publication.PageEnvelope;

/**
 * @author andreas
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class PolicyAuthorizer implements Authorizer {

    /**
     * Creates a new policy authorizer.
     */
    public PolicyAuthorizer() {
    }

    /**
     * Template method, override {@link #setup(Map)} to setup your authorizer.
     * @see org.apache.lenya.cms.ac2.Authorizer#authorize(org.apache.lenya.cms.ac2.Identity, java.lang.String, java.util.Map)
     */
    public boolean authorize(Identity identity, PageEnvelope envelope, Request request)
        throws AccessControlException {
        Policy policy = getAccessController().getPolicy(envelope);
        Role roles[] = policy.getRoles(identity);
        return roles.length > 0;
    }

    /**
     * Factory method to build the access controller.
     * @return An access controller.
     */
    protected AccessController getAccessController() {
        return controller;
    }

    protected static final String ACCESS_CONTROLLER_ELEMENT = "access-controller";
    protected static final String CLASSNAME_ATTRIBUTE = "src";

    private AccessController controller;

    /**
     * @see org.apache.lenya.cms.ac2.Authorizer#configure(org.apache.avalon.framework.configuration.Configuration)
     */
    public void configure(Configuration configuration) throws ConfigurationException {
        Configuration accessControllerConfiguration =
            configuration.getChild(ACCESS_CONTROLLER_ELEMENT);
        String className = accessControllerConfiguration.getAttribute(CLASSNAME_ATTRIBUTE);
        AccessController controller;
        try {
            controller = (AccessController) Class.forName(className).newInstance();
        } catch (Exception e) {
            throw new ConfigurationException("Configuration failed: ", e);
        }
        controller.configure(accessControllerConfiguration);
        this.controller = controller;
    }
}
