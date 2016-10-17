package org.mrcsparker.ceeql;

import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;

class CeeqlHandle implements AutoCloseable {

    private final Handle handle;

    public CeeqlHandle(DBI dbi) {
        this.handle = dbi.open();
    }

    public Handle getHandle() {
        return handle;
    }

    @Override
    public void close() throws Exception {
        handle.close();
    }
}
