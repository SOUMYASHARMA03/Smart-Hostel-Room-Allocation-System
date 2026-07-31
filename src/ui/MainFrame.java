package ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;

import util.UITheme;

public class MainFrame extends JFrame {

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel contentPanel = new JPanel(cardLayout);

    private final DashboardPanel dashboardPanel = new DashboardPanel();
    private final StudentPanel studentPanel = new StudentPanel();
    private final RoomPanel roomPanel = new RoomPanel();
    private final CompatibilityPanel compatibilityPanel = new CompatibilityPanel();
    private final WaitingListPanel waitingListPanel = new WaitingListPanel();
    private final HistoryPanel historyPanel = new HistoryPanel();
    private final ReportsPanel reportsPanel = new ReportsPanel();
    private final AboutPanel aboutPanel = new AboutPanel();

    private SidebarPanel sidebarPanel;

    public MainFrame() {

        setTitle("Smart Stay Allocator \u2014 Hostel Room Allocation System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int w = Math.min(1280, screen.width - 60);
        int h = Math.min(800, screen.height - 60);
        setSize(w, h);
        setMinimumSize(new Dimension(1000, 640));
        setLocationRelativeTo(null);

        getContentPane().setBackground(UITheme.BACKGROUND);
        setLayout(new BorderLayout());

        sidebarPanel = new SidebarPanel(this::navigateTo);
        add(sidebarPanel, BorderLayout.WEST);

        contentPanel.setBackground(UITheme.BACKGROUND);
        contentPanel.add(dashboardPanel, SidebarPanel.DASHBOARD);
        contentPanel.add(studentPanel, SidebarPanel.STUDENTS);
        contentPanel.add(roomPanel, SidebarPanel.ALLOCATION);
        contentPanel.add(compatibilityPanel, SidebarPanel.COMPATIBILITY);
        contentPanel.add(waitingListPanel, SidebarPanel.WAITING_LIST);
        contentPanel.add(historyPanel, SidebarPanel.HISTORY);
        contentPanel.add(reportsPanel, SidebarPanel.REPORTS);
        contentPanel.add(aboutPanel, SidebarPanel.PROJECT_INFO);

        add(contentPanel, BorderLayout.CENTER);

        navigateTo(SidebarPanel.DASHBOARD);

        setVisible(true);
    }

    // Called by the sidebar (and by panels that need to redirect, e.g.
    // "Allocate" buttons on the Dashboard jumping to the Rooms panel).
    public void navigateTo(String cardName) {

        cardLayout.show(contentPanel, cardName);
        sidebarPanel.select(cardName);

        if (cardName.equals(SidebarPanel.DASHBOARD)) {
            dashboardPanel.refreshData();
        } else if (cardName.equals(SidebarPanel.STUDENTS)) {
            studentPanel.refreshData();
        } else if (cardName.equals(SidebarPanel.ALLOCATION)) {
            roomPanel.refreshData();
        } else if (cardName.equals(SidebarPanel.COMPATIBILITY)) {
            compatibilityPanel.refreshData();
        } else if (cardName.equals(SidebarPanel.WAITING_LIST)) {
            waitingListPanel.refreshData();
        } else if (cardName.equals(SidebarPanel.HISTORY)) {
            historyPanel.refreshData();
        } else if (cardName.equals(SidebarPanel.REPORTS)) {
            reportsPanel.refreshData();
        } else if (cardName.equals(SidebarPanel.PROJECT_INFO)) {
            aboutPanel.refreshData();
        }
    }
}
