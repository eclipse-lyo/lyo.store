package org.eclipse.lyo.store;

/*-
 * #%L
 * Contributors:
 *     Jad El-khoury - initial implementation
 * %%
 * Copyright (C) 2020 KTH Royal Institute of Technology
 * %%
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * #L%
 */

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.lyo.store.Store;
import org.eclipse.lyo.store.StoreFactory;

public class StorePool {

    private int initialPoolSize;
    private URI defaultNamedGraphUri;
    private List<Store> storePool;
    private List<Store> usedStores;
    private Object lock = new Object();

    public StorePool (int initialPoolSize, URI defaultNamedGraphUri, URI sparqlQueryEndpoint, URI sparqlUpdateEndpoint, String userName, String password) {
        this.initialPoolSize = initialPoolSize; 
        this.defaultNamedGraphUri = defaultNamedGraphUri;
        this.storePool = new ArrayList<>(this.initialPoolSize);
        this.usedStores = new ArrayList<>();
        for (int i = 0; i < this.initialPoolSize; i++) {
            Store s = null;
            if( userName != null && password != null ){
                s = StoreFactory.sparql(sparqlQueryEndpoint.toString(), sparqlUpdateEndpoint.toString(), userName, password);
            }else{
                s = StoreFactory.sparql(sparqlQueryEndpoint.toString(), sparqlUpdateEndpoint.toString());
            }
            storePool.add(s);
        }
    }

    public URI getDefaultNamedGraphUri() {
        return defaultNamedGraphUri;
    }

    public Store getStore() {
        if (storePool.isEmpty()) {
            throw new RuntimeException("Maximum pool size reached, no available store connections!");
        }
        Store s;
        synchronized(lock) {
            if (storePool.isEmpty()) {
                throw new RuntimeException("Maximum pool size reached, no available store connections!");
            }
            s = storePool.remove(storePool.size() - 1);
            usedStores.add(s);
        }
        return s;
    }
     
    public boolean releaseStore(Store store) {
        synchronized(lock) {
            storePool.add(store);
            return usedStores.remove(store);
        }
    }
}
