package org.itas.core;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.itas.core.GameBase.DataStatus;
import org.itas.core.util.AutoClose;
import org.itas.util.Logger;
import org.itas.util.collection.CircularQueue;

public abstract class AbstractDBSync implements DBSync, AutoClose {

  protected abstract Connection getConnection() throws SQLException;
	
  protected abstract void addInsert(GameBase gameObject);

  protected abstract void addUpdate(GameBase gameObject);
		
  protected abstract void addDelete(GameBase gameObject);

  @Override
  public GameObject loadData(GameObject gameObject, String Id) {
	Connection conn = null;
	PreparedStatement ppst = null;
	ResultSet result = null;
	try {
	  conn = getConnection();
	  ppst = conn.prepareStatement(gameObject.selectSQL());
	  ppst.setString(1, Id);
	  result = ppst.executeQuery();
	  
	  GameObject data = null;
	  if (result.next()) {
	    data = gameObject.clone(Id);
		data.doResultSet(result);
	  }

	  return data;
	} catch (Exception e) {
	  Logger.error("[clazz=" + gameObject.getClass().getName() + "],[key=" + Id + "]", e);
	  return null;
	} finally {
	  close(conn, ppst, result);
	}
  }
	
  @Override
  public List<GameObject> loadDataArray(GameObject gameObject, Collection<String> IdArray) {
	Connection conn = null;
	PreparedStatement ppst = null;
	ResultSet result = null;
	try {
	  conn = getConnection();
	  ppst = conn.prepareStatement(gameObject.selectSQLArray());

	  int index = 1;
	  for (String Id : IdArray) {
		ppst.setString(index, Id);
		index ++;
	  }
	  result = ppst.executeQuery();
	  List<GameObject> dataList = new ArrayList<>(IdArray.size());
		
	  GameObject data;
	  while (result.next()) {
	    data = gameObject.clone(result.getString("Id"));
	    data.doResultSet(result);
	    dataList.add(data);
	  }

	  return dataList;
	} catch (Exception e) {
	  Logger.error("[clazz=" + gameObject.getClass().getName() + 
	      "],[key=" + String.join(", ", IdArray) + "]", e);
	  return null;
	} finally {
	  close(conn, ppst, result);
	}
  }
	
  @Override
  public void insertData(CircularQueue<GameObject> gameObjects) {
    int size = gameObjects.size();
    if (size <= 0) {
      return;
    }

    Connection conn = null;
    PreparedStatement statement = null;
    try {
      conn = getConnection();
      statement = conn.prepareStatement(gameObjects.peek().insertSQL());
			
      GameObject data;
      for (; size > 0; size--) {
        synchronized (gameObjects) {
    	  data = gameObjects.pop();
        }

        if (data.getDataStatus() == DataStatus.news) {
          data.doStatement(statement, DataStatus.news);
        }
      }

      statement.executeBatch();
    } catch (Exception e) {
      Logger.error("", e);
    } finally {
      close(conn, statement);
	}
  }

  @Override
  public void modifyData(CircularQueue<GameObject> gameObjects) {
	int size = gameObjects.size();
	if (size <= 0) {
	  return;
	}
		
	Connection conn = null;
	PreparedStatement statement = null;
	try {
	  conn = getConnection();
	  statement = conn.prepareStatement(gameObjects.peek().updateSQL());

	  GameObject data;
	  for (; size > 0; size--) {
		synchronized (gameObjects) {
		  data = gameObjects.pop();
		}

		if (data.getDataStatus() == DataStatus.modify) {
     	  data.doStatement(statement, DataStatus.modify);
		}
	  }

	  statement.executeBatch();
	} catch (Exception e) {
      Logger.error("", e);
	} finally {
	  close(conn, statement);
	}
  }
	
  @Override
  public void deleteData(CircularQueue<GameObject> gameObjects) {
    int size = gameObjects.size();
    if (size <= 0) {
      return;
    }

    Connection conn = null;
    PreparedStatement statement = null;
    try {
      conn = getConnection();
      statement = conn.prepareStatement(gameObjects.peek().deleteSQL());

      GameObject data;
      for (; size > 0; size--) {
      synchronized (gameObjects) {
        data = gameObjects.pop();
      }

      if (data.getDataStatus() == DataStatus.destory) {
        data.doStatement(statement, DataStatus.destory);
      }
      }

      statement.executeBatch();
    } catch (Exception e) {
      Logger.error("", e);
    } finally {
      close(conn, statement);
    }
  }
	
  @Override
  public int[] createTable(List<GameObject> gameObjects) throws SQLException {
    Connection conn = null;
    Statement statement = null;
    try {
      conn = getConnection();
      statement = conn.createStatement();
      for (GameObject gameObject : gameObjects) {
        gameObject.doCreate(statement);
      }

      return statement.executeBatch();
    } finally {
      close(conn, statement);
    }
  }

  @Override
  public int[] alterTable(List<GameObject> gameObjects) throws SQLException {
	Connection conn = null;
	Statement statement = null;
	try {
	  conn = getConnection();
	  statement = conn.createStatement();
	  
	  Set<String> clumns;
	  for (GameObject gameObject : gameObjects) {
	    clumns = tableColumns(gameObject.tableName());
	    gameObject.doAlter(statement, clumns);
	  }
			
	  return statement.executeBatch();
	} finally {
	  close(conn, statement);
	}
  }

  private Set<String> tableColumns(String tableName) throws SQLException {
	Connection conn = null;
	PreparedStatement ppst = null;
	ResultSet rs = null;
	try {
	  conn = getConnection();
		DatabaseMetaData data = conn.getMetaData();
    ResultSet result = data.getTables(null, "%", "%", new String[]{"TABLE", "VIEW"});
		if (!result.next()) {
			throw new SQLException("unkown database meta info...");
		}
		
		String dbName = result.getString("TABLE_CAT");
		
	  String sql = "SELECT COLUMN_NAME FROM information_schema.COLUMNS WHERE TABLE_NAME = ? AND TABLE_SCHEMA = ?;";
	  ppst = conn.prepareStatement(sql);
	  ppst.setString(1, tableName);
	  ppst.setString(2, dbName);
	  rs = ppst.executeQuery();

	  Set<String> columns = new HashSet<String>();
	  while (rs.next()) {
		columns.add(rs.getString("column_name"));
	  }

	  return columns;
	} finally {
	  close(conn, ppst, rs);
	}
  }

  protected AbstractDBSync() {
  }

}