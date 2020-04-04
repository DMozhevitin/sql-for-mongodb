import exception.SqlParsingException;
import expression.ComparisonOperation;
import expression.SelectStatement;
import expression.UnaryOperation;
import expression.WhereClause;
import parser.SqlParser;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Sql2MongoTranslator {
    private SqlParser sqlParser;
    private MongoQueryBuilder mongoQueryBuilder;

    private static final Map<String, String> sql2MongoComparisonOperations = Map.of(
            "=", "",
            "<>", "$ne",
            "<", "$lt",
            ">", "$gt",
            "<=", "$lte",
            ">=", "$gte"
    );

    private static final Map<String, String> sql2MongoUnaryOperations = Map.of(
            "OFFSET", "skip",
            "LIMIT", "limit"
    );

    public Sql2MongoTranslator() {
        this.sqlParser = new SqlParser();
        this.mongoQueryBuilder = new MongoQueryBuilder();
    }

    public String select2Find(String select) throws SqlParsingException {
        SelectStatement selectStatement = sqlParser.parseSelectStatement(select);
        WhereClause whereClause = selectStatement.getWhereClause();

        String findPredicate;
        if (whereClause == null) {
            findPredicate = MongoQueryBuilder.EMPTY_FIND_PREDICATE;
        } else {
            List<ComparisonOperation> ops = whereClause.getComparisonOperations().stream()
                    .map(op -> new ComparisonOperation(
                            sql2MongoComparisonOperations.get(op.getOperation()),
                            op.getLeftOperand(),
                            op.getRightOperand()
                    )).collect(Collectors.toList());

            findPredicate = mongoQueryBuilder.buildFindPredicate(ops);
        }

        List<UnaryOperation> unaryOperations = selectStatement.getUnaryOperations().stream()
                .map(op -> new UnaryOperation(sql2MongoUnaryOperations.get(op.getName()), op.getValue()))
                .collect(Collectors.toList());

        return mongoQueryBuilder.buildFind(
                selectStatement.getTableName(),
                findPredicate,
                unaryOperations,
                selectStatement.getSelectItems().toArray(new String[0]));
    }
}
