package ru.netology.banklogin.test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import ru.netology.banklogin.data.DataHelper;
import ru.netology.banklogin.data.SQLHelper;
import ru.netology.banklogin.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static ru.netology.banklogin.data.SQLHelper.cleanAuthCodes;
import static ru.netology.banklogin.data.SQLHelper.cleanDatabase;

public class BankTransactionTest {
    LoginPage loginPage;
//    String requestBody = "{}";

//    String login = DataHelper.getAuthInfoWithTestData().getLogin();
//    String password = DataHelper.getAuthInfoWithTestData().getPassword();

    @AfterEach
    void tearDown() {
        cleanAuthCodes();
    }

    @AfterAll
    static void tearDownAll() {
        cleanDatabase();
    }

//    @BeforeEach
//    public static void jsonObjects(String[] args) {
//        JSONObject object = new JSONObject();
//        object.put("login", DataHelper.getAuthInfoWithTestData().getLogin());
//        object.put("password", DataHelper.getAuthInfoWithTestData().getPassword());
//    }


    @Test
    @DisplayName("Should successfully login to dashboard with exist login and password from sut test data")
    void shouldSuccessfulLogin() {
//        GsonBuilder login = new GsonBuilder();
//        Gson gson = login.create();
//        String objects = gson.toJson(DataHelper.getAuthInfoWithTestData());
        given()
                .baseUri("http://localhost:9999/api/auth")
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .and()
                .body(loginPage.jsonLogin())
                .when()
                .post("/posts")
                .then()
        ;
//        GsonBuilder verify = new GsonBuilder();
//        Gson vgson = verify.create();
//        String vobjects = vgson.toJson(SQLHelper.getVerificationCode());
        var temp = given()
                .baseUri("http://localhost:9999/api/auth/verification")
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .and()
                .body(vobjects)
                .when()
                .post("/posts")
                .then().extract().response()
                ;
        System.out.println(temp);
//        given()
//                .baseUri("http://localhost:9999/api/cards")
//                .contentType(ContentType.JSON)
//                .when()
//                .get("/posts").then().log().body()
//                .then()
//                .extract()
//                .response()
//                .prettyPrint()
        ;

    }
}
