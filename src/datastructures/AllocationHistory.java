package datastructures;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;
import model.AllocationRecord;

public class AllocationHistory {

    private Stack<AllocationRecord> history;

    public AllocationHistory() {
        history = new Stack<>();
    }

    // Add New Record
    public void pushRecord(AllocationRecord record) {
        history.push(record);
    }

    // Undo Last Allocation
    public AllocationRecord undoLastRecord() {

        if (history.isEmpty()) {
            return null;
        }

        return history.pop();
    }

    // View Last Record
    public AllocationRecord peekLastRecord() {

        if (history.isEmpty()) {
            return null;
        }

        return history.peek();
    }

    // Check Empty
    public boolean isEmpty() {
        return history.isEmpty();
    }

    // Total Records
    public int getHistorySize() {
        return history.size();
    }

    // Chronological snapshot, oldest first (matches push order) — used
    // when saving to disk so reloading with pushRecord() rebuilds the
    // exact same stack order.
    public List<AllocationRecord> toChronologicalList() {
        return new ArrayList<>(history);
    }

    // Non-destructive snapshot, most recent record first (for JTable use)
    public List<AllocationRecord> toListMostRecentFirst() {

        List<AllocationRecord> list = new ArrayList<>(history);
        Collections.reverse(list);
        return list;
    }

    // Display History
    public void displayHistory() {

        if (history.isEmpty()) {
            System.out.println("No Allocation History Available.");
            return;
        }

        System.out.println("\n===== Allocation History =====");

        for (AllocationRecord record : history) {
            System.out.println(record);
            System.out.println("----------------------------");
        }
    }
}