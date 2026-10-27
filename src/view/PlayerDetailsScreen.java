package view;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;

public class PlayerDetailsScreen extends JPanel {
    
    private static final int MAX_PLAYERS = 4;
    private JTextField[] nameFields;
    private JComboBox<String>[] colorSelectors;
    private JLabel errorLabel;

    public PlayerDetailsScreen() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("הגדרת פרטי שחקנים", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        add(title, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(MAX_PLAYERS, 3, 10, 10));
        nameFields = new JTextField[MAX_PLAYERS];
        colorSelectors = new JComboBox[MAX_PLAYERS];
        
        String[] colors = {"Red", "Blue", "Green", "Yellow"};

        for (int i = 0; i < MAX_PLAYERS; i++) {
            centerPanel.add(new JLabel("Player " + (i + 1) + " Name:"));
            
            nameFields[i] = new JTextField();
            centerPanel.add(nameFields[i]);
            
            colorSelectors[i] = new JComboBox<>(colors);
            colorSelectors[i].setSelectedIndex(i); 
            centerPanel.add(colorSelectors[i]);
        }
        add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout(5, 5));
        
        errorLabel = new JLabel(" ");
        errorLabel.setForeground(Color.RED);
        errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        errorLabel.setFont(new Font("Arial", Font.BOLD, 14));
        bottomPanel.add(errorLabel, BorderLayout.NORTH);

        JButton startGameBtn = new JButton("Start Game");
        startGameBtn.setFont(new Font("Arial", Font.BOLD, 16));
        
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

        for (int i = 0; i < MAX_PLAYERS; i++) {
            String name = nameFields[i].getText().trim();
            String color = (String) colorSelectors[i].getSelectedItem();

            if (name.isEmpty()) {
                errorLabel.setText("שגיאה מדומה: אנא הזן שם לשחקן " + (i + 1) + "!");
                return;
            }

            if (selectedColors.contains(color)) {
                errorLabel.setText("שגיאה מדומה: הצבע " + color + " נבחר פעמיים. כל שחקן חייב צבע ייחודי!");
                return;
            }
            selectedColors.add(color);
        }

        errorLabel.setForeground(new Color(0, 153, 0)); 
        errorLabel.setText("הנתונים תקינים! (מעבר מדומה למסך המשחק...)");
        
    }

}