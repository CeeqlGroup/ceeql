package org.mrcsparker.ceeql;

import org.junit.Test;
import org.skife.jdbi.v2.ColonPrefixNamedParamStatementRewriter;
import org.skife.jdbi.v2.tweak.RewrittenStatement;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class CeeqlTemplateTest {
    @Test
    public void can_use_basic_template() {
        Ceeql ceeql = DbCreator.create();

        String sql = new StringBuilder()
                .append("SELECT * FROM products\n")
                .append("{{#if id}}\n")
                .append("  WHERE id = :id")
                .append("{{/if}}").toString();

        String output = ceeql.select(sql, new HashMap<>());
        assertEquals(output,
                "[{\"price\":100.0000,\"vendor_id\":1,\"name\":\"first\",\"id\":1},{\"price\":200.0000,\"vendor_id\":2,\"name\":\"second\",\"id\":2},{\"price\":300.0000,\"vendor_id\":3,\"name\":\"third\",\"id\":3}]");


        HashMap<String, String> args = new HashMap<>();
        args.put("id", "1");

        output = ceeql.select(sql, args);
        assertEquals(output,
                "[{\"price\":100.0000,\"vendor_id\":1,\"name\":\"first\",\"id\":1}]");

        ceeql.close();
    }

    @Test
    public void can_insert_using_each_template() throws JsonProcessingException {
        Ceeql ceeql = DbCreator.create();

        String sql = new StringBuilder()
                .append("{{#each items}}")
                .append("INSERT INTO products (\n")
                .append("price, vendor_id, name\n")
                .append(") VALUES (\n")
                .append(":price, :vendor_id, :name);")
                .append("{{/each}}").toString();

        List<Map<String, String>> items = new ArrayList<Map<String, String>>();

        Map<String, String> item = new HashMap<>();
        item.put("price", "1000");
        item.put("vendor_id", "100");
        item.put("name", "product_vendor_id_100");

        items.add(item);
        
        item = new HashMap<>();
        item.put("price", "2000");
        item.put("vendor_id", "200");
        item.put("name", "product_vendor_id_200");

        items.add(item);

        Map<String, String> args = new HashMap<String, String>();
        args.put("items", new ObjectMapper().writeValueAsString(items));
        
        String output = ceeql.insert(sql, args);

        ceeql.close();
    }

    @Test
    public void should_not_escape_characters_with_default_strategy_template() {
        Ceeql ceeql = DbCreator.create();

        String sql = new StringBuilder()
                .append("SELECT {{#each data_list.formula_list}}\n")
                .append("{{this}}{{#unless @last}},{{/unless}}\n")
                .append("{{/each}}\n")
                .append("FROM products\n")
                .toString();

        HashMap<String, String> args = new HashMap<>();
        args.put("data_list", "{\"formula_list\": [ \"price\", \"vendor_id\", \"name\", \"price > 150\" ] }");

        String output = ceeql.select(sql, args);
        System.out.println(output);
        assertEquals(output,
                "[{\"price > 150\":false,\"price\":100.0000,\"vendor_id\":1,\"name\":\"first\"},{\"price > 150\":true,\"price\":200.0000,\"vendor_id\":2,\"name\":\"second\"},{\"price > 150\":true,\"price\":300.0000,\"vendor_id\":3,\"name\":\"third\"}]");

        ceeql.close();
    }
    
    //
    // Custom helpers
    //
    
    @Test
    public void param_block() throws IOException {

        String sql = new StringBuilder()
                .append("{{#s}}{{#each drugs}}{{safe this}}{{#unless @last}},{{/unless}}{{/each}}{{/s}}")
                .toString();

        HashMap<String, String> args = new HashMap<>();
        args.put("drugs", "[ \"one\", \"two\", \"three\" ]");

        String output = CeeqlTemplate.apply(sql, args);
        System.out.println(output);
        assertEquals(
                "one,two,three",
        		args.get(output.substring(1)));

    }

}

