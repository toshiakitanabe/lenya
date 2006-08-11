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
package org.apache.lenya.cms.site.simple;

import org.apache.avalon.framework.service.ServiceSelector;
import org.apache.lenya.ac.impl.AbstractAccessControlTest;
import org.apache.lenya.cms.publication.Document;
import org.apache.lenya.cms.publication.DocumentFactory;
import org.apache.lenya.cms.publication.DocumentManager;
import org.apache.lenya.cms.publication.DocumentUtil;
import org.apache.lenya.cms.publication.Publication;
import org.apache.lenya.cms.publication.PublicationUtil;
import org.apache.lenya.cms.publication.ResourceType;
import org.apache.lenya.cms.repository.RepositoryUtil;
import org.apache.lenya.cms.repository.Session;
import org.apache.lenya.cms.site.Link;
import org.apache.lenya.cms.site.SiteException;
import org.apache.lenya.cms.site.SiteManager;
import org.apache.lenya.cms.site.SiteNode;
import org.apache.lenya.cms.site.SiteStructure;

public class SimpleSiteManagerTest extends AbstractAccessControlTest {

    public void testSimpleSiteManager() throws Exception {

        login("lenya");

        Session session = RepositoryUtil.getSession(getManager(), getRequest());
        DocumentFactory factory = DocumentUtil.createDocumentIdentityMap(getManager(), session);

        Publication pub = PublicationUtil.getPublication(getManager(), "blog");

        DocumentManager docManager = null;
        ServiceSelector selector = null;
        SiteManager siteManager = null;
        ServiceSelector resourceTypeSelector = null;

        try {
            selector = (ServiceSelector) getManager().lookup(SiteManager.ROLE + "Selector");
            siteManager = (SiteManager) selector.select(pub.getSiteManagerHint());
            SiteStructure structure = siteManager.getSiteStructure(factory,
                    pub,
                    Publication.AUTHORING_AREA);

            assertTrue(structure.contains("/sidebar"));

            SiteNode node = structure.getNode("/sidebar");
            assertNotNull(node);
            assertNotNull(node.getUuid());

            checkLinks(siteManager, node);

            docManager = (DocumentManager) getManager().lookup(DocumentManager.ROLE);

            resourceTypeSelector = (ServiceSelector) getManager().lookup(ResourceType.ROLE
                    + "Selector");
            ResourceType type = (ResourceType) resourceTypeSelector.select("entry");
            String contentSourceUri = structure.getRepositoryNode().getSourceURI();

            Document doc = docManager.add(getIdentityMap(),
                    type,
                    contentSourceUri,
                    pub,
                    Publication.AUTHORING_AREA,
                    "en",
                    "xml");

            structure.add("/foo", doc);

        } finally {
            if (selector != null) {
                if (siteManager != null) {
                    selector.release(siteManager);
                }
                getManager().release(selector);
            }
            if (docManager != null) {
                getManager().release(docManager);
            }
            if (resourceTypeSelector != null) {
                getManager().release(resourceTypeSelector);
            }
        }
        session.commit();
    }

    protected void checkLinks(SiteManager siteManager, SiteNode node) throws SiteException {
        String[] languages = node.getLanguages();
        assertTrue(languages.length > 0);
        for (int i = 0; i < languages.length; i++) {
            Link link = node.getLink(languages[i]);
            assertEquals(link.getLanguage(), languages[i]);
            assertNotNull(link.getLabel());
            Document doc = link.getDocument();
            assertNotNull(doc);

            String docUuid = doc.getUUID();
            String nodeUuid = node.getUuid();

            assertNotNull(doc.getUUID());
            assertEquals(docUuid, nodeUuid);
            assertEquals(doc.getLanguage(), link.getLanguage());

            try {
                siteManager.add("/sidebar", doc);
                assertTrue("No exception thrown", false);
            } catch (Exception expected) {
            }
        }
    }

}