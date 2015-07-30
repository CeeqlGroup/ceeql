package org.mrcsparker.ceeql.handlbars;

import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class SafeHelper implements Helper<Object> {

    private final static Logger log = LogManager.getLogger(SafeHelper.class);
    private static final String SQL_REGEX = "('.+--)|(--)|(\\|)|(%7C)";

    @Override
    public CharSequence apply(final Object context, Options options) throws IOException {

        if (context == null) {
            return "";
        }

        if (context instanceof Number) {
            return context.toString();
        } else {
            return context.toString().replace(SQL_REGEX, "");
        }
    }
}
