import exception.SqlParsingException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import com.mongodb.BasicDBObject;


public class Cli {
    public static void main(String[] args) throws IOException {
        Sql2MongoTranslator translator = new Sql2MongoTranslator();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Enter SELECT query if you want to translate it to MongoDB query.");
        System.out.println("Enter 'q' if you want to exit.");
        String line;
        while (!"q".equals(line = reader.readLine()) && line != null) {
            try {
                String mongo = translator.select2Find(line);

                System.out.println(translator.select2Find(line));

            } catch (SqlParsingException e) {
                System.err.println(e.getMessage());
            }
        }
    }
}
