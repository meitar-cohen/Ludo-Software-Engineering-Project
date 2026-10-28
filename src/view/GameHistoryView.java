package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

public class GameHistoryView extends JFrame {
    private static final Color NAVY = new Color(12, 20, 49);
    private static final Color BLUE = new Color(75, 110, 248);
    private static final Color MUTED = new Color(111, 123, 151);

    private final Object[][] sampleGames = {
        {"G-003", "27/10/2026 18:35", "Medium", "Noa", "00:23:15"},
        {"G-002", "26/10/2026 14:10", "Easy", "Meitar", "00:15:42"},
        {"G-001", "25/10/2026 20:05", "Hard", "Daniel", "00:31:04"}
    };
    private final String[][] rankings = {
        {"Noa", "Meitar", "Maor", "Daniel"},
        {"Meitar", "Daniel", "Maor"},
        {"Daniel", "Noa"}
    };

    public GameHistoryView() {
        setTitle("Ludo - Game History");
        setSize(1120, 690);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel background = new GradientBackground();
        background.setLayout(new GridBagLayout());
        JPanel layout = new JPanel(new GridLayout(1, 2, 28, 0));
        layout.setOpaque(false);
        layout.setPreferredSize(new Dimension(1020, 565));
        layout.add(new HistoryHero());
        layout.add(buildHistoryCard());
        background.add(layout);
        setContentPane(background);
    }

    private JPanel buildHistoryCard() {
        JPanel card = new RoundedCard();
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(28, 35, 22, 35));

        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        JLabel eyebrow = new JLabel("YOUR MATCHES  /  RESULTS");
        eyebrow.setFont(new Font("Segoe UI", Font.BOLD, 12));
        eyebrow.setForeground(BLUE);
        eyebrow.setAlignmentX(LEFT_ALIGNMENT);
        JLabel heading = new JLabel("Game history");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 29));
        heading.setForeground(NAVY);
        heading.setAlignmentX(LEFT_ALIGNMENT);
        JLabel subheading = new JLabel("Review previous games and final rankings.");
        subheading.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subheading.setForeground(MUTED);
        subheading.setAlignmentX(LEFT_ALIGNMENT);
        header.add(eyebrow); header.add(Box.createVerticalStrut(9));
        header.add(heading); header.add(Box.createVerticalStrut(4)); header.add(subheading);

        String[] columns = {"Game ID", "Date & Time", "Difficulty", "Winner", "Duration"};
        DefaultTableModel model = new DefaultTableModel(sampleGames, columns) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(38);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setFillsViewportHeight(true);
        table.setShowVerticalLines(false);
        table.setGridColor(new Color(231, 234, 244));
        table.setIntercellSpacing(new Dimension(0, 1));
        table.setSelectionBackground(new Color(234, 239, 255));
        table.setSelectionForeground(NAVY);
        JTableHeader headerTable = table.getTableHeader();
        headerTable.setFont(new Font("Segoe UI", Font.BOLD, 12));
        headerTable.setForeground(new Color(42, 51, 81));
        headerTable.setBackground(new Color(244, 246, 252));
        headerTable.setPreferredSize(new Dimension(0, 38));
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(225, 230, 242)));
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.setPreferredSize(new Dimension(416, 166));

        JTextArea ranking = new JTextArea();
        ranking.setEditable(false);
        ranking.setFocusable(false);
        ranking.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        ranking.setForeground(NAVY);
        ranking.setBackground(new Color(244, 246, 252));
        ranking.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(225, 230, 242)),
            new EmptyBorder(10, 14, 10, 14)));

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() >= 0) {
                int index = table.convertRowIndexToModel(table.getSelectedRow());
                StringBuilder text = new StringBuilder("FINAL RANKING - " + sampleGames[index][0]);
                String[] places = {"1st", "2nd", "3rd", "4th"};
                for (int i = 0; i < rankings[index].length; i++)
                    text.append("\n").append(places[i]).append(": ").append(rankings[index][i]);
                ranking.setText(text.toString());
            }
        });
        table.setRowSelectionInterval(0, 0);

        JPanel center = new JPanel();
        center.setOpaque(false);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.add(scroll); center.add(Box.createVerticalStrut(14)); center.add(ranking);

        JPanel actions = new JPanel();
        actions.setOpaque(false);
        actions.setLayout(new BorderLayout());
        JButton back = new ActionButton("Back", false);
        back.addActionListener(e -> { new MainMenuView().setVisible(true); dispose(); });
        actions.add(back, BorderLayout.CENTER);

        card.add(header, BorderLayout.NORTH);
        card.add(center, BorderLayout.CENTER);
        card.add(actions, BorderLayout.SOUTH);
        return card;
    }

    private static void quality(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    }

    private static class GradientBackground extends JPanel {
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            Graphics2D g = (Graphics2D) graphics.create(); quality(g);
            g.setPaint(new GradientPaint(0, 0, new Color(14, 22, 54), getWidth(), getHeight(), new Color(43, 39, 100)));
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(new Color(110, 103, 231, 45)); g.fillOval(-180, -210, 650, 650);
            g.setColor(new Color(72, 177, 246, 24)); g.fillOval(460, 320, 580, 580);
            g.setColor(new Color(244, 91, 146, 22)); g.fillOval(890, -230, 440, 440);
            g.dispose();
        }
    }

    private static class HistoryHero extends JPanel {
        HistoryHero() { setOpaque(false); }
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics); Graphics2D g = (Graphics2D) graphics.create(); quality(g);
            g.setFont(new Font("Segoe UI", Font.BOLD, 13)); g.setColor(new Color(177, 186, 240));
            g.drawString("EVERY MATCH HAS A STORY", 39, 78);
            g.setFont(new Font("Segoe UI", Font.BOLD, 58)); g.setColor(Color.WHITE);
            g.drawString("Look back.", 35, 158); g.drawString("Learn more.", 35, 221);
            g.setFont(new Font("Segoe UI", Font.PLAIN, 17)); g.setColor(new Color(203, 213, 248));
            g.drawString("Check results, winners, and final", 39, 267); g.drawString("rankings from every game.", 39, 294);

            int x = 96, y = 340, w = 310, h = 155;
            g.setColor(new Color(5, 10, 32, 70)); g.fillRoundRect(x + 9, y + 12, w, h, 28, 28);
            g.setColor(Color.WHITE); g.fillRoundRect(x, y, w, h, 28, 28);
            g.setColor(new Color(75, 110, 248)); g.fillRoundRect(x + 22, y + 24, 54, 54, 16, 16);
            g.setColor(new Color(255, 113, 135)); g.fillRoundRect(x + 84, y + 24, 54, 54, 16, 16);
            g.setColor(new Color(68, 205, 159)); g.fillRoundRect(x + 22, y + 86, 54, 54, 16, 16);
            g.setColor(new Color(255, 195, 78)); g.fillRoundRect(x + 84, y + 86, 54, 54, 16, 16);
            g.setFont(new Font("Segoe UI", Font.BOLD, 18)); g.setColor(NAVY);
            g.drawString("FINAL RANKING", x + 158, y + 50);
            g.setFont(new Font("Segoe UI", Font.PLAIN, 14)); g.setColor(MUTED);
            g.drawString("Winner: selected game", x + 158, y + 82);
            g.drawString("Duration and scores", x + 158, y + 108);
            g.dispose();
        }
    }

    private static class RoundedCard extends JPanel {
        RoundedCard() { setOpaque(false); }
        protected void paintComponent(Graphics graphics) {
            Graphics2D g = (Graphics2D) graphics.create(); quality(g); int w = getWidth(), h = getHeight();
            g.setColor(new Color(6, 11, 31, 45)); g.fill(new RoundRectangle2D.Double(6, 10, w - 12, h - 19, 38, 38));
            g.setColor(new Color(250, 251, 255)); g.fill(new RoundRectangle2D.Double(2, 2, w - 12, h - 19, 38, 38));
            g.dispose(); super.paintComponent(graphics);
        }
    }

    private static class ActionButton extends JButton {
        private final boolean primary; private boolean hovered;
        ActionButton(String text, boolean primary) {
            super(text); this.primary = primary;
            setFont(new Font("Segoe UI", Font.BOLD, 15)); setOpaque(false); setContentAreaFilled(false); setBorderPainted(false); setFocusPainted(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); setPreferredSize(new Dimension(400, 46)); setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
            addMouseListener(new MouseAdapter() { public void mouseEntered(MouseEvent e) { hovered = true; repaint(); } public void mouseExited(MouseEvent e) { hovered = false; repaint(); } });
        }
        protected void paintComponent(Graphics graphics) {
            Graphics2D g = (Graphics2D) graphics.create(); quality(g); int w = getWidth(), h = getHeight();
            g.setColor(hovered ? new Color(226, 234, 255) : new Color(239, 243, 253)); g.fillRoundRect(2, 2, w - 5, h - 5, 18, 18);
            g.setFont(getFont()); g.setColor(new Color(40, 53, 91)); FontMetrics fm = g.getFontMetrics();
            String text = getText(); g.drawString(text, (w - fm.stringWidth(text)) / 2, (h - fm.getHeight()) / 2 + fm.getAscent()); g.dispose();
        }
    }
}
