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
package org.eclipse.lyo.store.sync.handlers;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.lyo.oslc4j.trs.server.HistoryData;
import org.eclipse.lyo.oslc4j.trs.server.TrsEventHandler;
import org.eclipse.lyo.store.Store;
import org.eclipse.lyo.store.sync.Handler;
import org.eclipse.lyo.store.sync.change.Change;
import org.eclipse.lyo.store.sync.change.ChangeKind;
import org.eclipse.lyo.store.sync.change.HistoryResource;

public class TrsHandler<M> implements Handler<M> {

    private TrsEventHandler eventHandler;

    public TrsHandler(TrsEventHandler trsEventHandler) {
        this.eventHandler = trsEventHandler;
    }

    @Override
    public Collection<Change<M>> handle(Store store, Collection<Change<M>> changes) {
        updateChangelogHistory(changes);
        return changes;
    }

    protected List<HistoryData> updateChangelogHistory(Collection<Change<M>> changes) {
        List<HistoryData> changesHistory = changesHistory(changes);
        for (HistoryData element: changesHistory
             ) {
            if(HistoryData.CREATED.equals(element.getType()) {
                // FIXME: 2020-11-09 Either pass the resource or add "light" onCreatedMeta() resources
                eventHandler.onCreated();
            }
        }
        return changesHistory;
    }

    private List<HistoryData> changesHistory(Collection<Change<M>> changes) {
        return changes.stream().map(this::historyElementFromChange).collect(Collectors.toList());
    }

    private HistoryData historyElementFromChange(Change<M> change) {
        HistoryResource h = change.getHistoryResource();
        HistoryData historyData = HistoryData.getInstance(h.getTimestamp(), h.getResourceURI(),
                historyDataType(h.getChangeKindEnum()));
        return historyData;
    }

    private String historyDataType(ChangeKind changeKind) {
        if (changeKind.equals(ChangeKind.CREATION)) {
            return HistoryData.CREATED;
        } else if (changeKind.equals(ChangeKind.MODIFICATION)) {
            return HistoryData.MODIFIED;
        } else if (changeKind.equals(ChangeKind.DELETION)) {
            return HistoryData.DELETED;
        } else {
            throw new IllegalArgumentException("Illegal ChangeKind value");
        }
    }

}
