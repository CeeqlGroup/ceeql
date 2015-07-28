package org.mrcsparker.ceeql;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MessageTest {
    @Test
    public void ceeql_cannot_find_driver_error() throws Exception {
        final ObjectMapper mapper = new ObjectMapper();
        Ceeql p = new Ceeql("org.h2.Driver", "jdbc:h2:mem:test", "username", "password");

        CeeqlMessageDTO[] dtos = mapper.readValue(p.reconnect(), CeeqlMessageDTO[].class);

        assertEquals(dtos[0].getMessageType(), "message");
        assertEquals(dtos[0].getMessageSubType(), "Info");
        assertEquals(dtos[0].getMessage(), "Connected");

        p.close();
    }
}
