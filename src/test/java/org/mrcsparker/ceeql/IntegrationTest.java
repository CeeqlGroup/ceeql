package org.mrcsparker.ceeql;


import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import org.junit.Test;
import org.mrcsparker.ceeql.Ceeql;
import org.mrcsparker.ceeql.CeeqlTemplate;
import org.mrcsparker.ceeql.handlbars.ParamHelper;
import org.mrcsparker.ceeql.jdbi.NamedParameterRewriter.NameList;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.naming.event.NamespaceChangeListener;

import static org.junit.Assert.assertEquals;

public class IntegrationTest {
	
    @Test
    public void joinTest() throws IOException {
        String sql = new StringBuilder()
                .append("{{#s}}{{join drugs \",\"}}{{/s}}")
                .toString();

        HashMap<String, String> args = new HashMap<>();
        args.put("drugs", "[ \"one\", \"two\", \"three\" ]");

        String output = CeeqlTemplate.apply(sql, args);
        System.out.println(output);
        assertEquals(
                "one,two,three",
        		args.get(output.substring(1)));

        sql = new StringBuilder()
                .append("{{s (join drugs \",\")}}")
                .toString();

        output = CeeqlTemplate.apply(sql, args);
        System.out.println(output);
        assertEquals(
                "one,two,three",
        		args.get(output.substring(1)));
    }

    @Test
    public void identifierTest() throws IOException {
        String sql = new StringBuilder()
                .append("select {{s (join columns \",\") \"identifier\"}} from {{s table \"identifier\"}}")
                .toString();

        HashMap<String, String> args = new HashMap<>();
        args.put("columns", "[ \"one\", \"two\", \"three\" ]");
        args.put("table", "database.table");

        String output = CeeqlTemplate.apply(sql, args);
        System.out.println(output);
        assertEquals(
                "select one,two,three from database.table",
        		output);

        sql = new StringBuilder()
                .append("select {{s columns \"identifier\"}} from {{s table \"identifier\"}}")
                .toString();

        args = new HashMap<>();
        args.put("columns", "[ \"one\", \"two\", \"three\" ]");
        args.put("table", "database.table");

        output = CeeqlTemplate.apply(sql, args);
        System.out.println(output);
        assertEquals(
                "select one,two,three from database.table",
        		output);

    }

    @Test
    public void inTest() throws IOException {
        String sql = new StringBuilder()
                .append("select * from table where column in ({{s ids \"in\"}})")
                .toString();

        HashMap<String, String> args = new HashMap<>();
        args.put("ids", "[ \"one\", \"two\", \"three\" ]");

        Map<String, String> parameters = new HashMap<String, String>();
        NameList names = new NameList(new String[]{"p1","p2","p3"});
        
        String output = CeeqlTemplate.apply(sql, args, parameters, names);
        System.out.println(output);
        assertEquals(
                "select * from table where column in (:p1,:p2,:p3)",
        		output);
    }

}
