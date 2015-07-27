package org.mrcsparker.ceeql;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;


class DbCreator {

    private final static Logger log = LogManager.getLogger(DbCreator.class);

    public static Ceeql create() {
        Ceeql p = new Ceeql("org.h2.Driver", "jdbc:h2:mem:test", "username", "password");

        p.query("DROP TABLE IF EXISTS products");

        String sql =
                "CREATE TABLE products (" +
                        "id int(11) NOT NULL AUTO_INCREMENT, " +
                        "name varchar(20) NOT NULL DEFAULT '', " +
                        "vendor_id int(11) NOT NULL, " +
                        "price decimal(10,4) unsigned NOT NULL DEFAULT '0.0000', " +
                        "PRIMARY KEY (`id`) " +
                        ")";


        log.info(sql);
        p.query(sql);

        p.query("INSERT INTO products (name, price, vendor_id) VALUES ('first', 100.00, 1)");
        p.query("INSERT INTO products (name, price, vendor_id) VALUES ('second', 200.00, 2)");
        p.query("INSERT INTO products (name, price, vendor_id) VALUES ('third', 300.00, 3)");

        return p;
    }
}
