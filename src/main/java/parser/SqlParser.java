package parser;

import exception.SqlParsingException;
import expression.ComparisonOperation;
import expression.SelectStatement;
import expression.UnaryOperation;
import expression.WhereClause;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.*;
import net.sf.jsqlparser.util.TablesNamesFinder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


public class SqlParser {
    public SelectStatement parseSelectStatement(String select) throws SqlParsingException {
        Statement statement;
        try {
            statement = CCJSqlParserUtil.parse(select);
        } catch (JSQLParserException e) {
            throw new SqlParsingException("Can't parse select statement", e);
        }

        Select selectStatement;
        try {
            selectStatement = (Select) statement;
        } catch (ClassCastException e) {
            throw new SqlParsingException("This is not a select statement");
        }

        PlainSelect plainSelect = (PlainSelect)selectStatement.getSelectBody();

        String tableName = new TablesNamesFinder().getTableList(selectStatement).get(0);

        List<String> selectItemNames = getSelectItemNames(plainSelect);

        WhereClause where = getWhereClause(plainSelect);

        List<UnaryOperation> unaryOperations = getUnaryOperations(plainSelect, select);

        SelectStatement res = new SelectStatement(where, tableName, selectItemNames);
        res.setUnaryOperations(unaryOperations);

        return res;
    }


    private WhereClause getWhereClause(PlainSelect select) {
        BinaryExpression where = (BinaryExpression)select.getWhere();
        WhereClause wc = new WhereClause();

        if (where == null) {
            return wc;
        }

        setOperations(wc, where);

        return wc;
    }

    private void setOperations(WhereClause whereClause, BinaryExpression expr) {
        if (expr instanceof AndExpression) {
            setOperations(whereClause, (BinaryExpression)expr.getLeftExpression());
            setOperations(whereClause, (BinaryExpression)expr.getRightExpression());
        } else {
            whereClause.addOperation(new ComparisonOperation(
                    expr.getStringExpression(),
                    expr.getLeftExpression().toString(),
                    expr.getRightExpression().toString()
            ));
        }


    }

    private List<String> getSelectItemNames(PlainSelect select) {
        List<String> selectItemNames = new ArrayList<>();
        List<SelectItem> selectItems = (select).getSelectItems();

        if (selectItems.size() == 1 && ("*".equals(selectItems.get(0).toString()))) {
            return selectItemNames;
        }

        selectItems.forEach(item -> selectItemNames.add(item.toString()));

        return selectItemNames;
    }

    private List<UnaryOperation> getUnaryOperations(PlainSelect plainSelect, String selectStr) {
        List<UnaryOperation> res = new ArrayList<>();

        Long offset = Optional.of(plainSelect)
                .map(PlainSelect::getOffset)
                .map(Offset::getOffset)
                .orElse(null);

        Long limit = Optional.of(plainSelect)
                .map(PlainSelect::getLimit)
                .map(Limit::getRowCount)
                .map(expr -> ((LongValue)expr).getValue())
                .orElse(null);

        if (offset != null) {
            res.add(new UnaryOperation("OFFSET", offset));
        }

        if (limit != null) {
            res.add(new UnaryOperation("LIMIT", limit));
        }

        if (res.size() == 2) {
            if (selectStr.indexOf("LIMIT") < selectStr.indexOf("OFFSET")) {
                Collections.reverse(res);
            }
        }

        return res;
    }
}
