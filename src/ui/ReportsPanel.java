package ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import service.HostelService;
import util.FileManager;
import util.UITheme;

public class ReportsPanel extends JPanel implements Refreshable {

    private final HostelService hostelService = HostelService.getInstance();

    private JLabel totalStudentsValue;
    private JLabel totalRoomsValue;
    private JLabel availableValue;
    private JLabel occupiedValue;
    private JLabel waitingValue;
    private JLabel historyValue;
    private JLabel occupancyRateValue;

    public ReportsPanel() {

        setLayout(new BorderLayout());
        setBackground(UITheme.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));

        add(buildHeader(), BorderLayout.NORTH);

        JPanel center = new JPanel();
        center.setOpaque(false);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.add(buildSummaryCard());
        center.add(Box.createVerticalStrut(16));
        center.add(buildExportCard());

        add(center, BorderLayout.CENTER);
    }

    private JPanel buildHeader() {

        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 18, 0));
        panel.add(UITheme.heading("Reports"));
        panel.add(Box.createVerticalStrut(4));
        panel.add(UITheme.subtext("Snapshot summary and data export"));
        return panel;
    }

    private JPanel buildSummaryCard() {

        UITheme.RoundedPanel card = UITheme.card();
        card.setLayout(new BorderLayout());

        JLabel title = new JLabel("Current Snapshot");
        title.setFont(UITheme.fontBold(15));
        title.setForeground(UITheme.TEXT_PRIMARY);
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 14, 0));
        card.add(title, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(4, 2, 12, 12));
        grid.setOpaque(false);

        totalStudentsValue = new JLabel("0");
        totalRoomsValue = new JLabel("0");
        availableValue = new JLabel("0");
        occupiedValue = new JLabel("0");
        waitingValue = new JLabel("0");
        historyValue = new JLabel("0");
        occupancyRateValue = new JLabel("0%");

        grid.add(statRow("Total Students", totalStudentsValue));
        grid.add(statRow("Total Rooms", totalRoomsValue));
        grid.add(statRow("Available Rooms", availableValue));
        grid.add(statRow("Occupied Rooms", occupiedValue));
        grid.add(statRow("Waiting List Size", waitingValue));
        grid.add(statRow("Allocation Records", historyValue));
        grid.add(statRow("Occupancy Rate", occupancyRateValue));

        card.add(grid, BorderLayout.CENTER);
        return card;
    }

    private JPanel statRow(String label, JLabel value) {

        JPanel row = new JPanel();
        row.setOpaque(false);
        row.setLayout(new BoxLayout(row, BoxLayout.Y_AXIS));

        value.setFont(new Font(UITheme.FONT_FAMILY, Font.BOLD, 22));
        value.setForeground(UITheme.PRIMARY);
        value.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel caption = UITheme.subtext(label);
        caption.setAlignmentX(Component.LEFT_ALIGNMENT);

        row.add(value);
        row.add(caption);
        return row;
    }

    private JPanel buildExportCard() {

        UITheme.RoundedPanel card = UITheme.card();
        card.setLayout(new BorderLayout());

        JLabel title = new JLabel("Export Data");
        title.setFont(UITheme.fontBold(15));
        title.setForeground(UITheme.TEXT_PRIMARY);
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 14, 0));
        card.add(title, BorderLayout.NORTH);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 6));
        actions.setOpaque(false);

        UITheme.PillButton exportBtn = UITheme.primaryButton("Export to data/ Folder");
        exportBtn.addActionListener(e -> exportData());

        actions.add(exportBtn);
        actions.add(UITheme.subtext("Writes students.txt and rooms.txt (comma-separated)"));

        card.add(actions, BorderLayout.CENTER);
        return card;
    }

    private void exportData() {

        FileManager.createDataFolder();
        FileManager.saveStudents(hostelService.getStudentList(), "data/students.txt");
        FileManager.saveRooms(hostelService.getRoomManager(), "data/rooms.txt");

        JOptionPane.showMessageDialog(this,
                "Exported current students and rooms to the data/ folder.",
                "Export Complete", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void refreshData() {

        int total = hostelService.getTotalRooms();
        int occupied = hostelService.getOccupiedRooms();

        totalStudentsValue.setText(String.valueOf(hostelService.getTotalStudents()));
        totalRoomsValue.setText(String.valueOf(total));
        availableValue.setText(String.valueOf(hostelService.getAvailableRooms()));
        occupiedValue.setText(String.valueOf(occupied));
        waitingValue.setText(String.valueOf(hostelService.getWaitingStudents()));
        historyValue.setText(String.valueOf(hostelService.getAllocationHistory().getHistorySize()));

        int rate = total == 0 ? 0 : (int) Math.round((occupied * 100.0) / total);
        occupancyRateValue.setText(rate + "%");
    }
}
