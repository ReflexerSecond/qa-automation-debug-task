package fixtures;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterSuite;
import reporting.Report;

public abstract class WebFixture {

    @AfterSuite(alwaysRun = true)
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