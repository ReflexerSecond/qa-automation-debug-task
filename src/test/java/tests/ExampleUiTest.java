package tests;

import fixtures.WebFixture;
import org.testng.annotations.Test;
import pages.WebFormPage;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class ExampleUiTest extends WebFixture {
    @Test
    public void submitWebForm() {
        WebFormPage page = new WebFormPage();
        open("https://www.selenium.dev/selenium/web/web-form.html");
        $("h1")
                .shouldBe(visible)
                .shouldHave(text("Web form"));
        page.fillForm("QA Automation");
        page.verifyForm("QA Automation");
        page.submit();
        $("#message")
                .shouldBe(visible)
                .shouldHave(text("Received!"));
    }
}
