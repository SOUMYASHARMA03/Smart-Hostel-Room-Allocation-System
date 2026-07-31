package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import model.Room;
import model.Student;
import service.HostelService;
import util.UITheme;

public class RoomPanel extends JPanel implements Refreshable {

    private final HostelService hostelService = HostelService.getInstance();

    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField studentIdField;

    private static final String[] COLUMNS = {
            "Room No.", "Block", "Floor", "Capacity", "Occupied", "AC", "Status", "Occupants"
    };

    public RoomPanel() {

        setLayout(new BorderLayout());
        setBackground(UITheme.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));

        add(buildHeader(), BorderLayout.NORTH);

        JPanel center = new JPanel();
        center.setOpaque(false);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.add(buildAllocationCard());
        center.add(Box.createVerticalStrut(16));
        center.add(buildTableCard());

        add(center, BorderLayout.CENTER);
    }

    private JPanel buildHeader() {

        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 18, 0));
        panel.add(UITheme.heading("Smart Allocation"));
        panel.add(Box.createVerticalStrut(4));
        panel.add(UITheme.subtext("Rooms are stored in a HashMap keyed by room number"));
        return panel;
    }

    private JPanel buildAllocationCard() {

        UITheme.RoundedPanel card = UITheme.card();
        card.setLayout(new FlowLayout(FlowLayout.LEFT, 12, 6));

        studentIdField = new JTextField(12);
        studentIdField.setFont(UITheme.fontRegular(13));
        studentIdField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.BORDER, 1, true),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)));

        UITheme.PillButton smartBtn = UITheme.primaryButton("Smart Allocate");
        smartBtn.addActionListener(e -> smartAllocate());

        UITheme.PillButton basicBtn = UITheme.successButton("Allocate (AC Match Only)");
        basicBtn.addActionListener(e -> basicAllocate());

        UITheme.PillButton vacateBtn = UITheme.dangerButton("Vacate");
        vacateBtn.addActionListener(e -> vacate());

        card.add(new javax.swing.JLabel("Student ID:"));
        card.add(studentIdField);
        card.add(smartBtn);
        card.add(basicBtn);
        card.add(vacateBtn);

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

    private String requireStudentId() {

        String id = studentIdField.getText().trim();

        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter a Student ID first (e.g. ST001).",
                    "Missing Student ID", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        Student student = hostelService.searchStudent(id);

        if (student == null) {
            JOptionPane.showMessageDialog(this, "No student found with ID \"" + id + "\".",
                    "Not Found", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        return id;
    }

    private void smartAllocate() {

        String id = requireStudentId();
        if (id == null) return;

        boolean allocated = hostelService.smartAllocateRoom(id);

        if (allocated) {
            JOptionPane.showMessageDialog(this, "Room allocated using best roommate compatibility match.");
        } else {
            JOptionPane.showMessageDialog(this,
                    "No room currently free \u2014 student added to the waiting list.",
                    "Added to Waiting List", JOptionPane.INFORMATION_MESSAGE);
        }

        refreshData();
    }

    private void basicAllocate() {

        String id = requireStudentId();
        if (id == null) return;

        boolean allocated = hostelService.allocateRoom(id);

        if (allocated) {
            JOptionPane.showMessageDialog(this, "Room allocated successfully.");
        } else {
            JOptionPane.showMessageDialog(this,
                    "No matching room free \u2014 student added to the waiting list.",
                    "Added to Waiting List", JOptionPane.INFORMATION_MESSAGE);
        }

        refreshData();
    }

    private void vacate() {

        String id = requireStudentId();
        if (id == null) return;

        boolean vacated = hostelService.vacateRoom(id);

        if (!vacated) {
            JOptionPane.showMessageDialog(this, "This student does not currently occupy a room.",
                    "Nothing to Vacate", JOptionPane.WARNING_MESSAGE);
        }

        refreshData();
    }

    @Override
    public void refreshData() {

        tableModel.setRowCount(0);

        for (Room room : hostelService.getRoomManager().getAllRooms()) {

            tableModel.addRow(new Object[]{
                    room.getRoomNumber(),
                    room.getBlock(),
                    room.getFloor(),
                    room.getCapacity(),
                    room.getOccupiedBeds(),
                    room.isAcRoom() ? "Yes" : "No",
                    room.isAvailable() ? "Available" : "Full",
                    String.join(", ", room.getOccupants())
            });
        }
    }
}
