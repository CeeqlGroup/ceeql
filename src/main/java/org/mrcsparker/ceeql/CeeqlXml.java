package org.mrcsparker.ceeql;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class CeeqlXml {
    public static <T> String generate(T o) {
        final ObjectMapper mapper = new XmlMapper();

        try {
            return mapper.writeValueAsString(o);
        } catch (Exception e) {
            return CeeqlError.errorType(e.getClass().getSimpleName(), e.getMessage());
        }
    }
}
