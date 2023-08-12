package app.main.controller;

import app.main.model.HighScoreRecord;
import app.utility.database.QueryBuilder;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

public class HighScoreController {

    private static HighScoreController instance;
    public static HighScoreController getInstance() {
        if(instance == null) {
            instance = new HighScoreController();
        }
        return instance;
    }

    private HighScoreController() {}

    public HighScoreRecord findHighScoreRecord(int id) {
        Optional<ResultSet> res = QueryBuilder.select()
                .selectAll()
                .from(HighScoreRecord.TABLE_NAME)
                .where("id", "=", id)
                .executeQuery();
        if(res.isPresent()) {
            try {
                ResultSet resultSet = res.get();
                if(resultSet.next()) {
                    int resID = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    long timeSpent = resultSet.getLong("time_spent");
                    return new HighScoreRecord(resID, name, timeSpent);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    public void saveHighScore(String name, long winTime) {
        HighScoreRecord record = new HighScoreRecord(name, winTime);
        record.save();
    }

    public ArrayList<HighScoreRecord> getRanking() {
        ArrayList<HighScoreRecord> output = new ArrayList<>();

        Optional<ResultSet> opt = QueryBuilder.select()
                .selectAll()
                .from(HighScoreRecord.TABLE_NAME)
                .orderBy("time_spent", "ASC")
                .executeQuery();

        if(opt.isPresent()) {
            try {
                ResultSet resultSet = opt.get();
                while(resultSet.next()) {
                    HighScoreRecord newRecord = new HighScoreRecord(
                            resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getLong("time_spent")
                    );
                    output.add(newRecord);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return output;
    }
}
