package org.mrcsparker.ceeql;

import org.junit.Test;

import java.util.HashMap;

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
}
