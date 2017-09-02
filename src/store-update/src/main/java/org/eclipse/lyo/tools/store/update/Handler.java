package org.eclipse.lyo.tools.store.update;

import java.util.Collection;
import org.eclipse.lyo.tools.store.Store;
import org.eclipse.lyo.tools.store.update.change.Change;

/**
 * Handler is .
 *
 * @author Andrew Berezovskyi (andriib@kth.se)
 * @version $version-stub$
 * @since 0.0.0
 */
public interface Handler<M> {
    void handle(Store store, Collection<Change<M>> changes);
}
