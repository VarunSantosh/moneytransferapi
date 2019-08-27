
# MonetTransferAPI
A simple Rest API for money transfers between accounts.

##Tech Stack

* **Java 8** - programming language
* **SpringBoot** - framework used
* **H2** - in-memory database
* **Gradle** - build and packaging tool
* **JUnit5** - for unit testing and integration testing
* **jaCoCo** - for code coverage

## Building and Running

* Clone the git repo with command
```
git clone https://github.com/VarunSantosh/moneytransferapi.git
```
* Go to the project directory and build the app with command
```
gradle clean build
```
* Run the jar with command
```
java -jar build/libs/moneytransferapi-1.0.0.jar
```
###Alternatively
After cloning the repo
* Import the project as a gradle project in your IDE[IntelliJ recommended]
* Navigate to MoneyTransferApiApplication.java file
* Right click on the file and select run

Note: The app runs on port 8080

# Unit testing

After cloning the app

* Go to the project directory and run the test with command 

```
gradle clean test
```
 * We can check the code coverage report generated at location
 
 ```
build/reports/jacoco/test/html/index.html
```
open the file in browser of your choice

# Endpoints

```
- GET    /v1/ping - to check the health of the app
- GET    /v1/account/{id}/statement - get the account statement
- POST   /v1/account/{fromAccount}/transfer/{toAccount}/{amount} - transfer money between accounts
```

## Account Resources

## GET /v1/account/{id}/statement

##### Example

##### Request

```
GET  http://localhost:8080/v1/account/101/statement
```

##### Response
```
{
    "account_id": 101,
    "first_name": "VARUN",
    "last_name": "DAMANI",
    "balance": 4500.00,
    "transactions": [
        {
            "transaction_id": 107942404131866891,
            "transaction_amount": 500.00,
            "transaction_type": "Debit",
            "transaction_date": "2019-08-27T09:03:51.676+0000"
        }
    ]
}
```

## POST    /v1/account/{fromAccount}/transfer/{toAccount}/{amount}

##### Example

###### Request

```
POST  http://localhost:8080/account/101/transfer/102/500
```

###### Response

```
{
    "transaction_id": 107942404131866891,
    "from_account": 101,
    "to_account": 102,
    "amount": 500
}
```

# Things to Note
* Integration test is written in IntegrationTests.java file.
* At the start of the app we will already have 2 account with positive balance
```json
{
    "account_id": 101,
    "first_name": "VARUN",
    "last_name": "DAMANI",
    "balance": 5000.00,
    "transactions": []
}
```

```json
{
    "account_id": 102,
    "first_name": "SALMAN",
    "last_name": "KHAN",
    "balance": 50000.00,
    "transactions": []
}
```
  The above accounts could be used to play around.






