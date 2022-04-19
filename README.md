# Stocks!
Simple stock API for fetching information related to stocks.

## What you will need:
- The code: `git clone https://github.com/sethjones348/stocks`
- The following software installed on your computer:
  - `Java 8` or higher
  - `Docker`
  - `Maven`
  - 
## Acquiring an API key for the Polygon Stock API
- Navigate to https://polygon.io/
- Create a free account
- Save your free API key 

## Build and Execution of Web App
*Once the code is downloaded on your computer*
- Open a terminal
- Navigate to the home directory of the project
### Using maven:
- Run the following: `export POLYGON_API_KEY=<INSERT_YOUR_API_KEY_HERE>`
- Verify the previous command by running `echo $POLYGON_API_KEY`
- Execute the command: `./mvnw clean package`
- Execute the command `java -jar target/stocks-0.0.1-SNAPSHOT.jar`
### Using Docker:
- Run the following: `docker build -t <INSERT_WHAT_YOU_WANT_TO_CALL_THIS_IMAGE> .`
- Run the following: `docker run -e POLYGON_API_KEY=<INSERT_API_KEY> -p 8080:8080 -t <THE_NAME_YOU_MADE_UP_IN_THE_PREVIOUS_COMMAND>`

## Usage
- Open a separate terminal
- Run the following: `curl localhost:8080/stocks/AAPL`
- Congrats you just ran your first test!
- Replace AAPL with any stock symbol you want information on and execute the command again.

## Endpoints
- GET /stocks/{symbol}
  - `symbol` is of type `String`
  - `symbol` should be a valid stock exchange symbol (e.g., AAPL, IBM..)

### Note
- If you try and execute the command more than 1 time in a minute span it will not work since we are using the free version of the API :(
