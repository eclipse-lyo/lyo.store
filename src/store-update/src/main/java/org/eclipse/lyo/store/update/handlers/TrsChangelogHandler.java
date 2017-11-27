package org.eclipse.lyo.store.update.handlers;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.lyo.oslc4j.trs.provider.ChangeHistories;
import org.eclipse.lyo.oslc4j.trs.provider.HistoryData;
import org.eclipse.lyo.store.Store;
import org.eclipse.lyo.store.update.Handler;
import org.eclipse.lyo.store.update.change.Change;
import org.eclipse.lyo.store.update.change.ChangeKind;
import org.eclipse.lyo.store.update.change.HistoryResource;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

public class TrsChangelogHandler<T> implements Handler<T> {

    private ChangeHistories changeLog;

    public TrsChangelogHandler(ChangeHistories changeLog) {
        this.changeLog = changeLog;
    }

    @Override
    public Collection<Change<T>> handle(Store store, Collection<Change<T>> changes) {
        updateChangelogHistory(changes);
        return changes;
    }

    protected List<HistoryData> updateChangelogHistory(Collection<Change<T>> changes) {
        List<HistoryData> changesHistory = changesHistory(changes);
        changeLog.updateHistories(changesHistory);
        return changesHistory;
    }

    private List<HistoryData> changesHistory(Collection<Change<T>> changes) {
        return changes.stream().map(this::historyElementFromChange).collect(Collectors.toList());
    }

    private HistoryData historyElementFromChange(Change<T> change) {
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
