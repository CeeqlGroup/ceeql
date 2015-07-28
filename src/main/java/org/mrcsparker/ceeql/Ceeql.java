package org.mrcsparker.ceeql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.skife.jdbi.v2.*;

import javax.sql.DataSource;
import java.util.Map;

public class Ceeql implements AutoCloseable {

    private final static Logger log = LogManager.getLogger(Ceeql.class);

    private final String driverName;
    private final String url;
    private final String username;
    private final String password;

    private boolean isConnected;
    private Handle dbiHandle;

    public Ceeql(String driverName, String url, String username, String password) {
        this.driverName = driverName;
        this.url = url;
        this.username = username;
        this.password = password;
        this.isConnected = false;

        connectToDatabase();
    }

    public boolean isConnected() {
        return isConnected;
    }

    public Boolean query(String query) {
        try {
            dbiHandle.execute(query);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Handle getDbiHandle() {
        return dbiHandle;
    }

    public String selectOne(String sql, Map<String, String> args) {
        return new CeeqlSelect(dbiHandle, sql, args).first();
    }

    public String select(String sql, Map<String, String> args) {
        return new CeeqlSelect(dbiHandle, sql, args).all();
    }

    public String insert(String sql, Map<String, String> args) {
        return new CeeqlInsert(dbiHandle, sql, args).exec();
    }

    public String update(String sql, Map<String, String> args) {
        return new CeeqlUpdate(dbiHandle, sql, args).exec();
    }

    public String delete(String sql, Map<String, String> args) {
        return new CeeqlDelete(dbiHandle, sql, args).exec();
    }

    @Override
    public void close() {
        if (dbiHandle != null) {
            dbiHandle.close();
        }
        this.isConnected = false;
    }

    private String connectToDatabase() {
        try {
            log.debug("Connecting with driver " + driverName + " to " + url);

            Class clazz = Class.forName(driverName);
            log.debug(clazz);

            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(url);
            config.setUsername(username);
            config.setPassword(password);
            config.addDataSourceProperty("dataSourceClassName", driverName);

            DataSource ds = new HikariDataSource(config);

            DBI dbi = new DBI(ds);
            dbiHandle = dbi.open();

            this.isConnected = true;
            return CeeqlMessage.message("Connected");
        } catch (Exception e) {
            this.isConnected = false;
            return CeeqlError.errorType(e.getClass().getSimpleName(), e.getMessage());
        }
    }

    public String reconnect() {
        return connectToDatabase();
    }
}
