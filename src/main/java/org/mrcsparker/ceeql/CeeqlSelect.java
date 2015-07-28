package org.mrcsparker.ceeql;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.skife.jdbi.v2.Handle;
import org.skife.jdbi.v2.Query;

import java.util.List;
import java.util.Map;

public class CeeqlSelect {

    private final static Logger log = LogManager.getLogger(CeeqlSelect.class);

    private Query query;
    private Map<String, String> args;
    private List list;
    private Object object;
    private Exception error;

    public CeeqlSelect(Handle dbiHandle, String sql, Map<String, String> args) {
        this.query = CeeqlQuery.build(dbiHandle, CeeqlTemplate.apply(sql, args), args);
        this.args = args;
    }

    public CeeqlSelect first() {
        for (Map.Entry<String, String> arg : args.entrySet()) {
            query.bind(arg.getKey(), arg.getValue());
        }

        try {
            this.object = query.first();
        } catch(Exception e) {
            this.error = e;
        }
        return this;
    }

    public CeeqlSelect all() {
        for (Map.Entry<String, String> arg : args.entrySet()) {
            query.bind(arg.getKey(), arg.getValue());
        }

        try {
            this.list = query.list();
        } catch(Exception e) {
            this.error = e;
        }

        return this;
    }

    public String toJson() {

        if (error != null) {
            return CeeqlError.errorType(error.getClass().getSimpleName(), error.getMessage());
        }

        try {
            if (object != null) {
                return CeeqlJson.generate(object);
            } else {
                return CeeqlJson.generate(list);
            }
        } catch (Exception e) {
            return CeeqlError.errorType(e.getClass().getSimpleName(), e.getMessage());
        }
    }

    public Object toObject() {
        return object;
    }

    public List toList() {
        return list;
    }
}
