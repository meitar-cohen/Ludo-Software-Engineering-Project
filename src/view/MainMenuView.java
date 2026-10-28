package view;

import javax.swing.*;
import java.awt.*;

public class MainMenuView extends JFrame {

    private JButton newGameButton;
    private JButton gameHistoryButton;
    private JButton questionManagementButton;
    private JButton exitButton;

    private static final Color BACKGROUND = new Color(245, 247, 250);
    private static final Color PRIMARY = new Color(55, 110, 220);
    private static final Color EXIT_COLOR = new Color(210, 70, 70);

    public MainMenuView() {
        setTitle("Ludo");
        setSize(700, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel backgroundPanel = new JPanel(new GridBagLayout());
        backgroundPanel.setBackground(BACKGROUND);

        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(Color.WHITE);

        menuPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 225, 235), 1),
                BorderFactory.createEmptyBorder(45, 65, 45, 65)
        ));

        // Title
        JLabel titleLabel = new JLabel("LUDO");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 52));
        titleLabel.setForeground(PRIMARY);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Subtitle
        JLabel subtitleLabel = new JLabel("Play. Learn. Win.");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 17));
        subtitleLabel.setForeground(new Color(125, 130, 140));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Buttons
        newGameButton = createButton(
                "New Game",
                PRIMARY
        );

        gameHistoryButton = createButton(
                "Game History",
                PRIMARY
        );

        questionManagementButton = createButton(
                "Question Management",
                PRIMARY
        );

        exitButton = createButton(
                "Exit",
                EXIT_COLOR
        );

        // New Game navigation
        newGameButton.addActionListener(e -> {
            GameSetupView gameSetupView = new GameSetupView();
            gameSetupView.setVisible(true);
            dispose();
        });
        
        // מעבר למסך היסטוריית המשחקים
        gameHistoryButton.addActionListener(e -> {
            GameHistoryView gameHistoryView = new GameHistoryView();
            gameHistoryView.setVisible(true);
            dispose();
        });

        // Exit
        exitButton.addActionListener(e -> dispose());

        // Layout
        menuPanel.add(titleLabel);
        menuPanel.add(Box.createVerticalStrut(5));

        menuPanel.add(subtitleLabel);
        menuPanel.add(Box.createVerticalStrut(45));

        menuPanel.add(newGameButton);
        menuPanel.add(Box.createVerticalStrut(14));

        menuPanel.add(gameHistoryButton);
        menuPanel.add(Box.createVerticalStrut(14));

        menuPanel.add(questionManagementButton);
        menuPanel.add(Box.createVerticalStrut(25));

        menuPanel.add(exitButton);

        backgroundPanel.add(menuPanel);
        add(backgroundPanel);
    }

    private JButton createButton(String text, Color backgroundColor) {

        JButton button = new JButton(text);

        button.setAlignmentX(Component.CENTER_ALIGNMENT);

        button.setPreferredSize(new Dimension(300, 50));
        button.setMaximumSize(new Dimension(300, 50));
        button.setMinimumSize(new Dimension(300, 50));

        button.setFont(new Font("Arial", Font.BOLD, 16));

        button.setForeground(Color.WHITE);
        button.setBackground(backgroundColor);

        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(backgroundColor.darker(), 1),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));

        return button;
    }

    public JButton getNewGameButton() {
        return newGameButton;
    }

    public JButton getGameHistoryButton() {
        return gameHistoryButton;
    }

    public JButton getQuestionManagementButton() {
        return questionManagementButton;
    }

    public JButton getExitButton() {
        return exitButton;
    }
}