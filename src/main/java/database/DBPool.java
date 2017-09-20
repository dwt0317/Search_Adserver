package database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.mariadb.jdbc.MariaDbDataSource;

/**
 * @author dwt
 * 数据库连接线程池管理类
 */
public class DBPool {
    private static volatile DBPool pool;
    private MariaDbDataSource ds;
    private Map<Connection, Boolean> map;
  
    private String url;
    private String username;
    private String password;
    private int initPoolSize;
    private int maxPoolSize;
    private int waitTime;
     
    private DBPool() {
        
    }
     
    public static DBPool getInstance() {
        if (pool == null) {
            synchronized (DBPool.class) {
                if(pool == null) {
                    pool = new DBPool();
                }
            }
        }
        return pool;
    }
     
    public void init(String url, String username, String password, int initPoolSize, int maxPoolSize, int waitTime) {
    	this.url = url;
    	this.username = username;
    	this.password = password;
    	this.initPoolSize = initPoolSize;
    	this.maxPoolSize = maxPoolSize;
    	this.waitTime = waitTime;
        try {
            ds = new MariaDbDataSource();
            ds.setUrl(url);
            ds.setUser(username);
            ds.setPassword(password);
            map = new HashMap<Connection, Boolean>();
            for (int i = 0; i < initPoolSize; i++) {
                map.put(getNewConnection(), true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Function for unit test
     */
    public Connection getTestConnection(){
    	Connection conn = null;	  	
    	try {
    		ds = new MariaDbDataSource(); 
    		ds.setUrl("jdbc:mysql://127.0.0.1:3305/revivedb");
    		ds.setUser("reviveuser");
    		ds.setPassword("henqiguai");
    		conn = ds.getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return conn;
    }
    
    public Connection getNewConnection() {
        try {
            return ds.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
     
    public synchronized Connection getConnection() {
        Connection conn = null;
        try {
            for (Entry<Connection, Boolean> entry : map.entrySet()) {
                if (entry.getValue()) {
                    conn = entry.getKey();
                    map.put(conn, false);
                    break;
                }
            }
            if (conn == null) {
                if (map.size() < maxPoolSize) {
                    conn = getNewConnection();
                    map.put(conn, false);
                } else {
                    wait(waitTime);
                    conn = getConnection();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }
     
    public void releaseConnection(Connection conn) {
        if (conn == null) {
            return;
        }
        try {
            if(map.containsKey(conn)) {
                if (conn.isClosed()) {
                    map.remove(conn);
                } else {
                    if(!conn.getAutoCommit()) {
                        conn.setAutoCommit(true);
                    }
                    map.put(conn, true);
                }
            } else {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}