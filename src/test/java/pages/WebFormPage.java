package pages;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;

public class WebFormPage {

    //region constants
    private static final String URL = "https://www.selenium.dev/selenium/web/web-form.html";

    private final SelenideElement textInput = $("#my-text-id");
    private final SelenideElement checkbox = $("#my-check-2");
    private final SelenideElement button = $("button[type='submit']");
    private final SelenideElement message = $("#message");
    private final SelenideElement head = $("h1");
    //endregion

    //region actions
    public static WebFormPage open() {
        Selenide.open(URL);
        return new WebFormPage();
    }

    public WebFormPage fillForm(String text) {
        textInput.setValue(text);
        checkbox.setSelected(true);
        return this;
    }

    public WebFormPage verifyForm(String expectedText) {
        textInput.shouldHave(exactValue(expectedText));
        checkbox.shouldBe(checked);
        return this;
    }

    public WebFormPage submit() {
        button.click();
        return this;
    }
    //endregion

    //region verifications
    public WebFormPage verifyHeadShown() {
        head.shouldBe(visible).shouldHave(text("Web form"));
        return this;
    }

    public WebFormPage verifyMessageReceived() {
        message.shouldBe(visible).shouldHave(text("Received!"));
        return this;
    }
    //endregion
}