package org.mrcsparker.ceeql;

import com.opencsv.CSVWriter;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


class CeeqlCsv {
    public static String generate(List<Map<String, Object>> rows) {
        StringWriter writer = new StringWriter();
        CSVWriter csvWriter = new CSVWriter(writer);

        boolean header = false;
        String[] keys = {};
        ArrayList<String[]> output = new ArrayList<>();

        for (Map<String, Object> row : rows) {
            if (!header) {
                keys = row.keySet().toArray(new String[] {});
                Arrays.sort(keys);
                output.add(keys);
                header = true;
            }

            String[] values = new String[keys.length];
            int i = 0;
            for(final String key : keys) {
                final Object value = row.get(key);
                values[i++] = (value != null) ? value.toString() : "null";
            }
            output.add(values);

        }

        try {
            csvWriter.writeAll(output);
            return writer.toString();
        } catch (Exception e) {
            return CeeqlError.errorType(e.getClass().getSimpleName(), e.getMessage());
        }
    }

    public static String generate(Map<String, Object> row) {
        StringWriter writer = new StringWriter();
        CSVWriter csvWriter = new CSVWriter(writer);

        String[] keys = {};
        ArrayList<String[]> output = new ArrayList<>();

        keys = row.keySet().toArray(new String[] {});
        Arrays.sort(keys);
        output.add(keys);

        String[] values = new String[keys.length];
        int i = 0;
        for(final String key : keys) {
            final Object value = row.get(key);
            values[i++] = (value != null) ? value.toString() : "null";
        }
        output.add(values);
        
        try {
            csvWriter.writeAll(output);
            return writer.toString();
        } catch (Exception e) {
            return CeeqlError.errorType(e.getClass().getSimpleName(), e.getMessage());
        }
    }
}
