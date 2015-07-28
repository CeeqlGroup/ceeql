package org.mrcsparker.ceeql;

import org.skife.jdbi.v2.Handle;

import java.util.Map;

abstract class CeeqlAction {

    protected Handle dbiHandle;
    protected String sql;
    protected Map<String, String> args;
    protected org.skife.jdbi.v2.Update update;

    public CeeqlAction(Handle dbiHandle, String sql, Map<String, String> args) {
        this.dbiHandle = dbiHandle;
        this.sql = sql;
        this.args = args;
        this.update = CeeqlQuery.create(dbiHandle, CeeqlTemplate.apply(sql, args), args);
    }

    public String exec() {
        for (Map.Entry<String, String> arg : args.entrySet()) {
            update.bind(arg.getKey(), arg.getValue());
        }
        try {
            return CeeqlJson.generate(update.executeAndReturnGeneratedKeys().first());
        } catch (Exception e) {
            return CeeqlError.error(e);
        }
    }
}
