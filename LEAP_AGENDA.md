# Java DBMS Project LEAP - Agenda

**Prerequisite Knowledge:** Core Java + SQL  
**Technology Stack:** Java (JDK 17+), Spring Boot, Maven, REST API, JPA/Hibernate, MySQL, Thymeleaf, Postman

---

## Prerequisites
Students should have foundational knowledge and comfort with the following:
1. **Java (Core):** Familiarity with OOP concepts such as Classes, Objects, Constructors, Inheritance, Polymorphism, Interfaces, Methods, and basic Exception Handling.
2. **Java Collections (Basics):** Basic understanding of commonly used collections such as ArrayList, HashMap, and how to store and retrieve objects.
3. **SQL (Basics):** Understanding of relational databases, tables, Primary Keys, Foreign Keys, and basic SQL commands such as `CREATE`, `INSERT`, `SELECT`, `UPDATE`, and `DELETE`.
4. **Web Concepts (Awareness):** Basic understanding of how a web application works, including the concepts of a browser, client, server, request, and response.
5. **Programming Environment:** Students should be comfortable creating, compiling, running, and debugging basic Java programs using an IDE.

---

## 💻 System Setup (Must be completed before Day 1)
All students must have the following software installed and minimally configured on their personal or lab systems:
- **JDK 17+:** Java Development Kit. Java 17 LTS is recommended for a standardized lab environment.
- **Spring Boot:** Latest stable Spring Boot version compatible with the selected JDK.
- **Maven:** Build and dependency management tool for the Spring Boot project.
- **IntelliJ IDEA / VS Code / Eclipse:** IDE for developing and running Spring Boot applications.
- **MySQL 8+:** Relational database server for database integration and CRUD operations.
- **MySQL Workbench:** GUI tool for creating databases, tables, inserting records, and verifying database changes.
- **Postman:** Tool for creating, sending, and testing REST API requests such as `GET`, `POST`, `PUT`, and `DELETE`.

---

## Detailed 3-Day Agenda

### DAY 1 – Spring Boot Foundation & REST API

| Time | Section | Related Tasks | Expected Output |
|---|---|---|---|
| **8:40 am – 9:00 am** | 1. Introduction to the LEAP Project | Identify the project requirements, application workflow and tasks to be completed during the 3-day LEAP | Students identify the application requirements and development roadmap |
| **9:00 am – 9:30 am** | 2. Why Spring Boot? | Compare the project requirements with Spring Boot features and identify where Spring Boot will be used in the application | Students identify the role of Spring Boot in the project |
| **9:30 am – 10:00 am** | 3. Spring Boot Application & Maven Basics | Create the project structure, configure `pom.xml`, add required dependencies and execute the application | Project structure and dependencies configured |
| **10:00 am – 11:15 am** | 4. Create First Spring Boot Application | Create and run the Spring Boot application; identify project folders and verify successful execution | First Spring Boot application running successfully |
| *11:15 am – 11:35 am* | *Forenoon Break* | | |
| **11:35 am – 12:30 pm** | 5. Spring Boot Annotations & Controller | Create a Controller and implement GET and POST endpoints using `@RestController`, `@GetMapping` and `@PostMapping` | First controller and GET/POST endpoints created |
| **12:30 pm – 1:15 pm** | 6. REST API & Request–Response Flow | Develop GET and POST APIs, send requests and verify the returned responses | Basic REST APIs working successfully |
| *1:15 pm – 2:00 pm* | *Lunch Break* | | |
| **2:00 pm – 3:00 pm** | 7. Application Layers – Controller & Service | Create Controller and Service classes and connect the request flow from Controller → Service | Controller–Service request flow implemented |
| **3:00 pm – 4:10 pm** | 8. API Testing with Postman | Execute GET/POST requests in Postman, verify request parameters and responses, and correct API errors | APIs tested and validated successfully |

---

### DAY 2 – Database Integration, JPA & CRUD

| Time | Section | Related Tasks | Expected Output |
|---|---|---|---|
| **8:40 am – 9:10 am** | 9. JDBC vs JPA – Why ORM? | Compare a database operation using JDBC and JPA, identify the additional coding required with JDBC, and determine how JPA simplifies database interaction | Students understand the purpose and practical advantage of JPA/ORM |
| **9:10 am – 10:00 am** | 10. Database Configuration | Configure MySQL connection in Spring Boot, add the required database and JPA dependencies, configure `application.properties`, and verify the database connection | Spring Boot application successfully connected to MySQL |
| **10:00 am – 11:15 am** | 11. JPA Entity & Repository | Create an Entity class, map Java fields to database columns, configure the primary key, create a `JpaRepository`, and verify that the corresponding database table is created | Java Entity successfully mapped to a database table |
| *11:15 am – 11:35 am* | *Forenoon Break* | | |
| **11:35 am – 12:30 pm** | 12. CREATE Operation | Create the Controller, Service and Repository flow; implement a POST API; accept request data and save a new Entity record into MySQL | New records successfully inserted into the database |
| **12:30 pm – 1:15 pm** | 13. READ Operation | Implement GET APIs to retrieve all records and a specific record by ID; send requests and verify the returned database records | Database records successfully retrieved and displayed |
| *1:15 pm – 2:00 pm* | *Lunch Break* | | |
| **2:00 pm – 3:00 pm** | 14. UPDATE & DELETE Operations | Implement PUT and DELETE APIs; update existing records, delete selected records, test both operations, and verify the changes in MySQL | Records can be successfully updated and deleted |
| **3:00 pm – 4:10 pm** | 15. Complete CRUD Integration | Use Postman to verify the corresponding database changes | Complete CRUD application working |

---

### DAY 3 – UI, Validation, Testing & Finalization

| Time | Section | Related Tasks | Expected Output |
|---|---|---|---|
| **8:40 am – 9:10 am** | 16. Introduction to Web Interface | Set up the HTML, CSS & JavaScript interface, identify the required pages, and connect the UI request flow with the Spring Boot backend | Students understand the UI flow and backend communication |
| **9:10 am – 10:00 am** | 17. Create Web Pages & Forms | Create the required web pages and forms; implement fields for adding, viewing, editing and deleting application data | Basic web interface and forms created |
| **10:00 am – 11:15 am** | 18. UI–CRUD Integration | Connect the web forms and pages to the Spring Boot CRUD APIs; submit requests from the UI and display the returned data | CRUD operations accessible through the UI |
| *11:15 am – 11:35 am* | *Forenoon Break* | | |
| **11:35 am – 12:30 pm** | 19. Input Validation | Add validation rules for required fields and invalid input; test valid and invalid form submissions and display appropriate validation messages | Invalid input handled correctly |
| **12:30 pm – 1:15 pm** | 20. Exception Handling | Implement handling for common application, API and database errors; test failure scenarios and provide meaningful error responses/messages | Application errors handled gracefully |
| *1:15 pm – 2:00 pm* | *Lunch Break* | | |
| **2:00 pm – 3:00 pm** | 21. Application Testing & Debugging | Test the complete application workflow, including CRUD operations, form validation and error scenarios | Major issues identified and fixed |
| **3:00 pm – 4:10 pm** | 22. Project Finalization & Demonstration | Execute the complete application workflow, verify database operations, perform final testing, prepare the project for demonstration, and demonstrate the completed application | Complete project tested and ready for demonstration |

---

## 3-Day Learning Progression Summary

| Day | Focus | What You Learn | Final Achievement |
|---|---|---|---|
| **Day 1** | Spring Boot Foundation | Spring Boot, Maven, Controller, REST, Service, Postman | Build & test REST APIs |
| **Day 2** | DB Integration + CRUD | MySQL, JPA, Entity, Repository, CRUD | Build database-backed application |
| **Day 3** | UI + Validation + Testing | Thymeleaf, Forms, Validation, Exception Handling, Testing | Complete demonstrable application |
