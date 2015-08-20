package org.mrcsparker.ceeql;

import org.skife.jdbi.v2.Handle;

import java.util.Map;

class CeeqlSelect {

    private final org.skife.jdbi.v2.Query query;
    private final Map<String, String> args;

    public CeeqlSelect(Handle dbiHandle, String sql, Map<String, String> args) {
        this.query = CeeqlQuery.build(dbiHandle, CeeqlTemplate.apply(sql, args), args);
        this.args = args;
    }

    public String first(CeeqlOutputType type) {
        for (Map.Entry<String, String> arg : args.entrySet()) {
            query.bind(arg.getKey(), arg.getValue());
        }
        try {
            switch (type) {
                case JSON:
                    return CeeqlJson.generate(query.first());
                case CSV:
                    return CeeqlCsv.generate(((Map<String, Object>) query.first()));
                case XML:
                    return CeeqlXml.generate(query.first());
                default:
                    return "";
            }
        } catch (Exception e) {
            return CeeqlError.error(e);
        }
    }

    public String first() {
        return first(CeeqlOutputType.JSON);
    }

    public String all(CeeqlOutputType type) {

        for (Map.Entry<String, String> arg : args.entrySet()) {
            query.bind(arg.getKey(), arg.getValue());
        }

        try {
            switch (type) {
                case JSON:
                    return CeeqlJson.generate(query.list());
                case CSV:
                    return CeeqlCsv.generate(query.list());
                case XML:
                    return CeeqlXml.generate(query.list());
                default:
                    return "";
            }
        } catch (Exception e) {
            return CeeqlError.error(e);
        }
    }

    public String all() {
        return all(CeeqlOutputType.JSON);
    }
}
