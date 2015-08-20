package org.mrcsparker.ceeql;

import org.junit.Test;
import org.skife.jdbi.v2.Handle;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class CeeqlCsvTest {
    @Test
    public void can_create_csv_output() throws Exception {
        Ceeql p = DbCreator.create();

        String sql = "SELECT * FROM products";
        Map<String, String> args = new HashMap<>();
        Handle h = p.getDbiHandle();
        h.createQuery(sql);

        assertEquals(CeeqlCsv.generate(h.createQuery(sql).list()), "\"id\",\"name\",\"price\",\"vendor_id\"\n\"1\",\"first\",\"100.0000\",\"1\"\n\"2\",\"second\",\"200.0000\",\"2\"\n\"3\",\"third\",\"300.0000\",\"3\"\n");
    }

    @Test
    public void can_create_csv_output_for_first() throws Exception {
        Ceeql p = DbCreator.create();

        String sql = "SELECT * FROM products";
        Map<String, String> args = new HashMap<>();
        Handle h = p.getDbiHandle();
        h.createQuery(sql);

        assertEquals(CeeqlCsv.generate(h.createQuery(sql).first()), "\"id\",\"name\",\"price\",\"vendor_id\"\n\"1\",\"first\",\"100.0000\",\"1\"\n");
    }

    @Test
    public void can_call_all_from_select() throws Exception {
        Ceeql p = DbCreator.create();

        String sql = "SELECT * FROM products";
        Map<String, String> args = new HashMap<>();
        String all = new CeeqlSelect(p.getDbiHandle(), sql, args).all(CeeqlOutputType.CSV);

        assertEquals(all, "\"id\",\"name\",\"price\",\"vendor_id\"\n\"1\",\"first\",\"100.0000\",\"1\"\n\"2\",\"second\",\"200.0000\",\"2\"\n\"3\",\"third\",\"300.0000\",\"3\"\n");
    }

    @Test
    public void can_call_first_from_select() throws Exception {
        Ceeql p = DbCreator.create();

        String sql = "SELECT * FROM products";
        Map<String, String> args = new HashMap<>();
        String first = new CeeqlSelect(p.getDbiHandle(), sql, args).first(CeeqlOutputType.CSV);

        assertEquals(first, "\"id\",\"name\",\"price\",\"vendor_id\"\n\"1\",\"first\",\"100.0000\",\"1\"\n");
    }

    @Test
    public void can_call_all_from_ceeql_select_all_csv() throws Exception {
        Ceeql p = DbCreator.create();

        String sql = "SELECT * FROM products";
        Map<String, String> args = new HashMap<>();
        String all = p.selectCsv(sql, args);

        assertEquals(all, "\"id\",\"name\",\"price\",\"vendor_id\"\n\"1\",\"first\",\"100.0000\",\"1\"\n\"2\",\"second\",\"200.0000\",\"2\"\n\"3\",\"third\",\"300.0000\",\"3\"\n");
    }

    @Test
    public void can_call_first_from_select_one_csv() throws Exception {
        Ceeql p = DbCreator.create();

        String sql = "SELECT * FROM products";
        Map<String, String> args = new HashMap<>();
        String first = p.selectOneCsv(sql, args);

        assertEquals(first, "\"id\",\"name\",\"price\",\"vendor_id\"\n\"1\",\"first\",\"100.0000\",\"1\"\n");
    }
}
