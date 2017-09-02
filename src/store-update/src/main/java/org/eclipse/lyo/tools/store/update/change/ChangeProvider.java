package org.eclipse.lyo.tools.store.update.change;

import java.time.ZonedDateTime;
import java.util.Collection;

/**
 * ChangeProvider is .
 *
 * @author Andrew Berezovskyi (andriib@kth.se)
 * @version $version-stub$
 * @since 0.0.0
 */
public interface ChangeProvider<M> {
    Collection<Change<M>> getChangesSince(ZonedDateTime lastUpdate, M message);
}
