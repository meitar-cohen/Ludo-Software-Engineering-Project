package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;

public class PlayerDetailsScreen extends JPanel {
    
    private int numPlayers; 
    private String difficulty = "Easy";
    private JTextField[] nameFields;
    private JComboBox<String>[] colorSelectors;
    private JLabel errorLabel;

    private static final Color BACKGROUND = new Color(245, 247, 250);
    private static final Color PRIMARY = new Color(55, 110, 220);

    public PlayerDetailsScreen(int selectedPlayers, String difficulty) {
        this(selectedPlayers);
        if (difficulty != null) {
            this.difficulty = difficulty;
        }
    }

    public PlayerDetailsScreen(int selectedPlayers) {
        this.numPlayers = selectedPlayers; 
        
        setBackground(BACKGROUND);
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("PLAYER DETAILS", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 32));
        title.setForeground(PRIMARY);
        add(title, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(numPlayers, 3, 10, 10));
        centerPanel.setBackground(BACKGROUND);
        
        nameFields = new JTextField[numPlayers];
        colorSelectors = new JComboBox[numPlayers];
        
        String[] colors = {"Red", "Blue", "Green", "Yellow"};

        for (int i = 0; i < numPlayers; i++) {
            JLabel playerLabel = new JLabel("Player " + (i + 1) + " Name:");
            playerLabel.setFont(new Font("Arial", Font.BOLD, 16));
            centerPanel.add(playerLabel);
            
            nameFields[i] = new JTextField();
            centerPanel.add(nameFields[i]);
            
            colorSelectors[i] = new JComboBox<>(colors);
            colorSelectors[i].setSelectedIndex(i); 
            centerPanel.add(colorSelectors[i]);
        }
        add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout(5, 5));
        bottomPanel.setBackground(BACKGROUND);
        
        errorLabel = new JLabel(" ");
        errorLabel.setForeground(Color.RED);
        errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        errorLabel.setFont(new Font("Arial", Font.BOLD, 14));
        bottomPanel.add(errorLabel, BorderLayout.NORTH);

        JButton startGameBtn = new JButton("Start Game");
        startGameBtn.setFont(new Font("Arial", Font.BOLD, 16));
        startGameBtn.setBackground(PRIMARY);
        startGameBtn.setForeground(Color.WHITE);
        startGameBtn.setFocusPainted(false);
        startGameBtn.setPreferredSize(new Dimension(300, 50));
        startGameBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        startGameBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleStartGame();
            }
        });
        
        bottomPanel.add(startGameBtn, BorderLayout.SOUTH);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void handleStartGame() {
        errorLabel.setForeground(Color.RED); 
        Set<String> selectedColors = new HashSet<>();
        Set<String> selectedNames = new HashSet<>(); 

        for (int i = 0; i < numPlayers; i++) {
            String name = nameFields[i].getText().trim();
            String color = (String) colorSelectors[i].getSelectedItem();

            if (name.isEmpty()) {
                errorLabel.setText("Error: Please enter a name for Player " + (i + 1) + "!");
                return;
            }

            if (selectedNames.contains(name)) {
                errorLabel.setText("Error: The name '" + name + "' is already taken. Each player needs a unique name!");
                return;
            }
            selectedNames.add(name);

            if (selectedColors.contains(color)) {
                errorLabel.setText("Error: The color " + color + " is selected twice!");
                return;
            }
            selectedColors.add(color);
        }

        errorLabel.setForeground(new Color(0, 153, 0)); 
        errorLabel.setText("Success! Loading Game...");
        
        JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (topFrame != null) {
            String[] names = new String[numPlayers];
            String[] colors = new String[numPlayers];
            for (int i = 0; i < numPlayers; i++) {
                names[i] = nameFields[i].getText().trim();
                colors[i] = (String) colorSelectors[i].getSelectedItem();
            }
            topFrame.setTitle("Ludo - Game");
            topFrame.setContentPane(new GameScreen(names, colors, difficulty));
            topFrame.setSize(1150, 800);
            topFrame.setLocationRelativeTo(null);
            topFrame.revalidate();
            topFrame.repaint();
        }
    }
}