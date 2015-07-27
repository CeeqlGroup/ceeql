package org.mrcsparker.ceeql;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.skife.jdbi.v2.*;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    public List selectToList(String sql, Map<String, String> args) {
        Query q = dbiHandle.createQuery(applySqlTemplate(sql, args));

        for (Map.Entry<String, String> arg : args.entrySet()) {
            q.bind(arg.getKey(), arg.getValue());
        }

        return q.list();
    }

    public String select(String sql, Map<String, String> args) {
        Query q = dbiHandle.createQuery(applySqlTemplate(sql, args));

        for (Map.Entry<String, String> arg : args.entrySet()) {
            q.bind(arg.getKey(), arg.getValue());
        }

        try {
            return generateJson(q.list());
        } catch (Exception e) {
            return CeeqlError.errorType(e.getClass().getSimpleName(), e.getMessage());
        }
    }

    public Object selectOneAsObject(String sql, Map<String, String> args) {
        Query q = createQuery(applySqlTemplate(sql, args), args);
        return q.first();
    }

    public String selectOne(String sql, Map<String, String> args) {
        Query q = createQuery(applySqlTemplate(sql, args), args);

        try {
            return generateJson(q.first());
        } catch (Exception e) {
            return CeeqlError.errorType(e.getClass().getSimpleName(), e.getMessage());
        }
    }

    public String insert(String sql, Map<String, String> args) {
        GeneratedKeys q = createStatement(applySqlTemplate(sql, args), args);
        try {
            return generateJson(q.first());
        } catch (Exception e) {
            return CeeqlError.errorType(e.getClass().getSimpleName(), e.getMessage());
        }
    }

    public String insertBatch(String sql, ArrayList<Map<String, String>> argList) {

        log.info("Batch Insert: " + sql);

        for (Map<String, String> args : argList) {
            sql = applySqlTemplate(sql, args);
        }

        PreparedBatch q = dbiHandle.prepareBatch(sql);

        for (Map<String, String> args : argList) {
            PreparedBatchPart part = q.add();

            for (Map.Entry<String, String> arg : args.entrySet()) {
                part.bind(arg.getKey(), arg.getValue());
            }
        }

        try {
            return generateJson(q.execute());
        } catch (Exception e) {
            return CeeqlError.errorType(e.getClass().getSimpleName(), e.getMessage());
        }
    }

    public String update(String sql, Map<String, String> args) {
        GeneratedKeys q = createStatement(applySqlTemplate(sql, args), args);
        try {
            return generateJson(q.first());
        } catch (Exception e) {
            return CeeqlError.errorType(e.getClass().getSimpleName(), e.getMessage());
        }
    }

    public String delete(String sql, Map<String, String> args) {
        GeneratedKeys q = createStatement(applySqlTemplate(sql, args), args);
        try {
            return generateJson(q.list());
        } catch (Exception e) {
            return CeeqlError.errorType(e.getClass().getSimpleName(), e.getMessage());
        }
    }

    private <T> String generateJson(T o) {
        final ObjectMapper mapper = new ObjectMapper();

        try {
            return mapper.writeValueAsString(o);
        } catch (Exception e) {
            return CeeqlError.errorType(e.getClass().getSimpleName(), e.getMessage());
        }
    }

    @Override
    public void close() {
        if (dbiHandle != null) {
            dbiHandle.close();
        }
        this.isConnected = false;
    }

    private Query createQuery(String sql, Map<String, String> args) {
        Query q = dbiHandle.createQuery(sql);

        for (Map.Entry<String, String> arg : args.entrySet()) {
            q.bind(arg.getKey(), arg.getValue());
        }

        return q;
    }

    private GeneratedKeys createStatement(String sql, Map<String, String> args) {
        Update q = dbiHandle.createStatement(sql);

        for (Map.Entry<String, String> arg : args.entrySet()) {
            q.bind(arg.getKey(), arg.getValue());
        }

        return q.executeAndReturnGeneratedKeys();
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
            return CeeqlMessage.message();
        } catch (Exception e) {
            this.isConnected = false;
            return CeeqlError.errorType(e.getClass().getSimpleName(), e.getMessage());
        }
    }

    public String reconnect() {
        return connectToDatabase();
    }

    private String applySqlTemplate(String sql, Map<String, String> args) {
        try {
            Handlebars handlebars = new Handlebars();
            Template template = handlebars.compileInline(sql);
            return template.apply(args);
        } catch (Exception e) {
            return "";
        }
    }
}
