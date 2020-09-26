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

# New Relic Coding Challenge

Thank you for taking the time to complete this coding challenge. Best of luck, and don't forget to have fun!

Before getting started, let's cover some basics:

1) Please work on this individually. It's OK to consult Javadocs or review online resources, it is NOT OK to ask other individuals for help.

2) We recommend that you spend at most 4 hours on this activity. Feel free to use as much time as you’d like on this exercise, but keep the 48 hour return timeline in mind. 

3) As part of our commitment to diversity, equity and inclusion, we always conduct blind code reviews during our hiring process. Please do not include your name or any other identifying information in your code or submission.

Once completed, please return your solution via email attachment and refrain from posting or discussing this exercise with others. Please do not post your solution or this coding challenge on the internet.

## Stringville Raffle Contest

The city of Stringville organizes one of the most bizarre raffles in the country. Stringville citizens enter the competition by writing down their favorite string on a piece of paper along with their name, and putting this piece of paper in a big jar.

Unlike traditional raffles, Stringville's raffle rules are different. Instead of a single winner, there are multiple winners. Winners are selected based on a formula that is unknown to participants. At the end of the raffle the top `N` tickets with the highest scores are the winners.

For this year, the Stringville Raffle committee has privately agreed on the following formula. A ticket will be given:

```
+1 point if the string contains exactly three a's

+5 points if the string contains exactly two e's

+2 points for every two i's in the string

+3 points if the string contains exactly one g

+1 point for every u in the string

-10 points for every z in the string
```

Note: All strings are case-insensitive and characters in the string do not need to be consecutive to get points. Participants can submit more than 1 string.

Here are a few example scores:

**Raffle Ticket:** aaaiibbbru,Ivaana Bello

  Three a's (+1), two i's (+2), one u (+1) = 4 points

**Raffle Ticket:** eiiegiebeici,Camille Diaz

  Two i's (+2), two i's (+2), one g (+3) = 7 points

**Raffle Ticket:** eiiegbczu,Bob Bloch

  Two e's (+5), two i's (+2), one g (+3), one z (-10), one u (+1) = 1 point

**Raffle Ticket:** aaaluckycharmz,Derek Lee

  One u (+1), one z (-10) = -9 point

**Raffle Ticket:** bananaram,Bonnie Chang

  0 points

In this example, Camille, Ivaana, and Bob would be the lucky winners.

## Stringville Raffle Service

As a Software Engineer selected by Stringville to help automate this process, your task is to use this starter project to do the following:

1) Build an endpoint at `/submission` that will accept `POST` requests 

   The body of each submission will be plain text, containing the citizen's string followed by a comma and then their name. The payload (`string,name`) must not exceed 10,000 characters (ASCII only). The string must be at least one character. The name must be present.

   Sample ticket: `abekdkbjbjeheimaaabb,James LaCroix`

   If the ticket doesn't conform to the submission rules, then the endpoint should return a 400 status code with a message body explaining the reason why it wasn't accepted. Otherwise the endpoint should return a 200 status code indicating that the submission was accepted.

2) Build an endpoint at `/results` that will accept `GET` requests. The endpoint will display the top 10 winners based on the scoring system above.

   The endpoint should display a list of the top 10 winners and their scores. Each winner should be on a new line with their name followed by their score (separated by a comma).

   Example:

```
Cami Diaz,9
Ivaana Bello,5
Bob Bloch,1
...
```

3) Build an endpoint at `/health` that will accept `GET` requests. This endpoint should help you monitor your system. This should display any necessary information to know if your system is working as expected, including any useful statistics to monitor the health of your system. At a minimum, it should report the number of valid and invalid submissions.

4) Build an endpoint at `/reset` that will clear any saved or cached data, reset counts, results, and health checks.

5) Write unit tests that would help you validate your system. Don't forget to document any test cases you would have covered if you had more time.

6) Make your solution as performant as possible. The Stringville committee is interested in processing as many requests as possible. Describe any tools or scripts you used to measure performance, and what you measured (like requests per second), and any changes you made to make your solution more performant, if any.

Note:
  * Please use any dependency or libraries that you like.
  * Your solution must run on port 8080 (unless documented in your instructions), and respond to `/submission`, `/results`, `/health`, and `/reset` as specified in 1-4.
  * Please include comments where you think it's necessary.
  * Feel free to provide a write-up of anything you would have done differently with more time, or any efforts related to performance testing.

## Provided

1) A spring-boot gradle project is already stubbed out with a working test.

2) There is a run script `run.sh` which can start the application and a test script `test.sh` which shows basic examples of what requests look like. Feel free to edit these or create new scripts for testing purposes.

## Our Evaluation

We care not only about your solution, but also your general development approach. In addition to evaluating your solution for correctness and performance, we also heavily weigh your code structure and style.
