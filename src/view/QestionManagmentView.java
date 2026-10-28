package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class QuestionManagementView extends JFrame {

    private static final Color BACKGROUND = new Color(245, 247, 250);
    private static final Color PRIMARY = new Color(55, 110, 220);

    // רשימת שאלות בזיכרון לצורך אב הטיפוס
    private final List<Question> questions = new ArrayList<>();

    // מודל הטבלה שיציג את השאלות
    private final DefaultTableModel tableModel;

    public QuestionManagementView() {

        setTitle("Ludo - Question Management");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // יצירת שאלות לדוגמה
        loadSampleQuestions();

        // רקע המסך
        JPanel background = new JPanel(new GridBagLayout());
        background.setBackground(BACKGROUND);

        // כרטיס מרכזי
        JPanel card = new JPanel(new BorderLayout(0, 20));
        card.setBackground(Color.WHITE);
        card.setPreferredSize(new Dimension(900, 530));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 225, 235)),
            BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));

        // כותרת והסבר
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBackground(Color.WHITE);

        JLabel title = new JLabel("QUESTION MANAGEMENT");
        title.setFont(new Font("Arial", Font.BOLD, 32));
        title.setForeground(PRIMARY);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Manage the questions used in the Ludo game");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 15));
        subtitle.setForeground(new Color(110, 115, 130));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        header.add(title);
        header.add(Box.createVerticalStrut(8));
        header.add(subtitle);

        // יצירת טבלה שלא מאפשרת עריכה ישירה של תאים
        String[] columns = {
            "#", "Question", "Difficulty", "Correct Answer"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(tableModel);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setRowHeight(38);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));

        table.getColumnModel().getColumn(0).setPreferredWidth(45);
        table.getColumnModel().getColumn(1).setPreferredWidth(480);
        table.getColumnModel().getColumn(2).setPreferredWidth(100);
        table.getColumnModel().getColumn(3).setPreferredWidth(170);

        JScrollPane scrollPane = new JScrollPane(table);

        // כפתור חזרה לתפריט הראשי
        JButton backButton = new JButton("Back to Main Menu");
        backButton.setFont(new Font("Arial", Font.BOLD, 16));
        backButton.setForeground(Color.WHITE);
        backButton.setBackground(PRIMARY);
        backButton.setFocusPainted(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.setPreferredSize(new Dimension(220, 45));

        backButton.addActionListener(e -> {
            new MainMenuView().setVisible(true);
            dispose();
        });

     // כפתור להוספת שאלה חדשה
        JButton addButton = new JButton("Add Question");
        addButton.setFont(new Font("Arial", Font.BOLD, 16));
        addButton.setForeground(Color.WHITE);
        addButton.setBackground(PRIMARY);
        addButton.setFocusPainted(false);
        addButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addButton.setPreferredSize(new Dimension(180, 45));

        // פתיחת טופס הוספת שאלה
        addButton.addActionListener(e -> showAddQuestionDialog());
        
     // כפתור לעריכת שאלה קיימת
        JButton editButton = new JButton("Edit Question");
        editButton.setFont(new Font("Arial", Font.BOLD, 16));
        editButton.setForeground(Color.WHITE);
        editButton.setBackground(PRIMARY);
        editButton.setFocusPainted(false);
        editButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        editButton.setPreferredSize(new Dimension(180, 45));

        // פתיחת טופס עריכה עבור השאלה שנבחרה בטבלה
        editButton.addActionListener(e -> {

            int selectedRow = table.getSelectedRow();

            // בדיקה שהמשתמש בחר שאלה
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(
                    this,
                    "Please select a question to edit.",
                    "No Question Selected",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            // קבלת מיקום השאלה ברשימה
            int modelRow = table.convertRowIndexToModel(selectedRow);

            // פתיחת טופס העריכה
            showEditQuestionDialog(modelRow);
        });

        // אזור תחתון לכפתורים
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.add(addButton);
        bottomPanel.add(editButton);
        bottomPanel.add(backButton);

        // סידור רכיבי המסך
        card.add(header, BorderLayout.NORTH);
        card.add(scrollPane, BorderLayout.CENTER);
        card.add(bottomPanel, BorderLayout.SOUTH);

        background.add(card);
        add(background);

     // פתיחת טופס להוספת שאלה חדשה
        private void showAddQuestionDialog() {

            JTextField questionField = new JTextField(25);

            // ארבע תשובות אפשריות
            JTextField[] answerFields = new JTextField[4];

            JPanel form = new JPanel(new GridLayout(0, 1, 5, 7));

            form.add(new JLabel("Question:"));
            form.add(questionField);

            for (int i = 0; i < 4; i++) {
                answerFields[i] = new JTextField(25);

                form.add(new JLabel("Answer " + (i + 1) + ":"));
                form.add(answerFields[i]);
            }

            // בחירת התשובה הנכונה
            JComboBox<String> correctAnswerBox = new JComboBox<>(
                new String[]{"Answer 1", "Answer 2", "Answer 3", "Answer 4"}
            );

            form.add(new JLabel("Correct Answer:"));
            form.add(correctAnswerBox);

            // בחירת רמת הקושי
            JComboBox<String> difficultyBox = new JComboBox<>(
                new String[]{"Easy", "Medium", "Hard"}
            );

            form.add(new JLabel("Difficulty:"));
            form.add(difficultyBox);

            // הצגת הטופס עד לשמירת נתונים תקינים או ביטול
            while (true) {

                int result = JOptionPane.showConfirmDialog(
                    this,
                    form,
                    "Add New Question",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE
                );

                // ביטול הפעולה ללא הוספת שאלה
                if (result != JOptionPane.OK_OPTION) {
                    return;
                }

                String questionText = questionField.getText().trim();
                String[] answers = new String[4];

                boolean valid = !questionText.isEmpty();

                // בדיקה שכל ארבע התשובות מולאו
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

                // הוספת השאלה למאגר הזמני בזיכרון
                questions.add(new Question(
                    questionText,
                    answers,
                    correctIndex,
                    difficulty
                ));

                // עדכון הטבלה באופן מיידי
                refreshTable();

                JOptionPane.showMessageDialog(
                    this,
                    "Question added successfully!"
                );

                return;
            }
        }
        
        
     // פתיחת טופס לעריכת שאלה קיימת
        private void showEditQuestionDialog(int questionIndex) {

            // שליפת השאלה שנבחרה
            Question existingQuestion = questions.get(questionIndex);

            // הצגת נוסח השאלה הנוכחי
            JTextField questionField = new JTextField(
                existingQuestion.text, 25
            );

            // הצגת ארבע התשובות הנוכחיות
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

            // בחירת התשובה הנכונה הנוכחית
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

            // הצגת רמת הקושי הנוכחית
            JComboBox<String> difficultyBox = new JComboBox<>(
                new String[]{"Easy", "Medium", "Hard"}
            );

            difficultyBox.setSelectedItem(existingQuestion.difficulty);

            form.add(new JLabel("Difficulty:"));
            form.add(difficultyBox);

            // הצגת הטופס עד לשמירת נתונים תקינים או ביטול
            while (true) {

                int result = JOptionPane.showConfirmDialog(
                    this,
                    form,
                    "Edit Question",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE
                );

                // ביטול העריכה ללא שינוי השאלה
                if (result != JOptionPane.OK_OPTION) {
                    return;
                }

                String questionText = questionField.getText().trim();
                String[] answers = new String[4];

                boolean valid = !questionText.isEmpty();

                // בדיקה שכל ארבע התשובות מולאו
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

                // עדכון השאלה הקיימת בזיכרון
                questions.set(questionIndex, new Question(
                    questionText,
                    answers,
                    correctIndex,
                    difficulty
                ));

                // רענון הטבלה לאחר העריכה
                refreshTable();

                JOptionPane.showMessageDialog(
                    this,
                    "Question updated successfully!"
                );

                return;
            }
        }
        
        
        // הצגת השאלות בטבלה
        refreshTable();
    }

    // טעינת שאלות לדוגמה
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

    // רענון הטבלה מתוך רשימת השאלות
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

    // ייצוג שאלה במאגר הזמני
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