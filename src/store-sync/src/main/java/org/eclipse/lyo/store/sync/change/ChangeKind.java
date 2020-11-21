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
package org.eclipse.lyo.store.sync.change;

import org.eclipse.lyo.oslc4j.core.annotation.OslcName;
import org.eclipse.lyo.oslc4j.core.annotation.OslcNamespace;
import org.eclipse.lyo.oslc4j.core.annotation.OslcResourceShape;

/**
 * ChangeKind is .
 *
 * @author Andrew Berezovskyi (andriib@kth.se)
 * @version $version-stub$
 * @since 4.0.0
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
