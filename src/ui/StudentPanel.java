package ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import model.Student;
import service.HostelService;
import util.UITheme;
import util.Validation;

public class StudentPanel extends JPanel implements Refreshable {

    private final HostelService hostelService = HostelService.getInstance();

    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField searchField;

    private static final String[] COLUMNS = {
            "Student ID", "Reg. No.", "Name", "Gender", "Year",
            "Branch", "Phone", "Room", "Status"
    };

    public StudentPanel() {

        setLayout(new BorderLayout());
        setBackground(UITheme.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));

        add(buildHeader(), BorderLayout.NORTH);
        add(buildTableCard(), BorderLayout.CENTER);
    }

    private JPanel buildHeader() {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 18, 0));

        JPanel titleBox = new JPanel();
        titleBox.setOpaque(false);
        titleBox.setLayout(new BoxLayout(titleBox, BoxLayout.Y_AXIS));
        titleBox.add(UITheme.heading("Students"));
        titleBox.add(Box.createVerticalStrut(4));
        titleBox.add(UITheme.subtext("Registry backed by a doubly linked list"));
        panel.add(titleBox, BorderLayout.WEST);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actions.setOpaque(false);

        searchField = new JTextField(16);
        searchField.setFont(UITheme.fontRegular(13));
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.BORDER, 1, true),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)));

        UITheme.PillButton searchBtn = UITheme.mutedButton("Search");
        searchBtn.addActionListener(e -> searchStudent());

        UITheme.PillButton clearBtn = UITheme.mutedButton("Show All");
        clearBtn.addActionListener(e -> refreshData());

        UITheme.PillButton addBtn = UITheme.primaryButton("+ Add Student");
        addBtn.addActionListener(e -> openRegisterDialog());

        actions.add(searchField);
        actions.add(searchBtn);
        actions.add(clearBtn);
        actions.add(addBtn);

        panel.add(actions, BorderLayout.EAST);
        return panel;
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
        table.setRowHeight(30);
        table.setFont(UITheme.fontRegular(13));
        table.setSelectionBackground(new java.awt.Color(0xE9, 0xEF, 0xFF));
        table.getTableHeader().setFont(UITheme.fontBold(12));
        table.setShowVerticalLines(false);
        table.setGridColor(UITheme.BORDER);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        card.add(scrollPane, BorderLayout.CENTER);
        card.add(buildRowActions(), BorderLayout.SOUTH);

        return card;
    }

    private JPanel buildRowActions() {

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 12));
        panel.setOpaque(false);

        UITheme.PillButton allocateBtn = UITheme.successButton("Allocate Room");
        allocateBtn.addActionListener(e -> allocateSelected());

        UITheme.PillButton vacateBtn = UITheme.mutedButton("Vacate Room");
        vacateBtn.addActionListener(e -> vacateSelected());

        UITheme.PillButton removeBtn = UITheme.dangerButton("Remove Student");
        removeBtn.addActionListener(e -> removeSelected());

        panel.add(allocateBtn);
        panel.add(vacateBtn);
        panel.add(removeBtn);

        return panel;
    }

    private String selectedStudentId() {

        int row = table.getSelectedRow();

        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a student from the table first.",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        return (String) tableModel.getValueAt(row, 0);
    }

    private void allocateSelected() {

        String studentId = selectedStudentId();
        if (studentId == null) return;

        boolean allocated = hostelService.allocateRoom(studentId);

        if (allocated) {
            JOptionPane.showMessageDialog(this, "Room allocated successfully.");
        } else {
            JOptionPane.showMessageDialog(this,
                    "No matching room free right now \u2014 student added to the waiting list.",
                    "Added to Waiting List", JOptionPane.INFORMATION_MESSAGE);
        }

        refreshData();
    }

    private void vacateSelected() {

        String studentId = selectedStudentId();
        if (studentId == null) return;

        boolean vacated = hostelService.vacateRoom(studentId);

        if (!vacated) {
            JOptionPane.showMessageDialog(this, "This student does not currently occupy a room.",
                    "Nothing to Vacate", JOptionPane.WARNING_MESSAGE);
        }

        refreshData();
    }

    private void removeSelected() {

        String studentId = selectedStudentId();
        if (studentId == null) return;

        int confirm = JOptionPane.showConfirmDialog(this,
                "Remove student " + studentId + " from the registry?",
                "Confirm Removal", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            hostelService.removeStudent(studentId);
            refreshData();
        }
    }

    private void searchStudent() {

        String query = searchField.getText().trim();

        if (query.isEmpty()) {
            refreshData();
            return;
        }

        Student student = hostelService.searchStudent(query);
        tableModel.setRowCount(0);

        if (student != null) {
            tableModel.addRow(toRow(student));
        } else {
            JOptionPane.showMessageDialog(this, "No student found with ID \"" + query + "\".",
                    "Not Found", JOptionPane.WARNING_MESSAGE);
        }
    }

    private Object[] toRow(Student s) {
        return new Object[]{
                s.getStudentId(),
                s.getRegistrationNumber(),
                s.getName(),
                s.getGender(),
                s.getYear(),
                s.getBranch(),
                s.getPhoneNumber(),
                s.isRoomAllocated() ? String.valueOf(s.getAllocatedRoom()) : "\u2014",
                s.isRoomAllocated() ? "Allocated" : "Unassigned"
        };
    }

    @Override
    public void refreshData() {

        tableModel.setRowCount(0);

        List<Student> students = hostelService.getAllStudents();

        for (Student s : students) {
            tableModel.addRow(toRow(s));
        }
    }

    // ------------------------------------------------------------------
    // Registration dialog
    // ------------------------------------------------------------------
    private void openRegisterDialog() {

        Frame owner = (Frame) SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog(owner, "Register Student", true);
        dialog.setSize(460, 560);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(UITheme.BACKGROUND);

        JPanel form = new JPanel(new GridLayout(0, 2, 10, 10));
        form.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        form.setBackground(UITheme.BACKGROUND);

        JTextField regNoField = new JTextField();
        JTextField nameField = new JTextField();
        JComboBox<String> genderBox = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        JComboBox<Integer> yearBox = new JComboBox<>(new Integer[]{1, 2, 3, 4});
        JTextField branchField = new JTextField();
        JTextField phoneField = new JTextField();
        JTextField hostelField = new JTextField("A");
        JComboBox<String> foodBox = new JComboBox<>(new String[]{"Veg", "Non-Veg"});
        JCheckBox acBox = new JCheckBox("AC Room Preferred");
        JComboBox<String> studyBox = new JComboBox<>(new String[]{"Early Bird", "Night Owl"});
        JComboBox<String> cleanlinessBox = new JComboBox<>(new String[]{"High", "Medium", "Low"});
        JCheckBox medicalBox = new JCheckBox("Medical Emergency Priority");

        form.add(new JLabel("Registration No.:"));
        form.add(regNoField);
        form.add(new JLabel("Full Name:"));
        form.add(nameField);
        form.add(new JLabel("Gender:"));
        form.add(genderBox);
        form.add(new JLabel("Academic Year:"));
        form.add(yearBox);
        form.add(new JLabel("Branch:"));
        form.add(branchField);
        form.add(new JLabel("Phone Number:"));
        form.add(phoneField);
        form.add(new JLabel("Hostel Block:"));
        form.add(hostelField);
        form.add(new JLabel("Food Preference:"));
        form.add(foodBox);
        form.add(new JLabel("Study Habit:"));
        form.add(studyBox);
        form.add(new JLabel("Cleanliness:"));
        form.add(cleanlinessBox);
        form.add(acBox);
        form.add(medicalBox);

        dialog.add(form, BorderLayout.CENTER);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 14));
        footer.setBackground(UITheme.BACKGROUND);

        UITheme.PillButton saveBtn = UITheme.primaryButton("Register");
        saveBtn.addActionListener(e -> {

            String regNo = regNoField.getText().trim();
            String name = nameField.getText().trim();
            String phone = phoneField.getText().trim();
            int year = (Integer) yearBox.getSelectedItem();
            String branch = branchField.getText().trim();
            String hostel = hostelField.getText().trim();
            String food = (String) foodBox.getSelectedItem();
            String study = (String) studyBox.getSelectedItem();
            String cleanliness = (String) cleanlinessBox.getSelectedItem();

            boolean valid = Validation.validateStudentData(
                    name, regNo, phone, year, branch, hostel, food, study, cleanliness);

            if (!valid) {
                JOptionPane.showMessageDialog(dialog,
                        "Please check the form \u2014 name must be letters only, phone must be 10 digits, and all fields are required.",
                        "Invalid Data", JOptionPane.ERROR_MESSAGE);
                return;
            }

            hostelService.registerStudent(
                    regNo, name, (String) genderBox.getSelectedItem(), year, branch, phone,
                    hostel, food, acBox.isSelected(), study, cleanliness, medicalBox.isSelected());

            refreshData();
            dialog.dispose();
        });

        UITheme.PillButton cancelBtn = UITheme.mutedButton("Cancel");
        cancelBtn.addActionListener(e -> dialog.dispose());

        footer.add(cancelBtn);
        footer.add(saveBtn);
        dialog.add(footer, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }
}
