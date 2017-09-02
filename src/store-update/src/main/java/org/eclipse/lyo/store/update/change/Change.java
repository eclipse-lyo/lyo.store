package org.eclipse.lyo.store.update.change;

import org.eclipse.lyo.oslc4j.core.model.AbstractResource;

/**
 * Change is .
 *
 * @author Andrew Berezovskyi (andriib@kth.se)
 * @version $version-stub$
 * @since 0.0.0
 */
public class Change<M> {
    private final AbstractResource resource;
    private final HistoryResource historyResource;
    private final M message;

    public Change(AbstractResource resource, HistoryResource historyResource, M message) {
        this.resource = resource;
        this.historyResource = historyResource;
        this.message = message;
    }

    public AbstractResource getResource() {
        return resource;
    }

    public HistoryResource getHistoryResource() {
        return historyResource;
    }

    public M getMessage() {
        return message;
    }
}
