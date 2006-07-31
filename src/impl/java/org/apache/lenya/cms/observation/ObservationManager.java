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
package org.apache.lenya.cms.observation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.avalon.framework.logger.AbstractLogEnabled;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.avalon.framework.service.Serviceable;
import org.apache.avalon.framework.thread.ThreadSafe;
import org.apache.lenya.ac.Identity;
import org.apache.lenya.cms.publication.Document;
import org.apache.lenya.cms.publication.DocumentFactory;
import org.apache.lenya.cms.publication.DocumentUtil;
import org.apache.lenya.cms.publication.Publication;
import org.apache.lenya.cms.publication.PublicationUtil;
import org.apache.lenya.cms.repository.Node;
import org.apache.lenya.cms.repository.RepositoryUtil;
import org.apache.lenya.cms.repository.Session;

/**
 * Observation manager. Works as an observation registry and sends the notifications.
 */
public class ObservationManager extends AbstractLogEnabled implements ObservationRegistry,
        ThreadSafe, Serviceable {

    private Map identifier2listeners = new HashMap();
    private Map identifier2doc = new HashMap();
    private Set listeners = new HashSet();

    public synchronized void registerListener(RepositoryListener listener, Document doc)
            throws ObservationException {
        Set listeners = getListeners(doc);
        if (listeners.contains(listener)) {
            throw new ObservationException("The listener [" + listener
                    + "] is already registered for the document [" + doc + "].");
        }
        listeners.add(listener);
    }

    protected Set getListeners(Document doc) {
        Set listeners = (Set) this.identifier2listeners.get(doc.getIdentifier());
        if (listeners == null) {
            listeners = new HashSet();
            this.identifier2listeners.put(doc.getIdentifier(), listeners);
            this.identifier2doc.put(doc.getIdentifier(), doc);
        }
        return listeners;
    }

    public synchronized void registerListener(RepositoryListener listener)
            throws ObservationException {
        if (this.listeners.contains(listener)) {
            throw new ObservationException("The listener [" + listener + "] is already registered.");
        }
        this.listeners.add(listener);
    }

    public void documentChanged(Document doc) {
        RepositoryEvent event = new RepositoryEvent(doc);
        Set allListeners = getAllListeners(doc);
        Notifier notifier = new Notifier(allListeners, event) {
            public void notify(RepositoryListener listener, RepositoryEvent event) {
                listener.documentChanged(event);
            }
        };
        new Thread(notifier).run();
    }

    public void documentRemoved(Document doc) {
        RepositoryEvent event = new RepositoryEvent(doc);
        Set allListeners = getAllListeners(doc);
        Notifier notifier = new Notifier(allListeners, event) {
            public void notify(RepositoryListener listener, RepositoryEvent event) {
                listener.documentRemoved(event);
            }
        };
        new Thread(notifier).run();
    }

    protected Set getAllListeners(Document doc) {
        Set allListeners = new HashSet();
        synchronized (this) {
            allListeners.addAll(this.listeners);
            allListeners.addAll(getListeners(doc));
        }
        return allListeners;
    }

    public abstract class Notifier implements Runnable {

        private Set listeners;
        private RepositoryEvent event;

        public Notifier(Set listeners, RepositoryEvent event) {
            this.listeners = listeners;
            this.event = event;
        }

        public void run() {
            for (Iterator i = listeners.iterator(); i.hasNext();) {
                RepositoryListener listener = (RepositoryListener) i.next();
                notify(listener, event);
            }
        }
        
        public abstract void notify(RepositoryListener listener, RepositoryEvent event);
    }

    public void nodeChanged(Node node, Identity identity) {
        Document doc = getDocument(node, identity);
        if (doc != null) {
            documentChanged(doc);
        }
    }

    public void nodeRemoved(Node node, Identity identity) {
        Document doc = getDocument(node, identity);
        if (doc != null) {
            documentRemoved(doc);
        }
    }

    protected Document getDocument(Node node, Identity identity) {
        
        if (node.getSourceURI().endsWith(".xml")) {
            return null;
        }
        
        Document doc = null;

        final String sourceUri = node.getSourceURI();
        if (!sourceUri.startsWith("lenya://")) {
            throw new IllegalStateException("The source URI [" + sourceUri
                    + "] doesn't start with lenya://");
        }

        String path = sourceUri.substring("lenya://lenya/pubs/".length());

        String[] steps = path.split("/");
        String pubId = steps[0];
        String area = steps[2];

        try {

            Publication pub = PublicationUtil.getPublication(this.manager, pubId);

            Session session = RepositoryUtil.createSession(manager, identity);
            DocumentFactory factory = DocumentUtil.createDocumentIdentityMap(this.manager, session);
            
            String docPath = path.substring((pubId + "/content/" + area).length());
            
            String uuid;
            if (docPath.charAt(docPath.length() - 3) == '_') {
                uuid = docPath.substring(0, docPath.length() - "/index_en".length());
            }
            else {
                uuid = docPath.substring(1, docPath.length() - "/en".length());
            }
            
            String language = docPath.substring(docPath.length() - "en".length());
            
            doc = factory.get(pub, area, uuid, language);

            if (doc == null) {
                // this happens if the node was not a document node
                this.getLogger().info("No document found for node [" + sourceUri + "]");
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return doc;
    }

    private ServiceManager manager;

    public void service(ServiceManager manager) throws ServiceException {
        this.manager = manager;
    }

}
