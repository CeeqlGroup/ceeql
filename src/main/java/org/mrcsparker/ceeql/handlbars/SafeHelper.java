package org.mrcsparker.ceeql.handlbars;

import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;

import java.io.IOException;

public class SafeHelper implements Helper<Object> {

    private static final String SQL_REGEX = "('.+--)|(--)|(\\|)|(%7C)";

    @Override
    public CharSequence apply(final Object context, Options options) throws IOException {
        if (context instanceof Number) {
            return context.toString();
        } else {
            return context.toString().replace(SQL_REGEX, "");
        }
    }
}
