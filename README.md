ceeql

========

Maps SQL Queries to JSON

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
