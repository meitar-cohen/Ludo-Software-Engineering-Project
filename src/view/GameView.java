package view;

import javax.swing.*;
import java.awt.*;

/**
 * Game screen - PROTOTYPE.
 * Runs a fixed demo scenario (scripted dice results, no real game logic) so that
 * users can try a full flow: take a piece out of the castle, move it, answer a
 * question, land on a surprise square, see a turn pass, and finish the game.
 */
public class GameView extends JPanel {

    private static final Color BACKGROUND = new Color(245, 247, 250);
    private static final Color PRIMARY = new Color(55, 110, 220);
    private static final Color MUTED = new Color(110, 115, 130);

    private enum Stage {
        P1_ROLL_1, P1_TAKE_OUT, P1_ROLL_2, P1_MOVE_2,
        OTHER_ROLL, OTHER_PASS,
        P1_ROLL_3, P1_MOVE_3, BUSY, DONE
    }

    private final String[] names;
    private final String[] colors;
    private final String difficulty;
    private final boolean easy;

    private final BoardPanel board;
    private final DicePanel dice;
    private final JLabel timerLabel;
    private final JLabel formulaLabel;
    private final JTextArea statusArea;
    private final JButton rollButton;
    private final JButton endGameButton;
    private final JPanel playersPanel;

    private Timer gameTimer;
    private int secondsElapsed = 0;
    private int currentPlayer = 0;
    private Stage stage = Stage.P1_ROLL_1;
    private int pendingSteps = 0;

    /** Default constructor kept for compatibility (2 demo players, Easy). */
    public GameView() {
        this(new String[]{"Player 1", "Player 2"}, new String[]{"Red", "Blue"}, "Easy");
    }

    public GameView(String[] names, String[] colors, String difficulty) {
        this.names = names;
        this.colors = colors;
        this.difficulty = difficulty == null ? "Easy" : difficulty;
        this.easy = "Easy".equals(this.difficulty);

        setBackground(BACKGROUND);
        setLayout(new BorderLayout(15, 0));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // ---------- Board (left) ----------
        board = new BoardPanel(this.difficulty, colors);
        board.setPieceClickListener(new BoardPanel.PieceClickListener() {
            @Override
            public void castlePieceClicked() {
                onCastlePieceClicked();
            }

            @Override
            public void boardPieceClicked() {
                onBoardPieceClicked();
            }
        });
        add(board, BorderLayout.CENTER);

        // ---------- Side panel (right) ----------
        JPanel side = new JPanel();
        side.setLayout(new BoxLayout(side, BoxLayout.Y_AXIS));
        side.setBackground(Color.WHITE);
        side.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 225, 235), 1),
            BorderFactory.createEmptyBorder(15, 18, 15, 18)));
        side.setPreferredSize(new Dimension(330, 700));

        JLabel title = new JLabel("LUDO - " + this.difficulty);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setForeground(PRIMARY);
        side.add(leftAligned(title));

        timerLabel = new JLabel("Game time: 00:00:00");
        timerLabel.setFont(new Font("Arial", Font.BOLD, 15));
        timerLabel.setForeground(MUTED);
        side.add(Box.createVerticalStrut(4));
        side.add(leftAligned(timerLabel));

        side.add(Box.createVerticalStrut(12));
        playersPanel = new JPanel();
        playersPanel.setLayout(new BoxLayout(playersPanel, BoxLayout.Y_AXIS));
        playersPanel.setBackground(Color.WHITE);
        side.add(leftAligned(playersPanel));

        side.add(Box.createVerticalStrut(12));
        dice = new DicePanel(!easy, "Hard".equals(this.difficulty) ? "UltraBoost" : "Boost");
        side.add(leftAligned(dice));

        formulaLabel = new JLabel(" ");
        formulaLabel.setFont(new Font("Arial", Font.BOLD, 16));
        formulaLabel.setForeground(PRIMARY);
        side.add(Box.createVerticalStrut(6));
        side.add(leftAligned(formulaLabel));

        statusArea = new JTextArea(5, 20);
        statusArea.setEditable(false);
        statusArea.setLineWrap(true);
        statusArea.setWrapStyleWord(true);
        statusArea.setFont(new Font("Arial", Font.PLAIN, 14));
        statusArea.setBackground(new Color(240, 245, 255));
        statusArea.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        JScrollPane statusScroll = new JScrollPane(statusArea);
        statusScroll.setBorder(BorderFactory.createLineBorder(new Color(210, 220, 240)));
        statusScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
        statusScroll.setPreferredSize(new Dimension(290, 150));
        side.add(Box.createVerticalStrut(8));
        side.add(leftAligned(statusScroll));

        rollButton = createButton("Roll Dice", PRIMARY);
        rollButton.addActionListener(e -> onRoll());
        side.add(Box.createVerticalStrut(10));
        side.add(leftAligned(rollButton));

        endGameButton = createButton("End Game", new Color(40, 150, 90));
        endGameButton.setEnabled(false);
        endGameButton.addActionListener(e -> showEndScreen());
        side.add(Box.createVerticalStrut(8));
        side.add(leftAligned(endGameButton));

        JButton menuButton = createButton("Main Menu", new Color(110, 120, 140));
        menuButton.addActionListener(e -> backToMenu());
        side.add(Box.createVerticalStrut(8));
        side.add(leftAligned(menuButton));

        side.add(Box.createVerticalStrut(12));
        side.add(leftAligned(createLegend()));
        side.add(Box.createVerticalGlue());

        add(side, BorderLayout.EAST);

        refreshPlayers();
        setStatus(names[0] + ", it's your turn. Click \"Roll Dice\".");
        startTimer();
    }

    // ------------------------------------------------------------------
    // Scripted scenario
    // ------------------------------------------------------------------

    private void onRoll() {
        switch (stage) {
            case P1_ROLL_1:
                showRoll(6, 2);
                setStatus("You rolled a 6! A piece can leave the castle only with a 6 on the step die. "
                    + "Click a highlighted piece in your castle.");
                board.highlightCastlePieces();
                rollButton.setEnabled(false);
                stage = Stage.P1_TAKE_OUT;
                break;
            case P1_ROLL_2:
                pendingSteps = easy ? showRoll(4, 2) : showRoll(3, 2);
                setStatus("Move " + pendingSteps + " squares. Click your highlighted piece on the board.");
                board.highlightBoardPiece();
                rollButton.setEnabled(false);
                stage = Stage.P1_MOVE_2;
                break;
            case OTHER_ROLL:
                showRoll(3, 3);
                setStatus("All of " + names[currentPlayer] + "'s pieces are in the castle and the step die is not 6. "
                    + "No piece can move - the turn passes. Click \"Next Turn\".");
                rollButton.setText("Next Turn");
                stage = Stage.OTHER_PASS;
                break;
            case OTHER_PASS:
                currentPlayer++;
                rollButton.setText("Roll Dice");
                dice.clear();
                formulaLabel.setText(" ");
                if (currentPlayer < names.length) {
                    stage = Stage.OTHER_ROLL;
                } else {
                    currentPlayer = 0;
                    stage = Stage.P1_ROLL_3;
                }
                refreshPlayers();
                setStatus(names[currentPlayer] + ", it's your turn. Click \"Roll Dice\".");
                break;
            case P1_ROLL_3:
                pendingSteps = easy ? showRoll(4, 2) : showRoll(3, 2);
                setStatus("Move " + pendingSteps + " squares. Click your highlighted piece on the board.");
                board.highlightBoardPiece();
                rollButton.setEnabled(false);
                stage = Stage.P1_MOVE_3;
                break;
            default:
                break;
        }
    }

    private void onCastlePieceClicked() {
        if (stage != Stage.P1_TAKE_OUT) return;
        board.placeFirstPieceOnStart();
        setStatus("Your piece is on the start square (#). You rolled a 6, so you get another turn! "
            + "Click \"Roll Dice\" again.");
        rollButton.setEnabled(true);
        stage = Stage.P1_ROLL_2;
    }

    private void onBoardPieceClicked() {
        if (stage == Stage.P1_MOVE_2) {
            stage = Stage.BUSY;
            int target = board.getFirstPieceStep() + pendingSteps;
            board.animateFirstPieceTo(target, () -> {
                if (easy) {
                    setStatus("Moved " + pendingSteps + " squares.");
                    passTurn();
                } else {
                    askQuestion();
                }
            });
        } else if (stage == Stage.P1_MOVE_3) {
            stage = Stage.BUSY;
            int target = board.getFirstPieceStep() + pendingSteps;
            board.animateFirstPieceTo(target, () -> {
                if (easy) {
                    setStatus("You landed on a protection square (star). "
                        + "Pieces on this square cannot be captured.");
                    finishScenario();
                } else {
                    showSurprise();
                }
            });
        }
    }

    private void askQuestion() {
        Window owner = SwingUtilities.getWindowAncestor(this);
        QuestionDialog dialog = new QuestionDialog(owner);
        dialog.setVisible(true);
        boolean correct = dialog.isCorrect();
        int target = board.getFirstPieceStep() + (correct ? 1 : -1);
        setStatus(correct
            ? "Correct answer! Easy question: your piece moves 1 square forward."
            : "Wrong answer. Easy question: your piece moves 1 square back.");
        board.animateFirstPieceTo(target, this::passTurn);
    }

    private void showSurprise() {
        JPanel msg = new JPanel(new BorderLayout(0, 8));
        JLabel head = new JLabel("SURPRISE!", SwingConstants.CENTER);
        head.setOpaque(true);
        head.setBackground(BoardPanel.SURPRISE);
        head.setForeground(Color.WHITE);
        head.setFont(new Font("Arial", Font.BOLD, 22));
        head.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel text = new JLabel("<html><div style='width:260px;text-align:center'>"
            + "The drawn event is:<br><b>Go back 3 squares</b></div></html>", SwingConstants.CENTER);
        text.setFont(new Font("Arial", Font.PLAIN, 16));
        msg.add(head, BorderLayout.NORTH);
        msg.add(text, BorderLayout.CENTER);
        JOptionPane.showMessageDialog(this, msg, "Surprise square", JOptionPane.PLAIN_MESSAGE);

        int target = Math.max(0, board.getFirstPieceStep() - 3);
        setStatus("Surprise event: your piece moved 3 squares back.");
        board.animateFirstPieceTo(target, this::finishScenario);
    }

    private void passTurn() {
        currentPlayer = 1;
        stage = Stage.OTHER_ROLL;
        dice.clear();
        formulaLabel.setText(" ");
        rollButton.setEnabled(true);
        refreshPlayers();
        statusArea.append("\n\n" + names[currentPlayer] + ", it's your turn. Click \"Roll Dice\".");
    }

    private void finishScenario() {
        stage = Stage.DONE;
        rollButton.setEnabled(false);
        endGameButton.setEnabled(true);
        statusArea.append("\n\n(Prototype) End of the demo scenario. Click \"End Game\" "
            + "to see the end-of-game screen.");
    }

    /** Shows the dice values and returns the number of steps. */
    private int showRoll(int stepDie, int boostDie) {
        int steps;
        if (easy) {
            dice.show(stepDie, 0);
            steps = stepDie;
            formulaLabel.setText("Steps: " + steps);
        } else {
            dice.show(stepDie, boostDie);
            steps = stepDie * (boostDie - 1);
            formulaLabel.setText("Steps: " + stepDie + " x (" + boostDie + " - 1) = " + steps);
        }
        return steps;
    }

    // ------------------------------------------------------------------
    // Navigation
    // ------------------------------------------------------------------

    private void showEndScreen() {
        gameTimer.stop();
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (frame != null) {
            frame.setContentPane(new EndGameScreen(names, colors, difficulty, formatTime(secondsElapsed)));
            frame.setSize(700, 600);
            frame.setLocationRelativeTo(null);
            frame.revalidate();
            frame.repaint();
        }
    }

    private void backToMenu() {
        int answer = JOptionPane.showConfirmDialog(this,
            "Leave the game? An unfinished game is not saved to the history.",
            "Main Menu", JOptionPane.YES_NO_OPTION);
        if (answer != JOptionPane.YES_OPTION) return;
        gameTimer.stop();
        Window w = SwingUtilities.getWindowAncestor(this);
        new MainMenuView().setVisible(true);
        if (w != null) w.dispose();
    }

    // ------------------------------------------------------------------
    // UI helpers
    // ------------------------------------------------------------------

    private void refreshPlayers() {
        playersPanel.removeAll();
        JLabel header = new JLabel("Players");
        header.setFont(new Font("Arial", Font.BOLD, 15));
        playersPanel.add(header);
        for (int i = 0; i < names.length; i++) {
            boolean current = i == currentPlayer;
            JLabel l = new JLabel((current ? "\u25B6 " : "    ") + names[i] + " (" + colors[i] + ")");
            l.setFont(new Font("Arial", current ? Font.BOLD : Font.PLAIN, 15));
            l.setForeground(current ? BoardPanel.colorOf(colors[i]).darker() : MUTED);
            l.setBorder(BorderFactory.createEmptyBorder(3, 0, 3, 0));
            playersPanel.add(l);
        }
        playersPanel.revalidate();
        playersPanel.repaint();
    }

    private void setStatus(String text) {
        statusArea.setText(text);
        statusArea.setCaretPosition(0);
    }

    private JPanel createLegend() {
        JPanel legend = new JPanel(new GridLayout(0, 1, 0, 2));
        legend.setBackground(Color.WHITE);
        JLabel t = new JLabel("Legend");
        t.setFont(new Font("Arial", Font.BOLD, 14));
        legend.add(t);
        legend.add(legendRow("#", new Color(230, 230, 230), "Start square"));
        legend.add(legendRow("\u2605", new Color(255, 230, 150), "Protection square"));
        legend.add(legendRow("\u2192", new Color(230, 230, 230), "Arrow - to inner path (own colour)"));
        if (!easy) {
            legend.add(legendRow("?", BoardPanel.Q_EASY, "Question: easy / medium / hard"));
            legend.add(legendRow("!", BoardPanel.SURPRISE, "Surprise square"));
        }
        return legend;
    }

    private JPanel legendRow(String symbol, Color bg, String text) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        row.setBackground(Color.WHITE);
        JLabel s = new JLabel(symbol, SwingConstants.CENTER);
        s.setOpaque(true);
        s.setBackground(bg);
        s.setFont(new Font("Dialog", Font.BOLD, 13));
        s.setPreferredSize(new Dimension(22, 20));
        JLabel l = new JLabel(text);
        l.setFont(new Font("Arial", Font.PLAIN, 12));
        row.add(s);
        row.add(l);
        return row;
    }

    private static JComponent leftAligned(JComponent c) {
        c.setAlignmentX(Component.LEFT_ALIGNMENT);
        return c;
    }

    private JButton createButton(String text, Color bg) {
        JButton b = new JButton(text);
        b.setFont(new Font("Arial", Font.BOLD, 16));
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setMaximumSize(new Dimension(290, 44));
        b.setPreferredSize(new Dimension(290, 44));
        return b;
    }

    private void startTimer() {
        gameTimer = new Timer(1000, e -> {
            secondsElapsed++;
            timerLabel.setText("Game time: " + formatTime(secondsElapsed));
        });
        gameTimer.start();
    }

    static String formatTime(int seconds) {
        return String.format("%02d:%02d:%02d", seconds / 3600, (seconds % 3600) / 60, seconds % 60);
    }

   
    private static class DicePanel extends JPanel {
        private final boolean showBoost;
        private final String boostName;
        private int stepValue = 0;
        private int boostValue = 0;

        DicePanel(boolean showBoost, String boostName) {
            this.showBoost = showBoost;
            this.boostName = boostName;
            setBackground(Color.WHITE);
            setPreferredSize(new Dimension(290, 100));
            setMaximumSize(new Dimension(290, 100));
        }

        void show(int step, int boost) {
            stepValue = step;
            boostValue = boost;
            repaint();
        }

        void clear() {
            show(0, 0);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            drawDie(g2, 10, 10, 70, stepValue, true);
            caption(g2, "Step die", 10, 95, 70);
            if (showBoost) {
                drawDie(g2, 130, 10, 70, boostValue, false);
                caption(g2, boostName, 130, 95, 70);
            }
            g2.dispose();
        }

        private void caption(Graphics2D g2, String text, int x, int y, int w) {
            g2.setFont(new Font("Arial", Font.PLAIN, 12));
            g2.setColor(MUTED);
            int tw = g2.getFontMetrics().stringWidth(text);
            g2.drawString(text, x + (w - tw) / 2, y);
        }

        private void drawDie(Graphics2D g2, int x, int y, int s, int value, boolean pips) {
            g2.setColor(pips ? Color.WHITE : new Color(255, 245, 225));
            g2.fillRoundRect(x, y, s, s, 16, 16);
            g2.setColor(new Color(90, 95, 110));
            g2.setStroke(new BasicStroke(2.5f));
            g2.drawRoundRect(x, y, s, s, 16, 16);
            if (value == 0) {
                g2.setFont(new Font("Arial", Font.BOLD, 30));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString("?", x + (s - fm.stringWidth("?")) / 2, y + (s + fm.getAscent()) / 2 - 4);
                return;
            }
            if (!pips) {
                g2.setColor(new Color(200, 110, 0));
                g2.setFont(new Font("Arial", Font.BOLD, 36));
                FontMetrics fm = g2.getFontMetrics();
                String t = String.valueOf(value);
                g2.drawString(t, x + (s - fm.stringWidth(t)) / 2, y + (s + fm.getAscent()) / 2 - 4);
                return;
            }
            g2.setColor(new Color(40, 40, 50));
            int d = s / 6;
            int l = x + s / 4, m = x + s / 2, r = x + 3 * s / 4;
            int t = y + s / 4, c = y + s / 2, b = y + 3 * s / 4;
            int[][] spots;
            switch (value) {
                case 1: spots = new int[][]{{m, c}}; break;
                case 2: spots = new int[][]{{l, t}, {r, b}}; break;
                case 3: spots = new int[][]{{l, t}, {m, c}, {r, b}}; break;
                case 4: spots = new int[][]{{l, t}, {r, t}, {l, b}, {r, b}}; break;
                case 5: spots = new int[][]{{l, t}, {r, t}, {m, c}, {l, b}, {r, b}}; break;
                default: spots = new int[][]{{l, t}, {r, t}, {l, c}, {r, c}, {l, b}, {r, b}}; break;
            }
            for (int[] p : spots) {
                g2.fillOval(p[0] - d / 2, p[1] - d / 2, d, d);
            }
        }
    }
}
