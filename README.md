# Ceeql

Ceeql is a small, simple Java library that maps SQL queries to JSON.

## Technologies used

* JDBC for database support
* JDBI for queries
* Handlebars for template language
* HikariDB for connection pooling
* JUnit for full test coverage

## Code Status

[![Build Status](https://travis-ci.org/mrcsparker/ceeql.svg?branch=master)](https://travis-ci.org/mrcsparker/ceeql)

## API

Ceeql has a VERY simple API.  It only outputs to JSON - all query results, exceptions, and errors returns back as JSON, which means that the library is very easy to plug into a REST application.

There are very few methods exposed via the API, and every method returns a JSON string.

### Calling the API

```java

// Make a database connection, and open a connection pool
Seeql p = new Seeql(
    // jdbc driver
    "org.h2.Driver",
    // jdbc url		
    "jdbc:h2:mem:test",		
    "username", 
    "password");

// Create a argument list that you want to pass to the query
Map<String, String> args = new HashMap<String, String>();
args.put("name", "foo");

// Write the query.  Colons signify arguments
String query = "SELECT * FROM products WHERE name = :name"

// Call the query.  The query returns as a JSON string
String result = p.select(query, args);

// Close the connection pool
p.close();

// If you want to re-open the connection:
p.reconnect();

```

### SELECT all query

```java

String query = "SELECT id, name FROM products WHERE vendor_id = :vendorId";

Map<String, String> args = new HashMap<String, String>();
args.put("vendorId", "2");

Seeql p = new Seeql("org.h2.Driver", "jdbc:h2:mem:test", "username", "password");

String result = p.select(query, args);

p.close();

```

### SELECT one query

```java

String query = "SELECT id, name FROM products WHERE vendor_id = :vendorId";

Map<String, String> args = new HashMap<String, String>();
args.put("vendorId", "2");

Seeql p = new Seeql("org.h2.Driver", "jdbc:h2:mem:test", "username", "password");

String result = p.selectOne(query, args);

p.close();

```

### INSERT query

```java

String query = "INSERT INTO products (name, vendor_id) VALUES (:name, :vendorId)";

Map<String, String> args = new HashMap<String, String>();
args.put("vendorId", "2");
args.put("name", "Product name");

Seeql p = new Seeql("org.h2.Driver", "jdbc:h2:mem:test", "username", "password");

String result = p.insert(query, args);

p.close();

```

### UPDATE query

```java

String query = "UPDATE products SET name = :name WHERE vendor_id = :vendorId";

Map<String, String> args = new HashMap<String, String>();
args.put("vendorId", "2");
args.put("name", "New Name");

Seeql p = new Seeql("org.h2.Driver", "jdbc:h2:mem:test", "username", "password");

String result = p.update(query, args);

p.close();

```

### DELETE query

```java

String query = "DELETE FROM products WHERE vendor_id = :vendorId";

Map<String, String> args = new HashMap<String, String>();
args.put("vendorId", "2");

Seeql p = new Seeql("org.h2.Driver", "jdbc:h2:mem:test", "username", "password");

String result = p.delete(query, args);

p.close();

```

## Template language

Ceeql uses Handlebars templates:

```sql

SELECT * FROM products
WHERE name = :name
{{#if vendor_id}}
    AND vendor_id = :vendor_id
{{/if}}

```

For example, you can loop over data:

```json
items = [
    {
        "name": "product one",
        "price": 100.00,
        "vendor_id": 12
    },
    {
        "name": "product two",
        "price": 200,
        "vendor_id": 42
    }
]
```

```sql
#{{each items}}
    INSERT INTO table (
        name, price, vendor_id
    ) VALUES (
        '{{safe name}}', {{safe price}}, {{safe vendor_id}} 
    )
{{/each}}
```

## License

Ceeql is licensed under the GNU LGPL 3.  This means that you can use the library in
your proprietary applications.  Any changes you make to Ceeql need to be shared with
the rest of the community.

