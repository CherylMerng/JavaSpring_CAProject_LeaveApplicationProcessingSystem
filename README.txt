# How to run this project locally

1. Install MySQL server (for the database)

2. Install MySQL client (for friendly user interface to view the database):
Windows: HeidiSQL 
Mac (Intel): MySQLWorkBench 
Mac (M1 core): Sequel Ace

3. Connect to MySQL server from the client

4. Install Maven (for building the app)

5. Install Java SDK (for running the app)

5. Install Spring Tool Suite or Visual Studio Code (IDE for easier view of the code)

6. Update the application properties file in this application folder by replacing
 **** with your database name 
spring.datasource.url=jdbc:mysql://localhost:3306/****?createDatabaseIfNotExist=true
spring.datasource.username= ****
spring.datasource.password= ****

*OPTIONAL: (FOR LEAVEAPPLICATION ONLY!) change server.port into your desired port

7. Run LeaveApplicationAPI, followed by LeaveApplication
Within the root directory of this application:
run the following command on the terminal:
mvn spring-boot:run

*Important: BOTH LeaveApplication & LeaveApplicationAPI needs to be running in order for the application to work properly

8. Head to localhost:8083 (or replace 8083 if you have previously changed server.port) on your browser

9. Below are the default login credentials (from LeaveWebApplication.java in LeaveApplication src/main):
	- Admin:
		> Username: 2531
		> Password: John123@
	- Manager:
		> Username: 2811
		> Password: Sally123@
	- Employee:
		> Username: 1835
		> Password: Tom123@@
	
		> Username: 5833
		> Password: Jerry123@

10. To test email function, please change staff email into your own email address.
