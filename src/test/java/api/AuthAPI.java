package api;

import models.LoginRequestModel;
import models.LoginResponseModel;
import specs.Specs;
import tests.TestData;

import static io.restassured.RestAssured.given;

public class AuthAPI{

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
}