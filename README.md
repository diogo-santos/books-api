# books-write-api
Rest service for writing books domain with pagination from MySql

## Getting the code on your computer
- [ ] Java 8
- [ ] Maven 3+
- [ ] MySql
- [ ] Import and install common library from https://github.com/diogo-santos/books-common
- [ ] Import the project from GitHub https://github.com/diogo-santos/books-write-api

Set MySql database properties - application.properties
```
spring.datasource.url=jdbc:mysql://localhost:3306/books
spring.datasource.username=root
spring.datasource.password=mysqlpasswd
```

Run the app
```
cd books-write-api
mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=5001"
```

Execute tests
```
mvn clean test
```

## Test the App
Check API documentation at http://localhost:5001
