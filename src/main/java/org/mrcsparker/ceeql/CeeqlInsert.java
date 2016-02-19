package org.mrcsparker.ceeql;

import org.skife.jdbi.v2.Handle;

import java.io.IOException;
import java.util.Map;

class CeeqlInsert extends CeeqlAction {
    public CeeqlInsert(Handle dbiHandle, String sql, Map<String, String> args) throws IOException {
        super(dbiHandle, sql, args);
    }
}
