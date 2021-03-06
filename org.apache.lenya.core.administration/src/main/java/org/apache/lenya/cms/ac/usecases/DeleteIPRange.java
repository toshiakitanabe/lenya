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
package org.apache.lenya.cms.ac.usecases;

/**
 * Delete an IP range.
 *
 * @version $Id: DeleteIPRange.java 407305 2006-05-17 16:21:49Z andreas $
 */
public class DeleteIPRange extends AccessControlUsecase {

    /**
     * @see org.apache.lenya.cms.usecase.AbstractUsecase#doExecute()
     */
    protected void doExecute() throws Exception {
        super.doExecute();

        String id = getParameterAsString(IPRangeProfile.ID);
        org.apache.lenya.ac.IPRange ipRange = getIpRangeManager().getIPRange(id);
        if (ipRange == null) {
            throw new RuntimeException("IP range [" + id + "] not found.");
        }
        
        getIpRangeManager().remove(ipRange);
        ipRange.delete();
    }
}
