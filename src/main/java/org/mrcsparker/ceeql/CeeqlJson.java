package org.mrcsparker.ceeql;

import com.fasterxml.jackson.databind.ObjectMapper;

public class CeeqlJson {
    public static <T> String generate(T o) {
        final ObjectMapper mapper = new ObjectMapper();

        try {
            return mapper.writeValueAsString(o);
        } catch (Exception e) {
            return CeeqlError.errorType(e.getClass().getSimpleName(), e.getMessage());
        }
    }
}
