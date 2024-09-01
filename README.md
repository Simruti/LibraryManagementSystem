# Library Management System

## Overview

The Library Management System is a console-based Java application designed to manage books in a library. The system allows users to add new books, view book details, issue books to members, and return books. This application follows a Test-Driven Development (TDD) approach using JUnit 5 for testing and utilizes Java's JDBC API for database interactions.

## Features

- **Add Book:** Add a new book to the library if it doesn't already exist.
- **View All Books:** Display a list of all the books available in the library.
- **Issue Book:** Issue a book to a member if it is available.
- **Return Book:** Allow a member to return a borrowed book.

  ## Technologies Used

- **Programming Language:** Java
- **Database:** MySQL
- **Libraries:**
  - JUnit 5 for unit testing
  - Mockito for mocking dependencies during testing
- **IDE:** Intellij
- **Development Approach:** Test-Driven Development (TDD)

  ## Getting Started

### Prerequisites

To run this application, you will need:

- Java JDK 8 or above installed.
- MySQL installed and running.
- JDBC driver for MySQL configured in the classpath.
- A MySQL database with the following tables:
  - `book`
  - `book_issue`
  - member
 
**Database Setup:**

   To set up the database schema, use the SQL file provided in the `docs` folder.


**Configuration**
- Make sure to configure your database connection in the DbConnectivity class according to your local setup.

## Running the Application

Since this project does not have a main entry point for a console-based application, it is primarily intended for testing and development purposes. The main focus is on unit testing the `BookService` class. Here’s how you can interact with the project:

1. **Clone the Repository or Download the Project Files:**
   - Clone the repository using `git clone [repository-url]` or download the project files as a ZIP.

2. **Import the Project into Your IDE:**
   - Open your IDE (e.g., Intellij,NetBeans) and import the project.

3. **Update the `DbConnectivity` Class:**
   - Configure the `DbConnectivity` class with your database connection details.

4. **Run Tests:**
   - To validate the functionality of the `BookService` class and ensure everything is working as expected, run the unit tests using JUnit. The project uses JUnit 5 and Mockito for testing.

5. **Modify and Test:**
   - If you make changes to the code, you can modify the test cases or add new ones as needed. Re-run the tests to ensure that your changes are correct and do not introduce new issues.

