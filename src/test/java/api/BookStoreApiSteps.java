package api;

import static io.restassured.RestAssured.given;
import static specs.Specs.request;
import static specs.Specs.response;

public class BookStoreApiSteps {

    public static void deleteAllBooks(String token, String userId) {
        given(request)
                .header("Authorization", "Bearer " + token)
                .queryParam("UserId", userId)
                .when()
                .delete("/bookstore/v1/books")
                .then()
                .spec(response(204));
    }

    public static void addBook(String token, String userId, String isbn) {
        String jsonBody = String.format("{\"userId\":\"%s\",\"collectionOfIsbns\":[{\"isbn\":\"%s\"}]}",
                userId, isbn);

        given(request)
                .header("Authorization", "Bearer " + token)
                .body(jsonBody)
                .when()
                .post("/bookstore/v1/books")
                .then()
                .spec(response(201));
    }

    public static void deleteBook(String token, String userId, String isbn) {
        String jsonBody = String.format("{\"isbn\":\"%s\",\"userId\":\"%s\"}",
                isbn,userId);

        given(request)
                .header("Authorization", "Bearer " + token)
                .body(jsonBody)
                .when()
                .delete("/bookstore/v1/book")
                .then()
                .spec(response(204));
    }


}
