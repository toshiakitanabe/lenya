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
package org.apache.lenya.cms.cocoon.transformation;

import java.io.IOException;
import java.util.Map;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.lenya.cms.linking.LinkRewriter;
import org.apache.lenya.cms.linking.OutgoingLinkRewriter;
import org.apache.lenya.cms.repository.RepositoryUtil;
import org.apache.lenya.cms.repository.Session;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/**
 * <p>
 * Proxy transformer.
 * </p>
 * <p>
 * The resulting URLs can either be absolute (default) or relative. You can
 * either configure this when declaring the transformer:
 * </p>
 * <code><pre>
 *    &lt;map:transformer ... &gt;
 *      &lt;urls type=&quot;relative&quot;/&gt;
 *      ...
 *    &lt;/map:transformer&gt;
 * </pre></code>
 * <p>
 * or pass a parameter:
 * </p>
 * <code><pre>
 *    &lt;map:parameter name=&quot;urls&quot; value=&quot;relative&quot;/&gt;
 * </pre></code>
 */
public class ProxyTransformer extends AbstractLinkTransformer {

    protected static final String URL_TYPE_ABSOLUTE = "absolute";
    protected static final String URL_TYPE_RELATIVE = "relative";
    protected static final String PARAMETER_URLS = "urls";

    private boolean relativeUrls = false;
    private LinkRewriter rewriter;

    public void setup(SourceResolver _resolver, Map _objectModel, String _source,
            Parameters _parameters) throws ProcessingException, SAXException, IOException {
        super.setup(_resolver, _objectModel, _source, _parameters);
        Request _request = ObjectModelHelper.getRequest(_objectModel);

        try {
            if (_parameters.isParameter(PARAMETER_URLS)) {
                setUrlType(_parameters.getParameter(PARAMETER_URLS));
            }
            Session session = RepositoryUtil.getSession(this.manager, _request);
            this.rewriter = new OutgoingLinkRewriter(this.manager, session, _request
                    .getRequestURI(), this.relativeUrls);
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected void handleLink(String linkUrl, AttributeConfiguration config, AttributesImpl newAttrs)
            throws Exception {
        if (this.rewriter.matches(linkUrl)) {
            setAttribute(newAttrs, config.attribute, this.rewriter.rewrite(linkUrl));
        }
    }

    public void configure(Configuration config) throws ConfigurationException {
        super.configure(config);
        Configuration urlConfig = config.getChild(PARAMETER_URLS, false);
        if (urlConfig != null) {
            String value = urlConfig.getAttribute("type");
            setUrlType(value);
        }
    }

    protected void setUrlType(String value) throws ConfigurationException {
        if (value.equals(URL_TYPE_RELATIVE)) {
            this.relativeUrls = true;
        } else if (value.equals(URL_TYPE_ABSOLUTE)) {
            this.relativeUrls = false;
        } else {
            throw new ConfigurationException("Invalid URL type [" + value
                    + "], must be relative or absolute.");
        }
    }

}
