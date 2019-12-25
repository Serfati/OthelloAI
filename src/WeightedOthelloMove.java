import java.util.Objects;

public class WeightedOthelloMove implements IMove {
    public int row;
    public int col;


    public WeightedOthelloMove(int row, int col) {
        this.row = row;
        this.col = col;
    }


    public WeightedOthelloMove(WeightedOthelloMove toCopy) {
        this(toCopy.row, toCopy.col);
    }


    @Override
    public boolean equals(Object otherMove) {
        if (otherMove instanceof WeightedOthelloMove) {
            if (this.row == ((WeightedOthelloMove) otherMove).row && this.col == ((WeightedOthelloMove) otherMove).col)
                return true;
        }
        return false;
    }


    @Override
    public int hashCode() {
        int hashCode = Objects.hash(row, col);
        return hashCode;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[ ").append(row).append(" , ").append(col).append(" ]");
        return sb.toString();
    }
}
