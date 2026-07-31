package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import util.UITheme;

// Left-hand navigation rail. Clicking an item calls back into MainFrame
// to switch the CardLayout content and to keep the highlighted item in sync.
public class SidebarPanel extends JPanel {

    public static final String DASHBOARD = "Dashboard";
    public static final String STUDENTS = "Students";
    public static final String ALLOCATION = "Smart Allocation";
    public static final String COMPATIBILITY = "Compatibility";
    public static final String WAITING_LIST = "Waiting List";
    public static final String HISTORY = "History";
    public static final String REPORTS = "Reports";
    public static final String PROJECT_INFO = "Project Info";

    private final Map<String, NavItem> items = new LinkedHashMap<>();
    private String selected = DASHBOARD;
    private final Consumer<String> onNavigate;

    public SidebarPanel(Consumer<String> onNavigate) {

        this.onNavigate = onNavigate;

        setLayout(new BorderLayout());
        setBackground(UITheme.SIDEBAR_BG);
        setPreferredSize(new Dimension(230, 0));
        setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        add(buildBrand(), BorderLayout.NORTH);
        add(buildNav(), BorderLayout.CENTER);
        add(buildFooter(), BorderLayout.SOUTH);
    }

    private JPanel buildBrand() {

        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 22));

        JLabel logo = new JLabel("\u2302 Smart Stay");
        logo.setForeground(Color.WHITE);
        logo.setFont(new Font(UITheme.FONT_FAMILY, Font.BOLD, 18));

        panel.add(logo);
        return panel;
    }

    private JPanel buildNav() {

        JPanel nav = new JPanel();
        nav.setOpaque(false);
        nav.setLayout(new BoxLayout(nav, BoxLayout.Y_AXIS));
        nav.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));

        addItem(nav, DASHBOARD, "\u2302");
        addItem(nav, STUDENTS, "\u2699");
        addItem(nav, ALLOCATION, "\u2318");
        addItem(nav, COMPATIBILITY, "\u2665");
        addItem(nav, WAITING_LIST, "\u23F3");
        addItem(nav, HISTORY, "\u2637");
        addItem(nav, REPORTS, "\u2637");
        addItem(nav, PROJECT_INFO, "\u2139");

        nav.add(Box.createVerticalGlue());
        return nav;
    }

    private void addItem(JPanel nav, String label, String icon) {

        NavItem item = new NavItem(label, icon);
        item.setAlignmentX(Component.LEFT_ALIGNMENT);

        item.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                select(label);
                if (onNavigate != null) {
                    onNavigate.accept(label);
                }
            }
        });

        items.put(label, item);
        nav.add(item);
        nav.add(Box.createVerticalStrut(4));

        item.setSelected(label.equals(selected));
    }

    public void select(String label) {
        selected = label;
        for (Map.Entry<String, NavItem> entry : items.entrySet()) {
            entry.getValue().setSelected(entry.getKey().equals(label));
        }
    }

    private JPanel buildFooter() {

        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 16));

        JLabel version = new JLabel("v1.0 \u00b7 DSA Edition");
        version.setForeground(UITheme.SIDEBAR_TEXT_MUTED);
        version.setFont(UITheme.fontRegular(11));

        panel.add(version);
        return panel;
    }

    // A single clickable nav row, highlighted when selected
    private static class NavItem extends JPanel {

        private boolean selectedState = false;
        private boolean hovered = false;
        private final JLabel textLabel;

        NavItem(String text, String icon) {

            setLayout(new BorderLayout());
            setOpaque(true);
            setBackground(UITheme.SIDEBAR_BG);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
            setPreferredSize(new Dimension(200, 42));
            setBorder(BorderFactory.createEmptyBorder(0, 14, 0, 8));

            textLabel = new JLabel(icon + "   " + text);
            textLabel.setForeground(UITheme.SIDEBAR_TEXT);
            textLabel.setFont(UITheme.fontRegular(13));
            textLabel.setHorizontalAlignment(SwingConstants.LEFT);

            add(textLabel, BorderLayout.CENTER);

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    hovered = true;
                    refresh();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    hovered = false;
                    refresh();
                }
            });
        }

        void setSelected(boolean value) {
            selectedState = value;
            refresh();
        }

        private void refresh() {
            if (selectedState) {
                setBackground(UITheme.SIDEBAR_SELECTED);
                textLabel.setForeground(Color.WHITE);
                textLabel.setFont(UITheme.fontBold(13));
            } else if (hovered) {
                setBackground(UITheme.SIDEBAR_HOVER);
                textLabel.setForeground(Color.WHITE);
                textLabel.setFont(UITheme.fontRegular(13));
            } else {
                setBackground(UITheme.SIDEBAR_BG);
                textLabel.setForeground(UITheme.SIDEBAR_TEXT);
                textLabel.setFont(UITheme.fontRegular(13));
            }
            repaint();
        }
    }
}
