package ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import util.UITheme;

public class AboutPanel extends JPanel implements Refreshable {

    public AboutPanel() {

        setLayout(new BorderLayout());
        setBackground(UITheme.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));

        add(buildHeader(), BorderLayout.NORTH);

        JPanel body = new JPanel();
        body.setOpaque(false);
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));

        body.add(dsaCard());
        body.add(Box.createVerticalStrut(16));
        body.add(architectureCard());
        body.add(Box.createVerticalStrut(16));
        body.add(techStackCard());
        body.add(Box.createVerticalStrut(16));
        body.add(complexityCard());
        body.add(Box.createVerticalStrut(24));

        JScrollPane scrollPane = new JScrollPane(body);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel buildHeader() {

        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 18, 0));
        panel.add(UITheme.heading("About This Project"));
        panel.add(Box.createVerticalStrut(4));
        panel.add(UITheme.subtext("Smart Stay Allocator \u2014 how it's built, and why"));
        return panel;
    }

    private JPanel sectionCard(String title, String subtitle) {

        UITheme.RoundedPanel card = UITheme.card();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(UITheme.fontBold(16));
        titleLabel.setForeground(UITheme.TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(titleLabel);

        if (subtitle != null) {
            JLabel subtitleLabel = UITheme.subtext(subtitle);
            subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            subtitleLabel.setBorder(BorderFactory.createEmptyBorder(2, 0, 12, 0));
            card.add(subtitleLabel);
        } else {
            card.add(Box.createVerticalStrut(12));
        }

        return card;
    }

    private JPanel dsaCard() {

        JPanel card = sectionCard("Core Data Structures", "Every feature is backed by a specific structure, not a generic list");

        card.add(dsaRow("Doubly Linked List", "Student registry",
                "datastructures/StudentLinkedList.java",
                "O(1) insert at either end, traversal in both directions for listing/searching students",
                UITheme.PRIMARY));

        card.add(Box.createVerticalStrut(10));

        card.add(dsaRow("HashMap", "Room lookup",
                "manager/RoomManager.java",
                "O(1) average lookup by room number instead of scanning every room",
                UITheme.ACCENT_TEAL));

        card.add(Box.createVerticalStrut(10));

        card.add(dsaRow("Priority Queue (binary heap)", "Waiting list",
                "manager/WaitingListManager.java",
                "Highest-priority student (medical emergency, then senior year) is served first, O(log n) insert/remove",
                UITheme.WARNING));

        card.add(Box.createVerticalStrut(10));

        card.add(dsaRow("Stack", "Allocation history",
                "datastructures/AllocationHistory.java",
                "LIFO order matches \"undo the most recent action\" naturally, O(1) push/pop",
                UITheme.DANGER));

        return card;
    }

    private JPanel dsaRow(String structure, String feature, String location, String why, java.awt.Color accent) {

        JPanel row = new JPanel();
        row.setOpaque(false);
        row.setLayout(new BoxLayout(row, BoxLayout.Y_AXIS));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 0));
        row.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 3, 0, 0, accent),
                BorderFactory.createEmptyBorder(2, 12, 2, 0)));

        JLabel headline = new JLabel(structure + "  \u2192  " + feature);
        headline.setFont(UITheme.fontBold(13));
        headline.setForeground(UITheme.TEXT_PRIMARY);
        headline.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel loc = new JLabel(location);
        loc.setFont(UITheme.fontRegular(11));
        loc.setForeground(accent.darker());
        loc.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel whyLabel = new JLabel("<html><body style='width:640px'>" + why + "</body></html>");
        whyLabel.setFont(UITheme.fontRegular(12));
        whyLabel.setForeground(UITheme.TEXT_MUTED);
        whyLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        whyLabel.setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));

        row.add(headline);
        row.add(loc);
        row.add(whyLabel);

        return row;
    }

    private JPanel architectureCard() {

        JPanel card = sectionCard("Architecture", "Layered packages, each with one job");

        String[][] layers = {
                {"model", "Plain data classes \u2014 Student, Room, AllocationRecord"},
                {"datastructures", "The hand-built structures \u2014 linked list, stack"},
                {"manager", "Owns a structure and exposes safe operations on it \u2014 rooms, waiting list"},
                {"service", "HostelService orchestrates everything (singleton); RoommateMatcher scores compatibility"},
                {"util", "Validation, file persistence, and the shared UI theme"},
                {"ui", "Swing screens \u2014 one JPanel per sidebar page, swapped via CardLayout"}
        };

        JPanel grid = new JPanel(new GridLayout(layers.length, 1, 0, 8));
        grid.setOpaque(false);
        grid.setAlignmentX(Component.LEFT_ALIGNMENT);

        for (String[] layer : layers) {
            JLabel row = new JLabel("<html><b>" + layer[0] + "</b> \u2014 " + layer[1] + "</html>");
            row.setFont(UITheme.fontRegular(13));
            row.setForeground(UITheme.TEXT_PRIMARY);
            grid.add(row);
        }

        card.add(grid);
        return card;
    }

    private JPanel techStackCard() {

        JPanel card = sectionCard("Tech Stack", null);

        JLabel text = new JLabel(
                "<html><body style='width:700px'>"
                        + "Java (Swing) for the UI, laid out as a single window using <b>CardLayout</b> "
                        + "so every screen is a swap of the center panel rather than a separate JFrame. "
                        + "No database or web server \u2014 all state lives in the data structures above, "
                        + "with allocation history and room/student snapshots optionally saved to plain text "
                        + "files under <b>data/</b>."
                        + "</body></html>");
        text.setFont(UITheme.fontRegular(13));
        text.setForeground(UITheme.TEXT_PRIMARY);

        card.add(text);
        return card;
    }

    private JPanel complexityCard() {

        JPanel card = sectionCard("Complexity Cheat Sheet", "Handy for quick recall during a viva/demo");

        String[][] rows = {
                {"Doubly Linked List", "Insert O(1)", "Search / Remove O(n)"},
                {"HashMap", "Get / Put O(1) avg", "Worst case O(n)"},
                {"Priority Queue (heap)", "Insert / Remove O(log n)", "Peek O(1)"},
                {"Stack", "Push / Pop O(1)", "Peek O(1)"}
        };

        JPanel grid = new JPanel(new GridLayout(rows.length, 1, 0, 6));
        grid.setOpaque(false);
        grid.setAlignmentX(Component.LEFT_ALIGNMENT);

        for (String[] r : rows) {
            JLabel row = new JLabel("<html><b>" + r[0] + "</b> \u2014 " + r[1] + ", " + r[2] + "</html>");
            row.setFont(UITheme.fontRegular(13));
            row.setForeground(UITheme.TEXT_PRIMARY);
            grid.add(row);
        }

        card.add(grid);
        return card;
    }

    @Override
    public void refreshData() {
        // Static reference content \u2014 nothing to reload
    }
}
