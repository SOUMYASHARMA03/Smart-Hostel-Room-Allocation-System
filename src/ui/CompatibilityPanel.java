package ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import model.Room;
import model.Student;
import service.HostelService;
import service.RoommateMatcher;
import util.UITheme;

public class CompatibilityPanel extends JPanel implements Refreshable {

    private final HostelService hostelService = HostelService.getInstance();
    private final RoommateMatcher matcher = hostelService.getRoommateMatcher();

    private JTextField studentIdField;
    private DefaultTableModel tableModel;
    private JTable table;

    private static final String[] COLUMNS = {
            "Room No.", "Block", "Free Beds", "Sample Occupant", "Score", "Match"
    };

    public CompatibilityPanel() {

        setLayout(new BorderLayout());
        setBackground(UITheme.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));

        add(buildHeader(), BorderLayout.NORTH);

        JPanel center = new JPanel();
        center.setOpaque(false);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.add(buildQueryCard());
        center.add(Box.createVerticalStrut(16));
        center.add(buildTableCard());

        add(center, BorderLayout.CENTER);
    }

    private JPanel buildHeader() {

        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 18, 0));
        panel.add(UITheme.heading("Roommate Compatibility"));
        panel.add(Box.createVerticalStrut(4));
        panel.add(UITheme.subtext("Scores food, study habit, cleanliness, AC and year overlap for each room"));
        return panel;
    }

    private JPanel buildQueryCard() {

        UITheme.RoundedPanel card = UITheme.card();
        card.setLayout(new FlowLayout(FlowLayout.LEFT, 12, 6));

        studentIdField = new JTextField(12);
        studentIdField.setFont(UITheme.fontRegular(13));
        studentIdField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.BORDER, 1, true),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)));

        UITheme.PillButton checkBtn = UITheme.primaryButton("Check Compatibility");
        checkBtn.addActionListener(e -> runCheck());

        card.add(new JLabel("Student ID:"));
        card.add(studentIdField);
        card.add(checkBtn);

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
        scrollPane.setPreferredSize(new java.awt.Dimension(100, 400));

        card.add(scrollPane, BorderLayout.CENTER);
        return card;
    }

    private void runCheck() {

        String id = studentIdField.getText().trim();

        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter a Student ID first (e.g. ST001).",
                    "Missing Student ID", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Student student = hostelService.searchStudent(id);

        if (student == null) {
            JOptionPane.showMessageDialog(this, "No student found with ID \"" + id + "\".",
                    "Not Found", JOptionPane.WARNING_MESSAGE);
            return;
        }

        tableModel.setRowCount(0);

        for (Room room : hostelService.getRoomManager().getAllRooms()) {

            if (room.isFull()) continue;

            Student sample = null;
            String sampleLabel = "\u2014 (empty room)";

            if (!room.getOccupants().isEmpty()) {
                sample = hostelService.searchStudent(room.getOccupants().get(0));
                sampleLabel = sample != null ? sample.getName() : room.getOccupants().get(0);
            }

            int score = matcher.calculateRoomCompatibility(student, room, sample);
            String label = matcher.compatibilityLabel(score);

            tableModel.addRow(new Object[]{
                    room.getRoomNumber(),
                    room.getBlock(),
                    room.getCapacity() - room.getOccupiedBeds(),
                    sampleLabel,
                    score + "%",
                    label
            });
        }

        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No rooms with a free bed are currently available.",
                    "No Rooms", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    @Override
    public void refreshData() {
        // Compatibility is computed on demand for a chosen student;
        // nothing to preload when navigating here.
    }
}
