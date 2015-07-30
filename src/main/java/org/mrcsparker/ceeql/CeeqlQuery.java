package org.mrcsparker.ceeql;

import org.skife.jdbi.v2.Handle;
import org.skife.jdbi.v2.Query;
import org.skife.jdbi.v2.Update;

import java.util.Map;

class CeeqlQuery {
    public static Query build(Handle dbiHandle, String sql, Map<String, String> args) {
        org.skife.jdbi.v2.Query q = dbiHandle.createQuery(sql);

        for (Map.Entry<String, String> arg : args.entrySet()) {
            q.bind(arg.getKey(), arg.getValue());
        }

        return q;
    }

    public static Update create(Handle dbiHandle, String sql, Map<String, String> args) {
        Update q = dbiHandle.createStatement(sql);

        for (Map.Entry<String, String> arg : args.entrySet()) {
            q.bind(arg.getKey(), arg.getValue());
        }

        return q;
    }
}
