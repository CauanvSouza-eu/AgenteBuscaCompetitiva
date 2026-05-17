public class AIResult {
    public final Move bestMove;
    public final long nodesEvaluated;
    public final long executionTimeNanos;
    public final int bestScore;

    public AIResult(Move bestMove, long nodesEvaluated, long executionTimeNanos, int bestScore) {
        this.bestMove = bestMove;
        this.nodesEvaluated = nodesEvaluated;
        this.executionTimeNanos = executionTimeNanos;
        this.bestScore = bestScore;
    }
}