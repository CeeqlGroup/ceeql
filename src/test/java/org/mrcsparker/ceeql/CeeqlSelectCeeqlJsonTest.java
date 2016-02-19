package org.mrcsparker.ceeql;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import static org.hamcrest.core.StringContains.containsString;

public class CeeqlSelectCeeqlJsonTest {
    @Test
    public void can_map_select_to_json() {
        Ceeql p = DbCreator.create();


        String sql = "SELECT * FROM products";
        Map<String, String> args = new HashMap<>();

        String output = p.select(sql, args);

        assertEquals(output,
                "[{\"price\":100.0000,\"vendor_id\":1,\"name\":\"first\",\"id\":1},{\"price\":200.0000,\"vendor_id\":2,\"name\":\"second\",\"id\":2},{\"price\":300.0000,\"vendor_id\":3,\"name\":\"third\",\"id\":3}]");

        p.close();
    }

    @Test
    public void can_map_select_with_id_arg_to_json() {
        Ceeql p = DbCreator.create();

        String sql = "SELECT * FROM products WHERE id = :id";
        Map<String, String> args = new HashMap<>();
        args.put("id", "1");

        String output = p.select(sql, args);

        assertEquals(output,
                "[{\"price\":100.0000,\"vendor_id\":1,\"name\":\"first\",\"id\":1}]");

        p.close();
    }

    @Test
    public void can_map_select_with_vendor_arg_to_json() {
        Ceeql p = DbCreator.create();

        String sql = "SELECT * FROM products WHERE vendor_id = :vendorId1 OR vendor_id = :vendorId2";
        Map<String, String> args = new HashMap<>();
        args.put("vendorId1", "1");
        args.put("vendorId2", "2");

        String output = p.select(sql, args);

        assertEquals(output,
                "[{\"price\":100.0000,\"vendor_id\":1,\"name\":\"first\",\"id\":1},{\"price\":200.0000,\"vendor_id\":2,\"name\":\"second\",\"id\":2}]");

        p.close();
    }

    @Test
    public void should_return_json_error_message_when_handlebars_query_syntax_cannot_be_parsed() {
        Ceeql p = DbCreator.create();

        String sql = "select {{#if cols}}{{#each cols}} MIN({{safe this}}) as min_{{safe this}},MAX({{safe this}}) as max_{{safe this}} from {{tablename}};";
        Map<String, String> args = new HashMap<>();
        args.put("vendorId1", "1");
        args.put("vendorId2", "2");

        String output = p.select(sql, args);

        assertThat(output, containsString("[{\"messageType\":\"error\",\"messageSubType\":\"HandlebarsException\",\"timestamp\":"));
        assertThat(output, containsString(sql));

        p.close();
    }
}
