package org.mrcsparker.ceeql;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.github.jknack.handlebars.Handlebars;
import org.mrcsparker.ceeql.handlbars.SafeHelper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

class CeeqlTemplate {

    public static String apply(String s, Map<String, String> args) throws IOException {

        ObjectReader mapper = new ObjectMapper().readerFor(Object.class);

        HashMap<String, Object> parsedArgs = new HashMap<>();
        for (Map.Entry<String, String> a : args.entrySet()) {
            try {
                parsedArgs.put(a.getKey(), mapper.readValue(a.getValue()));
            } catch (Exception e) {
                parsedArgs.put(a.getKey(), a.getValue());
            }
        }

        // We are not escaping the string sequence at this step. By default Handlebars escape a string
        // sequence using an HTML strategy for characters ( < > " \' ` &). This step is replacing
        // valid SQL statements characters with their escaped counter part causing queries to fail.
        // The next step after Handlebars preprocessing is to execute the SQL statement which will be
        // appropriately escaped because JDBI uses binding of values to avoid any SQL injection attacks.
        Handlebars handlebars = new Handlebars().with(CharSequence::toString);
        handlebars.registerHelper("safe", new SafeHelper());
        com.github.jknack.handlebars.Template template = handlebars.compileInline(s);

        return template.apply(parsedArgs);
    }
}
