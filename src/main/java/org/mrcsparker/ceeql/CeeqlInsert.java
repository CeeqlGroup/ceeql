package org.mrcsparker.ceeql;

import org.skife.jdbi.v2.GeneratedKeys;
import org.skife.jdbi.v2.Handle;
import org.skife.jdbi.v2.Query;
import org.skife.jdbi.v2.Update;

import java.util.Map;
import java.util.Queue;

public class CeeqlInsert {
    private Update update;

    public CeeqlInsert(Handle dbiHandle, String sql, Map<String, String> args) {
        this.update = CeeqlQuery.create(dbiHandle, CeeqlTemplate.apply(sql, args), args);
    }

    public String toJson() {
        try {
            return CeeqlJson.generate(update.executeAndReturnGeneratedKeys().first());
        } catch (Exception e) {
            return CeeqlError.errorType(e.getClass().getSimpleName(), e.getMessage());
        }
    }
}
