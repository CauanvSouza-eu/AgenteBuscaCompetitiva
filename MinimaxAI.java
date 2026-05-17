import java.util.List;

public class MinimaxAI {
    private final char aiPlayer;
    private final char humanPlayer;
    private final int maxDepth;

    public MinimaxAI(char aiPlayer, char humanPlayer, int maxDepth) {
        this.aiPlayer = aiPlayer;
        this.humanPlayer = humanPlayer;
        this.maxDepth = maxDepth;
    }

    public AIResult chooseBestMove(Board board) {
        long startTime = System.nanoTime();

        int bestValue = Integer.MIN_VALUE;
        Move bestMove = null;
        long totalNodes = 0;

        List<Move> moves = board.getAvailableMoves();

        for (Move move : moves) {
            board.makeMove(move, aiPlayer);

            SearchResult result = minimax(board, 0, false);

            board.undoMove(move);

            totalNodes += result.nodes;

            if (result.score > bestValue) {
                bestValue = result.score;
                bestMove = move;
            }
        }

        long endTime = System.nanoTime();

        return new AIResult(bestMove, totalNodes, endTime - startTime, bestValue);
    }

    private SearchResult minimax(Board board, int depth, boolean maximizingPlayer) {
        long nodes = 1;

        char winner = board.checkWinner();

        if (winner == aiPlayer) {
            return new SearchResult(100000 - depth, nodes);
        }

        if (winner == humanPlayer) {
            return new SearchResult(-100000 + depth, nodes);
        }

        if (board.isDraw()) {
            return new SearchResult(0, nodes);
        }

        if (depth >= maxDepth) {
            return new SearchResult(board.evaluateHeuristic(aiPlayer, humanPlayer), nodes);
        }

        List<Move> moves = board.getAvailableMoves();

        if (maximizingPlayer) {
            int bestValue = Integer.MIN_VALUE;

            for (Move move : moves) {
                board.makeMove(move, aiPlayer);

                SearchResult child = minimax(board, depth + 1, false);
                nodes += child.nodes;

                board.undoMove(move);

                bestValue = Math.max(bestValue, child.score);
            }

            return new SearchResult(bestValue, nodes);
        } else {
            int bestValue = Integer.MAX_VALUE;

            for (Move move : moves) {
                board.makeMove(move, humanPlayer);

                SearchResult child = minimax(board, depth + 1, true);
                nodes += child.nodes;

                board.undoMove(move);

                bestValue = Math.min(bestValue, child.score);
            }

            return new SearchResult(bestValue, nodes);
        }
    }
}