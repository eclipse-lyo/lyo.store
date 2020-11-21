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

import org.eclipse.lyo.oslc4j.core.model.AbstractResource;

/**
 * Change is .
 *
 * @version $version-stub$
 * @since 4.0.0
 */
public class Change<T> {
    /**
     * Not IExtendedResource due to the use of AbstractResource throughout Lyo generated code.
     */
    private final AbstractResource resource;
    private final HistoryResource historyResource;
    private final T message;

    public Change(AbstractResource resource, HistoryResource historyResource, T message) {
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

    public T getMessage() {
        return message;
    }
}
