package stepDefinitions;


import io.cucumber.java.PendingException;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;


public class Steps {
    //private static final String USER_ID = "5cf4bc25-6c52-450c-ac9b-b9218812fbc3";
    private static final String BASE_URL = "https://bookstore.toolsqa.com";

    private static String USER_ID;
    private static String USERNAME;
    private static String PASSWORD;
    private static String token;
    private static Response response;
    private static String jsonString;
    private static String bookId;
    private Object objList;


    @Given("I am an authorized user for {string} and {string}")
    public void iAmAnAuthorizedUserForAnd(String User_Nm, String User_Pwd) throws PendingException {

        //Given("^I am an authorized user for \"([^\"]*)\" and \"([^\"]*)\"$", (String User_Nm, String User_Pwd) -> {
        //public void iAmAnAuthorizedUser(String User_Nm, String User_Pwd) throws PendingException{

        final SimpleDateFormat simpleDtFmt = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
        //method 1
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        //System.out.println(timestamp);

        //method 2 - via Date
        //Date date = new Date();
        //System.out.println(new Timestamp(date.getTime()));

        //return number of milliseconds since January 1, 1970, 00:00:00 GMT
        //System.out.println(timestamp.getTime());

        //format timestamp
        //System.out.println(simpleDtFmt.format(timestamp));

        RestAssured.baseURI = BASE_URL;
        RequestSpecification request = RestAssured.given();

        USERNAME = User_Nm + timestamp.getTime();
        System.out.println("Username: " + USERNAME);
        PASSWORD = User_Pwd;
        System.out.println("Password: " + PASSWORD);

        /*
            The POST API to create a new user with the help of login credentials provided
            i.e. 'username' and 'password'
        */
        try {
            request.header("Content-Type", "application/json");
            response = request.body("{ \"userName\": \"" + USERNAME + "\", \"password\": \"" + PASSWORD + "\"}").post("/Account/v1/User");
            System.out.println("\nI am newly registered user| Response Status: " + response.getStatusLine());
            jsonString = response.getBody().asString();
            System.out.println("Response:\n" + jsonString);


            String[] sResponseArray;
            sResponseArray = jsonString.split("\"");
            USER_ID = sResponseArray[3];
            System.out.println("I am an authorized user| User-ID: " + USER_ID);

        /*
            The POST API to generate a token for newly registered user on BookStore APP with the help of login credentials provided
            i.e. 'username' and 'password' to make it authorised
        */
            request.header("Content-Type", "application/json");
            response = request.body("{ \"userName\":\"" + USERNAME + "\", \"password\":\"" + PASSWORD + "\"}").post("/Account/v1/GenerateToken");

            // Reader header of a give name:- Header named Content-Type
            String contentType = response.header("Content-Type");
            System.out.println("Content-Type value: " + contentType);
            Assert.assertEquals(contentType /* actual value */, "application/json; charset=utf-8" /* expected value */);

            // Reader header of a give name:- Header named Server
            String serverType = response.header("Server");
            System.out.println("Server value: " + serverType);
            //Assert.assertEquals(serverType /* actual value */, "nginx/1.12.1" /* expected value */);

            // Reader header of a give name:- Header named Content-Encoding
            String acceptLanguage = response.header("Content-Encoding");
            System.out.println("Content-Encoding: " + acceptLanguage);
            //Assert.assertEquals(contentEncoding /* actual value */, "gzip" /* expected value */);

            int iStatusCode = response.getStatusCode();
            String sStatusLine = response.getStatusLine();
            System.out.println("Status Code : " + iStatusCode);
            System.out.println("Status Line : " + sStatusLine);
            //Assert.assertEquals(sStatusLine /*actual value*/, "HTTP/1.1 200 OK" /*expected value*/, "Valid status code returned !!");


            String jsonString = response.getBody().asString();
            System.out.println("Response:\n" + response.getBody().asString());


            /*
            JsonPath jsonPath = new JsonPath(jsonString);
            System.out.println("Code Breaks 1!!");
            // token = JsonPath.from(jsonString).get("token");
            JsonPath jsonPathEvaluator = response.jsonPath();
            System.out.println("Code Breaks 2!!");

            // Exception: java.lang.NoClassDefFoundError: org/apache/groovy/io/StringBuilderWriter
            //String sToken = (String) jsonPathEvaluator.get("$");
            String sToken = response.jsonPath().getString("$");
            System.out.println("Code Breaks 3!!");
            */

            // String[] sResponseArray;
            sResponseArray = jsonString.split("\":\"");
            token = sResponseArray[1];
            System.out.println("I am an authorized user| Generated Token: " + token);

        } catch (PendingException pe) {
            System.out.println("Pending Exception| Exception raised: " + pe.getMessage());
        } catch (Exception e) {
            System.out.println("Exception| Exception raised: " + e.getMessage());
        }

    }


    @Given("A list of books are available")
    public void listOfBooksAreAvailable() {
        RestAssured.baseURI = BASE_URL;
        RequestSpecification request = RestAssured.given();
        response = request.get("/BookStore/v1/Books");

        System.out.println("\n\n\nA list of books are available| Response Status: " + response.getStatusLine());

        jsonString = response.asString();
        System.out.println("Response:\n" + jsonString);
        /* Code Not Working
        List<Map<String, String>> books = JsonPath.from(jsonString).get("books");
        Assert.assertTrue(books.size() > 0);

        bookId = books.get(0).get("isbn");
        */
        String[] sResponseArray = jsonString.split("\"");
        bookId = sResponseArray[5];
        System.out.println("A list of books are available| ISBN of 1st Record: " + bookId);


    }

    @When("I add a book to my reading list")
    public void addBookInList() {
        RestAssured.baseURI = BASE_URL;
        RequestSpecification request = RestAssured.given();

        request.header("Content-Type", "application/json");
        response = request.body("{ \"userName\":\"" + USERNAME + "\", \"password\":\"" + PASSWORD + "\"}").post("/Account/v1/GenerateToken");
        jsonString = response.asString();
        System.out.println("Response:\n" + jsonString);

        System.out.println("User_ID: " + USER_ID);
        request.header("Authorization", "Bearer " + token).header("Content-Type", "application/json");
        response = request.body("{ \"userId\": \"" + USER_ID + "\", " + "\"collectionOfIsbns\": [ { \"isbn\": \"" + bookId + "\" } ]}").post("/BookStore/v1/Books");
        jsonString = response.asString();
        System.out.println("Response:\n" + jsonString);

        System.out.println("\n\n\nI add a book to my reading list| Response Status: " + response.getStatusLine());
    }


    @Then("The book is added to my reading list")
    public void bookIsAdded() {

        //Assert.assertEquals(201, response.getStatusCode());
        //String bookId = "9781449331818";
        RestAssured.baseURI = BASE_URL;
        RequestSpecification request = RestAssured.given();
        response = request.get("/BookStore/v1/Book?ISBN=" + bookId);

        System.out.println("\n\n\nThe book is added| Response Status: " + response.getStatusLine());

        jsonString = response.asString();
        System.out.println("Response:\n" + jsonString);

        Assert.assertEquals(200, response.getStatusCode());
        // throw new io.cucumber.java.PendingException();
    }

    /*
    @Then("the book is added")
    public void the_book_is_added() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }
    */


    @When("I remove a book from my reading list")
    public void removeBookFromList() {

        RestAssured.baseURI = BASE_URL;
        RequestSpecification request = RestAssured.given();

        /*
        request.header("Content-Type", "application/json");
        response = request.body("{ \"userName\": \"" + USERNAME + "\", \"password\": \"" + PASSWORD + "\"}").post("/Account/v1/User");
        jsonString = response.asString();
        System.out.println("Response:\n" + jsonString);
        */

        System.out.println("User ID: " + USER_ID);
        System.out.println("Book ID: " + bookId);
        request.header("Authorization", "Bearer " + token).header("Content-Type", "application/json");
        response = request.body("{ \"isbn\": \"" + bookId + "\", \"userId\": \"" + USER_ID + "\"}").delete("/BookStore/v1/Book");
        jsonString = response.asString();
        System.out.println("Response:\n" + jsonString);


        /*
        curl -X POST "https://bookstore.toolsqa.com/Account/v1/User" -H "accept: application/json" -H "Content-Type: application/json" -d "{ \"userName\": \"shailesh_naveen\", \"password\": \"Shall#123\"}"
         */
    }


    @Then("The book is removed")
    public void bookIsRemoved() {
        //Assert.assertEquals(204, response.getStatusCode());

        RestAssured.baseURI = BASE_URL;
        RequestSpecification request = RestAssured.given();

        request.header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json");

        response = request.delete("/BookStore/v1/Book");
        Assert.assertEquals(200, response.getStatusCode());

        jsonString = response.asString();
        List<Map<String, String>> booksOfUser = JsonPath.from(jsonString).get("books");
        System.out.println("The book is removed| Response Status: " + response.getStatusLine());
        Assert.assertEquals(0, booksOfUser.size());
    }

    /*
    @Then("the book is removed")
    public void the_book_is_removed() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }
    */

}