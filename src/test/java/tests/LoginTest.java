package tests;

import fixtures.WebFixture;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.SearchPage;

import static pages.LoginPage.TEST_EMAIL;
import static pages.LoginPage.TEST_PASSWORD;

public class LoginTest extends WebFixture {

    private final SearchPage searchPage = new SearchPage();
    private LoginPage loginPage;

    @BeforeMethod
    public void openLoginPage() {
        loginPage = LoginPage.open();
    }

    @Test
    public void searchPageIsHiddenBeforeLogin() {
        verifyStillOnLoginPage();
    }

    @Test
    public void validCredentialsOpenSearchPage() {
        loginPage.loginAsTestUser().verifyIsShown().verifyLoggedInAs(TEST_EMAIL);
    }

    @Test
    public void enterInPasswordFieldSubmitsForm() {
        loginPage.enterCredentials(TEST_EMAIL, TEST_PASSWORD)
                .pressEnterInPasswordField()
                .verifyIsShown();
    }

    @Test
    public void enterInEmailFieldSubmitsForm() {
        loginPage.enterCredentials(TEST_EMAIL, TEST_PASSWORD)
                .pressEnterInEmailField()
                .verifyIsShown();
    }

    @DataProvider
    public Object[][] wrongCredentials() {
        return new Object[][]{{TEST_EMAIL, "wrong"},
                {"other@test.test", TEST_PASSWORD},
                {TEST_EMAIL, ""},
                {"TEST@test.test", TEST_PASSWORD}};
    }

    @Test(dataProvider = "wrongCredentials")
    public void wrongCredentialsAreRejected(String email, String password) {
        loginPage.login(email, password)
                .verifyCredentialsAlert();
        verifyStillOnLoginPage();
    }

    @DataProvider
    public Object[][] invalidEmails() {
        return new Object[][]{{""},
                {"test"},
                {"test@test"},
                {"te st@test.test"},
                {"test..a@test.test"}};
    }

    @Test(dataProvider = "invalidEmails")
    public void invalidEmailFormatIsRejected(String email) {
        loginPage.login(email, TEST_PASSWORD)
                .verifyEmailFormatAlert();
        verifyStillOnLoginPage();
    }

    @Test
    public void credentialsAlertCanBeClosed() {
        loginPage.login(TEST_EMAIL, "wrong")
                .verifyCredentialsAlert()
                .closeCredentialsAlert()
                .verifyNoCredentialsAlert();
    }

    @Test
    public void logoutShowsLoginPageWithEmptyFields() {
        loginPage.loginAsTestUser()
                .logout()
                .verifyIsShown()
                .verifyFieldsEmpty();
        searchPage.verifyIsHidden();
    }

    private void verifyStillOnLoginPage() {
        loginPage.verifyIsShown();
        searchPage.verifyIsHidden();
    }
}