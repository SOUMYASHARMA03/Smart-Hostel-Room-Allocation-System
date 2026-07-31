package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import util.UITheme;

public class LoginFrame extends JFrame {

    public LoginFrame() {

        setTitle("Smart Stay Allocator \u2014 Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(920, 560);
        setLocationRelativeTo(null);
        setResizable(false);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(buildBrandPanel(), BorderLayout.WEST);
        getContentPane().add(buildFormPanel(), BorderLayout.CENTER);
    }

    private JPanel buildBrandPanel() {

        JPanel panel = new JPanel();
        panel.setBackground(UITheme.SIDEBAR_BG);
        panel.setPreferredSize(new Dimension(380, 0));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(60, 40, 40, 40));

        JLabel title = new JLabel("Smart Stay Allocator");
        title.setForeground(Color.WHITE);
        title.setFont(new Font(UITheme.FONT_FAMILY, Font.BOLD, 26));
        title.setAlignmentX(JLabel.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel("<html>Hostel Room Allocation System<br>powered by core data structures</html>");
        subtitle.setForeground(UITheme.SIDEBAR_TEXT);
        subtitle.setFont(UITheme.fontRegular(14));
        subtitle.setAlignmentX(JLabel.LEFT_ALIGNMENT);
        subtitle.setBorder(BorderFactory.createEmptyBorder(14, 0, 0, 0));

        JLabel features = new JLabel(
                "<html><br>&#9679; Doubly Linked List &mdash; student registry"
                        + "<br>&#9679; HashMap &mdash; room lookup"
                        + "<br>&#9679; Priority Queue &mdash; waiting list"
                        + "<br>&#9679; Stack &mdash; allocation history</html>");
        features.setForeground(UITheme.SIDEBAR_TEXT_MUTED);
        features.setFont(UITheme.fontRegular(13));
        features.setAlignmentX(JLabel.LEFT_ALIGNMENT);
        features.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        panel.add(title);
        panel.add(subtitle);
        panel.add(features);

        return panel;
    }

    private JPanel buildFormPanel() {

        JPanel outer = new JPanel(new GridBagLayout());
        outer.setBackground(UITheme.BACKGROUND);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(UITheme.CARD_BG);
        form.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.BORDER, 1, true),
                BorderFactory.createEmptyBorder(36, 40, 36, 40)));

        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = 0;
        gc.insets = new Insets(8, 0, 8, 0);
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.gridwidth = 2;

        JLabel welcome = new JLabel("Welcome back, Warden");
        welcome.setFont(UITheme.fontTitle());
        welcome.setHorizontalAlignment(SwingConstants.LEFT);

        JLabel sub = UITheme.subtext("Sign in to manage hostel rooms and students");

        gc.gridy = 0;
        form.add(welcome, gc);
        gc.gridy = 1;
        gc.insets = new Insets(2, 0, 20, 0);
        form.add(sub, gc);

        JTextField userField = new JTextField("admin");
        JPasswordField passField = new JPasswordField();

        styleField(userField);
        styleField(passField);

        gc.insets = new Insets(6, 0, 6, 0);
        gc.gridy = 2;
        form.add(new JLabel("Username"), gc);
        gc.gridy = 3;
        form.add(userField, gc);
        gc.gridy = 4;
        form.add(new JLabel("Password"), gc);
        gc.gridy = 5;
        form.add(passField, gc);

        UITheme.PillButton loginBtn = UITheme.primaryButton("Enter Dashboard");
        loginBtn.setPreferredSize(new Dimension(200, 42));
        loginBtn.addActionListener(e -> {

            String username = userField.getText().trim();

            if (username.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a username.",
                        "Login Required", JOptionPane.WARNING_MESSAGE);
                return;
            }

            dispose();
            new MainFrame();
        });

        gc.gridy = 6;
        gc.insets = new Insets(24, 0, 0, 0);
        form.add(loginBtn, gc);

        outer.add(form);
        return outer;
    }

    private void styleField(JTextField field) {
        field.setFont(UITheme.fontRegular(14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.BORDER, 1, true),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)));
        field.setColumns(20);
    }
}
