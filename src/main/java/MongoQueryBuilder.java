import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import expression.ComparisonOperation;
import expression.UnaryOperation;

public class MongoQueryBuilder {
    private static final String COMMO = ", ";

    public static final String EMPTY_FIND_PREDICATE = "{}";

    public String buildFind(String collectionName,
                            String findPredicate,
                            List<UnaryOperation> unaryOperations,
                            String... args) {
        StringBuilder sb = new StringBuilder();
        String items = args.length > 0
                ? COMMO + "{" + Arrays.stream(args).map(s -> s + ": 1").collect(Collectors.joining(COMMO)) + "}"
                : "";

        sb.append(String.format("db.%s.find(", collectionName))

                .append(findPredicate)
                .append(items)
                .append(")");

        unaryOperations.forEach(op -> sb.append(buildUnaryOperation(op)));

        return sb.toString();
    }

    public String buildFindPredicate(List<ComparisonOperation> operations) {
        return "{" + operations.stream()
                .map(this::buildComparisonOperation)
                .collect(Collectors.joining(COMMO)) + "} ";
    }

    public String buildComparisonOperation(ComparisonOperation op) {
        StringBuilder sb = new StringBuilder();

        sb.append(op.getLeftOperand())
                .append(": ");

        if (op.getOperation().isEmpty()) {
            sb.append(op.getRightOperand());
        } else {
            sb.append("{")
                    .append(op.getOperation())
                    .append(": ")
                    .append(op.getRightOperand())
                    .append("}");
        }


        return sb.toString();
    }

    private String buildUnaryOperation(UnaryOperation op) {
        return String.format(".%s(%d)", op.getName(), op.getValue());
    }

}
