package org.mrcsparker.ceeql;

import org.skife.jdbi.v2.Handle;
import org.skife.jdbi.v2.Query;

import java.util.Map;

public class CeeqlQuery {
    public static Query build(Handle dbiHandle, String sql, Map<String, String> args) {
        Query q = dbiHandle.createQuery(sql);

        for (Map.Entry<String, String> arg : args.entrySet()) {
            q.bind(arg.getKey(), arg.getValue());
        }

        return q;
    }
}
