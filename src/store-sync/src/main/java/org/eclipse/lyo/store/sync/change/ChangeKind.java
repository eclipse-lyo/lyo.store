package org.eclipse.lyo.store.sync.change;

import org.eclipse.lyo.oslc4j.core.annotation.OslcName;
import org.eclipse.lyo.oslc4j.core.annotation.OslcNamespace;
import org.eclipse.lyo.oslc4j.core.annotation.OslcResourceShape;

/**
 * ChangeKind is .
 *
 * @author Andrew Berezovskyi (andriib@kth.se)
 * @version $version-stub$
 * @since 0.0.0
 */
@OslcNamespace(HistoryResource.NS_TRS)
@OslcName(ChangeKind.NAME)
@OslcResourceShape(title = "Change Kind Resource Shape", describes = HistoryResource.NS_TRS + ChangeKind.NAME)
public enum ChangeKind {
    // Strings taken from the TRS provider implementation
    CREATION("Created"),
    MODIFICATION("Modified"),
    DELETION("Deleted");

    public static final String NAME = "ChangeKind";
    private final String created;

    ChangeKind(String created) {
        this.created = created;
    }

    public static ChangeKind fromString(String text) {
        if (text != null) {
            for (ChangeKind kind : ChangeKind.values()) {
                if (text.equalsIgnoreCase(kind.created)) {
                    return kind;
                }
            }
        }
        throw new IllegalArgumentException("No constant with text " + text + " found");
    }

    @Override
    public String toString() {
        return created;
    }
}
