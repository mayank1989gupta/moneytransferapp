#Money Transferr Application
    Sample monney transfer app between acccounts built using
    Spark Java Framework &, HSQL.
    
    Sample data / Initial scripts for DB is loaded from:
     "resources/hsqldb -> transfers.script"
     
    The application/rest services are availables on port: 8080,
    which is configured in the main class.

## Assumptions
    No Rest Versions
    Failed transfer cases are not handled
    Pagination not taken care for results

## Building the Application
    mvn clean package

## Running the application
    java -jar ./target/money-transfer-bet-accounts-1.0.0.jar

## Rest End Points

### Account Services / Rest
    POST /account -> Create a new account
    GET /accounts -> Fetch all registered accounts
    GET /account/:id -> Fetch account by ID
    GET /account/:id/transfers -> Fetch all transfers for the given ID

### Transfer Services/ Rest
    POST /transfer -> Create a new transfer
    GET /transfers -> Fetch all transfers
    GET /transfer/:id -> Fetch all transfer for given ID
    