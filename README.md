Blok - Joan's playground
========================


![Circle CI status](https://circleci.com/gh/jvalduvieco/blok/tree/master.svg?style=shield&circle-token=a16d67a1d04eb1b2429215265fe108bda2a866c1)

Simple blog application built using CQRS, spark, postgres. More to come

I started from:

* [Getting started with Spark: it is possible to create lightweight RESTful application also in Java](http://tomassetti.me/getting-started-with-spark-it-is-possible-to-create-lightweight-restful-application-also-in-java/)
* [Spark and Databases: Configuring Spark to work with Sql2o in a testable way](http://sparktutorials.weebly.com/tutorials/spark-and-databases-configuring-spark-to-work-with-sql2o-in-a-testable-way)

A lot of refactoring has been done since those examples.

Start the application
=====================

You need a PostgreSQL database. Run on it the code listed in the directory db.

Now run the application. You can specify where to find your database, for example:

```
mvn install
mvn exec:java -pl apps/Api -Dexec.args="--db-host myDBServer --db-port 5432"
```

To adjust log level:
```
-Dorg.slf4j.simpleLogger.defaultLogLevel=debug
```

Notes
=====

* http://maven.apache.org/guides/introduction/introduction-to-the-pom.html#Example_2
* http://saltnlight5.blogspot.com.es/2013/08/how-to-configure-slf4j-with-different.html
* https://books.sonatype.com/mvnex-book/reference/multimodule.html
* https://c0deattack.wordpress.com/2012/04/01/cucumber-jvm-example-continued-refactoring/
* https://github.com/cucumber/cucumber/wiki
