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
package org.apache.lenya.cms.cocoon.components.modules.input;

import org.apache.avalon.framework.activity.Initializable;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.avalon.framework.service.Serviceable;
import org.apache.cocoon.components.modules.input.AbstractInputModule;
import org.apache.lenya.cms.usecase.Operation;
import org.apache.lenya.cms.usecase.UnitOfWork;

/**
 * Super class for operation-based input modules.
 *
 * @version $Id$ 
 */
public class OperationModule extends AbstractInputModule implements Operation, Serviceable, Initializable {

    /**
     * Ctor.
     */
    public OperationModule() {
        super();
    }

    private UnitOfWork unitOfWork;

    /**
     * @see org.apache.lenya.cms.usecase.Operation#getUnitOfWork()
     */
    public UnitOfWork getUnitOfWork() {
        return this.unitOfWork;
    }

    protected ServiceManager manager;

    /**
     * @see org.apache.avalon.framework.service.Serviceable#service(org.apache.avalon.framework.service.ServiceManager)
     */
    public void service(ServiceManager _manager) throws ServiceException {
        this.manager = _manager;
    }

    /**
     * @see org.apache.avalon.framework.activity.Initializable#initialize()
     */
    public void initialize() throws Exception {
        this.unitOfWork = (UnitOfWork) this.manager.lookup(UnitOfWork.ROLE);
    }

    /**
     * @see org.apache.avalon.framework.activity.Disposable#dispose()
     */
    public void dispose() {
        if (this.manager != null) {
            if (this.unitOfWork != null) {
                this.manager.release(this.unitOfWork);
            }
        }
    }
    
}