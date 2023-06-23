package app.utility.database;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class UpdateQuery {
    private DBConnect database;
    private String updateEntry = "";
    private String setEntry = "";
    private String whereEntry = "";
    private ArrayList<Object> parameters = new ArrayList<>();
    private HashMap<String, String> updateChanges = new HashMap<>();
    public UpdateQuery(String table) {
        this.updateEntry = "UPDATE " + table;
        database = DBConnect.getInstance();
    }
    public UpdateQuery set(String key, Object changes) {
        setEntry += String.format("%s `%s` = ?",
                setEntry.isEmpty() ? "SET" : ",",
                key);
        parameters.add(changes);
        return this;
    }
    public UpdateQuery where(String rawCondition) {
        if(whereEntry.isEmpty()) {
            whereEntry = "WHERE ";
        } else {
            whereEntry += " AND ";
        }
        whereEntry += rawCondition;
        return this;
    }

    public UpdateQuery where(String column, String operator, Object value) {
        return this.addWhereCondition("WHERE", column, operator, value);
    }

    public UpdateQuery whereAnd(String rawCondition) {
        if(whereEntry.isEmpty()) {
            where(rawCondition);
            return this;
        }
        whereEntry += " AND " + rawCondition;
        return this;
    }

    public UpdateQuery whereAnd(String column, String operator, Object value) {
        if(whereEntry.isEmpty()) {
            where(column, operator, value);
            return this;
        }
        return this.addWhereCondition("AND", column, operator, value);
    }

    public UpdateQuery whereOr(String rawCondition) {
        if(whereEntry.isEmpty()) {
            where(rawCondition);
            return this;
        }
        whereEntry += " OR " + rawCondition;
        return this;
    }

    public UpdateQuery whereOr(String column, String operator, Object value) {
        if(whereEntry.isEmpty()) {
            where(column, operator, value);
            return this;
        }
        return this.addWhereCondition("OR", column, operator, value);
    }
    private UpdateQuery addWhereCondition(String prefix, String column, String operator, Object value) {
        if(!whereEntry.isEmpty() && !whereEntry.endsWith(" ")) {
            whereEntry += " ";
        }
        whereEntry += prefix + " " + column + " " + operator + " ?";
        parameters.add(value);
        return this;
    }
    public String generateQuery() {
        return String.format("%s %s %s;", updateEntry, setEntry, whereEntry);
    }

    public void executeUpdate() {
        String query = generateQuery();
        try {
            PreparedStatement ps = database.prepareStatement(query);
            for (int i = 0; i < parameters.size(); i++) {
                ps.setObject(i + 1, parameters.get(i));
            }
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to execute update", e);
        }
    }
}
