package view;

import javax.swing.*;
import java.awt.*;

/**
 * End-of-game screen - PROTOTYPE.
 * Shows a fixed ranking (in the order the players were entered),
 * the game duration and a confirmation that the game was saved to the history.
 */
public class EndGameScreen extends JPanel {

    private static final Color BACKGROUND = new Color(245, 247, 250);
    private static final Color PRIMARY = new Color(55, 110, 220);

    public EndGameScreen(String[] names, String[] colors, String difficulty, String duration) {
        setBackground(BACKGROUND);
        setLayout(new GridBagLayout());

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 225, 235), 1),
            BorderFactory.createEmptyBorder(35, 60, 35, 60)));

        JLabel title = new JLabel("GAME OVER");
        title.setFont(new Font("Arial", Font.BOLD, 40));
        title.setForeground(PRIMARY);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(title);

        JLabel info = new JLabel("Difficulty: " + difficulty + "   |   Game time: " + duration);
        info.setFont(new Font("Arial", Font.PLAIN, 16));
        info.setForeground(new Color(110, 115, 130));
        info.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(Box.createVerticalStrut(6));
        card.add(info);
        card.add(Box.createVerticalStrut(25));

        String[] places = {"1st", "2nd", "3rd", "4th"};
        for (int i = 0; i < names.length; i++) {
            JLabel row = new JLabel(places[i] + "   " + names[i] + "  (" + colors[i] + ")");
            row.setFont(new Font("Arial", i == 0 ? Font.BOLD : Font.PLAIN, i == 0 ? 22 : 18));
            row.setForeground(BoardPanel.colorOf(colors[i]).darker());
            row.setAlignmentX(Component.CENTER_ALIGNMENT);
            card.add(row);
            card.add(Box.createVerticalStrut(8));
        }

        JLabel saved = new JLabel("The game was saved to the game history.");
        saved.setFont(new Font("Arial", Font.ITALIC, 14));
        saved.setForeground(new Color(30, 140, 60));
        saved.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(Box.createVerticalStrut(15));
        card.add(saved);
        card.add(Box.createVerticalStrut(25));

        JButton menu = new JButton("Back to Main Menu");
        menu.setFont(new Font("Arial", Font.BOLD, 16));
        menu.setBackground(PRIMARY);
        menu.setForeground(Color.WHITE);
        menu.setFocusPainted(false);
        menu.setAlignmentX(Component.CENTER_ALIGNMENT);
        menu.setMaximumSize(new Dimension(300, 50));
        menu.setPreferredSize(new Dimension(300, 50));
        menu.addActionListener(e -> {
            Window w = SwingUtilities.getWindowAncestor(this);
            new MainMenuView().setVisible(true);
            if (w != null) w.dispose();
        });
        card.add(menu);

        add(card);
    }
}
