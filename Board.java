import java.util.ArrayList;
import java.util.List;

public class Board {
    public static final int SIZE = 4;
    public static final char EMPTY = '-';

    private final char[][] cells;

    public Board() {
        this.cells = new char[SIZE][SIZE];
        reset();
    }

    public void reset() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                cells[i][j] = EMPTY;
            }
        }
    }

    public char getCell(int row, int col) {
        return cells[row][col];
    }

    public boolean isValidPosition(int row, int col) {
        return row >= 0 && row < SIZE && col >= 0 && col < SIZE;
    }

    public boolean isEmptyCell(int row, int col) {
        return cells[row][col] == EMPTY;
    }

    public boolean makeMove(Move move, char player) {
        if (!isValidPosition(move.row, move.col) || !isEmptyCell(move.row, move.col)) {
            return false;
        }
        cells[move.row][move.col] = player;
        return true;
    }

    public void undoMove(Move move) {
        cells[move.row][move.col] = EMPTY;
    }

    public List<Move> getAvailableMoves() {
        List<Move> moves = new ArrayList<>();

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (cells[i][j] == EMPTY) {
                    moves.add(new Move(i, j));
                }
            }
        }

        return moves;
    }

    public boolean isFull() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (cells[i][j] == EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }

    public char checkWinner() {
        for (int i = 0; i < SIZE; i++) {
            char first = cells[i][0];
            if (first != EMPTY) {
                boolean win = true;
                for (int j = 1; j < SIZE; j++) {
                    if (cells[i][j] != first) {
                        win = false;
                        break;
                    }
                }
                if (win) {
                    return first;
                }
            }
        }

        for (int j = 0; j < SIZE; j++) {
            char first = cells[0][j];
            if (first != EMPTY) {
                boolean win = true;
                for (int i = 1; i < SIZE; i++) {
                    if (cells[i][j] != first) {
                        win = false;
                        break;
                    }
                }
                if (win) {
                    return first;
                }
            }
        }

        char mainDiag = cells[0][0];
        if (mainDiag != EMPTY) {
            boolean win = true;
            for (int i = 1; i < SIZE; i++) {
                if (cells[i][i] != mainDiag) {
                    win = false;
                    break;
                }
            }
            if (win) {
                return mainDiag;
            }
        }

        char secondDiag = cells[0][SIZE - 1];
        if (secondDiag != EMPTY) {
            boolean win = true;
            for (int i = 1; i < SIZE; i++) {
                if (cells[i][SIZE - 1 - i] != secondDiag) {
                    win = false;
                    break;
                }
            }
            if (win) {
                return secondDiag;
            }
        }

        return EMPTY;
    }

    public boolean isDraw() {
        return checkWinner() == EMPTY && isFull();
    }

    public int evaluateHeuristic(char aiPlayer, char humanPlayer) {
        int score = 0;

        for (int i = 0; i < SIZE; i++) {
            score += evaluateLine(
                    cells[i][0], cells[i][1], cells[i][2], cells[i][3],
                    aiPlayer, humanPlayer
            );
        }

        for (int j = 0; j < SIZE; j++) {
            score += evaluateLine(
                    cells[0][j], cells[1][j], cells[2][j], cells[3][j],
                    aiPlayer, humanPlayer
            );
        }

        score += evaluateLine(
                cells[0][0], cells[1][1], cells[2][2], cells[3][3],
                aiPlayer, humanPlayer
        );

        score += evaluateLine(
                cells[0][3], cells[1][2], cells[2][1], cells[3][0],
                aiPlayer, humanPlayer
        );

        return score;
    }

    private int evaluateLine(char a, char b, char c, char d, char aiPlayer, char humanPlayer) {
        char[] line = {a, b, c, d};

        int aiCount = 0;
        int humanCount = 0;

        for (char cell : line) {
            if (cell == aiPlayer) {
                aiCount++;
            } else if (cell == humanPlayer) {
                humanCount++;
            }
        }

        if (aiCount > 0 && humanCount > 0) {
            return 0;
        }

        if (aiCount > 0) {
            return lineScore(aiCount);
        }

        if (humanCount > 0) {
            return -lineScore(humanCount);
        }

        return 0;
    }

    private int lineScore(int count) {
        switch (count) {
            case 1:
                return 1;
            case 2:
                return 10;
            case 3:
                return 100;
            case 4:
                return 1000;
            default:
                return 0;
        }

    }

    public void printBoard() {
    System.out.println();
    System.out.println("    0   1   2   3");
    for (int i = 0; i < SIZE; i++) {
        System.out.print(i + " | ");
        for (int j = 0; j < SIZE; j++) {
            System.out.print(cells[i][j]);
            if (j < SIZE - 1) {
                System.out.print(" | ");
            }
        }
        System.out.println(" |");
    }
    System.out.println();
}
}