package org.mrcsparker.ceeql;


import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import org.junit.Test;
import org.mrcsparker.ceeql.Ceeql;
import org.mrcsparker.ceeql.CeeqlTemplate;
import org.mrcsparker.ceeql.handlbars.ParameterHelper;
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

        Map<String, String> args = new HashMap<>();
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
                .append("select {{identifier (join columns \",\")}} from {{identifier table}}")
                .toString();

        Map<String, String> args = new HashMap<>();
        args.put("columns", "[ \"one\", \"two\", \"three\" ]");
        args.put("table", "database.table");

        String output = CeeqlTemplate.apply(sql, args);
        System.out.println(output);
        assertEquals(
                "select one,two,three from database.table",
        		output);

        sql = new StringBuilder()
                .append("select {{identifier columns}} from {{identifier table}}")
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
    public void listTest() throws IOException {
        String sql = new StringBuilder()
                .append("select * from table where column in ({{s keys}})")
                .toString();

        Map<String, String> args = new HashMap<>();
        args.put("keys", "[ \"one\", \"two\", \"three\" ]");

        Map<String, String> parameters = new HashMap<String, String>();
        NameList names = new NameList(new String[]{"p1","p2","p3"});
        
        String output = CeeqlTemplate.apply(sql, args, parameters, names);
        System.out.println(output);
        assertEquals(
                "select * from table where column in (:p1,:p2,:p3)",
        		output);

        sql = new StringBuilder()
                .append("select * from table where column in ({{number ids}})")
                .toString();

        args = new HashMap<>();
        args.put("ids", "[ 1, 2, 3 ]");

        parameters = new HashMap<String, String>();
        names = new NameList();
        
        output = CeeqlTemplate.apply(sql, args, parameters, names);
        System.out.println(output);
        assertEquals(
                "select * from table where column in (1,2,3)",
        		output);        
    }

}
