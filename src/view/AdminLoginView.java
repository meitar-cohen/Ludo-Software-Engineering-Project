package view;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class AdminLoginView extends JFrame {

    private static final Color BACKGROUND = new Color(245, 247, 250);
    private static final Color PRIMARY = new Color(55, 110, 220);

    private static final String DEMO_PASSWORD = "admin";

    public AdminLoginView(Runnable onLoginSuccess) {

        setTitle("Ludo - Admin Login");
        setSize(700, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel background = new JPanel(new GridBagLayout());
        background.setBackground(BACKGROUND);

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 225, 235)),
            BorderFactory.createEmptyBorder(45, 65, 45, 65)
        ));

        JLabel title = new JLabel("ADMIN LOGIN");
        title.setFont(new Font("Arial", Font.BOLD, 36));
        title.setForeground(PRIMARY);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Sign in to manage questions");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 16));
        subtitle.setForeground(new Color(110, 115, 130));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 18));
        passwordField.setMaximumSize(new Dimension(300, 45));
        passwordField.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel errorLabel = new JLabel(" ");
        errorLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        errorLabel.setForeground(new Color(210, 70, 70));
        errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton loginButton = createButton("Login", PRIMARY);

        loginButton.addActionListener(e -> {

            char[] enteredPassword = passwordField.getPassword();
            boolean correctPassword = Arrays.equals(
                enteredPassword,
                DEMO_PASSWORD.toCharArray()
            );
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

        JButton backButton = createButton("Back to Main Menu", PRIMARY);

        backButton.addActionListener(e -> {
            new MainMenuView().setVisible(true);
            dispose();
        });

        JLabel demoLabel = new JLabel("Demo password: admin123");
        demoLabel.setFont(new Font("Arial", Font.ITALIC, 13));
        demoLabel.setForeground(new Color(125, 130, 140));
        demoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(title);
        card.add(Box.createVerticalStrut(8));
        card.add(subtitle);
        card.add(Box.createVerticalStrut(35));
        card.add(passwordField);
        card.add(Box.createVerticalStrut(10));
        card.add(errorLabel);
        card.add(Box.createVerticalStrut(15));
        card.add(loginButton);
        card.add(Box.createVerticalStrut(12));
        card.add(backButton);
        card.add(Box.createVerticalStrut(18));
        card.add(demoLabel);

        background.add(card);
        add(background);
    }

    private JButton createButton(String text, Color color) {

        JButton button = new JButton(text);

        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setBackground(color);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(300, 50));
        button.setPreferredSize(new Dimension(300, 50));

        return button;
    }
}
