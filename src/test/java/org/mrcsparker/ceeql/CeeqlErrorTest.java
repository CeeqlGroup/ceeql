package org.mrcsparker.ceeql;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CeeqlErrorTest {

    @Test
    public void ceeql_cannot_find_driver_error() throws Exception {
        final ObjectMapper mapper = new ObjectMapper();
        Ceeql p = new Ceeql("org.test.Driver", "jdbc:h2:mem:test", "username", "password");

        CeeqlMessageDTO[] dtos = mapper.readValue(p.reconnect(), CeeqlMessageDTO[].class);

        assertEquals(dtos[0].getMessageType(), "error");
        assertEquals(dtos[0].getMessageSubType(), "ClassNotFoundException");
        assertEquals(dtos[0].getMessage(), "org.test.Driver");

        p.close();
    }
}
