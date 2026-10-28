package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.Arrays;

public class AdminLoginView extends JFrame {
    private static final String DEMO_PASSWORD = "admin";

    public AdminLoginView(Runnable onLoginSuccess) {
        setTitle("Ludo - Admin Login");
        setSize(1120, 690);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel background = new GradientBackground();
        background.setLayout(new GridBagLayout());

        JPanel layout = new JPanel(new GridLayout(1, 2, 28, 0));
        layout.setOpaque(false);
        layout.setPreferredSize(new Dimension(1020, 565));
        layout.add(new LoginHero());
        layout.add(buildLoginCard(onLoginSuccess));

        background.add(layout);
        setContentPane(background);
    }

    private JPanel buildLoginCard(Runnable onLoginSuccess) {
        JPanel card = new RoundedCard();
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(48, 45, 38, 45));

        JPanel form = new JPanel();
        form.setOpaque(false);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));

        JLabel eyebrow = new JLabel("LUDO  /  ADMIN ACCESS");
        eyebrow.setFont(new Font("Segoe UI", Font.BOLD, 12));
        eyebrow.setForeground(new Color(75, 110, 248));
        eyebrow.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(eyebrow);
        form.add(Box.createVerticalStrut(11));

        JLabel heading = new JLabel("Ludo control");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 32));
        heading.setForeground(new Color(12, 20, 49));
        heading.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(heading);
        form.add(Box.createVerticalStrut(8));

        JLabel subtitle = new JLabel("Enter the password to manage game questions.");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        subtitle.setForeground(new Color(111, 123, 151));
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(subtitle);
        form.add(Box.createVerticalStrut(38));

        JLabel passwordLabel = new JLabel("PASSWORD");
        passwordLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        passwordLabel.setForeground(new Color(42, 51, 81));
        passwordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(passwordLabel);
        form.add(Box.createVerticalStrut(9));

        JPasswordField passwordField = new JPasswordField();
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        passwordField.setForeground(new Color(12, 20, 49));
        passwordField.setCaretColor(new Color(75, 110, 248));
        passwordField.setAlignmentX(Component.LEFT_ALIGNMENT);
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 52));
        passwordField.setPreferredSize(new Dimension(400, 52));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 226, 241), 1, true),
            new EmptyBorder(8, 14, 8, 14)));
        form.add(passwordField);
        form.add(Box.createVerticalStrut(8));

        JLabel demoPasswordLabel = new JLabel("Demo password: admin");
        demoPasswordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        demoPasswordLabel.setForeground(new Color(111, 123, 151));
        demoPasswordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(demoPasswordLabel);
        form.add(Box.createVerticalStrut(4));

        JLabel errorLabel = new JLabel(" ");
        errorLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        errorLabel.setForeground(new Color(210, 70, 70));
        errorLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(errorLabel);

        card.add(form, BorderLayout.CENTER);

        JPanel actions = new JPanel();
        actions.setOpaque(false);
        actions.setLayout(new BoxLayout(actions, BoxLayout.Y_AXIS));

        ActionButton loginButton = new ActionButton("Login", true);
        loginButton.addActionListener(e -> {
            char[] enteredPassword = passwordField.getPassword();
            boolean correctPassword = Arrays.equals(enteredPassword, DEMO_PASSWORD.toCharArray());
            Arrays.fill(enteredPassword, '\0');
            if (correctPassword) {
                onLoginSuccess.run();
                dispose();
            } else {
                errorLabel.setText("Incorrect password. Please try again.");
                passwordField.setText("");
            }
        });
        getRootPane().setDefaultButton(loginButton);

        ActionButton backButton = new ActionButton("Back", false);
        backButton.addActionListener(e -> {
            new MainMenuView().setVisible(true);
            dispose();
        });

        actions.add(loginButton);
        actions.add(Box.createVerticalStrut(11));
        actions.add(backButton);
        card.add(actions, BorderLayout.SOUTH);
        return card;
    }

    private static void quality(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    }

    private static class GradientBackground extends JPanel {
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            Graphics2D g = (Graphics2D) graphics.create();
            quality(g);
            g.setPaint(new GradientPaint(0, 0, new Color(14, 22, 54), getWidth(), getHeight(), new Color(43, 39, 100)));
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(new Color(110, 103, 231, 45)); g.fillOval(-180, -210, 650, 650);
            g.setColor(new Color(72, 177, 246, 24)); g.fillOval(460, 320, 580, 580);
            g.setColor(new Color(244, 91, 146, 22)); g.fillOval(890, -230, 440, 440);
            g.dispose();
        }
    }

    private static class LoginHero extends JPanel {
        LoginHero() { setOpaque(false); }
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            Graphics2D g = (Graphics2D) graphics.create();
            quality(g);
            g.setFont(new Font("Segoe UI", Font.BOLD, 13));
            g.setColor(new Color(177, 186, 240));
            g.drawString("LUDO CONTROL", 39, 78);
            g.setFont(new Font("Segoe UI", Font.BOLD, 58));
            g.setColor(Color.WHITE);
            g.drawString("Control the", 35, 158);
            g.drawString("game.", 35, 221);
            g.setFont(new Font("Segoe UI", Font.PLAIN, 17));
            g.setColor(new Color(203, 213, 248));
            g.drawString("Manage the questions used", 39, 267);
            g.drawString("during every Ludo match.", 39, 294);

            int x = 105, y = 340, size = 210;
            g.setColor(new Color(5, 10, 32, 70)); g.fillRoundRect(x + 9, y + 12, size, size, 28, 28);
            g.setColor(Color.WHITE); g.fillRoundRect(x, y, size, size, 28, 28);
            g.setColor(new Color(75, 110, 248)); g.fillRoundRect(x + 62, y + 48, 86, 115, 24, 24);
            g.setColor(new Color(210, 220, 255)); g.fillOval(x + 73, y + 60, 64, 64);
            g.setColor(new Color(75, 110, 248)); g.fillOval(x + 91, y + 78, 28, 28);
            g.setColor(new Color(75, 110, 248)); g.fillRoundRect(x + 101, y + 102, 8, 42, 4, 4);
            g.setColor(new Color(255, 255, 255)); g.fillRoundRect(x + 82, y + 132, 46, 12, 6, 6);
            g.dispose();
        }
    }

    private static class RoundedCard extends JPanel {
        RoundedCard() { setOpaque(false); }
        protected void paintComponent(Graphics graphics) {
            Graphics2D g = (Graphics2D) graphics.create();
            quality(g);
            int w = getWidth(), h = getHeight();
            g.setColor(new Color(6, 11, 31, 45)); g.fill(new RoundRectangle2D.Double(6, 10, w - 12, h - 19, 38, 38));
            g.setColor(new Color(250, 251, 255)); g.fill(new RoundRectangle2D.Double(2, 2, w - 12, h - 19, 38, 38));
            g.dispose();
            super.paintComponent(graphics);
        }
    }

    private static class ActionButton extends JButton {
        private final boolean primary;
        private boolean hovered;
        ActionButton(String text, boolean primary) {
            super(text);
            this.primary = primary;
            setFont(new Font("Segoe UI", Font.BOLD, 16));
            setOpaque(false); setContentAreaFilled(false); setBorderPainted(false); setFocusPainted(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setAlignmentX(Component.CENTER_ALIGNMENT);
            setPreferredSize(new Dimension(400, primary ? 52 : 46));
            setMaximumSize(new Dimension(Integer.MAX_VALUE, primary ? 52 : 46));
            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { hovered = true; repaint(); }
                public void mouseExited(MouseEvent e) { hovered = false; repaint(); }
            });
        }
        protected void paintComponent(Graphics graphics) {
            Graphics2D g = (Graphics2D) graphics.create();
            quality(g);
            int w = getWidth(), h = getHeight();
            if (primary) {
                g.setPaint(new GradientPaint(0, 0, hovered ? new Color(107, 112, 255) : new Color(75, 110, 248), w, h,
                    hovered ? new Color(150, 92, 246) : new Color(118, 76, 226)));
            } else {
                g.setColor(hovered ? new Color(226, 234, 255) : new Color(239, 243, 253));
            }
            g.fillRoundRect(2, 2, w - 5, h - 5, 18, 18);
            if (isFocusOwner()) {
                g.setColor(new Color(80, 130, 240)); g.setStroke(new BasicStroke(2));
                g.drawRoundRect(4, 4, w - 9, h - 9, 15, 15);
            }
            g.setFont(getFont());
            g.setColor(primary ? Color.WHITE : new Color(40, 53, 91));
            FontMetrics fm = g.getFontMetrics();
            String text = getText();
            g.drawString(text, (w - fm.stringWidth(text)) / 2, (h - fm.getHeight()) / 2 + fm.getAscent());
            g.dispose();
        }
    }
}
