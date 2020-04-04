import exception.SqlParsingException;

public class Test {
    public static void main(String[] args) throws SqlParsingException {
        Sql2MongoTranslator translator = new Sql2MongoTranslator();
        System.out.println(translator.select2Find("SELECT * FROM sales LIMIT 10"));
//        System.out.println(translator.select2Find("SELECT name, surname FROM collection"));
//        System.out.println(translator.select2Find("SELECT * FROM customers WHERE age > 22"));
//        System.out.println(translator.select2Find("SELECT * FROM customers WHERE age > 22 AND name = 'Vasya'"));
//        System.out.println(translator.select2Find("SELECT * FROM collection LIMIT 5 OFFSET 10"));
    }
}
