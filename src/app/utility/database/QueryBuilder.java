package app.utility.database;

import java.util.ArrayList;

public class QueryBuilder {
    public static InsertQuery insert(String table) {
        return new InsertQuery(table);
    }
    public static SelectQuery select() {
        return new SelectQuery();
    }
    public static UpdateQuery update(String tableName) {
        return new UpdateQuery(tableName);
    }
}
