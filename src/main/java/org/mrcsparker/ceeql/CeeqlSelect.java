package org.mrcsparker.ceeql;

import org.skife.jdbi.v2.Handle;

import java.util.Map;

class CeeqlSelect {

    private org.skife.jdbi.v2.Query query;
    private Map<String, String> args;

    public CeeqlSelect(Handle dbiHandle, String sql, Map<String, String> args) {
        this.query = CeeqlQuery.build(dbiHandle, CeeqlTemplate.apply(sql, args), args);
        this.args = args;
    }

    public String first() {
        for (Map.Entry<String, String> arg : args.entrySet()) {
            query.bind(arg.getKey(), arg.getValue());
        }
        try {
            return CeeqlJson.generate(query.first());
        } catch (Exception e) {
            return CeeqlError.error(e);
        }
    }

    public String all() {
        for (Map.Entry<String, String> arg : args.entrySet()) {
            query.bind(arg.getKey(), arg.getValue());
        }
        try {
            return CeeqlJson.generate(query.list());
        } catch (Exception e) {
            return CeeqlError.error(e);
        }
    }
}
