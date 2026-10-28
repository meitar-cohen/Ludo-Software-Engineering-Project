package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.border.EmptyBorder;
import java.awt.geom.RoundRectangle2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class QuestionManagementView extends JFrame {


    private final List<Question> questions = new ArrayList<>();

    private DefaultTableModel tableModel;

    public QuestionManagementView() {
        setTitle("Ludo - Question Management");
        setSize(1120, 690);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        loadSampleQuestions();

        JPanel background = new QuestionGradientBackground();
        background.setLayout(new GridBagLayout());
        JPanel layout = new JPanel(new GridLayout(1, 2, 28, 0));
        layout.setOpaque(false);
        layout.setPreferredSize(new Dimension(1020, 565));
        layout.add(new QuestionHero());
        layout.add(buildQuestionCard());
        background.add(layout);
        setContentPane(background);
    }

    private JPanel buildQuestionCard() {
        JPanel card = new QuestionRoundedCard();
        card.setLayout(new BorderLayout(0, 14));
        card.setBorder(new EmptyBorder(28, 35, 22, 35));

        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        JLabel eyebrow = new JLabel("ADMIN  /  QUESTION BANK");
        eyebrow.setFont(new Font("Segoe UI", Font.BOLD, 12));
        eyebrow.setForeground(new Color(75, 110, 248));
        eyebrow.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel heading = new JLabel("Question management");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 29));
        heading.setForeground(new Color(12, 20, 49));
        heading.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel subheading = new JLabel("Add, edit, or remove game questions.");
        subheading.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subheading.setForeground(new Color(111, 123, 151));
        subheading.setAlignmentX(Component.LEFT_ALIGNMENT);
        header.add(eyebrow); header.add(Box.createVerticalStrut(9)); header.add(heading);
        header.add(Box.createVerticalStrut(4)); header.add(subheading);

        String[] columns = {"#", "Question", "Difficulty", "Correct Answer"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(38);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setFillsViewportHeight(true);
        table.setShowVerticalLines(false);
        table.setGridColor(new Color(231, 234, 244));
        table.setSelectionBackground(new Color(234, 239, 255));
        table.setSelectionForeground(new Color(12, 20, 49));
        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setFont(new Font("Segoe UI", Font.BOLD, 12));
        tableHeader.setForeground(new Color(42, 51, 81));
        tableHeader.setBackground(new Color(244, 246, 252));
        tableHeader.setPreferredSize(new Dimension(0, 38));
        table.getColumnModel().getColumn(0).setPreferredWidth(40);
        table.getColumnModel().getColumn(1).setPreferredWidth(235);
        table.getColumnModel().getColumn(2).setPreferredWidth(82);
        table.getColumnModel().getColumn(3).setPreferredWidth(120);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(225, 230, 242)));
        scroll.getViewport().setBackground(Color.WHITE);

        JPanel actions = new JPanel(new GridLayout(2, 2, 10, 10));
        actions.setOpaque(false);
        JButton add = new QuestionActionButton("Add Question", true);
        JButton edit = new QuestionActionButton("Edit Question", true);
        JButton delete = new QuestionActionButton("Delete Question", false);
        JButton back = new QuestionActionButton("Back", false);
        add.addActionListener(e -> showAddQuestionDialog());
        edit.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a question to edit.", "No Question Selected", JOptionPane.WARNING_MESSAGE);
                return;
            }
            showEditQuestionDialog(table.convertRowIndexToModel(selectedRow));
        });
        delete.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a question to delete.", "No Question Selected", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int index = table.convertRowIndexToModel(selectedRow);
            Question selected = questions.get(index);
            int confirmation = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this question?\n\n" + selected.text,
                "Confirm Deletion", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (confirmation == JOptionPane.YES_OPTION) { questions.remove(index); refreshTable(); }
        });
        back.addActionListener(e -> { new MainMenuView().setVisible(true); dispose(); });
        actions.add(add); actions.add(edit); actions.add(delete); actions.add(back);

        card.add(header, BorderLayout.NORTH);
        card.add(scroll, BorderLayout.CENTER);
        card.add(actions, BorderLayout.SOUTH);
        refreshTable();
        return card;
    }

    private static void questionQuality(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    }

    private static class QuestionGradientBackground extends JPanel {
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics); Graphics2D g = (Graphics2D) graphics.create(); questionQuality(g);
            g.setPaint(new GradientPaint(0, 0, new Color(14, 22, 54), getWidth(), getHeight(), new Color(43, 39, 100)));
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(new Color(110, 103, 231, 45)); g.fillOval(-180, -210, 650, 650);
            g.setColor(new Color(72, 177, 246, 24)); g.fillOval(460, 320, 580, 580);
            g.dispose();
        }
    }

    private static class QuestionHero extends JPanel {
        QuestionHero() { setOpaque(false); }
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics); Graphics2D g = (Graphics2D) graphics.create(); questionQuality(g);
            g.setFont(new Font("Segoe UI", Font.BOLD, 13)); g.setColor(new Color(177, 186, 240));
            g.drawString("BUILD THE CHALLENGE", 39, 78);
            g.setFont(new Font("Segoe UI", Font.BOLD, 58)); g.setColor(Color.WHITE);
            g.drawString("Ask better.", 35, 158); g.drawString("Play smarter.", 35, 221);
            g.setFont(new Font("Segoe UI", Font.PLAIN, 17)); g.setColor(new Color(203, 213, 248));
            g.drawString("Manage the questions that turn", 39, 267); g.drawString("each move into a challenge.", 39, 294);

            int x = 85, y = 340, w = 330, h = 165;
            g.setColor(new Color(5, 10, 32, 70)); g.fillRoundRect(x + 9, y + 12, w, h, 28, 28);
            g.setColor(Color.WHITE); g.fillRoundRect(x, y, w, h, 28, 28);
            g.setColor(new Color(75, 110, 248)); g.fillRoundRect(x + 22, y + 22, 48, 48, 14, 14);
            g.setColor(Color.WHITE); g.setFont(new Font("Segoe UI", Font.BOLD, 24)); g.drawString("?", x + 39, y + 56);
            g.setColor(new Color(12, 20, 49)); g.setFont(new Font("Segoe UI", Font.BOLD, 16));
            g.drawString("Question bank", x + 86, y + 42);
            g.setFont(new Font("Segoe UI", Font.PLAIN, 13)); g.setColor(new Color(111, 123, 151));
            g.drawString("Easy, Medium, or Hard", x + 86, y + 66);
            g.setColor(new Color(238, 241, 249)); g.fillRoundRect(x + 22, y + 88, w - 44, 54, 12, 12);
            g.setColor(new Color(75, 110, 248)); g.fillOval(x + 37, y + 104, 22, 22);
            g.setColor(Color.WHITE); g.fillOval(x + 43, y + 110, 10, 10);
            g.setColor(new Color(12, 20, 49)); g.setFont(new Font("Segoe UI", Font.BOLD, 13)); g.drawString("Four answers  /  one winner", x + 72, y + 120);
            g.dispose();
        }
    }

    private static class QuestionRoundedCard extends JPanel {
        QuestionRoundedCard() { setOpaque(false); }
        protected void paintComponent(Graphics graphics) {
            Graphics2D g = (Graphics2D) graphics.create(); questionQuality(g); int w = getWidth(), h = getHeight();
            g.setColor(new Color(6, 11, 31, 45)); g.fill(new RoundRectangle2D.Double(6, 10, w - 12, h - 19, 38, 38));
            g.setColor(new Color(250, 251, 255)); g.fill(new RoundRectangle2D.Double(2, 2, w - 12, h - 19, 38, 38));
            g.dispose(); super.paintComponent(graphics);
        }
    }

    private static class QuestionActionButton extends JButton {
        private final boolean primary; private boolean hovered;
        QuestionActionButton(String text, boolean primary) {
            super(text); this.primary = primary;
            setFont(new Font("Segoe UI", Font.BOLD, 13)); setOpaque(false); setContentAreaFilled(false); setBorderPainted(false); setFocusPainted(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); setPreferredSize(new Dimension(190, 46));
            addMouseListener(new MouseAdapter() { public void mouseEntered(MouseEvent e) { hovered = true; repaint(); } public void mouseExited(MouseEvent e) { hovered = false; repaint(); } });
        }
        protected void paintComponent(Graphics graphics) {
            Graphics2D g = (Graphics2D) graphics.create(); questionQuality(g); int w = getWidth(), h = getHeight();
            if (primary) g.setColor(hovered ? new Color(92, 123, 255) : new Color(75, 110, 248));
            else g.setColor(hovered ? new Color(226, 234, 255) : new Color(239, 243, 253));
            g.fillRoundRect(2, 2, w - 5, h - 5, 16, 16);
            g.setFont(getFont()); g.setColor(primary ? Color.WHITE : new Color(40, 53, 91)); FontMetrics fm = g.getFontMetrics();
            String text = getText(); g.drawString(text, (w - fm.stringWidth(text)) / 2, (h - fm.getHeight()) / 2 + fm.getAscent()); g.dispose();
        }
    }

    private void showAddQuestionDialog() {

            JTextField questionField = new JTextField(25);

            JTextField[] answerFields = new JTextField[4];

            JPanel form = new JPanel(new GridLayout(0, 1, 5, 7));

            form.add(new JLabel("Question:"));
            form.add(questionField);

            for (int i = 0; i < 4; i++) {
                answerFields[i] = new JTextField(25);

                form.add(new JLabel("Answer " + (i + 1) + ":"));
                form.add(answerFields[i]);
            }

            JComboBox<String> correctAnswerBox = new JComboBox<>(
                new String[]{"Answer 1", "Answer 2", "Answer 3", "Answer 4"}
            );

            form.add(new JLabel("Correct Answer:"));
            form.add(correctAnswerBox);

            JComboBox<String> difficultyBox = new JComboBox<>(
                new String[]{"Easy", "Medium", "Hard"}
            );

            form.add(new JLabel("Difficulty:"));
            form.add(difficultyBox);

            while (true) {

                int result = JOptionPane.showConfirmDialog(
                    this,
                    form,
                    "Add New Question",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE
                );

                if (result != JOptionPane.OK_OPTION) {
                    return;
                }

                String questionText = questionField.getText().trim();
                String[] answers = new String[4];

                boolean valid = !questionText.isEmpty();

                for (int i = 0; i < 4; i++) {
                    answers[i] = answerFields[i].getText().trim();

                    if (answers[i].isEmpty()) {
                        valid = false;
                    }
                }

                if (!valid) {
                    JOptionPane.showMessageDialog(
                        this,
                        "Please fill in the question and all four answers.",
                        "Missing Information",
                        JOptionPane.WARNING_MESSAGE
                    );
                    continue;
                }

                int correctIndex = correctAnswerBox.getSelectedIndex();

                String difficulty =
                    (String) difficultyBox.getSelectedItem();

                questions.add(new Question(
                    questionText,
                    answers,
                    correctIndex,
                    difficulty
                ));

                refreshTable();

                JOptionPane.showMessageDialog(
                    this,
                    "Question added successfully!"
                );

                return;
            }
        }
        
        
        private void showEditQuestionDialog(int questionIndex) {

            Question existingQuestion = questions.get(questionIndex);

            JTextField questionField = new JTextField(
                existingQuestion.text, 25
            );

            JTextField[] answerFields = new JTextField[4];

            JPanel form = new JPanel(new GridLayout(0, 1, 5, 7));

            form.add(new JLabel("Question:"));
            form.add(questionField);

            for (int i = 0; i < 4; i++) {

                answerFields[i] = new JTextField(
                    existingQuestion.options[i], 25
                );

                form.add(new JLabel("Answer " + (i + 1) + ":"));
                form.add(answerFields[i]);
            }

            JComboBox<String> correctAnswerBox = new JComboBox<>(
                new String[]{
                    "Answer 1",
                    "Answer 2",
                    "Answer 3",
                    "Answer 4"
                }
            );

            correctAnswerBox.setSelectedIndex(
                existingQuestion.correctIndex
            );

            form.add(new JLabel("Correct Answer:"));
            form.add(correctAnswerBox);

            JComboBox<String> difficultyBox = new JComboBox<>(
                new String[]{"Easy", "Medium", "Hard"}
            );

            difficultyBox.setSelectedItem(existingQuestion.difficulty);

            form.add(new JLabel("Difficulty:"));
            form.add(difficultyBox);

            while (true) {

                int result = JOptionPane.showConfirmDialog(
                    this,
                    form,
                    "Edit Question",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE
                );

                if (result != JOptionPane.OK_OPTION) {
                    return;
                }

                String questionText = questionField.getText().trim();
                String[] answers = new String[4];

                boolean valid = !questionText.isEmpty();

                for (int i = 0; i < 4; i++) {

                    answers[i] = answerFields[i].getText().trim();

                    if (answers[i].isEmpty()) {
                        valid = false;
                    }
                }

                if (!valid) {
                    JOptionPane.showMessageDialog(
                        this,
                        "Please fill in the question and all four answers.",
                        "Missing Information",
                        JOptionPane.WARNING_MESSAGE
                    );
                    continue;
                }

                int correctIndex = correctAnswerBox.getSelectedIndex();

                String difficulty =
                    (String) difficultyBox.getSelectedItem();

                questions.set(questionIndex, new Question(
                    questionText,
                    answers,
                    correctIndex,
                    difficulty
                ));

                refreshTable();

                JOptionPane.showMessageDialog(
                    this,
                    "Question updated successfully!"
                );

                
                
                return;
            }
        }

    private void loadSampleQuestions() {

    	
    	
        questions.add(new Question(
            "Which language runs on the JVM?",
            new String[]{"Python", "Java", "HTML", "SQL"},
            1, "Easy"
        ));

        questions.add(new Question(
            "What does CPU stand for?",
            new String[]{
                "Central Processing Unit",
                "Computer Power Unit",
                "Central Program Utility",
                "Control Processing User"
            },
            0, "Medium"
        ));

        questions.add(new Question(
            "Which data structure uses FIFO?",
            new String[]{"Stack", "Tree", "Queue", "Graph"},
            2, "Hard"
        ));
    }

    private void refreshTable() {

        tableModel.setRowCount(0);

        for (int i = 0; i < questions.size(); i++) {

            Question question = questions.get(i);

            tableModel.addRow(new Object[]{
                i + 1,
                question.text,
                question.difficulty,
                question.options[question.correctIndex]
            });
        }
    }

    private static class Question {

        String text;
        String[] options;
        int correctIndex;
        String difficulty;

        Question(String text, String[] options,
                 int correctIndex, String difficulty) {

            this.text = text;
            this.options = options;
            this.correctIndex = correctIndex;
            this.difficulty = difficulty;
        }
    }
}
