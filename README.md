# SimpleBlog

> A picture blog focused on easy sharing of images

## Architecture

 The application consists of two parts, a frontend and backend. Both
 are delivered through the same container. The project is SpringBoot
 based and can be leveraged through an embedded application environment
 or an external web application platform.
 
 ##### Backend
 
 The backend is exposed as a REST interface, implemented using Jersey. 
 Restricted methods in the REST interface are secured
 using the Basic Authentication mechanism.
 
 The Rest interface consists of two Rest controllers:
 - UserController - For managing logins and creation of new users
 - PostController - For CRUD operation on Post objects 
 
 All posts stored in the blog are searchable and this is achieved
 through the use of Hibernate Search and Lucene search indexes. The
 search functionality is leveraged through the Post Rest controller. The
 search is keyword based ad both title and body of the blog posts are
 included in the search. Stop words (english) are not included in the
 search. The fields to be included in the search index are annotated with
 `@Field(termVector = TermVector.YES)`
 
 Data is stored in an embedded H2 database by using JPA. Base classes
 are BlogUser and Post. The database not persisted between restarts
 and is re-initialised upon each restart of the application. By default
 a BlogUser is created and database is initialised with two blog posts.
 
 User management is not implemented and needs to be implemented to 
 achieve features such as
 - functionality for adding and removing users
 - maintaining hierarchies of users
 - manage individual blogs
 
 ###### Deployment with web application platform
 
 The application is expected to be deployed in a web application platform and is tested with
 Tomcat version 9.0.8. It can also be deployed as a standalone Spring boot application with
 embedded Tomcat, see below for instructions on how to do that.
 
 To build and deploy the application perform the command in the root directory of 
 the project:
 
```
mvn clean install
```
 
 Then copy the file backend/target/backend.war to the webapps directory in the Tomcat installation.
 
 Launch a web browser and open the following url: http://localhost:8080/backend
 
 The ability to add new users is not included in the current version of
 the application. So in order to log in use the following credentials:
 ```
Username: user@test.com
Password: 12345Qw
 ```
 
 
 ###### Standalone deployment
 
 To deploy the application as a standalone Springboot application a minor change is needed in the
 source code. Open the file frontend/config/index.js. Change row 53 from:
  
```
assetsPublicPath: '/backend',
```

to
  
```
assetsPublicPath: '/',
```

Then build the application by performing the following steps from the root directory of the
project:
 
 ```
 mvn clean install
 cd backend/target
 java -jar backend.war
  ```
 After the Springboot application has started, launch a web browser and open the 
 url http://localhost:8080/
 
  The ability to add new users is not included in the current version of
  the application. So in order to log in use the following credentials:
  ```
 Username: user@test.com
 Password: 12345Qw
  ```
 
 ##### Frontend
 
 The frontend is implemented using Vue.js and Bootstrap (using the 
 bootstrap-vue library). The frontend is a single page application
 and server interaction are performed with http requests through
 using the library vue-resource.
 
 The login mechanism for the application is simple and based upon sending
 user credentials (email and password) over http. Users can log in by
 using a form which will generate the http call to the server. When
 the user is logged in the application will utilise the vue.js local 
 storage to hold the user information and a basic authentication token. 
 This token will be used when coomunicating with the secured backend 
 Rest methods. Upon logout the information in the local storage is
 cleared.
 
 Posts are displayed one at a time with the most recent image being
 the first one. Navigation between the posts is done by using the
 arrow keys on the keyboard.
 
 When logged in there will be a form on the right hand side for 
 adding new posts. The form validates the input values. With one
 exception, there is no special validation in the form for asserting 
 that the supplied url is for an actual image. 
