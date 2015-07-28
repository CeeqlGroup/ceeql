package org.mrcsparker.ceeql;

import org.skife.jdbi.v2.Handle;
import java.util.Map;

class CeeqlDelete extends CeeqlAction {
    public CeeqlDelete(Handle dbiHandle, String sql, Map<String, String> args) {
        super(dbiHandle, sql, args);
    }

    @Override
    public String exec() {
        for (Map.Entry<String, String> arg : args.entrySet()) {
            update.bind(arg.getKey(), arg.getValue());
        }
        try {
            return CeeqlJson.generate(update.executeAndReturnGeneratedKeys().list());
        } catch (Exception e) {
            return CeeqlError.error(e);
        }
    }
}