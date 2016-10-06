package org.mrcsparker.ceeql.handlbars;

import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.skife.jdbi.v2.Handle;
import org.skife.jdbi.v2.Query;

import java.io.IOException;
import java.sql.SQLException;

public class SafeHelper implements Helper<Object> {

    private final static Logger log = LogManager.getLogger(SafeHelper.class);
    private Handle dbiHandle;

    private String replaceString(String input) {
        input = input.replaceAll("'", "''");
        return input;
    }

    @Override
    public CharSequence apply(final Object context, Options options) throws IOException {

        if (context == null) {
            return "";
        }

        if (context instanceof Number) {
            return context.toString();
        } else {
            return replaceString(context.toString());
        }
    }
}
