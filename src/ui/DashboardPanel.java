package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import model.AllocationRecord;
import service.HostelService;
import util.UITheme;

public class DashboardPanel extends JPanel implements Refreshable {

    private final HostelService hostelService = HostelService.getInstance();

    private JLabel totalStudentsValue;
    private JLabel totalRoomsValue;
    private JLabel availableRoomsValue;
    private JLabel occupiedRoomsValue;
    private JLabel waitingValue;

    private JPanel activityList;

    public DashboardPanel() {

        setLayout(new BorderLayout());
        setBackground(UITheme.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));

        add(buildHeader(), BorderLayout.NORTH);

        JPanel center = new JPanel();
        center.setOpaque(false);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.add(Box.createVerticalStrut(20));
        center.add(buildStatsRow());
        center.add(Box.createVerticalStrut(20));
        center.add(buildActivityCard());

        add(center, BorderLayout.CENTER);
    }

    private JPanel buildHeader() {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        JPanel titleBox = new JPanel();
        titleBox.setOpaque(false);
        titleBox.setLayout(new BoxLayout(titleBox, BoxLayout.Y_AXIS));

        JLabel title = UITheme.heading("Dashboard");
        JLabel subtitle = UITheme.subtext("Live overview of students, rooms and allocations");
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        titleBox.add(title);
        titleBox.add(Box.createVerticalStrut(4));
        titleBox.add(subtitle);

        panel.add(titleBox, BorderLayout.WEST);
        return panel;
    }

    private JPanel buildStatsRow() {

        JPanel row = new JPanel(new GridLayout(1, 5, 16, 0));
        row.setOpaque(false);
        row.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 110));

        totalStudentsValue = new JLabel("0");
        totalRoomsValue = new JLabel("0");
        availableRoomsValue = new JLabel("0");
        occupiedRoomsValue = new JLabel("0");
        waitingValue = new JLabel("0");

        row.add(statCard("Total Students", totalStudentsValue, UITheme.PRIMARY));
        row.add(statCard("Total Rooms", totalRoomsValue, UITheme.ACCENT_TEAL));
        row.add(statCard("Available Rooms", availableRoomsValue, UITheme.SUCCESS));
        row.add(statCard("Occupied Rooms", occupiedRoomsValue, UITheme.WARNING));
        row.add(statCard("Waiting List", waitingValue, UITheme.DANGER));

        return row;
    }

    private JPanel statCard(String label, JLabel valueLabel, Color accent) {

        UITheme.RoundedPanel card = UITheme.card();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JLabel dot = new JLabel("\u25CF");
        dot.setForeground(accent);
        dot.setFont(UITheme.fontRegular(14));
        dot.setAlignmentX(Component.LEFT_ALIGNMENT);

        valueLabel.setFont(new Font(UITheme.FONT_FAMILY, Font.BOLD, 30));
        valueLabel.setForeground(UITheme.TEXT_PRIMARY);
        valueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel caption = UITheme.subtext(label);
        caption.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(dot);
        card.add(Box.createVerticalStrut(8));
        card.add(valueLabel);
        card.add(Box.createVerticalStrut(2));
        card.add(caption);

        return card;
    }

    private JPanel buildActivityCard() {

        UITheme.RoundedPanel card = UITheme.card();
        card.setLayout(new BorderLayout());

        JLabel title = new JLabel("Recent Activity");
        title.setFont(UITheme.fontBold(15));
        title.setForeground(UITheme.TEXT_PRIMARY);
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));
        card.add(title, BorderLayout.NORTH);

        activityList = new JPanel();
        activityList.setOpaque(false);
        activityList.setLayout(new BoxLayout(activityList, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(activityList);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setPreferredSize(new java.awt.Dimension(100, 300));

        card.add(scrollPane, BorderLayout.CENTER);

        return card;
    }

    @Override
    public void refreshData() {

        totalStudentsValue.setText(String.valueOf(hostelService.getTotalStudents()));
        totalRoomsValue.setText(String.valueOf(hostelService.getTotalRooms()));
        availableRoomsValue.setText(String.valueOf(hostelService.getAvailableRooms()));
        occupiedRoomsValue.setText(String.valueOf(hostelService.getOccupiedRooms()));
        waitingValue.setText(String.valueOf(hostelService.getWaitingStudents()));

        activityList.removeAll();

        List<AllocationRecord> records = hostelService.getAllocationHistory().toListMostRecentFirst();

        if (records.isEmpty()) {
            JLabel empty = UITheme.subtext("No activity yet \u2014 register a student and allocate a room to get started.");
            activityList.add(empty);
        } else {
            int shown = 0;
            for (AllocationRecord record : records) {
                if (shown >= 12) break;
                activityList.add(activityRow(record));
                activityList.add(Box.createVerticalStrut(6));
                shown++;
            }
        }

        activityList.revalidate();
        activityList.repaint();
    }

    private JPanel activityRow(AllocationRecord record) {

        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setBorder(BorderFactory.createEmptyBorder(6, 0, 6, 0));
        row.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 34));

        Color color = record.getAction().startsWith("VACATED") ? UITheme.DANGER : UITheme.SUCCESS;

        JLabel text = new JLabel(record.getStudentId() + "  \u2014  Room " + record.getRoomNumber());
        text.setFont(UITheme.fontRegular(13));
        text.setForeground(UITheme.TEXT_PRIMARY);

        JLabel action = UITheme.badge(record.getAction(), color);

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        left.setOpaque(false);
        left.add(text);

        row.add(left, BorderLayout.WEST);
        row.add(action, BorderLayout.EAST);

        return row;
    }
}
