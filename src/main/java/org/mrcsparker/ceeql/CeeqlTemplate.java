package org.mrcsparker.ceeql;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;

import java.util.ArrayList;
import java.util.Map;

public class CeeqlTemplate {
    public static String apply(String s, Map<String, String> args) {
        try {
            Handlebars handlebars = new Handlebars();
            Template template = handlebars.compileInline(s);
            return template.apply(args);
        } catch (Exception e) {
            return "";
        }
    }

    public static String apply(String s, ArrayList<Map<String, String>> args) {
        try {
            Handlebars handlebars = new Handlebars();
            Template template = handlebars.compileInline(s);
            return template.apply(args);
        } catch (Exception e) {
            return "";
        }
    }
}
