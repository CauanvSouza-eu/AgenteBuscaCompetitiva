import javax.swing.*;
import java.awt.*;

public class GameScreen extends JFrame {

    private static final char HUMAN = 'X';
    private static final char AI = 'O';
    private static final int MAX_DEPTH = 6;

    private final Board board;
    private final JButton[][] buttons;
    private final JLabel statusLabel;
    private final JLabel nodesLabel;
    private final JLabel timeLabel;
    private final JLabel moveLabel;
    private final JButton resetButton;

    private boolean gameOver;
    private boolean aiThinking;

    public GameScreen() {
        this.board = new Board();
        this.buttons = new JButton[Board.SIZE][Board.SIZE];
        this.statusLabel = new JLabel("Sua vez (X)");
        this.nodesLabel = new JLabel("Nós avaliados: -");
        this.timeLabel = new JLabel("Tempo: -");
        this.moveLabel = new JLabel("Melhor jogada: -");
        this.resetButton = new JButton("Reiniciar");

        this.gameOver = false;
        this.aiThinking = false;

        configurarJanela();
        criarLayout();
        atualizarTabuleiro();
    }

    private void configurarJanela() {
        setTitle("Jogo da Velha 4x4 - Minimax");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(650, 720);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void criarLayout() {
        setLayout(new BorderLayout(10, 10));

        JPanel topPanel = new JPanel(new GridLayout(4, 1));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));

        statusLabel.setFont(new Font("Arial", Font.BOLD, 20));
        nodesLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        timeLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        moveLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        topPanel.add(statusLabel);
        topPanel.add(nodesLabel);
        topPanel.add(timeLabel);
        topPanel.add(moveLabel);

        JPanel boardPanel = new JPanel(new GridLayout(Board.SIZE, Board.SIZE, 8, 8));
        boardPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        Font buttonFont = new Font("Arial", Font.BOLD, 36);

        for (int i = 0; i < Board.SIZE; i++) {
            for (int j = 0; j < Board.SIZE; j++) {
                JButton button = new JButton("");
                button.setFont(buttonFont);
                button.setFocusPainted(false);

                final int row = i;
                final int col = j;

                button.addActionListener(e -> handleHumanMove(row, col));

                buttons[i][j] = button;
                boardPanel.add(button);
            }
        }

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

        resetButton.setFont(new Font("Arial", Font.BOLD, 16));
        resetButton.addActionListener(e -> resetGame());

        bottomPanel.add(resetButton);

        add(topPanel, BorderLayout.NORTH);
        add(boardPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void handleHumanMove(int row, int col) {
        if (gameOver || aiThinking) {
            return;
        }

        Move move = new Move(row, col);
        boolean success = board.makeMove(move, HUMAN);

        if (!success) {
            return;
        }

        atualizarTabuleiro();

        if (verificarFimDeJogo()) {
            return;
        }

        fazerJogadaIA();
    }

    private void fazerJogadaIA() {
        aiThinking = true;
        atualizarTabuleiro();
        statusLabel.setText("IA pensando...");

        SwingWorker<AIResult, Void> worker = new SwingWorker<>() {
            @Override
            protected AIResult doInBackground() {
                MinimaxAI ai = new MinimaxAI(AI, HUMAN, MAX_DEPTH);
                return ai.chooseBestMove(board);
            }

            @Override
            protected void done() {
                try {
                    AIResult result = get();

                    nodesLabel.setText("Nós avaliados: " + result.nodesEvaluated);
                    timeLabel.setText(String.format("Tempo: %.3f ms", result.executionTimeNanos / 1_000_000.0));
                    moveLabel.setText("Melhor jogada: " + (result.bestMove != null ? result.bestMove : "-"));

                    if (result.bestMove != null) {
                        board.makeMove(result.bestMove, AI);
                    }

                    aiThinking = false;

                    if (!verificarFimDeJogo()) {
                        statusLabel.setText("Sua vez (X)");
                    }

                    atualizarTabuleiro();

                } catch (Exception ex) {
                    aiThinking = false;
                    statusLabel.setText("Erro ao executar a IA.");
                    atualizarTabuleiro();
                    ex.printStackTrace();
                }
            }
        };

        worker.execute();
    }

    private boolean verificarFimDeJogo() {
        char winner = board.checkWinner();

        if (winner == HUMAN) {
            statusLabel.setText("Você venceu!");
            gameOver = true;
            atualizarTabuleiro();
            return true;
        }

        if (winner == AI) {
            statusLabel.setText("A IA venceu!");
            gameOver = true;
            atualizarTabuleiro();
            return true;
        }

        if (board.isDraw()) {
            statusLabel.setText("Empate!");
            gameOver = true;
            atualizarTabuleiro();
            return true;
        }

        return false;
    }

    private void atualizarTabuleiro() {
        for (int i = 0; i < Board.SIZE; i++) {
            for (int j = 0; j < Board.SIZE; j++) {
                char cell = board.getCell(i, j);
                JButton button = buttons[i][j];

                if (cell == Board.EMPTY) {
                    button.setText("");
                    button.setEnabled(!gameOver && !aiThinking);
                    button.setBackground(Color.WHITE);
                    button.setForeground(Color.BLACK);
                } else {
                    button.setText(String.valueOf(cell));
                    button.setEnabled(false);

                    if (cell == HUMAN) {
                        button.setForeground(new Color(0, 102, 204));
                    } else {
                        button.setForeground(new Color(204, 51, 51));
                    }
                }
            }
        }
    }

    private void resetGame() {
        board.reset();
        gameOver = false;
        aiThinking = false;

        statusLabel.setText("Sua vez (X)");
        nodesLabel.setText("Nós avaliados: -");
        timeLabel.setText("Tempo: -");
        moveLabel.setText("Melhor jogada: -");

        atualizarTabuleiro();
    }
}