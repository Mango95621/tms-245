package database;

import com.alibaba.druid.filter.logging.Log4j2Filter;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import configs.Config;
import configs.ServerConfig;

import java.sql.SQLException;
import java.util.Collections;

public class DatabaseLoader
{
    private static final DruidDataSource druidDataSource = new DruidDataSource();
    private static final java.util.Scanner scanner = new java.util.Scanner(System.in);

    static {
        checkConnection();
        String dbUrl = "jdbc:mysql://" + ServerConfig.DB_IP + ":" + ServerConfig.DB_PORT + "/" + ServerConfig.DB_NAME + "?autoReconnect=true&characterEncoding=UTF8&zeroDateTimeBehavior=convertToNull&useSSL=false";
        druidDataSource.setDriverClassName("com.mysql.jdbc.Driver");
        druidDataSource.setUrl(dbUrl);
        druidDataSource.setUsername(ServerConfig.DB_USER);
        druidDataSource.setPassword(ServerConfig.DB_PASSWORD);
        Log4j2Filter filter = new Log4j2Filter();
        filter.setConnectionLogEnabled(false);
        filter.setStatementLogEnabled(false);
        filter.setResultSetLogEnabled(false);
        filter.setStatementExecutableSqlLogEnable(true);
        druidDataSource.setProxyFilters(Collections.singletonList(filter));
        druidDataSource.setInitialSize(ServerConfig.DB_INITIALPOOLSIZE);
        druidDataSource.setMinIdle(ServerConfig.DB_MINPOOLSIZE);
        druidDataSource.setMaxActive(ServerConfig.DB_MAXPOOLSIZE);
        druidDataSource.setMaxWait(60000);
        druidDataSource.setTimeBetweenEvictionRunsMillis(60000);
        druidDataSource.setMinEvictableIdleTimeMillis(300000);
        druidDataSource.setRemoveAbandoned(true);
        druidDataSource.setRemoveAbandonedTimeoutMillis(ServerConfig.DB_TIMEOUT);
        druidDataSource.setValidationQuery("SELECT 1 FROM dual");
        druidDataSource.setTestWhileIdle(true);
        druidDataSource.setTestOnBorrow(false);
        druidDataSource.setTestOnReturn(false);
        druidDataSource.setPoolPreparedStatements(false);
        druidDataSource.setMaxPoolPreparedStatementPerConnectionSize(20);
    }

    private static void checkConnection() {
        try {
            String url = "jdbc:mysql://" + ServerConfig.DB_IP + ":" + ServerConfig.DB_PORT + "/" + ServerConfig.DB_NAME + "?useSSL=false";


            java.sql.DriverManager.getConnection(url, ServerConfig.DB_USER, ServerConfig.DB_PASSWORD).close();
        } catch (SQLException e) {
            System.err.println("无法连接到数据库，请先设定数据库信息");

            System.err.print("数据库主机:");
            ServerConfig.DB_IP = scanner.next();
            Config.setProperty("db.ip", ServerConfig.DB_IP);

            System.err.print("数据库端口:");
            ServerConfig.DB_PORT = scanner.next();
            Config.setProperty("db.port", ServerConfig.DB_PORT);

            System.err.print("数据库名称:");
            ServerConfig.DB_NAME = scanner.next();
            Config.setProperty("db.name", ServerConfig.DB_NAME);

            System.err.print("用户名:");
            ServerConfig.DB_USER = scanner.next();
            Config.setProperty("db.user", ServerConfig.DB_USER);

            System.err.print("密码:");
            String passwd = scanner.next();
            if (passwd.equals("_EMPTY_")) passwd = "";
            ServerConfig.DB_PASSWORD = passwd;
            Config.setProperty("db.password", ServerConfig.DB_PASSWORD);

            checkConnection();
        }
    }

    public static DruidPooledConnection getConnection() {
        try {
            return druidDataSource.getConnection();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    public static void closeAll() {
        druidDataSource.close();
    }

    public static void restart() {
        if (druidDataSource.isClosed()) {
            try {
                druidDataSource.restart();
            } catch (SQLException e) {
                throw new DatabaseException(e);
            }
        }
    }
}
