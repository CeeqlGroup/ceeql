package org.mrcsparker.ceeql;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class CeeqlDeleteJsonTest {
    @Test
    public void can_delete_all_data() {
        Ceeql p = DbCreator.create();

        String sql = "SELECT * FROM products";
        Map<String, String> args = new HashMap<>();

        String output = p.select(sql, args).all().toJson();

        assertEquals(output,
                "[{\"price\":100.0000,\"vendor_id\":1,\"name\":\"first\",\"id\":1},{\"price\":200.0000,\"vendor_id\":2,\"name\":\"second\",\"id\":2},{\"price\":300.0000,\"vendor_id\":3,\"name\":\"third\",\"id\":3}]");


        sql = "DELETE FROM products";
        args = new HashMap<>();

        output = p.delete(sql, args);

        assertEquals(output, "[]");

        sql = "SELECT * FROM products";
        args = new HashMap<>();

        output = p.select(sql, args).all().toJson();

        assertEquals(output, "[]");

        p.close();
    }

    @Test
    public void can_delete_one_item_of_data() {
        Ceeql p = DbCreator.create();

        String sql = "SELECT * FROM products";
        Map<String, String> args = new HashMap<>();

        String output = p.select(sql, args).all().toJson();

        assertEquals(output,
                "[{\"price\":100.0000,\"vendor_id\":1,\"name\":\"first\",\"id\":1},{\"price\":200.0000,\"vendor_id\":2,\"name\":\"second\",\"id\":2},{\"price\":300.0000,\"vendor_id\":3,\"name\":\"third\",\"id\":3}]");


        sql = "DELETE FROM products WHERE id = :id";
        args = new HashMap<>();
        args.put("id", "1");

        output = p.delete(sql, args);

        assertEquals(output, "[]");

        sql = "SELECT * FROM products";
        args = new HashMap<>();

        output = p.select(sql, args).all().toJson();

        assertEquals(output, "[{\"price\":200.0000,\"vendor_id\":2,\"name\":\"second\",\"id\":2},{\"price\":300.0000,\"vendor_id\":3,\"name\":\"third\",\"id\":3}]");


        p.close();
    }
}
