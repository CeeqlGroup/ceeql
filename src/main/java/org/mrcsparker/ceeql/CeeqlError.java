package org.mrcsparker.ceeql;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Date;

public class CeeqlError implements ICeeqlMessage {
    private long timestamp;
    private String errorType;
    private String message;

    public CeeqlError(String message) {
        this.timestamp = new Date().getTime();
        this.message = message;
    }

    public CeeqlError(String errorType, String message) {
        this.errorType = errorType;
        this.timestamp = new Date().getTime();
        this.message = message;
    }

    public static String error(String message) {
        CeeqlError e = new CeeqlError(message);
        return e.toJson();
    }

    public static String errorType(String errorType, String message) {
        CeeqlError e = new CeeqlError(errorType, message);
        return e.toJson();
    }


    public String toJson() {
        CeeqlMessageDTO m = new CeeqlMessageDTO();
        m.setMessageType("error");
        if (!errorType.equals("")) {
            m.setMessageSubType(errorType);
        }
        m.setTimestamp(timestamp);
        m.setMessage(message);

        ArrayList<CeeqlMessageDTO> l = new ArrayList();
        l.add(m);

        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(l);
        } catch (JsonProcessingException e) {
            return "[{\"message\":\"no results\"}]";
        }
    }

}
