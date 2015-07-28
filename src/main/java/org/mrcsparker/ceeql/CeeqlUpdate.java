package org.mrcsparker.ceeql;

import org.skife.jdbi.v2.Handle;

import java.util.Map;

class CeeqlUpdate extends CeeqlAction {
    public CeeqlUpdate(Handle dbiHandle, String sql, Map<String, String> args) {
        super(dbiHandle, sql, args);
    }
}
