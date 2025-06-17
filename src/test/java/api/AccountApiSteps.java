package api;

import io.restassured.response.Response;
import models.LoginRequestModel;
import models.LoginResponseModel;
import models.UserBooksResponseModel;
import specs.Specs;
import tests.TestData;

import static io.restassured.RestAssured.given;
import static specs.Specs.request;
import static specs.Specs.response;

public class AccountApiSteps {

    public static LoginResponseModel login() {
        LoginRequestModel request = new LoginRequestModel(TestData.USERNAME,TestData.PASSWORD);
        return
                given(Specs.request)
                        .body(request)
                        .when()
                        .post("/account/v1/login")
                        .then()
                        .spec(Specs.response(200))
                        .extract().as(LoginResponseModel.class);
    }

    public static UserBooksResponseModel getUserBooks(String token, String userId) {
        Response response = given(request)
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/account/v1/user/" + userId)
                .then()
                .spec(response(200))
                .extract().response();

        return response.as(UserBooksResponseModel.class);
    }
}