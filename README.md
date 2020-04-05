# SQL to MongoDB translator

* Supports only **SELECT** queries.
* Column list ->  *projection* of **find** command
* **OFFSET** -> **skip**
* **LIMIT** -> **limit**
* **WHERE** predicates -> **find** predicates 
* Multiple predicates must be joined by **AND**

### Example: 
`SELECT id, name FROM user WHERE age > 30 AND name <> 'John' LIMIT 10 OFFSET 5` will be translated into 
`db.user.find({age: {$gt: 30}, name: {$ne: 'John'}}, {id: 1, name: 1}).limit(10).skip(5)`

In order to test the translator, run **Cli.java** and enter your queries.

Also there is a dummy Mongo collection placed in **data.json**. Ensure that you have running **MongoDB** database on port **27017**, run **DbEmulator.java** and try 2 make some **SQL** queries to **MongoDB** collection
