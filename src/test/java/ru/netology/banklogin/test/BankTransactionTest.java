package ru.netology.banklogin.test;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static ru.netology.banklogin.data.DataGenerator.LoginAuthentication.getLogonForRegisteredUser;
import static ru.netology.banklogin.data.DataGenerator.LoginAuthentication.getToken;
import static ru.netology.banklogin.data.DataGenerator.TransferCardByCard.postTransferFirstToSecond;
import static ru.netology.banklogin.data.SQLHelper.cleanAuthCodes;
import static ru.netology.banklogin.data.SQLHelper.cleanDatabase;

public class BankTransactionTest {

    @AfterEach
    void tearDown() {
        cleanAuthCodes();
    }

    @AfterAll
    static void tearDownAll() {
        cleanDatabase();
    }

    @Test
    @DisplayName("Should successful transaction from card to card")
    void shouldSuccessfulTransactionCardByCard() {
        getLogonForRegisteredUser();
        var token = getToken();
        Response response = given()
                .baseUri("http://localhost:9999/api/transfer")
                .auth().oauth2(token.getToken())
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .and()
                .body(postTransferFirstToSecond(5000))
                .when()
                .post()
                .then().extract().response();

        Assertions.assertEquals(200, response.statusCode());
    }
}

