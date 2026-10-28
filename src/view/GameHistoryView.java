
package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class GameHistoryView extends JFrame {

    private static final Color BACKGROUND = new Color(245, 247, 250);
    private static final Color PRIMARY = new Color(55, 110, 220);

    // נתוני הדגמה בלבד - עדיין אין שמירה לקובץ אמיתי
    private final Object[][] sampleGames = {
        {"G-003", "27/10/2026 18:35", "Medium", "Noa", "00:23:15"},
        {"G-002", "26/10/2026 14:10", "Easy", "Meitar", "00:15:42"},
        {"G-001", "25/10/2026 20:05", "Hard", "Daniel", "00:31:04"}
    };

    // דירוג המשתתפים בכל משחק לדוגמה
    private final String[][] rankings = {
        {"Noa", "Meitar", "Maor", "Daniel"},
        {"Meitar", "Daniel", "Maor"},
        {"Daniel", "Noa"}
    };

    public GameHistoryView() {
        setTitle("Ludo - Game History");
        setSize(850, 620);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel backgroundPanel = new JPanel(new GridBagLayout());
        backgroundPanel.setBackground(BACKGROUND);

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 225, 235)),
            BorderFactory.createEmptyBorder(30, 35, 30, 35)
        ));

        // כותרת המסך
        JLabel title = new JLabel("GAME HISTORY");
        title.setFont(new Font("Arial", Font.BOLD, 36));
        title.setForeground(PRIMARY);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Sample games - prototype preview");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 15));
        subtitle.setForeground(new Color(125, 130, 140));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // יצירת טבלת המשחקים
        String[] columns = {
            "Game ID", "Date & Time", "Difficulty",
            "Winner", "Duration"
        };

        DefaultTableModel model = new DefaultTableModel(sampleGames, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(model);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setRowHeight(34);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        table.getColumnModel().getColumn(1).setPreferredWidth(160);
        table.getColumnModel().getColumn(0).setPreferredWidth(75);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(725, 160));
        scrollPane.setMaximumSize(new Dimension(725, 160));
        scrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);

        // אזור הצגת דירוג המשתתפים במשחק שנבחר
        JTextArea rankingDetails = new JTextArea();
        rankingDetails.setEditable(false);
        rankingDetails.setFocusable(false);
        rankingDetails.setFont(new Font("Arial", Font.PLAIN, 16));
        rankingDetails.setBackground(new Color(248, 249, 252));
        rankingDetails.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 225, 235)),
            BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));
        rankingDetails.setPreferredSize(new Dimension(725, 125));
        rankingDetails.setMaximumSize(new Dimension(725, 125));
        rankingDetails.setAlignmentX(Component.CENTER_ALIGNMENT);

        // הצגת הדירוג המלא בעת בחירת משחק בטבלה
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() >= 0) {
                int index = table.convertRowIndexToModel(table.getSelectedRow());

                StringBuilder text = new StringBuilder(
                    "FINAL RANKING - " + sampleGames[index][0]
                );

                String[] places = {"1st", "2nd", "3rd", "4th"};

                for (int i = 0; i < rankings[index].length; i++) {
                    text.append("\n")
                        .append(places[i])
                        .append(": ")
                        .append(rankings[index][i]);
                }

                rankingDetails.setText(text.toString());
            }
        });

        // בחירת המשחק הראשון כברירת מחדל
        table.setRowSelectionInterval(0, 0);

        // כפתור חזרה לתפריט הראשי
        JButton backButton = new JButton("Back to Main Menu");
        backButton.setFont(new Font("Arial", Font.BOLD, 16));
        backButton.setForeground(Color.WHITE);
        backButton.setBackground(PRIMARY);
        backButton.setFocusPainted(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.setMaximumSize(new Dimension(300, 50));
        backButton.setPreferredSize(new Dimension(300, 50));

        backButton.addActionListener(e -> {
            new MainMenuView().setVisible(true);
            dispose();
        });

        // סידור רכיבי המסך
        card.add(title);
        card.add(Box.createVerticalStrut(6));
        card.add(subtitle);
        card.add(Box.createVerticalStrut(22));
        card.add(scrollPane);
        card.add(Box.createVerticalStrut(15));
        card.add(rankingDetails);
        card.add(Box.createVerticalStrut(20));
        card.add(backButton);

        backgroundPanel.add(card);
        add(backgroundPanel);
    }
}
