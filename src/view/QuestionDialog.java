package view;

import javax.swing.*;
import java.awt.*;

/**
 * Question pop-up - PROTOTYPE.
 * Shows one fixed easy question with 4 answers. The player must choose an
 * answer before confirming, then sees whether the answer was correct.
 */
public class QuestionDialog extends JDialog {

    private static final String QUESTION =
        "Which document defines the software requirements according to IEEE Std 830?";
    private static final String[] ANSWERS = {
        "SRS - Software Requirements Specification",
        "SDP - Software Development Plan",
        "STD - Software Test Document",
        "SDD - Software Design Description"
    };
    private static final int CORRECT_INDEX = 0;

    private int selected = -1;
    private boolean correct = false;

    public QuestionDialog(Window owner) {
        super(owner, "Question square", ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout(0, 12));
        root.setBackground(Color.WHITE);
        root.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        JLabel header = new JLabel("QUESTION - EASY (correct: +1 square, wrong: -1 square)",
            SwingConstants.CENTER);
        header.setOpaque(true);
        header.setBackground(BoardPanel.Q_EASY);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Arial", Font.BOLD, 15));
        header.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        root.add(header, BorderLayout.NORTH);

        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setBackground(Color.WHITE);
        center.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        JLabel q = new JLabel("<html><div style='width:380px'>" + QUESTION + "</div></html>");
        q.setFont(new Font("Arial", Font.BOLD, 16));
        q.setAlignmentX(Component.LEFT_ALIGNMENT);
        center.add(q);
        center.add(Box.createVerticalStrut(12));

        ButtonGroup group = new ButtonGroup();
        JButton confirm = new JButton("Confirm answer");
        confirm.setEnabled(false);
        for (int i = 0; i < ANSWERS.length; i++) {
            final int index = i;
            JRadioButton option = new JRadioButton(ANSWERS[i]);
            option.setFont(new Font("Arial", Font.PLAIN, 15));
            option.setBackground(Color.WHITE);
            option.setAlignmentX(Component.LEFT_ALIGNMENT);
            option.addActionListener(e -> {
                selected = index;
                confirm.setEnabled(true);
            });
            group.add(option);
            center.add(option);
            center.add(Box.createVerticalStrut(4));
        }

        JLabel feedback = new JLabel("Choose an answer to continue.");
        feedback.setFont(new Font("Arial", Font.BOLD, 15));
        feedback.setForeground(new Color(110, 115, 130));
        feedback.setAlignmentX(Component.LEFT_ALIGNMENT);
        center.add(Box.createVerticalStrut(8));
        center.add(feedback);
        root.add(center, BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttons.setBackground(Color.WHITE);
        JButton cont = new JButton("Continue");
        cont.setVisible(false);
        styleButton(confirm, new Color(55, 110, 220));
        styleButton(cont, new Color(40, 150, 90));

        confirm.addActionListener(e -> {
            correct = selected == CORRECT_INDEX;
            if (correct) {
                feedback.setText("Correct! Your piece moves 1 square forward.");
                feedback.setForeground(new Color(30, 140, 60));
            } else {
                feedback.setText("Wrong. The correct answer is: " + ANSWERS[CORRECT_INDEX].substring(0, 3)
                    + ". Your piece moves 1 square back.");
                feedback.setForeground(new Color(200, 50, 50));
            }
            confirm.setVisible(false);
            cont.setVisible(true);
            for (Component c : center.getComponents()) {
                if (c instanceof JRadioButton) c.setEnabled(false);
            }
        });
        cont.addActionListener(e -> dispose());

        buttons.add(confirm);
        buttons.add(cont);
        root.add(buttons, BorderLayout.SOUTH);

        setContentPane(root);
        pack();
        setLocationRelativeTo(owner);
    }

    private void styleButton(JButton b, Color bg) {
        b.setFont(new Font("Arial", Font.BOLD, 15));
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setPreferredSize(new Dimension(200, 40));
    }

    public boolean isCorrect() {
        return correct;
    }
}
