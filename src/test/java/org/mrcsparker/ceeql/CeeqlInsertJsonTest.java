package org.mrcsparker.ceeql;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class CeeqlInsertJsonTest {

    @Test
    public void can_insert_record() {
        Ceeql p = DbCreator.create();

        String sql = "SELECT * FROM products";
        Map<String, String> args = new HashMap<>();

        String output = p.select(sql, args).all().toJson();

        assertEquals(output,
                "[{\"price\":100.0000,\"vendor_id\":1,\"name\":\"first\",\"id\":1},{\"price\":200.0000,\"vendor_id\":2,\"name\":\"second\",\"id\":2},{\"price\":300.0000,\"vendor_id\":3,\"name\":\"third\",\"id\":3}]");


        sql = "INSERT INTO products (name, vendor_id, price) VALUES (:name, :vendor_id, :price)";
        args = new HashMap<>();
        args.put("name", "fourth");
        args.put("vendor_id", "4");
        args.put("price", "400.0000");

        output = p.insert(sql, args).toJson();

        assertEquals(output,
                "{\"scope_identity()\":4}");
    }
}
