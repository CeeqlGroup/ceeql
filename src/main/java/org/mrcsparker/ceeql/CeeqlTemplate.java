package org.mrcsparker.ceeql;

import com.github.jknack.handlebars.Handlebars;

import java.util.ArrayList;
import java.util.Map;

class CeeqlTemplate {
    public static String apply(String s, Map<String, String> args) {
        try {
            Handlebars handlebars = new Handlebars();
            com.github.jknack.handlebars.Template template = handlebars.compileInline(s);
            return template.apply(args);
        } catch (Exception e) {
            return "";
        }
    }

    public static String apply(String s, ArrayList<Map<String, String>> args) {
        try {
            Handlebars handlebars = new Handlebars();
            com.github.jknack.handlebars.Template template = handlebars.compileInline(s);
            return template.apply(args);
        } catch (Exception e) {
            return "";
        }
    }
}
