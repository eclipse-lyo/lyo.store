package org.eclipse.lyo.store.update;

import java.util.Collection;
import org.eclipse.lyo.store.Store;
import org.eclipse.lyo.store.update.change.Change;

/**
 * Handler is .
 *
 * @author Andrew Berezovskyi (andriib@kth.se)
 * @version $version-stub$
 * @since 0.0.0
 */
public interface Handler<M> {
    Collection<Change<M>> handle(Store store, Collection<Change<M>> changes);
}
