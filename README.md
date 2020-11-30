# books-api
Rest service for Books domain with pagination from MySql

## Getting the code on your computer
- [ ] Java 8
- [ ] Maven 3+
- [ ] MySql
- [ ] Import and install common library from https://github.com/diogo-santos/books-common
- [ ] Import the project from GitHub https://github.com/diogo-santos/books-api

Set MySql database properties - application.properties
```
spring.datasource.url=jdbc:mysql://localhost:3306/books
spring.datasource.username=root
spring.datasource.password=mysqlpasswd
```

Run the app
```
cd books-api
mvn spring-boot:run
```

Execute tests
```
mvn clean test
```

## Test the App
Check API documentation at http://localhost:5000
