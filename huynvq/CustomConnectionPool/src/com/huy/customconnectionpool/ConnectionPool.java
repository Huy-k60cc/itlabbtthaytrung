package com.huy.customconnectionpool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;
//client goi vao sau 1 thoi gian ko co connection thi timeout
//min size
//max size

public class ConnectionPool {
    public static final String DRIVER = "com.mysql.jdbc.Driver";
    public static final String DB_USER_NAME = "root";
    public static final String DB_PASS_WORD = "";
    public static final Integer POOL_MIN_CONNECTIONS = 5;
    public static final Integer POOL_MAX_CONNECTIONS = 10;
    public static final Long CONNECTION_TIME_OUT = Long.valueOf(10 * 1000000000);
    private Integer numberOfConnections;
    private List<Connection> availableConnections;
    private HashMap<Connection, Long> usedConnections; //luu connection duoc dung va thoi gian bat dau connection
    private String dbURL;

    public ConnectionPool(String dbURL) throws SQLException, ClassNotFoundException {
        this.dbURL = dbURL;
        availableConnections = new ArrayList<Connection>();
        usedConnections = new HashMap<Connection, Long>();
        for (int i = 0; i < POOL_MIN_CONNECTIONS; i++) {
            availableConnections.add(createConnection(dbURL));
        }
        numberOfConnections = 5;
    }

    private Connection createConnection(String dbURL) throws SQLException, ClassNotFoundException {
        Class.forName(DRIVER);
        return DriverManager.getConnection(dbURL, DB_USER_NAME, DB_PASS_WORD);
    }

    public synchronized Connection getConnection() throws SQLException, ClassNotFoundException {
        if (availableConnections.size() == 0) {
            if (numberOfConnections < POOL_MAX_CONNECTIONS) {
                availableConnections.add(createConnection(dbURL));
                numberOfConnections++;
            } else {
                System.out.println("no connection left");
                return null;
            }
        } else {
            Connection conn = availableConnections.remove(availableConnections.size() - 1);
            usedConnections.put(conn, System.nanoTime());
            return conn;
        }
        return null;
    }

    public synchronized boolean releaseConnection(Connection conn) {
        if (null != conn) {
            usedConnections.remove(conn);
            availableConnections.add(conn);
            return true;
        }
        return false;
    }

    public Long getConenctionStartTime(Connection conn) {
        return usedConnections.get(conn);
    }

    public int numberOfAvailableConnection() {
        return availableConnections.size();
    }
}