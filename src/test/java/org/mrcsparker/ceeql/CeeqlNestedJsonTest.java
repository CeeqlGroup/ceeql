package org.mrcsparker.ceeql;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;


public class CeeqlNestedJsonTest {

    /*
    // This is a place-setter for the next release.
    // Nested JSON support
    @Test
    public void lists_dotted_data_as_nested() {
        Ceeql p = DbCreator.create();

        String sql = "SELECT name \"product.name\" FROM products where id = :id";
        Map<String, String> args = new HashMap<>();
        args.put("id", "1");

        String output = p.select(sql, args);

        assertEquals(output,
            "[{\"product\": {\"name\":\"first\"}}]");

        p.close();
    }
    */
}
