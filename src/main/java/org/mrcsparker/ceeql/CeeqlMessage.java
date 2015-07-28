package org.mrcsparker.ceeql;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Date;

public class CeeqlMessage implements ICeeqlMessage {

    private long timestamp;
    private String message;

    private CeeqlMessage(String message) {
        this.timestamp = new Date().getTime();
        this.message = message;
    }

    public static String message(String message) {
        CeeqlMessage e = new CeeqlMessage(message);
        return e.toJson();
    }

    @Override
    public String toJson() {
        CeeqlMessageDTO m = new CeeqlMessageDTO();
        m.setMessageType("message");
        m.setMessageSubType("Info");
        m.setTimestamp(timestamp);
        m.setMessage(message);

        ArrayList<CeeqlMessageDTO> l = new ArrayList<>();
        l.add(m);

        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(l);
        } catch (JsonProcessingException e) {
            return "";
        }
    }
}
