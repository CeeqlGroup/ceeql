package org.mrcsparker.ceeql;

import org.junit.Test;
import org.skife.jdbi.v2.util.StringMapper;

import static org.junit.Assert.assertEquals;

public class CeeqlGenericQueryTest {
    @Test
    public void can_run_generic_query() {
        Ceeql p = new Ceeql("org.h2.Driver", "jdbc:h2:mem:test", "username", "password");
        Boolean result = p.query("DROP TABLE IF EXISTS something; create table something (id int primary key, name varchar(100))");
        assertEquals(result, true);
        p.close();

    }

    @Test
    public void can_run_generic_query_with_insert() {
        Ceeql p = new Ceeql("org.h2.Driver", "jdbc:h2:mem:test", "username", "password");

        Boolean result = p.query("DROP TABLE IF EXISTS something; create table something (id int primary key, name varchar(100))");
        assertEquals(result, true);

        result = p.query("insert into something (id, name) values (1, 'Brian')");
        assertEquals(result, true);

        p.close();

    }

    @Test
    public void can_run_select_on_generic_query() {
        Ceeql p = new Ceeql("org.h2.Driver", "jdbc:h2:mem:test", "username", "password");

        Boolean result = p.query("DROP TABLE IF EXISTS something; create table something (id int primary key, name varchar(100))");
        assertEquals(result, true);

        result = p.query("insert into something (id, name) values (1, 'Brian')");
        assertEquals(result, true);

        String name = p.getDbiHandle().createQuery("select name from something").map(StringMapper.FIRST).first();
        assertEquals(name, "Brian");

        p.close();

    }
}
