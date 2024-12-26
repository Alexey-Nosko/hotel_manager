## Hotel manager(Spring)

Description:
The project is the development of a hotel management system including functionality for managers and users.
In addition, administrative functions are provided for hotel management and manager registration.

## Installation:
Step 1: Download the repository

## Launch:
1) Docker is required to run this project
2) Start the containers using the Docker Compose file
3) After a successful launch, you will be able to access the application at:
http://localhost:8091/hotel/start
4) In order to log in, you can use the following usernames and passwords
For the ADMIN role:
admin
admpass
For the role of manager:
manag
pas
For the client role:
al
123

## Application features:
1) As an admin you can CRUD the hotel and register a manager
2) As a manager you can view available and booked hotel rooms, cancel reservations, change room configurations.
3) As a customer you can rate a hotel, add a hotel to your bookmarks, view all hotels in your bookmarks, top up your balance, book a room
4) You can also view hotels by filter, view your profile and change your password regardless of your role

## Used technologies:
To implement this project I used the programming language java (Java 17) and Spring framework. Initially for tests I used h2 database,
but later I switched to a standalone database postgreSql. In my project I needed links between tables in the database and for this I used ORM system hibernate.
To control changes and code quality I have written unit and integration tests. 
I have micro service application located in different locations to be independent for this I used docker. For writing web pages I used html and thymeleaf.
To provide version control , reliability and consistency of data in the database I used Liquibase. I have a horizontal scaling plan in my application for this I used ngnx. 


