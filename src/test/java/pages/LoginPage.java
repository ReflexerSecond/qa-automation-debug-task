package pages;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

import java.net.URISyntaxException;
import java.util.Objects;

import static com.codeborne.selenide.Condition.empty;
import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage {

    //region constants
    private static String pageUrl() {
        try {
            return Objects.requireNonNull(LoginPage.class.getClassLoader().getResource(PAGE_RESOURCE))
                    .toURI()
                    .toString();
        } catch (URISyntaxException e) {
            throw new IllegalStateException("Cannot resolve " + PAGE_RESOURCE, e);
        }
    }

    public static final String TEST_EMAIL = "test@test.test";
    public static final String TEST_PASSWORD = "test";

    private static final String PAGE_RESOURCE = "task.html";

    private final SelenideElement authPage = $("#authPage");
    private final SelenideElement emailInput = $("#loginEmail");
    private final SelenideElement passwordInput = $("#loginPassword");
    private final SelenideElement loginButton = $("#authButton");

    private final SelenideElement emailFormatAlert = $("#emailFormatError");
    private final SelenideElement credentialsAlert = $("#invalidEmailPassword");
    //endregion

    //region actions
    public static LoginPage open() {
        Selenide.open(pageUrl());
        return new LoginPage();
    }

    public SearchPage loginAsTestUser() {
        login(TEST_EMAIL, TEST_PASSWORD);
        return new SearchPage();
    }

    public LoginPage login(String email, String password) {
        enterCredentials(email, password);
        loginButton.click();
        return this;
    }

    public LoginPage enterCredentials(String email, String password) {
        emailInput.setValue(email);
        passwordInput.setValue(password);
        return this;
    }

    public SearchPage pressEnterInEmailField() {
        emailInput.pressEnter();
        return new SearchPage();
    }

    public SearchPage pressEnterInPasswordField() {
        passwordInput.pressEnter();
        return new SearchPage();
    }

    public LoginPage closeCredentialsAlert() {
        credentialsAlert.$(".form-alert__close").click();
        return this;
    }
    //endregion

    //region verifications
    public LoginPage verifyIsShown() {
        authPage.shouldBe(visible);
        return this;
    }

    public LoginPage verifyEmailFormatAlert() {
        emailFormatAlert.shouldBe(visible).shouldHave(text("Неверный формат Email"));
        return this;
    }

    public LoginPage verifyCredentialsAlert() {
        credentialsAlert.shouldBe(visible).shouldHave(text("Неверный Email или пароль"));
        return this;
    }

    public LoginPage verifyNoCredentialsAlert() {
        credentialsAlert.shouldNot(exist);
        return this;
    }

    public LoginPage verifyFieldsEmpty() {
        emailInput.shouldBe(empty);
        passwordInput.shouldBe(empty);
        return this;
    }
    //endregion
}