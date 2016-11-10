# Ceeql

Ceeql is a small, simple Java library that maps SQL queries to JSON, XML, or CSV.

## Technologies used

* JDBC for database support
* JDBI for queries
* Handlebars for template language
* HikariDB for connection pooling
* JUnit for full test coverage

## Code Status

[![alt text](https://travis-ci.org/mrcsparker/ceeql.svg?branch=master "Build Status")](https://travis-ci.org/mrcsparker/ceeql)

## Install

### Maven

Add this to your `<repositories>` section:

```xml
<repository>
    <snapshots>
        <enabled>false</enabled>
    </snapshots>
    <id>bintray-mrcsparker-maven</id>
    <name>bintray</name>
    <url>http://dl.bintray.com/mrcsparker/maven</url>
</repository>
```

Add this to your `<dependencies>` section:

```xml
<dependency>
    <groupId>org.mrcsparker</groupId>
    <artifactId>ceeql</artifactId>
    <version>0.9.2</version>
</dependency>
```

## API

Ceeql has a VERY simple API.  It only outputs to JSON, XML, or CSV - all query results, exceptions, and errors returns back as JSON, XML, or CSV, which means that the library is very easy to plug into a REST application.

There are very few methods exposed via the API, and every method returns a JSON/XML/CSV string.  By default, all results are returned as JSON.

### Calling the API

```java

// Make a database connection, and open a connection pool
Ceeql p = new Ceeql(
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

Ceeql p = new Ceeql("org.h2.Driver", "jdbc:h2:mem:test", "username", "password");

String result = p.select(query, args);

p.close();

```

### SELECT one query

```java

String query = "SELECT id, name FROM products WHERE vendor_id = :vendorId";

Map<String, String> args = new HashMap<String, String>();
args.put("vendorId", "2");

Ceeql p = new Ceeql("org.h2.Driver", "jdbc:h2:mem:test", "username", "password");

String result = p.selectOne(query, args);

p.close();

```

### INSERT query

```java

String query = "INSERT INTO products (name, vendor_id) VALUES (:name, :vendorId)";

Map<String, String> args = new HashMap<String, String>();
args.put("vendorId", "2");
args.put("name", "Product name");

Ceeql p = new Ceeql("org.h2.Driver", "jdbc:h2:mem:test", "username", "password");

String result = p.insert(query, args);

p.close();

```

### UPDATE query

```java

String query = "UPDATE products SET name = :name WHERE vendor_id = :vendorId";

Map<String, String> args = new HashMap<String, String>();
args.put("vendorId", "2");
args.put("name", "New Name");

Ceeql p = new Ceeql("org.h2.Driver", "jdbc:h2:mem:test", "username", "password");

String result = p.update(query, args);

p.close();

```

### DELETE query

```java

String query = "DELETE FROM products WHERE vendor_id = :vendorId";

Map<String, String> args = new HashMap<String, String>();
args.put("vendorId", "2");

Ceeql p = new Ceeql("org.h2.Driver", "jdbc:h2:mem:test", "username", "password");

String result = p.delete(query, args);

p.close();

```

### AutoCloseable interface

```java

String query = "INSERT INTO products (name, vendor_id) VALUES (:name, :vendorId)";

Map<String, String> args = new HashMap<String, String>();
args.put("vendorId", "2");
args.put("name", "Product name");

try (Ceeql p = new Ceeql("org.h2.Driver", "jdbc:h2:mem:test", "username", "password")) {
    String result = p.insert(query, args);
} catch (Exception e) {
    e.printStackTrace();
}

```

### Batch queries

Batch queries are either Parameterized or Dynamic. 
Parameterized batch templates must result in the same sql for all iterations in the batch collection. 
Dynamic batch templates can't use any parameters and should result in a unique sql statement for every iteration of the batch.
It is therefore not recommended to use Dynamic batches without proper security measures.  

```java
StringBuilder s = new StringBuilder();
    s.append("{{#each batch}}\n");
    s.append("  INSERT INTO products(\n");
    s.append("    name, price, vendor_id\n");
    s.append("  ) VALUES (\n");
    s.append("    :name, :price, :vendor_id\n");
    s.append("  );\n");
    s.append("{{/each}}\n");
String sql = s.toString();

HashMap<String, String> map = new HashMap<>();
map.put("batch", buildJson());

c.batch(sql, map);
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
        {{s name}}, {{s price}}, {{s vendor_id}}
    )
{{/each}}
```

### Helpers

[Handlebars.java](https://jknack.github.io/handlebars.java/helpers.html) for an overview of helpers.

Variable helper:
```sql
  {{name context? [argument]* [hash]*}}
```

Section helper:
```sql
  {{#name context? [argument]* [hash]*}}  
  ...  
  {{/name}}
```

Subexpression helper:
```sql
  {{... (name context? [argument]* [hash]*)}}
```

#### Built-in helpers:

* concat
```sql
  {{concat context? [argument]* [separator=""]}}
```
  Joins context and arguments with separator. All lists are automatically transformed into a CSV string.
* each
```sql
  {{#each context?}}
```
* eq
```sql
  {{eq context? [argument]*}}
```
  Returns the first argument that equals the context or empty otherwise.
* identifier
```sql
  {{identifier context?}}
```
  Only accepts a resulting CSV input of 1-3 level dotted ``[a-zA-Z_]\w*`` optionally quoted using ' or ". 
* if (alias: t)
```sql
  {{if context? [then] [else]}}
```
* join 
```sql
  {{join context? [argument]* separator [prefix=""] [suffix=""]}}
```
* number
```sql
  {{number context?}}
```
  Only accepts a resulting CSV input of integers.
* parameter (alias: s)
```sql
  {{parameter context?}}
```
* safe
```sql
  {{safe context?}}
  Deprecated: see parameter.
```
* unless (alias: f)
```sql
  {{unless context? [then] [else]}}
```
* with
```sql
  {{#with context?}}
```

## Security

Care should be taken to use parameters whenever possible. 
The safe helper should no longer be used. 
Filtering or switched values should be used for templates manipulating syntactic sql tokens.
Service level authorization should be used when possible (out of scope).

## Idioms

* Default value
```sql
  {{f value 1}}
```
* Limit input
```sql
  {{t value "one" "two" "thee"}}
```
* Identifiers  
  Note: Although the following will prevent sql injection, it must also be properly service authorized.
```sql
  select {{identifier columns}} from {{identifier table}}
```
* Numbers  
  Note: Although the following will prevent sql injection, it must also be properly service authorized.
```sql
  select column from table where id in ({{number ids}})
```


## Building

`./gradlew build`

## Testing

`./gradlew test`

__run a single test__:

`./gradlew -Dtest.single=CeeqlCsvTest test`

## Patches

... are welcome!

## License

Ceeql is licensed under the GNU LGPL 3.  This means that you can use the library in
your proprietary applications.  Any changes you make to Ceeql need to be shared with
the rest of the community.
