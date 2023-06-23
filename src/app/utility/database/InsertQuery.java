package app.utility.database;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class InsertQuery {
    private DBConnect database;
    private String insertCommand = "";
    private String keyEntry = "";
    private String valueEntry = "";
    private ArrayList<Object[]> objectList = new ArrayList<>();

    InsertQuery(String tableName) {
        this.insertCommand = String.format("INSERT INTO `%s`", tableName);
        database = DBConnect.getInstance();
    }
    public InsertQuery keys(String[] keys) {
        String parsed = "";
        int len = keys.length;
        for(int i = 0; i < len; i++) {
            parsed += "`" + keys[i] + "`";
            if(i < len - 1) {
                parsed += ", ";
            }
        }
        keyEntry = String.format("(%s)", parsed);
        return this;
    }

    public InsertQuery value(Object[] objects) {
        ArrayList<Object[]> arr = new ArrayList<>();
        arr.add(objects);
        return values(arr);
    }
    public InsertQuery values(ArrayList<Object[]> values) {
        String parsed = "VALUES\r\n";
        int len = values.size();
        for(int i = 0; i < len; i++) {
            Object[] entry = values.get(i);
            objectList.add(entry);
            parsed += parseValue(entry);
            if(i < len - 1) {
                parsed += ", \r\n";
            }
        }
        valueEntry = parsed;
        return this;
    }
    private String parseValue(Object[] entry) {
        String parsed = "";
        int len = entry.length;
        for(int i = 0; i < len; i++) {
            Object obj = entry[i];
            parsed += "?";
            if(i < len - 1) {
                parsed += ", ";
            }
        }
        return String.format("(%s)", parsed);
    }
    public void executeInsert() {
        String sql = String.format("%s %s %s;", insertCommand, keyEntry, valueEntry);
        try {
            PreparedStatement ps = database.prepareStatement(sql);
            for(int i = 0, count = 1; i < objectList.size(); i++) {
                Object[] entry = objectList.get(i);
                for(int j = 0; j < entry.length; j++, count++) {
                    ps.setObject(count, entry[j]);
                }
            }
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}