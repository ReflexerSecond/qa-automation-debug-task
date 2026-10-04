package tests;

import fixtures.WebFixture;
import org.testng.annotations.Test;
import pages.WebFormPage;


public class ExampleUiTest extends WebFixture {
    @Test
    public void submitWebForm() {
        WebFormPage page = WebFormPage.open();
        page.verifyHeadShown();
        String text = "QA Automation";
        page.fillForm(text);
        page.verifyForm(text);
        page.submit();
        page.verifyMessageReceived();
    }
}
