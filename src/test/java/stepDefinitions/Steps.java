package stepDefinitions;


import java.util.List;
import java.util.Map;

//import io.cucumber.core.internal.gherkin.deps.com.google.gson.JsonObject;
import org.junit.Assert;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;


public class Steps {
    private static final String USER_ID = "5cf4bc25-6c52-450c-ac9b-b9218812fbc3";
    // 44803acb-1f41-4083-8201-8d8ad1089ca6 - Shailesh
    // 5d8b48f8-7112-4197-8281-e593319c32da - Neha
    private static final String USERNAME = "shailesh_naveen";
    // shailesh
    private static final String PASSWORD = "Shail#123";
    // Shail#123
    private static final String BASE_URL = "https://bookstore.toolsqa.com";


    private static String token;
    private static Response response;
    private static String jsonString;
    private static String bookId;


    @Given("I am an authorized user")
    public void iAmAnAuthorizedUser() {

        RestAssured.baseURI = BASE_URL;
        RequestSpecification request = RestAssured.given();

        request.header("Content-Type", "application/json");
        response = request.body("{ \"userName\":\"" + USERNAME + "\", \"password\":\"" + PASSWORD + "\"}").post("/Account/v1/GenerateToken");

        System.out.println("\n\n\nI am an authorized use| Response Status: " + response.getStatusLine());

        String jsonString = response.asString();
        System.out.println(jsonString);
        /*
        //token = JsonPath.from(jsonString).get("token");
        //System.out.println(token);
         */
        //token = JsonPath.from(jsonString).getString("[0].token");
        //token = JsonPath.from(jsonString).getString("token");
        System.out.println("Authorized User, with Token Key: " + token);

    }


    @Given("A list of books are available")
    public void listOfBooksAreAvailable() {
        RestAssured.baseURI = BASE_URL;
        RequestSpecification request = RestAssured.given();
        response = request.get("/BookStore/v1/Books");

        System.out.println("\n\n\nA list of books are available| Response Status: " + response.getStatusLine());

        jsonString = response.asString();
        System.out.println(jsonString);
        /*
        List<Map<String, String>> books = JsonPath.from(jsonString).get("books");
        //Assert.assertTrue(books.size() > 0);

        bookId = books.get(0).get("isbn");
        */
    }

    @When("I add a book to my reading list")
    public void addBookInList() {
        RestAssured.baseURI = BASE_URL;
        RequestSpecification request = RestAssured.given();

        token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyTmFtZSI6InNoYWlsZXNoX25hdmVlbiIsInBhc3N3b3JkIjoiU2hhaWwjMTIzIiwiaWF0IjoxNTk2NTY1MDc1fQ.z-6DIZNaiXu-quSO5Gy7CzHYzK3nw6ABOLU4yFP5CVw";
        request.header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json");

        response = request.body("{ \"userId\": \"" + USER_ID + "\", " +
                "\"collectionOfIsbns\": [ { \"isbn\": \"" + bookId + "\" } ]}")
                .post("/BookStore/v1/Books");

        System.out.println("\n\n\nI add a book to my reading list| Response Status: " + response.getStatusLine());

        jsonString = response.asString();
        System.out.println(jsonString);
    }


    @Then("The book is added")
    public void bookIsAdded() {

        //Assert.assertEquals(201, response.getStatusCode());
        String bookId = "9781449331818";
        RestAssured.baseURI = BASE_URL;
        RequestSpecification request = RestAssured.given();
        response = request.get("/BookStore/v1/Book?ISBN=9781449325862");

        System.out.println("\n\n\nThe book is added| Response Status: " + response.getStatusLine());

        jsonString = response.asString();
        System.out.println(jsonString);

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

        token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyTmFtZSI6InNoYWlsZXNoX25hdmVlbiIsInBhc3N3b3JkIjoiU2hhaWwjMTIzIiwiaWF0IjoxNTk2NTY1MDc1fQ.z-6DIZNaiXu-quSO5Gy7CzHYzK3nw6ABOLU4yFP5CVw";
        request.header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json");

        System.out.println("\n\n\nUser ID: " + USER_ID);
        response = request.body("{ \"isbn\": \"" + bookId + "\", \"userId\": \"" + USER_ID + "\"}")
                .delete("/BookStore/v1/Book");

        System.out.println("I remove a book from my reading list| Response Status: " + response.getStatusLine());
        Assert.assertEquals(204, response.getStatusCode());
        jsonString = response.asString();
        System.out.println(jsonString);
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

