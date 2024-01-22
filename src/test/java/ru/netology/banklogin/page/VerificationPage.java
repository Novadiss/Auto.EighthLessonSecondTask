package ru.netology.banklogin.page;

import com.codeborne.selenide.SelenideElement;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Value;
import ru.netology.banklogin.data.DataHelper;
import ru.netology.banklogin.data.SQLHelper;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class VerificationPage {
    private final SelenideElement codeField = $("[data-test-id=code] input");
    private final SelenideElement verifyButton = $("[data-test-id=action-verify]");
    private final SelenideElement errorNotification = $("[data-test-id='error-notification'] .notification__content");

    public void verifyVerificationPageVisibly() {
        codeField.shouldBe(visible);
    }

    public void verifyErrorNotification(String expectedText) {
        errorNotification.shouldHave(exactText(expectedText)).shouldBe(visible);
    }

    public DashboardPage validVerify(String verificationCode) {
        verify(verificationCode);
        return new DashboardPage();
    }

    public void verify(String verificationCode) {
        codeField.setValue(verificationCode);
        verifyButton.click();
    }

    public VerificationJson getVerifyForJson(){return new VerificationJson(DataHelper.getAuthInfoWithTestData().getLogin(), SQLHelper.getVerificationCode().getCode());}

    public String veryfyJson() {
        GsonBuilder verify = new GsonBuilder();
        Gson verGson = verify.create();
        String objects = verGson.toJson(getVerifyForJson());
        return objects;
    }

    @Value
    public class VerificationJson{
        String login;
        String  code;
    }

}
