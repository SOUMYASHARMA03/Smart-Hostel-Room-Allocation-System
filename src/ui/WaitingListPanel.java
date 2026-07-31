package ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import model.Student;
import service.HostelService;
import util.UITheme;

public class WaitingListPanel extends JPanel implements Refreshable {

    private final HostelService hostelService = HostelService.getInstance();

    private DefaultTableModel tableModel;
    private JTable table;

    private static final String[] COLUMNS = {
            "Queue Pos.", "Student ID", "Name", "Year", "Medical Priority", "AC Pref."
    };

    public WaitingListPanel() {

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
        panel.add(UITheme.heading("Waiting List"));
        panel.add(Box.createVerticalStrut(4));
        panel.add(UITheme.subtext("Backed by a priority queue \u2014 medical cases and senior years rank first"));
        return panel;
    }

    private JPanel buildActionsCard() {

        UITheme.RoundedPanel card = UITheme.card();
        card.setLayout(new FlowLayout(FlowLayout.LEFT, 12, 6));

        UITheme.PillButton processBtn = UITheme.primaryButton("Process Next in Queue");
        processBtn.addActionListener(e -> processNext());

        card.add(processBtn);
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

    private void processNext() {

        int before = hostelService.getWaitingStudents();

        hostelService.processWaitingList();

        int after = hostelService.getWaitingStudents();

        if (after == before) {
            JOptionPane.showMessageDialog(this,
                    "No room is currently free for the next student in line.",
                    "Cannot Process", JOptionPane.WARNING_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Next student in the queue has been allocated a room.");
        }

        refreshData();
    }

    @Override
    public void refreshData() {

        tableModel.setRowCount(0);

        List<Student> waiting = hostelService.getWaitingListManager().getWaitingStudents();

        int pos = 1;
        for (Student s : waiting) {
            tableModel.addRow(new Object[]{
                    pos++,
                    s.getStudentId(),
                    s.getName(),
                    s.getYear(),
                    s.isMedicalEmergency() ? "Yes" : "No",
                    s.isAcPreference() ? "AC" : "Non-AC"
            });
        }
    }
}
