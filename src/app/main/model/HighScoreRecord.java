package app.main.model;

import app.utility.database.QueryBuilder;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class HighScoreRecord {
    public static final String TABLE_NAME = "HighScoreRecord";
    private int id;
    private String name;
    private long timeSpent;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(long timeSpent) {
        this.timeSpent = timeSpent;
    }

    public HighScoreRecord() {
        this.id = -1;
        this.name = "";
        this.timeSpent = -1;
    }

    public HighScoreRecord(String name, long timeSpent) {
        id = -1;
        this.name = name;
        this.timeSpent = timeSpent;
    }
    public HighScoreRecord(int id, String name, long timeSpent) {
        this.id = id;
        this.name = name;
        this.timeSpent = timeSpent;
    }

    public int generateID() {
        Optional<ResultSet> res = QueryBuilder.select()
                .select("ic_count","IFNULL(max(id) + 1, 1)")
                .from(TABLE_NAME).executeQuery();
        if(res.isPresent()) {
            try {
                ResultSet resultSet = res.get();
                if(resultSet.next()) {
                    int output = resultSet.getInt("ic_count");
                    return output;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return -1;
    }

    public void save() {
        if(id == -1) {
            QueryBuilder.insert(TABLE_NAME)
                    .value(new Object[] {this.id = generateID(), name, timeSpent})
                    .executeInsert();
        }
        else {
            QueryBuilder.update(TABLE_NAME)
                    .set("name", name)
                    .set("time_spent", timeSpent)
                    .where("id", "=", id)
                    .executeUpdate();
        }
    }

    public String getTimeSpentFormat() {
        long min = timeSpent / 60000;
        long sec = (timeSpent / 1000) % 60;
        long ms = timeSpent % 1000;
        return String.format("%02d:%02d.%03d", min, sec, ms);
    }
}
