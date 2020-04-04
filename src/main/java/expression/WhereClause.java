package expression;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WhereClause {
    private List<ComparisonOperation> comparisonOperations;

    public WhereClause() {
        comparisonOperations = new ArrayList<>();
    }

    public void addOperation(ComparisonOperation op) {
        comparisonOperations.add(op);
    }

    public List<ComparisonOperation> getComparisonOperations() {
        return comparisonOperations;
    }

    @Override
    public int hashCode() {
        return Objects.hash(comparisonOperations);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null) {
            return false;
        }

        if (getClass() != o.getClass()) {
            return false;
        }

        WhereClause that = (WhereClause) o;

        return Objects.equals(getComparisonOperations(), that.getComparisonOperations());
    }
}
