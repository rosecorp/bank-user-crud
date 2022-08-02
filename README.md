[![Build Status](https://app.travis-ci.com/rosecorp/bank-user-crud.svg?branch=main)](https://app.travis-ci.com/rosecorp/bank-user-crud)

# User Bank Application
User Bank Application allows performing CRUD operations on the User object.

# Application Description
## Summary 
Application can treat data as follow
1. As an object
2. Create a user
3. Update a user
4. Soft delete users
5. Filtered and sorted, in Hateo format with links to other pages as well

## Goals 
This application support simple API V1 for basic consumer but also has v2 API that returns data in paginated and sorted format.

For example: If there is a table with 1 million rows in our Database, and our client is a website, 
it is probably not a good solution to have the http response to our client to contain 1 million rows. 
Instead, our goal in this app is to give the client parts of the data and provide links to access the other data if required.
This is where hateoas comes into picture. In the HTTP Response, it provides links to the first, current, next and last pages of data.

## Non Goals
This applicaton is not show all the aspect of the optimisation of the code such us:
- caching
- idempotency solution - it is complex problem to sort out in distributed systems
- security concerns - this application is not showing solution for sorting out security problems, those problems can be sorted out in many ways depending on the usecase
- scalability -  this application is not showing how to sort out a problem of deploying services in distributed systems using Kubernetes


# Application Setup
## Java Files 
### User 
This is the User Entity class. Each User has an id(sequence generated), first name, last name, title, job title, DOB. 
### User Endpoint
The Controller holds the end points defined. There are 5 endpoints with different functions defined later in the document
### User Service
Service holds the business logic and calls the required repository methods to fetch data from the DB
### UserRepository
Repository class for the user entity
### UserModel and UserModelAssembler
2 Classes required for implementing pagination with hateoas
### UserRequestDto, UserResponseDto and UserDtoAssembler
3 Classes required for implementing ACL layer between HTTP layer and Model and Enyity layer

## Database Setup
### Database and Connectivity
For this application we are using an in memory H2 Database.
As this is a H2 DB no external installation needs to be done on the system running the application.
Once the application is running, it can be accessed using the web browser: http://localhost:8183/h2-ui
Username: sa
Enter the above username, without any password and click connect.
### DB migration flyway
Application has implemented flyway schema mechanism that allows controlling iterative changes of the schema in db.

# Application Execution
## Running the application
To run the application you need java 11 and maven installed.
Navigate to the root folder of the application and run
```
mvn spring-boot:run
```
Or you can use an IDE that support java applications such as IntelliJ and run the application on the IDE 
## Testing the application
To test the application, below are the endpoints that can be executed once the application is running.
You can directly try the Url on your web browser or use tools such as postman.


### Endpoint 1: User can be found using search criteria
Request Type: POST
Request Url: http://localhost:8183/api/v1/users
Description:  This url returns ALL the users as a List after applying filter conditions to first name and last name and id.
Search Request:
```
{
  "id": "ca24755d-91e5-4c56-82af-507bf2461754",
  "firstName": "John",
  "lastName": "Smith"
}
```

Response Format:
```
[{
    "id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
    "title": "MR",
    "firstName": "Jacob",
    "lastName": "Smith",
    "dateOfBirth": "1985-08-01",
    "jobTitle": "It Developer",
    "createdStamp": "2022-08-01T21:33:16.925Z"
}]
```


### Endpoint 2: Filtered and sorted but in page format(without hateoas and links to other pages)
Request Type: POST
Request Url: http://localhost:8183/api/v2/users?page=0&size=10&sortList=firstName&sortOrder=ASC
Description: This url returns the users of the specified page with the specified size, after applying filter conditions to first name and last name and sorting the data.

Search Request:
```
{
  "id": "ca24755d-91e5-4c56-82af-507bf2461754",
  "firstName": "John",
  "lastName": "Smith"
}
```
Response Format:
```
   {
   "content": [
      {
          "id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
          "title": "MR",
          "firstName": "Jacob",
          "lastName": "Smith",
          "dateOfBirth": "1985-08-01",
          "jobTitle": "It Developer",
          "createdStamp": "2022-08-01T21:33:16.925Z"
      },
      {
          "id": "3fa85f64-5717-4562-b3fc-2c963f66afa3",
          "title": "MISS",
          "firstName": "Barbara",
          "lastName": "Black",
          "dateOfBirth": "1995-08-01",
          "jobTitle": "Nurse",
          "createdStamp": "2021-08-01T21:33:16.925Z"
      }
   ],
   "pageable": {
      "sort": {
      "empty": false,
      "unsorted": false,
      "sorted": true
      },
      "offset": 0,
      "pageNumber": 0,
      "pageSize": 10,
      "paged": true,
      "unpaged": false
   },
   "last": true,
   "totalPages": 1,
   "totalElements": 4,
   "size": 10,
   "number": 0,
   "sort": {
      "empty": false,
      "unsorted": false,
      "sorted": true
   },
   "first": true,
   "numberOfElements": 4,
   "empty": false
   }
```


### Endpoint 3: Soft Delete Users marking them as deleted = true in DB
Request Type: DELETE
Request Url: http://localhost:8183/api/v1/user/3fa85f64-5717-4562-b3fc-2c963f66afa6
Description: This url returns the user id after deletion process
Response Format:
```
{
   "id": "3fa85f64-5717-4562-b3fc-2c963f66afa6"
}
```

### Endpoint 4: User can be created by the POST operation
Request Type: POST
Request Url: http://localhost:8183/api/v1/user
Response Body:
```
{
    "title": "MR",
    "firstName": "Jacob",
    "lastName": "Smith",
    "dateOfBirth": "1985-08-01",
    "jobTitle": "It Developer",
}
```

Description: This url returns the id object after post process
Response Format:
```
{
   "id": "3fa85f64-5717-4562-b3fc-2c963f66afa6"
}
```

### Endpoint 5: User can be updated by the PUT operation
Request Type: PUT
Request Url: http://localhost:8183/api/v1/user/3fa85f64-5717-4562-b3fc-2c963f66afa6
Response Body:
```
{
    "title": "MR",
    "firstName": "Jacob",
    "lastName": "Smith",
    "dateOfBirth": "1985-08-01",
    "jobTitle": "It Developer",
}
```

Description: This url returns the USER object after PUT process
Response Format:
```
{
    "title": "MR",
    "firstName": "Jacob",
    "lastName": "Smith",
    "dateOfBirth": "1985-08-01",
    "jobTitle": "It Developer",
}
```
