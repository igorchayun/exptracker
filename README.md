##Expenses Tracker Web Application Test Task
Igor Chayun

Link to view task condition: http://prntscr.com/nngrp5

###Build instructions
To build executable .jar file type the following:

*mvn clean package*

To start App type the following:

*mvn spring-boot:run*

Go to url http://localhost:8080/

###Notes
* To login you can use:

		login: admin
		password: 1
		
* You can also register a new user with USER Role.
Usernames must be unique and 
Password cannot be empty

* After authentication you are taken to the main page with links to available sections depending on the user role 

* Documentation page for REST API http://localhost:8080/swagger-ui.html (available to all authenticated users)

* Section with user management http://localhost:8080/users (available only to users with MANAGER or ADMIN roles)
  * Users are searched by name.
  * In editing a user, a new password is set if the New password field is not empty  
  * Deleting users is implemented not by real deletion from the database, 
		but by setting the attribute active = false

* Expense records management section http://localhost:8080/expenses (available only to users with USER or ADMIN roles) 
  * You can search and filter expense records by using the Description and Comment Fields
  * The Start and End dates are also taken into account
  * If the Start and End dates are not specified - the expenses for the entire period are displayed
  * If the End date is not specified - expenses up to and including the current date are displayed
  * If no Start date is specified , charges are displayed starting from the date of the first expense entered
  * Total amount spent is calculated taking into account all filters
  * Average expenses per day are calculated only if there is no filter by Description or Comment Fields
by formula: Total amount spent/number of days per period
    * If no End date is specified , the period starts from the date of the first expense included
    * If the End date is not specified - the period to the current date inclusive

* Section with management of all expense records of all users http://localhost:8080/user-expenses (available only to users with ADMIN role)
  * You can edit any expense except the id and author fields