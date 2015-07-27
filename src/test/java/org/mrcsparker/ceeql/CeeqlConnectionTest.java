package org.mrcsparker.ceeql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class CeeqlConnectionTest {
    @Test
    public void isConnected_should_be_false_for_bad_drivers() {
        Ceeql p = new Ceeql("org.test.Driver", "jdbc:h2:mem:test", "username", "password");
        assertEquals(p.isConnected(), false);
    }

    @Test
    public void isConnected_should_be_true_for_good_drivers() {
        Ceeql p = new Ceeql("org.h2.Driver", "jdbc:h2:mem:test", "username", "password");
        assertEquals(p.isConnected(), true);
        p.close();
    }

    @Test
    public void isConnected_try_with_resources() {
        int i = 0;
        try(Ceeql p = new Ceeql("org.h2.Driver", "jdbc:h2:mem:test", "username", "password")) {
            assertEquals(p.isConnected(), true);
            i++;
        } catch (Exception e) {
            assertTrue(false);
        }
        assertEquals(i, 1);
    }

    @Test
    public void can_reconnect() throws Exception {
        Ceeql p = DbCreator.create();

        String sql = "SELECT * FROM products WHERE vendor_id = :vendorId1 OR vendor_id = :vendorId2";
        Map<String, String> args = new HashMap<>();
        args.put("vendorId1", "1");
        args.put("vendorId2", "2");

        String output = p.select(sql, args);

        assertEquals(output,
            "[{\"price\":100.0000,\"vendor_id\":1,\"name\":\"first\",\"id\":1},{\"price\":200.0000,\"vendor_id\":2,\"name\":\"second\",\"id\":2}]");

        p.close();

        assertFalse(p.isConnected());

        ObjectMapper mapper = new ObjectMapper();
        CeeqlMessageDTO[] dtos = mapper.readValue(p.select(sql, args), CeeqlMessageDTO[].class);

        assertEquals(dtos[0].getMessageType(), "error");
        assertEquals(dtos[0].getMessageSubType(), "UnableToCreateStatementException");

        mapper = new ObjectMapper();
        dtos = mapper.readValue(p.reconnect(), CeeqlMessageDTO[].class);

        assertEquals(dtos[0].getMessageType(), "message");
        assertEquals(dtos[0].getMessageSubType(), "Info");
        assertEquals(dtos[0].getMessage(), "Connected");

        assertTrue(p.isConnected());

        output = p.select(sql, args);

        assertEquals(output,
            "[{\"price\":100.0000,\"vendor_id\":1,\"name\":\"first\",\"id\":1},{\"price\":200.0000,\"vendor_id\":2,\"name\":\"second\",\"id\":2}]");


    }
}
