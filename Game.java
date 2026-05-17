import java.util.Scanner;

public class Game {
    private static final char HUMAN = 'X';
    private static final char AI = 'O';
    private static final int MAX_DEPTH = 6;

    private final Board board;
    private final MinimaxAI ai;
    private final Scanner scanner;

    public Game() {
        this.board = new Board();
        this.ai = new MinimaxAI(AI, HUMAN, MAX_DEPTH);
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("=== JOGO DA VELHA 4x4 ===");
        System.out.println("Humano: X");
        System.out.println("IA: O");
        System.out.println("Vitória com 4 símbolos em linha.");
        System.out.println();

        while (true) {
            board.printBoard();

            if (board.checkWinner() == HUMAN) {
                System.out.println("Você venceu!");
                break;
            }

            if (board.checkWinner() == AI) {
                System.out.println("A IA venceu!");
                break;
            }

            if (board.isDraw()) {
                System.out.println("Empate!");
                break;
            }

            humanTurn();

            board.printBoard();

            if (board.checkWinner() == HUMAN) {
                System.out.println("Você venceu!");
                break;
            }

            if (board.checkWinner() == AI) {
                System.out.println("A IA venceu!");
                break;
            }

            if (board.isDraw()) {
                System.out.println("Empate!");
                break;
            }

            aiTurn();
        }

        scanner.close();
    }

    private void humanTurn() {
        while (true) {
            System.out.print("Digite a linha: ");
            int row = scanner.nextInt();

            System.out.print("Digite a coluna: ");
            int col = scanner.nextInt();

            if (!board.isValidPosition(row, col)) {
                System.out.println("Posição inválida. Tente novamente.");
                continue;
            }

            Move move = new Move(row, col);

            if (!board.makeMove(move, HUMAN)) {
                System.out.println("Casa ocupada. Tente novamente.");
                continue;
            }

            break;
        }
    }

    private void aiTurn() {
        System.out.println("IA pensando...");

        AIResult result = ai.chooseBestMove(board);

        if (result.bestMove != null) {
            board.makeMove(result.bestMove, AI);
        }

        double executionMillis = result.executionTimeNanos / 1_000_000.0;

        System.out.println("=== Indicadores da busca ===");
        System.out.println("Número de nós avaliados: " + result.nodesEvaluated);
        System.out.printf("Tempo de execução: %.3f ms%n", executionMillis);
        if (result.bestMove != null) {
            System.out.println("Melhor jogada encontrada: " + result.bestMove);
        } else {
            System.out.println("Melhor jogada encontrada: nenhuma");
        }
        System.out.println("Valor da melhor jogada: " + result.bestScore);
        System.out.println();
    }
}