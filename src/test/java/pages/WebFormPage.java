package pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.checked;
import static com.codeborne.selenide.Condition.value;
import static com.codeborne.selenide.Selenide.$;

public class WebFormPage {

    private final SelenideElement textInput = $("[name='my-text']");
    private final SelenideElement checkbox = $("[name='my-check']");

    public WebFormPage fillForm(String text) {
        textInput.setValue(text);
        checkbox
                .setSelected(true)
                .shouldBe(checked);
        return this;
    }

    public WebFormPage verifyForm(String expectedText) {
        textInput.shouldHave(value(expectedText));
        return this;
    }

    public WebFormPage submit() {
        $("button").click();
        return this;
    }
}