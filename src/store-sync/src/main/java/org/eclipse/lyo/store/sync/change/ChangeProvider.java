package org.eclipse.lyo.store.sync.change;

import java.time.ZonedDateTime;
import java.util.Collection;

/**
 * ChangeProvider is .
 *
 * @author Andrew Berezovskyi (andriib@kth.se)
 * @version $version-stub$
 * @since 0.0.0
 */
public interface ChangeProvider<T> {
    Collection<Change<T>> getChangesSince(ZonedDateTime lastUpdate, T message);
}
