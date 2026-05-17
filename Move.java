public class Move {
    public final int row;
    public final int col;

    public Move(int row, int col) {
        this.row = row;
        this.col = col;
    }

    @Override
    public String toString() {
        return "linha " + row + ", coluna " + col;
    }
}