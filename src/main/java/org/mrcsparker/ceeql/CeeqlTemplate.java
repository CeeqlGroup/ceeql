package org.mrcsparker.ceeql;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.github.jknack.handlebars.Handlebars;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mrcsparker.ceeql.handlbars.SafeHelper;

import java.util.HashMap;
import java.util.Map;

class CeeqlTemplate {

    private final static Logger log = LogManager.getLogger(CeeqlTemplate.class);

    public static String apply(String s, Map<String, String> args) {

        ObjectReader mapper = new ObjectMapper().readerFor(Object.class);

        HashMap<String, Object> parsedArgs = new HashMap<>();
        for (Map.Entry<String, String> a : args.entrySet()) {
            try {
                parsedArgs.put(a.getKey(), mapper.readValue(a.getValue()));
            } catch (Exception e) {
                parsedArgs.put(a.getKey(), a.getValue());
            }
        }

        try {
            Handlebars handlebars = new Handlebars();
            handlebars.registerHelper("safe", new SafeHelper());
            com.github.jknack.handlebars.Template template = handlebars.compileInline(s);

            return template.apply(parsedArgs);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
