package org.mrcsparker.ceeql.model;

import java.util.ArrayList;
import java.util.List;

public class Product {
    public String name;
    public double price;
    public int vendorId;

    public Product(String name, double price, int vendorId) {
        this.name = name;
        this.price = price;
        this.vendorId = vendorId;
    }

    public static String schema() {
        return "CREATE TABLE products (" +
                "id int(11) NOT NULL AUTO_INCREMENT, " +
                "name varchar(20) NOT NULL DEFAULT '', " +
                "vendor_id int(11) NOT NULL, " +
                "price decimal(10,4) unsigned NOT NULL DEFAULT '0.0000', " +
                "PRIMARY KEY (`id`) " +
                ")";
    }

    public static List<Product> initialList() {
        ArrayList<Product> products = new ArrayList<>();

        products.add(new Product("first", 100.00, 1));
        products.add(new Product("second", 200.00, 2));
        products.add(new Product("third", 300.00, 3));

        return products;
    }

    public static String toJson(List<Product> products) {

        StringBuilder json = new StringBuilder();

        json.append("[\n");

        Product lastProduct = products.get(products.size() - 1);

        for (Product product : products) {
            json
                    .append("  {\n")
                    .append("    \"name\": \"" + product.name + "\",")
                    .append("    \"price\": " + product.price + ",")
                    .append("    \"vendor_id\": " + product.vendorId);
            if (product.equals(lastProduct)) {
                json.append("  }\n");
            } else {
                json.append("  },\n");
            }
        }

        json.append("]\n");

        return json.toString();
    }
}
