package ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import model.AllocationRecord;
import service.HostelService;
import util.UITheme;

public class HistoryPanel extends JPanel implements Refreshable {

    private final HostelService hostelService = HostelService.getInstance();
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    private DefaultTableModel tableModel;
    private JTable table;

    private static final String[] COLUMNS = {
            "Student ID", "Room No.", "Action", "Timestamp"
    };

    public HistoryPanel() {

        setLayout(new BorderLayout());
        setBackground(UITheme.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));

        add(buildHeader(), BorderLayout.NORTH);

        JPanel center = new JPanel();
        center.setOpaque(false);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.add(buildActionsCard());
        center.add(Box.createVerticalStrut(16));
        center.add(buildTableCard());

        add(center, BorderLayout.CENTER);
    }

    private JPanel buildHeader() {

        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 18, 0));
        panel.add(UITheme.heading("Allocation History"));
        panel.add(Box.createVerticalStrut(4));
        panel.add(UITheme.subtext("Backed by a Stack \u2014 most recent action first, undo pops the top"));
        return panel;
    }

    private JPanel buildActionsCard() {

        UITheme.RoundedPanel card = UITheme.card();
        card.setLayout(new FlowLayout(FlowLayout.LEFT, 12, 6));

        UITheme.PillButton undoBtn = UITheme.dangerButton("Undo Last Action");
        undoBtn.addActionListener(e -> undoLast());

        card.add(undoBtn);
        return card;
    }

    private JPanel buildTableCard() {

        UITheme.RoundedPanel card = UITheme.card();
        card.setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(COLUMNS, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setRowHeight(28);
        table.setFont(UITheme.fontRegular(13));
        table.getTableHeader().setFont(UITheme.fontBold(12));
        table.setShowVerticalLines(false);
        table.setGridColor(UITheme.BORDER);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setPreferredSize(new java.awt.Dimension(100, 420));

        card.add(scrollPane, BorderLayout.CENTER);
        return card;
    }

    private void undoLast() {

        if (hostelService.getAllocationHistory().isEmpty()) {
            JOptionPane.showMessageDialog(this, "There is no history to undo.",
                    "Nothing to Undo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        AllocationRecord undone = hostelService.undoLastAction();

        if (undone != null) {
            JOptionPane.showMessageDialog(this,
                    "Reversed: " + undone.getAction() + " for " + undone.getStudentId()
                            + " (Room " + undone.getRoomNumber() + ")");
        }

        refreshData();
    }

    @Override
    public void refreshData() {

        tableModel.setRowCount(0);

        List<AllocationRecord> records = hostelService.getAllocationHistory().toListMostRecentFirst();

        for (AllocationRecord record : records) {
            tableModel.addRow(new Object[]{
                    record.getStudentId(),
                    record.getRoomNumber(),
                    record.getAction(),
                    record.getTimestamp().format(TIME_FORMAT)
            });
        }
    }
}
