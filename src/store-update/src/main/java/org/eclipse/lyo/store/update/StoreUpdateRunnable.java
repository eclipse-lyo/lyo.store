package org.eclipse.lyo.store.update;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import org.eclipse.lyo.store.Store;
import org.eclipse.lyo.store.update.change.Change;
import org.eclipse.lyo.store.update.change.ChangeProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Requests latest {@link Change} collection from {@link ChangeProvider} and triggers each
 * handler afterwards. Stores the timestamp of the newest change to request new updates starting
 * from that point in time.
 * <p>
 * <p>Handlers will be notified with different message per service provider as accessible via
 * {@link OSLCMessage#getServiceProviderId()}.</p>
 *
 * @author Andrew Berezovskyi (andriib@kth.se)
 * @version $version-stub$
 * @since 0.18.0
 */
class StoreUpdateRunnable<M extends OSLCMessage> implements Runnable {

    private static final ZonedDateTime LAST_UPDATE_DEFAULT = ZonedDateTime.of(1971, 1, 1, 1, 1, 1,
            1, ZoneId.of("UTC"));
    private final Logger log = LoggerFactory.getLogger(StoreUpdateRunnable.class);
    private final ChangeProvider<M> changeProvider;
    private final M message;
    private final Store store;
    private final List<Handler<M>> handlers;
    private ZonedDateTime lastUpdate;

    /**
     * @param store          Initialised Store
     * @param changeProvider Change provider
     * @param lastUpdate     Initial timestamp. If null, will be set to {@link
     *                       StoreUpdateRunnable#LAST_UPDATE_DEFAULT}
     * @param message        Initial message to the {@link ChangeProvider}
     * @param handlers       List of handlers to be notified.
     */
    StoreUpdateRunnable(final Store store, final ChangeProvider<M> changeProvider,
            final ZonedDateTime lastUpdate, final M message, final List<Handler<M>> handlers) {
        this.store = store;
        this.changeProvider = changeProvider;
        this.message = message;
        this.handlers = handlers;
        if (lastUpdate != null) {
            this.lastUpdate = lastUpdate;
        } else {
            this.lastUpdate = StoreUpdateRunnable.LAST_UPDATE_DEFAULT;
        }
    }

    @Override
    public void run() {
        try {
            log.trace("Running background update");
            Collection<Change<M>> changes = null;
            try {
                changes = changeProvider.getChangesSince(lastUpdate, message);
            } catch (final Exception e) {
                log.error("ChangeProvider threw an exception", e);
            }
            if (changes != null && !changes.isEmpty()) {
                for (final Handler<M> handler : handlers) {
                    log.trace("Notifying {}", handler);
                    try {
                        handler.handle(store, changes);
                    } catch (final Exception e) {
                        log.warn("Handler {} threw an exception", handler, e);
                    }
                }
                for (final Change change : changes) {
                    if (change != null) {
                        final Date date = change.getHistoryResource().getTimestamp();
                        final ZonedDateTime dateTime = date.toInstant()
                                .atZone(ZoneId.systemDefault());
                        if (lastUpdate.isBefore(dateTime)) {
                            lastUpdate = dateTime;
                        }
                    }
                }
                log.trace("Setting previous revision to {}", lastUpdate);
            }
        } catch (final Exception e) {
            // ExecutorService will terminate the whole schedule if a Runnable throws an exception
            log.error("A handler threw an exception!", e);
        }
    }
}
