package parser;

import exception.SqlParsingException;
import expression.ComparisonOperation;
import expression.SelectStatement;
import expression.UnaryOperation;
import expression.WhereClause;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;

public class Test_SqlParser {
    private static final SqlParser sqlParser = new SqlParser();

    @Test
    public void sampleTest_1() throws SqlParsingException {
        SelectStatement expectedSelect = new SelectStatement();
        expectedSelect.setTableName("sales");
        expectedSelect.setSelectItems(List.of());
        expectedSelect.setUnaryOperations(List.of(
                new UnaryOperation("LIMIT", 10L)
        ));
        expectedSelect.setWhereClause(new WhereClause());

        String sql = "SELECT * FROM sales LIMIT 10";

        test(expectedSelect, sql);
    }

    @Test
    public void sampleTest_2() throws SqlParsingException {
        SelectStatement expectedSelect = new SelectStatement();
        expectedSelect.setTableName("collection");
        expectedSelect.setSelectItems(List.of());
        expectedSelect.setUnaryOperations(List.of(
                new UnaryOperation("OFFSET", 5L),
                new UnaryOperation("LIMIT", 10L)
        ));
        expectedSelect.setWhereClause(new WhereClause());

        String sql = "SELECT * FROM collection OFFSET 5 LIMIT 10";

        test(expectedSelect, sql);
    }


    @Test
    public void sampleTest_3() throws SqlParsingException {
        WhereClause whereClause = new WhereClause();
        whereClause.addOperation(new ComparisonOperation(">", "age", "22"));
        whereClause.addOperation(new ComparisonOperation("=", "name", "'Vasya'"));

        SelectStatement expectedSelect = new SelectStatement();
        expectedSelect.setTableName("customers");
        expectedSelect.setSelectItems(List.of());
        expectedSelect.setUnaryOperations(List.of());
        expectedSelect.setWhereClause(whereClause);

        String sql = "SELECT * FROM customers WHERE age > 22 AND name = 'Vasya'";

        test(expectedSelect, sql);
    }

    @Test
    public void sampleTest_4() throws SqlParsingException {
        SelectStatement expectedSelect = new SelectStatement();
        expectedSelect.setTableName("collection");
        expectedSelect.setSelectItems(List.of("name", "surname"));
        expectedSelect.setUnaryOperations(List.of());
        expectedSelect.setWhereClause(new WhereClause());

        String sql = "SELECT name, surname FROM collection";

        test(expectedSelect, sql);
    }

    @Test
    public void test_queryIsNotSelect() {
        String sql = "INSERT INTO collection (id, name) VALUES (1, 'abc')";

        assertThrows(SqlParsingException.class, () -> sqlParser.parseSelectStatement(sql));
    }

    @Test
    public void test_incorrectQuery() {
        String sql = "SELEC * FROM user";

        assertThrows(SqlParsingException.class, () -> sqlParser.parseSelectStatement(sql));
    }

    @Test
    public void test_AllPredicates_AllUnaryOps() throws SqlParsingException {
        WhereClause whereClause = new WhereClause();
        whereClause.addOperation(new ComparisonOperation(">", "first", "22"));
        whereClause.addOperation(new ComparisonOperation("=", "second", "'second'"));
        whereClause.addOperation(new ComparisonOperation("<", "third", "123"));
        whereClause.addOperation(new ComparisonOperation("<>", "fourth", "4444"));

        SelectStatement expectedSelect = new SelectStatement();
        expectedSelect.setTableName("collection");
        expectedSelect.setSelectItems(List.of("first", "second", "third"));
        expectedSelect.setUnaryOperations(List.of(
                new UnaryOperation("LIMIT", 10L),
                new UnaryOperation("OFFSET", 5L)
        ));
        expectedSelect.setWhereClause(whereClause);

        String sql = "SELECT first, second, third FROM collection WHERE first > 22 AND " +
                "second = 'second' AND third < 123 AND fourth <> 4444 LIMIT 10 OFFSET 5";


        test(expectedSelect, sql);
    }

    public void test_ignoreCase() throws SqlParsingException {
        SelectStatement expectedSelect = new SelectStatement();
        expectedSelect.setTableName("collection");
        expectedSelect.setWhereClause(new WhereClause());
        expectedSelect.setUnaryOperations(List.of());
        expectedSelect.setSelectItems(List.of());

        String sql = "sElEcT * FrOM collection";

        test(expectedSelect, sql);
    }

    public void test(SelectStatement expectedSelect, String sql) throws SqlParsingException {
        SelectStatement actualSelect = sqlParser.parseSelectStatement(sql);

        assertEquals(expectedSelect, actualSelect);
    }
}
