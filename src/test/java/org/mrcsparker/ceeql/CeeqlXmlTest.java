package org.mrcsparker.ceeql;

import org.junit.Test;
import org.skife.jdbi.v2.Handle;
import org.skife.jdbi.v2.Query;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class CeeqlXmlTest {
    @Test
    public void can_create_csv_output() throws Exception {
        Ceeql p = DbCreator.create();

        String sql = "SELECT * FROM products";
        Map<String, String> args = new HashMap<>();
        Handle h = p.getDbiHandle();
        Query q = h.createQuery(sql);
        assertEquals(CeeqlXml.generate(q.list()), "<ArrayList xmlns=\"\"><item><price>100.0000</price><vendor_id>1</vendor_id><name>first</name><id>1</id></item><item><price>200.0000</price><vendor_id>2</vendor_id><name>second</name><id>2</id></item><item><price>300.0000</price><vendor_id>3</vendor_id><name>third</name><id>3</id></item></ArrayList>");
    }
}
