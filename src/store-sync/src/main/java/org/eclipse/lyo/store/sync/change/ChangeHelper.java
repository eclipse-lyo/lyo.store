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

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * ChangeHelper is .
 *
 * @author Andrew Berezovskyi (andriib@kth.se)
 * @version $version-stub$
 * @since 4.0.0
 */
public class ChangeHelper {

    public static List<Change> changesCreated(Collection<Change> allChanges) {
        return allChanges.stream()
                .filter(change -> Objects.equals(change.getHistoryResource().getChangeKindEnum(), ChangeKind.CREATION))
                .collect(Collectors.toList());
    }

    public static List<Change> changesModified(Collection<Change> allChanges) {
        return allChanges.stream().filter(
                change -> Objects.equals(change.getHistoryResource().getChangeKindEnum(), ChangeKind.MODIFICATION))
                .collect(Collectors.toList());
    }

    public static List<HistoryResource> historyFrom(Collection<Change> changes) {
        return ChangeHelper.mapFn(changes, Change::getHistoryResource);
    }

    public static <T, R> List<R> mapFn(Collection<T> changes, Function<T, R> mapper) {
        return changes.stream().map(mapper).collect(Collectors.toList());
    }
}
