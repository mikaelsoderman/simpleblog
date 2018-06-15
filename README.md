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
 
 User management is not implemented and needs to be implemented to 
 achieve features such as
 - functionality for adding and removing users
 - maintaining hierarchies of users
 - manage individual blogs
 
 ###### Standalone deployment
 
 The application can be executed either as a standalone pure Springboot
 application or as a war package for deployment inside a web application
 platform such as Tomcat.
 
 To run as a standalone application perform the following steps:
 
 ```
 mvn clean install
 cd backend/target
 java -jar simpleblog-0.0.1-SNAPSHOT.war
  ```
 After the Springboot application has started, launch a web browser and 
 open the url http://localhost:8080/
 
 ###### Deployment with web application platform
 
 
 
 ##### Frontend
 
 The frontend is implemented using Vue.js and Bootstrap (using the 
 bootstrap-vue library). The frontend is a single page application
 and server interaction are performed with http requests through
 using the library vue-resource.
 
 The login mechanism for the application is simple based upon sending
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
 
 The frontend can be built and delivered along with the backend application
 or be served in development mode as a standalone applcication. See
 below for details on serving the frontend.
 
 ``` bash
 # install dependencies
 npm install
 
 # serve with hot reload at localhost:8080
 npm run dev
 
 # build for production with minification
 npm run build
 
 # build for production and view the bundle analyzer report
 npm run build --report
 
 # run unit tests
 npm run unit
 
 # run e2e tests
 npm run e2e
 
 # run all tests
 npm test
 ```