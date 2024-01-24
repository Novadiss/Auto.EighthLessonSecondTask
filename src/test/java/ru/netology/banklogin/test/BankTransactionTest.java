package ru.netology.banklogin.test;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import ru.netology.banklogin.data.DataHelper;

import static io.restassured.RestAssured.given;
import static ru.netology.banklogin.data.DataGenerator.LoginAuthentication.getLogonForRegisteredUser;
import static ru.netology.banklogin.data.DataGenerator.LoginAuthentication.getToken;
import static ru.netology.banklogin.data.DataGenerator.TransferCardByCard.postTransferFirstToSecond;
import static ru.netology.banklogin.data.SQLHelper.*;

public class BankTransactionTest {
    DataHelper dataHelper;

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
        int amount = 5000;
        getLogonForRegisteredUser();
        var token = getToken();
        var expectedFirstCardBalance = getFirstCardBalance().getBalance() - 5000;
        var expectedSecondCardBalance = getSecondCardBalance().getBalance() - 5000;
        Response response = given()
                .baseUri("http://localhost:9999/api/transfer")
                .auth().oauth2(token.getToken())
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .and()
                .body(postTransferFirstToSecond(amount))
                .when()
                .post()
                .then().extract().response();
        var actualFirstCardBalance = getFirstCardBalance().getBalance();
        var actualSecondCardBalance = getSecondCardBalance().getBalance();

        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertEquals(expectedFirstCardBalance, actualFirstCardBalance);
        Assertions.assertEquals(expectedSecondCardBalance, actualSecondCardBalance);
    }
}

