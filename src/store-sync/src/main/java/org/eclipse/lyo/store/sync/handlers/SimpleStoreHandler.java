/*
 * Copyright (c) 2020 Contributors to the Eclipse Foundation
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v. 1.0 which accompanies this distribution.
 *
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: EPL-1.0 OR BSD-3-Clause
 */
package org.eclipse.lyo.store.sync.handlers;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import org.eclipse.lyo.oslc4j.core.model.AbstractResource;
import org.eclipse.lyo.oslc4j.core.model.ServiceProvider;
import org.eclipse.lyo.store.Store;
import org.eclipse.lyo.store.StoreAccessException;
import org.eclipse.lyo.store.sync.Handler;
import org.eclipse.lyo.store.sync.ServiceProviderMessage;
import org.eclipse.lyo.store.sync.change.Change;
import org.eclipse.lyo.store.sync.change.ChangeKind;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is a simple handler that puts resources into named graphs per
 * {@link ServiceProvider} if the message implements
 * {@link ServiceProviderMessage}.
 * 
 * @author Andrew Berezovskyi
 * @version $version-stub$
 * @param <T>
 * @since 2.4.0
 *
 */
public class SimpleStoreHandler<T> implements Handler<T> {

    private final URI defaultGraph;

    private final Logger log = LoggerFactory.getLogger(SimpleStoreHandler.class);

    private final Store store;

    public SimpleStoreHandler(Store store) {
        this.store = store;
        try {
            this.defaultGraph = new URI("urn:x-arq:DefaultGraph");
        } catch (URISyntaxException e) {
            // this should never happen - we don't want the exception trail
            // so we cheat with wrapping this in an unmanaged exception
            // that will halt the execution.
            IllegalStateException exception = new IllegalStateException(e);
            log.error("Failed to generate default graph URI");
            throw exception;
        }
    }

    @Override
    public Collection<Change<T>> handle(Store store, Collection<Change<T>> changes) {
        if (changes == null || changes.isEmpty()) {
            log.warn("Empty change list cannot be handled");
            return changes;
        }

        Optional<Change<T>> firstChange = changes.stream().findFirst();
        if (firstChange.get().getMessage() instanceof ServiceProviderMessage) {
            Map<?, List<Change<T>>> map = changes.stream()
                    .collect(Collectors.groupingBy(c -> c.getMessage().getClass()));
            for (Entry<?, List<Change<T>>> changeSet : map.entrySet()) {
                URI spURI = ((ServiceProviderMessage) changeSet.getKey()).getServiceProviderUri();
                persistChanges(spURI, changeSet.getValue());
            }
        } else {
            persistChanges(defaultGraph, changes);
        }

        return changes;
    }

    private void persistChanges(URI spURI, Collection<Change<T>> value) {
        try {
            persistUpdates(spURI, value);
        } catch (StoreAccessException e) {
            log.error("Failed to persist updates", e);
        }

        persistDeletions(spURI, value);
    }

    private void persistDeletions(URI spURI, Collection<Change<T>> value) {
        Collection<URI> deletedResources = value.stream()
                .filter(c -> c.getHistoryResource().getChangeKindEnum() == ChangeKind.DELETION)
                .map(c -> c.getResource().getAbout()).collect(Collectors.toList());
        store.deleteResources(spURI, deletedResources.toArray(new URI[0]));
    }

    private void persistUpdates(URI spURI, Collection<Change<T>> value) throws StoreAccessException {
        Collection<AbstractResource> updatedResources = value.stream()
                .filter(c -> c.getHistoryResource().getChangeKindEnum() == ChangeKind.CREATION
                        || c.getHistoryResource().getChangeKindEnum() == ChangeKind.MODIFICATION)
                .map(c -> c.getResource()).collect(Collectors.toList());
        store.appendResources(spURI, updatedResources);
    }

}
