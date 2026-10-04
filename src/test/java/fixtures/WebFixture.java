package fixtures;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterMethod;
import reporting.Report;

public abstract class WebFixture {

    @AfterMethod(alwaysRun = true)
    public void teardown() {
        try {
            if (WebDriverRunner.hasWebDriverStarted()
                    && WebDriverRunner.getWebDriver() instanceof RemoteWebDriver driver) {
                Report.attachSessionInfo(driver.getSessionId());
            }
        } finally {
            Selenide.closeWebDriver();
        }
    }
}