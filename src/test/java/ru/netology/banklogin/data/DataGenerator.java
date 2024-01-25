package ru.netology.banklogin.data;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Value;

import static io.restassured.RestAssured.given;
import static ru.netology.banklogin.data.DataGenerator.LoginAuthentication.getVerifyToken;

public class DataGenerator {
    private static final RequestSpecification requestSpec = new RequestSpecBuilder()

            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    private DataGenerator() {
    }

    private static LogonInfo loginPostRequest(LogonInfo user) {
        given()
                .spec(requestSpec)
                .body(user)
                .when()
                .post("/api/auth")
                .then()
                .statusCode(200)
        ;
        return user;
    }

    private static String valueOfToken() {
        Response response = given()
                .spec(requestSpec)
                .body(LoginAuthentication.getVerify())
                .when()
                .post("/api/auth/verification")
                .then()
                .extract()
                .response();
        return response.jsonPath().getString("token");
    }

    public static void transactionCardByCard(String firstCard, String secondCard, int amount) {
        given()
                .spec(requestSpec)
                .auth().oauth2(getVerifyToken().getToken())
                .and()
                .body(TransferCardByCard.postCardByCard(firstCard, secondCard, amount))
                .when()
                .post("/api/transfer")
                .then()
                .statusCode(200)
                ;
    }

    public static class LoginAuthentication {
        private LoginAuthentication() {

        }

        public static LogonInfo getLogonInfo() {
            return new LogonInfo(DataHelper.getAuthInfoWithTestData().getLogin(), DataHelper.getAuthInfoWithTestData().getPassword());
        }

        public static VerifyInfo getVerify() {
            return new VerifyInfo(DataHelper.getAuthInfoWithTestData().getLogin(), SQLHelper.getVerificationCode().getCode());
        }

        public static LogonInfo getLogonForRegisteredUser() {
            return loginPostRequest(getLogonInfo());
        }

        public static Token getVerifyToken() {
            return new Token(valueOfToken());
        }
    }

    public static class TransferCardByCard {
        private TransferCardByCard() {

        }

        public static Transfer postCardByCard(String firstCard, String secondCard, int amount) {
            return new Transfer(firstCard, secondCard, amount);
        }

    }

    @Value
    public static class LogonInfo {
        String login;
        String password;
    }

    @Value
    public static class VerifyInfo {
        String login;
        String code;
    }

    @Value
    public static class Token {
        String token;
    }

    @Value
    public static class Transfer {
        String from;
        String to;
        int amount;
    }
}
