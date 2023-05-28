package app.utility;

import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class Model {
  private String tableName;
  private int id;

  public String getTableName() {
    return tableName;
  }

  public int getId() {
    return id;
  }

  public boolean idNotSet() {
    return id == -1;
  }

  protected void setId(int id) {
    this.id = id;
  }

  protected Model(String tableName, int id) {
    this.tableName = tableName;
    this.id = id;
  }

  protected Model(String tableName) {
    this.tableName = tableName;
    this.id = -1;
  }

  public void save() {
    if (exists())
      return;
    if (idNotSet())
      id = nextID(tableName);
    insertDB();
  }

  protected int nextID(String tableName) {
    DBConnect conn = DBConnect.getInstance();
    ResultSet res = conn.executeQuery("SELECT MAX(id) AS `output` FROM " + tableName);
    try {
      res.next();
      return res.getInt(1) + 1;
    } catch (SQLException e) {
      e.printStackTrace();
      return -1;
    }
  }

  public void deleteDB() {
    DBConnect c = DBConnect.getInstance();
    String sql = String.format("DELETE FROM %s WHERE id = '%s';", tableName, getId());
    c.executeUpdate(sql);
  }

  protected abstract void insertDB();

  public abstract boolean exists();
}
