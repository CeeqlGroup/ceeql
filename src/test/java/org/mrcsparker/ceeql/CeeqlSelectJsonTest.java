package org.mrcsparker.ceeql;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class CeeqlSelectJsonTest {
    @Test
    public void can_map_select_to_json() {
        Ceeql p = DbCreator.create();


        String sql = "SELECT * FROM products";
        Map<String, String> args = new HashMap<>();

        String output = p.select(sql, args).all().toJson();

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

        String output = p.select(sql, args).all().toJson();

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

        String output = p.select(sql, args).all().toJson();

        assertEquals(output,
                "[{\"price\":100.0000,\"vendor_id\":1,\"name\":\"first\",\"id\":1},{\"price\":200.0000,\"vendor_id\":2,\"name\":\"second\",\"id\":2}]");

        p.close();
    }
}
