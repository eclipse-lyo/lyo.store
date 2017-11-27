package org.eclipse.lyo.store.update.handlers;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.eclipse.lyo.oslc4j.core.model.AbstractResource;
import org.eclipse.lyo.oslc4j.core.model.ServiceProvider;
import org.eclipse.lyo.store.Store;
import org.eclipse.lyo.store.StoreAccessException;
import org.eclipse.lyo.store.update.Handler;
import org.eclipse.lyo.store.update.ServiceProviderMessage;
import org.eclipse.lyo.store.update.change.Change;
import org.eclipse.lyo.store.update.change.ChangeHelper;
import org.eclipse.lyo.store.update.change.ChangeKind;
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
