import exception.SqlParsingException;
import org.junit.Test;
import static org.junit.Assert.*;

public class Test_Sql2MongoTranslator {
    private static final Sql2MongoTranslator translator = new Sql2MongoTranslator();

    private static final String EOLN = System.lineSeparator();
    private static final String TAB = "\t";

    @Test
    public void sampleTest_1() throws SqlParsingException {
        String sql = "SELECT * FROM sales LIMIT 10";

        String expectedMongo = "db.sales.find({}).limit(10)";


        test(expectedMongo, sql);
    }

    @Test
    public void sampleTest_2() throws SqlParsingException {
        String sql = "SELECT name, surname FROM collection";

        String expectedMongo = "db.collection.find({}, {name: 1, surname: 1})";

        test(expectedMongo, sql);
    }

    @Test
    public void sampleTest_3() throws SqlParsingException {
        String sql = "SELECT * FROM collection OFFSET 5 LIMIT 10";

        String expectedMongo = "db.collection.find({}).skip(5).limit(10)";

        test(expectedMongo, sql);
    }

    @Test
    public void sampleTest_4() throws SqlParsingException {
        String sql = "SELECT * FROM customers WHERE age > 22 AND name = 'Vasya'";

        String expectedMongo = "db.customers.find({age: {$gt: 22}, name: 'Vasya'})";

        test(expectedMongo, sql);
    }

    public void test(String expectedMongo, String sql) throws SqlParsingException {
        String actualMongo = translator.select2Find(sql);

        System.out.println();
        System.out.println(actualMongo);
        assertEquals(expectedMongo, actualMongo);
    }
}
