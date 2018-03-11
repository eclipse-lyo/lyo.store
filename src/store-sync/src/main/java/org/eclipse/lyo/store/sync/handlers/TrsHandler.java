package org.eclipse.lyo.store.sync.handlers;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.lyo.oslc4j.trs.provider.ChangeHistories;
import org.eclipse.lyo.oslc4j.trs.provider.HistoryData;
import org.eclipse.lyo.store.Store;
import org.eclipse.lyo.store.sync.Handler;
import org.eclipse.lyo.store.sync.change.Change;
import org.eclipse.lyo.store.sync.change.ChangeKind;
import org.eclipse.lyo.store.sync.change.HistoryResource;

public class TrsHandler<M> implements Handler<M> {

    private ChangeHistories changeLog;

    public TrsHandler(ChangeHistories changeLog) {
        this.changeLog = changeLog;
    }

    @Override
    public Collection<Change<M>> handle(Store store, Collection<Change<M>> changes) {
        updateChangelogHistory(changes);
        return changes;
    }

    protected List<HistoryData> updateChangelogHistory(Collection<Change<M>> changes) {
        List<HistoryData> changesHistory = changesHistory(changes);
        changeLog.updateHistories(changesHistory);
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
