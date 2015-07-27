# ceeql

Ceeql is a Java library that maps SQL queries to JSON

[![Build Status](https://travis-ci.org/mrcsparker/ceeql.svg?branch=master)](https://travis-ci.org/mrcsparker/ceeql)


## SELECT all query

```java

String query = "SELECT id, name FROM products WHERE vendor_id = :vendorId";

Map<String, String> args = new HashMap<String, String>();
args.put("vendorId", "2");

Seeql p = new Seeql("org.h2.Driver", "jdbc:h2:mem:test", "username", "password");

String result = p.select(query, args);

p.close();

```

## SELECT one query

```java

String query = "SELECT id, name FROM products WHERE vendor_id = :vendorId";

Map<String, String> args = new HashMap<String, String>();
args.put("vendorId", "2");

Seeql p = new Seeql("org.h2.Driver", "jdbc:h2:mem:test", "username", "password");

String result = p.selectOne(query, args);

p.close();

```

## INSERT query

```java

String query = "INSERT INTO products (name, vendor_id) VALUES (:name, :vendorId)";

Map<String, String> args = new HashMap<String, String>();
args.put("vendorId", "2");
args.put("name", "Product name");

Seeql p = new Seeql("org.h2.Driver", "jdbc:h2:mem:test", "username", "password");

String result = p.insert(query, args);

p.close();

```

## UPDATE query

```java

String query = "UPDATE products SET name = :name WHERE vendor_id = :vendorId";

Map<String, String> args = new HashMap<String, String>();
args.put("vendorId", "2");
args.put("name", "New Name");

Seeql p = new Seeql("org.h2.Driver", "jdbc:h2:mem:test", "username", "password");

String result = p.update(query, args);

p.close();

```

## DELETE query

```java

String query = "DELETE FROM products WHERE vendor_id = :vendorId";

Map<String, String> args = new HashMap<String, String>();
args.put("vendorId", "2");

Seeql p = new Seeql("org.h2.Driver", "jdbc:h2:mem:test", "username", "password");

String result = p.delete(query, args);

p.close();

```

## Template language

Ceeql uses Handlebars templates for templating.

```sql

SELECT * FROM products
WHERE name = :name
{{#if vendor_id}}
    AND vendor_id = :vendor_id
{{/if}}

```

## License

Ceeql is licensed under the GNU LGPL 3.  This means that you can use the library in
your proprietary applications.  Any changes you make to Ceeql need to be shared with
the rest of the community.

