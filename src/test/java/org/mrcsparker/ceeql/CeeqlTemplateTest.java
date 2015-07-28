package org.mrcsparker.ceeql;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
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
    public void can_insert_using_each_template() {
        Ceeql ceeql = DbCreator.create();

        String sql = new StringBuilder()
                .append("{{each items}}")
                .append("INSERT INTO products (\n")
                .append("price, vendor_id, name\n")
                .append(") VALUES (\n")
                .append(":price, :vendor_id, :name);")
                .append("{{/each}}").toString();

        ArrayList<Map<String, String>> argList = new ArrayList<>();

        Map<String, String> items = new HashMap<>();
        items.put("price", "1000");
        items.put("vendor_id", "100");
        items.put("name", "product_vendor_id_100");

        argList.add(items);

        items = new HashMap<>();
        items.put("price", "2000");
        items.put("vendor_id", "200");
        items.put("name", "product_vendor_id_200");

        argList.add(items);

        items = new HashMap<>();
        items.put("price", "3000");
        items.put("vendor_id", "300");
        items.put("name", "product_vendor_id_300");

        argList.add(items);

        //String output = ceeql.insert(sql, argList);

        ceeql.close();
    }
}
