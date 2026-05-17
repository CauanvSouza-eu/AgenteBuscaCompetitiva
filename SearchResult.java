public class SearchResult {
    public final int score;
    public final long nodes;

    public SearchResult(int score, long nodes) {
        this.score = score;
        this.nodes = nodes;
    }
}