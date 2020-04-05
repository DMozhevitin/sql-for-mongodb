import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.*;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.DBCollectionFindOptions;
import com.mongodb.client.model.Projections;
import exception.SqlParsingException;
import org.apache.commons.lang3.StringUtils;
import org.bson.BsonDocument;
import org.bson.Document;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.mongodb.util.JSON;

public class DbEmulator {
    private static final String DATA_PATH = "resources" + File.separator + "data.json";

    public static void main(String[] args) {
        Sql2MongoTranslator translator = new Sql2MongoTranslator();
        try {
            DBCollection collection = initDb();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
                String line;
                System.out.println("Enter SELECT query if you want to translate it to MongoDB query.");
                System.out.println("Enter 'q' if you want to exit.");
                while (!"q".equals(line = reader.readLine()) && line != null) {
                    try {
                        String mongo = translator.select2Find(line);
                        Cursor cursor = parseAndGetCursor(mongo, collection);
                        while (cursor.hasNext()) {
                            System.out.println(cursor.next());
                        }

                    } catch (SqlParsingException e) {
                        System.err.println(e.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Unable to read mock data from file.");
        }
    }

    private static Cursor parseAndGetCursor(String mongo, DBCollection collection) {
        String jsonPart = StringUtils.chop(mongo.substring(mongo.indexOf("(") + 1));
        String skipAndLimitPart;

        int skipAndLimitPartBegin = mongo.indexOf(")");
        if (skipAndLimitPartBegin == mongo.length() - 1) {
            skipAndLimitPart = null;
        } else {
            skipAndLimitPart = mongo.substring(skipAndLimitPartBegin + ").".length());
        }

        String[] splitted = jsonPart.split(" , ");

        DBCursor res;
        if (splitted.length == 2) {
            BasicDBObject findPredicate = BasicDBObject.parse(splitted[0]);
            BasicDBObject projection = BasicDBObject.parse(splitted[1]);
            DBCollectionFindOptions options = new DBCollectionFindOptions();
            options.projection(projection);
            res = collection.find(findPredicate, new DBCollectionFindOptions().projection(projection));
        } else {
            BasicDBObject findPredicate = BasicDBObject.parse(splitted[0]);
            res = collection.find(findPredicate);

        }

        if (skipAndLimitPart == null) {
            return res;
        } else {
            if (skipAndLimitPart.contains(".")) {
                String[] skipAndLimit = skipAndLimitPart.split(".");
                setSkipOrLimit(skipAndLimit[0], res);
                setSkipOrLimit(skipAndLimit[1], res);
            } else {
                setSkipOrLimit(skipAndLimitPart, res);
            }

            return res;
        }
    }

    private static void setSkipOrLimit(String s, DBCursor cursor) {
        int count = Integer.parseInt(StringUtils.chop(s.substring(s.indexOf("(") + 1)));
        if (s.startsWith("limit")) {
           cursor.limit(count);
        } else {
            cursor.skip(count);
        }
    }

    private static DBCollection initDb() throws IOException {
        MongoClient mongoClient = new MongoClient("localhost", 27017);
        DB db = mongoClient.getDB("sql-for-mongodb-db");
        DBCollection collection = db.getCollection("testCollection");
        Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
        mongoLogger.setLevel(Level.SEVERE);

        DBCursor all = collection.find();

        if (all.hasNext()) {
            return collection;
        } else {
            try (BufferedReader reader = Files.newBufferedReader(
                    Path.of(DATA_PATH))) {
                String data = reader.lines().collect(Collectors.joining());
                BasicDBObject mockData = BasicDBObject.parse(data);
                BasicDBList mockDataList = (BasicDBList) mockData.get("mockData");
                mockDataList.forEach(obj -> {
                    BasicDBObject listItem = (BasicDBObject) obj;
                    collection.insert(listItem);
                });

                return collection;
            }
        }
    }
}
