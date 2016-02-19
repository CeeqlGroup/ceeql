package org.mrcsparker.ceeql;

import org.skife.jdbi.v2.Handle;

import java.io.IOException;
import java.util.Map;

abstract class CeeqlAction {

    protected final Map<String, String> args;
    protected final org.skife.jdbi.v2.Update update;

    public CeeqlAction(Handle dbiHandle, String sql, Map<String, String> args) throws IOException {
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
