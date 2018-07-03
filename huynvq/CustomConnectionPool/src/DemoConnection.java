import com.huy.customconnectionpool.ConnectionPool;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DemoConnection {
    ConnectionPool pool;
    List<Connection> connectionList;
    boolean isRunningChecking;

    public static void main(String[] args) {
        DemoConnection demoConnection = new DemoConnection();
        Connection conn1 = demoConnection.creatNewConnection();
        Connection conn2 = demoConnection.creatNewConnection();
        System.out.println(demoConnection.pool.numberOfAvailableConnection());
        Connection conn3 = demoConnection.creatNewConnection();
        Connection conn4 = demoConnection.creatNewConnection();
        Connection conn5 = demoConnection.creatNewConnection();
        Connection conn6 = demoConnection.creatNewConnection();
        System.out.println(demoConnection.pool.numberOfAvailableConnection());
        demoConnection.realeaseConnection(conn1);
        demoConnection.realeaseConnection(conn2);
        System.out.println(demoConnection.pool.numberOfAvailableConnection());

        Thread connectionThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (demoConnection.isRunningChecking == true) {
                    for (Iterator i = demoConnection.getConnectionList().iterator(); i.hasNext(); ) {
                        Connection connection = (Connection) i.next();
                        if (connection != null && System.nanoTime() - demoConnection.getPool().getConenctionStartTime(connection)
                                < ConnectionPool.CONNECTION_TIME_OUT) {
                            demoConnection.getConnectionList().remove(connection);
                        }
                        else {
                            try {
                                Connection connectionAvailableInPool = demoConnection.getPool().getConnection();
                                if (connectionAvailableInPool != null) {
                                    demoConnection.getConnectionList().remove(connection);
                                    demoConnection.getConnectionList().add(connectionAvailableInPool);
                                }
                            } catch (SQLException e) {
                                e.printStackTrace();
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    System.out.println(demoConnection.getPool().numberOfAvailableConnection());
                }
            }
        });
        connectionThread.start();
    }

    public DemoConnection() {
        isRunningChecking = true;
        connectionList = new ArrayList<>();
        try {
            pool = new ConnectionPool("jdbc:mysql://localhost:3308/student");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Connection creatNewConnection() {
        Connection conn = null;
        try {
            conn = pool.getConnection();
            connectionList.add(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return conn;
    }

    public void realeaseConnection(Connection conn) {
        pool.releaseConnection(conn);
        connectionList.remove(conn);
    }

    public ConnectionPool getPool() {
        return pool;
    }

    public void setPool(ConnectionPool pool) {
        this.pool = pool;
    }

    public List<Connection> getConnectionList() {
        return connectionList;
    }

    public void setConnectionList(List<Connection> connectionList) {
        this.connectionList = connectionList;
    }
}
