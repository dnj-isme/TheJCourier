package app.utility.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

public class SelectQuery {
    private DBConnect database;
    private String columnEntry = "";
    private String tableEntry = "";
    private String whereEntry = "";
    private String groupByEntry = "";
    private String havingEntry = "";
    private String orderByEntry = "";
    private String limitEntry = "";
    private ArrayList<Object> parameters = new ArrayList<>();

    SelectQuery() {
        database = DBConnect.getInstance();
    }
    public SelectQuery selectAll() {
        if(!columnEntry.isEmpty()) {
            columnEntry += ",\r\n";
        }
        columnEntry += "*";
        return this;
    }
    public SelectQuery select(String columnName) {
        if(!columnEntry.isEmpty()) {
            columnEntry += ",\r\n";
        }
        columnEntry += "`" + columnName + "`";
        return this;
    }
    public SelectQuery select(String key, String sql) {
        if(!columnEntry.isEmpty()) {
            columnEntry += ",\r\n";
        }
        columnEntry += String.format("%s AS `%s`", sql, key);
        return this;
    }
    public SelectQuery from(String table) {
        if(!tableEntry.isEmpty()) {
            System.out.println("Warning! Accessing from() after join() will reset the table reference");
        }
        tableEntry = table;
        return this;
    }
    public SelectQuery join(String originTable, String originKey, String joinTable, String joinKey) {
        if(tableEntry.isEmpty()) {
            System.out.println("Warning! Attempting to call join() before from(). The input is denied.");
            return this;
        }
        tableEntry += String.format("\r\nJOIN %s ON %s.%s = %s.%s", joinTable, originTable, originKey, joinTable, joinKey);
        return this;
    }
    public SelectQuery where(String rawCondition) {
        if(whereEntry.isEmpty()) {
            whereEntry = "WHERE ";
        } else {
            whereEntry += " AND ";
        }
        whereEntry += rawCondition;
        return this;
    }

    public SelectQuery where(String column, String operator, Object value) {
        return this.addWhereCondition("WHERE", column, operator, value);
    }

    public SelectQuery whereAnd(String rawCondition) {
        if(whereEntry.isEmpty()) {
            where(rawCondition);
            return this;
        }
        whereEntry += " AND " + rawCondition;
        return this;
    }

    public SelectQuery whereAnd(String column, String operator, Object value) {
        if(whereEntry.isEmpty()) {
            where(column, operator, value);
            return this;
        }
        return this.addWhereCondition("AND", column, operator, value);
    }

    public SelectQuery whereOr(String rawCondition) {
        if(whereEntry.isEmpty()) {
            where(rawCondition);
            return this;
        }
        whereEntry += " OR " + rawCondition;
        return this;
    }

    public SelectQuery whereOr(String column, String operator, Object value) {
        if(whereEntry.isEmpty()) {
            where(column, operator, value);
            return this;
        }
        return this.addWhereCondition("OR", column, operator, value);
    }

    private SelectQuery addWhereCondition(String prefix, String column, String operator, Object value) {
        if(!whereEntry.isEmpty() && !whereEntry.endsWith(" ")) {
            whereEntry += " ";
        }
        whereEntry += prefix + " " + column + " " + operator + " ?";
        parameters.add(value);
        return this;
    }

    public SelectQuery groupBy(String... columns) {
        if (groupByEntry.isEmpty()) {
            groupByEntry = "GROUP BY ";
        } else {
            groupByEntry += ", ";
        }
        groupByEntry += String.join(", ", columns);
        return this;
    }

    public SelectQuery having(String rawCondition) {
        if(havingEntry.isEmpty()) {
            havingEntry = "HAVING ";
        } else {
            havingEntry += " AND ";
        }
        havingEntry += rawCondition;
        return this;
    }

    public SelectQuery having(String column, String operator, Object value) {
        return this.addHavingCondition("HAVING", column, operator, value);
    }

    public SelectQuery havingAnd(String rawCondition) {
        if(havingEntry.isEmpty()) {
            having(rawCondition);
            return this;
        }
        havingEntry += " AND " + rawCondition;
        return this;
    }

    public SelectQuery havingAnd(String column, String operator, Object value) {
        if(havingEntry.isEmpty()) {
            having(column, operator, value);
            return this;
        }
        return this.addHavingCondition("AND", column, operator, value);
    }

    public SelectQuery havingOr(String rawCondition) {
        if(havingEntry.isEmpty()) {
            having(rawCondition);
            return this;
        }
        havingEntry += " OR " + rawCondition;
        return this;
    }

    public SelectQuery havingOr(String column, String operator, Object value) {
        if(havingEntry.isEmpty()) {
            having(column, operator, value);
            return this;
        }
        return this.addHavingCondition("OR", column, operator, value);
    }

    private SelectQuery addHavingCondition(String prefix, String column, String operator, Object value) {
        if(!havingEntry.isEmpty() && !havingEntry.endsWith(" ")) {
            havingEntry += " ";
        }
        havingEntry += prefix + " " + column + " " + operator + " ?";
        parameters.add(value);
        return this;
    }

    public SelectQuery orderBy(String column, String order) {
        if (orderByEntry.isEmpty()) {
            orderByEntry = "ORDER BY ";
        } else {
            orderByEntry += ", ";
        }
        orderByEntry += column + " " + order;
        return this;
    }

    public SelectQuery limit(int limit) {
        limitEntry = "LIMIT " + limit;
        return this;
    }

    public String buildQuery() {
        String query = "SELECT " + columnEntry
                + "\nFROM " + tableEntry
                + (whereEntry.isEmpty() ? "" : "\n" + whereEntry)
                + (groupByEntry.isEmpty() ? "" : "\n" + groupByEntry)
                + (havingEntry.isEmpty() ? "" : "\n" + havingEntry)
                + (orderByEntry.isEmpty() ? "" : "\n" + orderByEntry)
                + (limitEntry.isEmpty() ? "" : "\n" + limitEntry);
        return query;
    }

    public Optional<ResultSet>  executeQuery() {
        String sql = this.buildQuery();
        try {
            PreparedStatement ps = database.prepareStatement(sql);
            for (int i = 0; i < parameters.size(); i++) {
                ps.setObject(i+1, parameters.get(i));
            }
            return Optional.of(ps.executeQuery());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
