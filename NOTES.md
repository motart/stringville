# Assumptions
1) For flexibility, allow Stringville to set their own rules without a need to recompile.
1) the health monitor does not report directly the status of the app. i.e. if the app is down the health monitor is 
irresponsive. Should this assumption be altered, we'd need a new microservice to monitor the Springville app for us. 


# TO-DO
1) Sort results before submitting them
1) Document additional test cases
1) Anything I'd have done differently with more time


# With more time
1) Add logging to file
1) Create a lightweight angular page for data display and rules config??
1) Resolve the </br> /n issue
1) Handle error page instead of displaying white label page

# Done
1) Renamed most classes from Stringville to Submission for clarity

# Dependencies
1) Added H2, Guava, JPA, ...

# How to see data
http://localhost:8080/h2-console 
user = sa
password=password
<configurable through application.properties>



