package util;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

// Central place for the app's colors, fonts and reusable Swing widgets,
// mirroring the Smart Stay Allocator web palette (deep blue primary,
// teal accent, dark navy sidebar).
public final class UITheme {

    private UITheme() {}

    // Core palette
    public static final Color PRIMARY = new Color(0x25, 0x63, 0xEB);
    public static final Color PRIMARY_DARK = new Color(0x1D, 0x4E, 0xD8);
    public static final Color ACCENT_TEAL = new Color(0x14, 0xB8, 0xA6);
    public static final Color SUCCESS = new Color(0x22, 0xC5, 0x5E);
    public static final Color WARNING = new Color(0xF5, 0x9E, 0x0B);
    public static final Color DANGER = new Color(0xDC, 0x26, 0x26);
    public static final Color INFO = new Color(0x38, 0x8B, 0xFF);

    public static final Color BACKGROUND = new Color(0xF6, 0xF7, 0xFB);
    public static final Color CARD_BG = Color.WHITE;
    public static final Color BORDER = new Color(0xE3, 0xE6, 0xEF);
    public static final Color TEXT_PRIMARY = new Color(0x1E, 0x22, 0x33);
    public static final Color TEXT_MUTED = new Color(0x6B, 0x72, 0x87);

    public static final Color SIDEBAR_BG = new Color(0x1B, 0x1F, 0x30);
    public static final Color SIDEBAR_HOVER = new Color(0x27, 0x2D, 0x45);
    public static final Color SIDEBAR_SELECTED = new Color(0x25, 0x63, 0xEB);
    public static final Color SIDEBAR_TEXT = new Color(0xC9, 0xCE, 0xDD);
    public static final Color SIDEBAR_TEXT_MUTED = new Color(0x7C, 0x82, 0x9A);

    // Fonts
    public static final String FONT_FAMILY = "Segoe UI";

    public static Font fontRegular(int size) {
        return new Font(FONT_FAMILY, Font.PLAIN, size);
    }

    public static Font fontBold(int size) {
        return new Font(FONT_FAMILY, Font.BOLD, size);
    }

    public static Font fontTitle() {
        return new Font(FONT_FAMILY, Font.BOLD, 22);
    }

    // ----------------------------------------------------------------
    // Rounded panel used as a "card" container throughout the app
    // ----------------------------------------------------------------
    public static class RoundedPanel extends JPanel {

        private final int radius;
        private final Color background;
        private final Color border;

        public RoundedPanel(int radius, Color background, Color border) {
            this.radius = radius;
            this.background = background;
            this.border = border;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(background);
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);

            if (border != null) {
                g2.setColor(border);
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
            }

            g2.dispose();
            super.paintComponent(g);
        }
    }

    public static RoundedPanel card() {
        RoundedPanel panel = new RoundedPanel(14, CARD_BG, BORDER);
        panel.setBorder(BorderFactory.createEmptyBorder(18, 20, 18, 20));
        return panel;
    }

    // ----------------------------------------------------------------
    // Pill-shaped colored button
    // ----------------------------------------------------------------
    public static class PillButton extends JButton {

        private final Color base;
        private final Color hover;
        private Color current;

        public PillButton(String text, Color base) {
            super(text);
            this.base = base;
            this.hover = base.darker();
            this.current = base;

            setForeground(Color.WHITE);
            setFont(fontBold(13));
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setOpaque(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    current = hover;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    current = base;
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(isEnabled() ? current : new Color(0xC7, 0xCB, 0xD6));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), getHeight(), getHeight());
            g2.dispose();
            super.paintComponent(g);
        }
    }

    public static PillButton primaryButton(String text) {
        return new PillButton(text, PRIMARY);
    }

    public static PillButton successButton(String text) {
        return new PillButton(text, SUCCESS);
    }

    public static PillButton dangerButton(String text) {
        return new PillButton(text, DANGER);
    }

    public static PillButton mutedButton(String text) {
        return new PillButton(text, new Color(0x8A, 0x90, 0xA6));
    }

    // ----------------------------------------------------------------
    // Small colored status pill / badge (label with rounded background)
    // ----------------------------------------------------------------
    public static JLabel badge(String text, Color color) {

        JLabel label = new JLabel(text, SwingConstants.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 30));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), getHeight(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }
        };

        label.setOpaque(false);
        label.setForeground(color.darker());
        label.setFont(fontBold(11));
        label.setBorder(BorderFactory.createEmptyBorder(4, 12, 4, 12));

        return label;
    }

    // Section heading used at the top of every panel
    public static JLabel heading(String text) {
        JLabel label = new JLabel(text);
        label.setFont(fontTitle());
        label.setForeground(TEXT_PRIMARY);
        return label;
    }

    public static JLabel subtext(String text) {
        JLabel label = new JLabel(text);
        label.setFont(fontRegular(13));
        label.setForeground(TEXT_MUTED);
        return label;
    }

    public static void styleComponent(Component c) {
        c.setFont(fontRegular(13));
    }
}
