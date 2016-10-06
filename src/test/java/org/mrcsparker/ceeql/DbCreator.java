package org.mrcsparker.ceeql;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.mrcsparker.ceeql.model.Product;

public class DbCreator {

    private final static Logger log = LogManager.getLogger(DbCreator.class);

    public static Ceeql create(boolean insertData) {
        Ceeql p = new Ceeql("org.h2.Driver", "jdbc:h2:mem:test", "username", "password");

        p.query("DROP TABLE IF EXISTS products");
        p.query(Product.schema());

        if (insertData) {
            for (Product product : Product.initialList()) {
                p.query("INSERT INTO products (name, price, vendor_id) VALUES (?, ?, ?)",
                        product.name, product.price, product.vendorId);
            }
        }

        return p;
    }

    public static Ceeql create() {
        return create(true);
    }
}
