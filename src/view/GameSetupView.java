package view;

import javax.swing.*;
import java.awt.*;

public class GameSetupView extends JFrame {

    private JComboBox<Integer> playerCountComboBox;
    private JComboBox<String> difficultyComboBox;
    private JButton continueButton;
    private JButton backButton;

    private static final Color BACKGROUND = new Color(245, 247, 250);
    private static final Color PRIMARY = new Color(55, 110, 220);
    private static final Color BACK_COLOR = new Color(110, 120, 140);

    public GameSetupView() {

        setTitle("Ludo - New Game");
        setSize(700, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Background
        JPanel backgroundPanel = new JPanel(new GridBagLayout());
        backgroundPanel.setBackground(BACKGROUND);

        // Main white card
        JPanel setupPanel = new JPanel();
        setupPanel.setLayout(new BoxLayout(setupPanel, BoxLayout.Y_AXIS));
        setupPanel.setBackground(Color.WHITE);

        setupPanel.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                    new Color(220, 225, 235), 1
                ),
                BorderFactory.createEmptyBorder(
                    45, 65, 45, 65
                )
            )
        );

        // Title
        JLabel titleLabel = new JLabel("NEW GAME");
        titleLabel.setFont(
            new Font("Arial", Font.BOLD, 42)
        );
        titleLabel.setForeground(PRIMARY);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Subtitle
        JLabel subtitleLabel =
            new JLabel("Choose your game settings");

        subtitleLabel.setFont(
            new Font("Arial", Font.PLAIN, 17)
        );

        subtitleLabel.setForeground(
            new Color(125, 130, 140)
        );

        subtitleLabel.setAlignmentX(
            Component.CENTER_ALIGNMENT
        );

        // Players label
        JLabel playersLabel =
            new JLabel("Number of Players");

        playersLabel.setFont(
            new Font("Arial", Font.BOLD, 17)
        );

        playersLabel.setAlignmentX(
            Component.CENTER_ALIGNMENT
        );

        // Player selection
        Integer[] playerOptions = {2, 3, 4};

        playerCountComboBox =
            new JComboBox<>(playerOptions);

        playerCountComboBox.setMaximumSize(
            new Dimension(300, 40)
        );

        playerCountComboBox.setPreferredSize(
            new Dimension(300, 40)
        );

        playerCountComboBox.setFont(
            new Font("Arial", Font.PLAIN, 16)
        );

        playerCountComboBox.setAlignmentX(
            Component.CENTER_ALIGNMENT
        );

        // Difficulty label
        JLabel difficultyLabel =
            new JLabel("Difficulty");

        difficultyLabel.setFont(
            new Font("Arial", Font.BOLD, 17)
        );

        difficultyLabel.setAlignmentX(
            Component.CENTER_ALIGNMENT
        );

        // Difficulty selection
        String[] difficultyOptions = {
            "Easy",
            "Medium",
            "Hard"
        };

        difficultyComboBox =
            new JComboBox<>(difficultyOptions);

        difficultyComboBox.setMaximumSize(
            new Dimension(300, 40)
        );

        difficultyComboBox.setPreferredSize(
            new Dimension(300, 40)
        );

        difficultyComboBox.setFont(
            new Font("Arial", Font.PLAIN, 16)
        );

        difficultyComboBox.setAlignmentX(
            Component.CENTER_ALIGNMENT
        );

        // Buttons
        continueButton =
            createButton("Continue", PRIMARY);

        backButton =
            createButton("Back", BACK_COLOR);

        // Back -> Main Menu
        backButton.addActionListener(e -> {

            MainMenuView mainMenuView =
                new MainMenuView();

            mainMenuView.setVisible(true);

            dispose();
        });
        
        continueButton.addActionListener(e -> {
            int selectedPlayers = (Integer) playerCountComboBox.getSelectedItem();
            
            JFrame playerDetailsFrame = new JFrame("Ludo - Player Details");
            playerDetailsFrame.setSize(700, 600);
            playerDetailsFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            playerDetailsFrame.setLocationRelativeTo(null);
            playerDetailsFrame.setResizable(false);
            
            String selectedDifficulty = (String) difficultyComboBox.getSelectedItem();
            playerDetailsFrame.add(new PlayerDetailsScreen(selectedPlayers, selectedDifficulty));
            
            playerDetailsFrame.setVisible(true);
            dispose(); 
        });

        // Layout
        setupPanel.add(titleLabel);

        setupPanel.add(
            Box.createVerticalStrut(5)
        );

        setupPanel.add(subtitleLabel);

        setupPanel.add(
            Box.createVerticalStrut(40)
        );

        setupPanel.add(playersLabel);

        setupPanel.add(
            Box.createVerticalStrut(10)
        );

        setupPanel.add(playerCountComboBox);

        setupPanel.add(
            Box.createVerticalStrut(30)
        );

        setupPanel.add(difficultyLabel);

        setupPanel.add(
            Box.createVerticalStrut(10)
        );

        setupPanel.add(difficultyComboBox);

        setupPanel.add(
            Box.createVerticalStrut(40)
        );

        setupPanel.add(continueButton);

        setupPanel.add(
            Box.createVerticalStrut(15)
        );

        setupPanel.add(backButton);

        backgroundPanel.add(setupPanel);

        add(backgroundPanel);
    }

    private JButton createButton(
        String text,
        Color backgroundColor
    ) {

        JButton button =
            new JButton(text);

        button.setAlignmentX(
            Component.CENTER_ALIGNMENT
        );

        button.setPreferredSize(
            new Dimension(300, 50)
        );

        button.setMaximumSize(
            new Dimension(300, 50)
        );

        button.setMinimumSize(
            new Dimension(300, 50)
        );

        button.setFont(
            new Font(
                "Arial",
                Font.BOLD,
                16
            )
        );

        button.setForeground(Color.WHITE);

        button.setBackground(
            backgroundColor
        );

        button.setFocusPainted(false);

        button.setCursor(
            new Cursor(
                Cursor.HAND_CURSOR
            )
        );

        button.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                    backgroundColor.darker(),
                    1
                ),
                BorderFactory.createEmptyBorder(
                    10, 20, 10, 20
                )
            )
        );

        return button;
    }

    public JComboBox<Integer>
        getPlayerCountComboBox() {

        return playerCountComboBox;
    }

    public JComboBox<String>
        getDifficultyComboBox() {

        return difficultyComboBox;
    }

    public JButton getContinueButton() {

        return continueButton;
    }

    public JButton getBackButton() {

        return backButton;
    }
}