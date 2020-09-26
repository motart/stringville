# Assumptions
1) For flexibility, allow Stringville to set their own rules without a need to recompile.
1) the health monitor does not report directly the status of the app. i.e. if the app is down the health monitor is 
irresponsive. Should this assumption be altered, we'd need a new microservice to monitor the Springville app for us. 


# With more time
1) Add logging to file
1) Create a lightweight angular page for data display and rules config??
1) Resolve the </br> /n issue, currently works in postman (\n) but not in browser (</br>)
1) Handle error page instead of displaying white label page

# Dependencies
1) H2: Used an in-memory DB to spare the grader the need to do any additional installations
1) Guava: Used Charmatcher to verify is the submission is ascii or not
1) JPA: Used JPA for persistence

# How to see data in browser
http://localhost:8080/h2-console 
user = sa
password=password
<configurable through application.properties>

# How to test this app
1) Provided with the initial template were
    1) run.sh: This enables the tester to start the app from the command line using `$ ./run.sh`
    1) test.sh: This enables the tester to run test on an already up and running app, in localhost and on port 8080, from the command line using `$ ./test.sh`

``Note: Test cases can be edited and increased by editing test.sh``

