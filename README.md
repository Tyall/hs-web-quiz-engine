# Web Quiz Engine
![](https://img.shields.io/badge/Java-Programming-green)
## Description
An educational project focused on polishing skills of creating RESTful APIs with Spring Boot.
It utilizes core aspects of Spring Boot's Web, Data and Security modules in a simple application of Web Quiz Engine.
The application servers as a platform to add, view and solve simple quizzes. Users can add quizzes themselves or browse available ones added by other users and try to solve them.
Quizzes have to pass validation rules in order to get added to the database.
Each user has his own profile with created and completed quizzes assigned to them. Thanks to authentication mechanism users can't interfere with data of other users. 

## Technologies
* Java 17
* Spring Boot
* JUnit 5
* Gradle
## Concepts Implemented

This project utilizes concepts of:
* REST API
* Spring Security
* Spring Data
* Relational databases
* Validation
* Authentication
* Pagination
* Unit testing

## Installation

**1. Clone the repository:**

```
$ git clone https://github.com/Tyall/hs-web-quiz-engine.git
$ cd hs-blockchain
```
**2. Compile the code**

Use chosen compiler or built in IDE to compile the source code.

**3. Run the simulation:**

```
$ java src/blockchain/Main.java
```
## Usage

#### To perform any actions users have to register themselves

Request:
```
POST /api/register
```
Example body:
```
{
  "email": "test@mail.org",
  "password": "123"
}
```
### After successful registartion, the users can perform following actions using their credentials to authenticate requests

Request:
```
GET /api/quizzes?page={page_nr}
```
Returns a paginated collection of all available quizzes


#### Get paginated collection of all solved quizzes for requesting user

Request:
```
GET /api/quizzes/completed?page={page_nr}
```


#### Add new quiz

Request:
```
POST /api/quizzes
```
Example body:
```
{
  "title": "<string, not null, not empty>",
  "text": "<string, <not null, not empty>",
  "options": ["<string 1>","<string 2>","<string 3>", ...],
  "answer": [<integer>,<integer>, ...]
}
```

#### Get single quiz by id

Request:
```
GET /api/quizzes/{id}
```


#### Solve quiz by given id

Request:
```
POST /api/quizzes/{id}/solve
```
Multiple answers can be provided if quiz is of a multi-choice type

Example body:
```
{
  "answer": [<integer>, <integer>, ...]
}
```

#### Delete quiz by given id

Request:
```
DELETE /api/quizzes/{id}
```
User has to be the creator of the quiz that is requested to be deleted


## Project status

ToDo:
* Improve code coverage with tests by adding unit tests to test controllers, custom repositories and services
* API documentation
