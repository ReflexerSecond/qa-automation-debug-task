package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

import java.util.Arrays;

import static com.codeborne.selenide.CollectionCondition.exactTexts;
import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.checked;
import static com.codeborne.selenide.Condition.empty;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.value;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class SearchPage {

    //region constants
    private static final String ANY_GENDER = "Любой";

    private final SelenideElement page = $("#searchPage");
    private final SelenideElement loggedInEmail = $("#authUserEmail");
    private final SelenideElement logoutButton = $("#logoutButton");

    private final SelenideElement emailInput = $("#searchEmail");
    private final SelenideElement nameInput = $("#searchName");
    private final SelenideElement genderSelect = $("#searchGender");
    private final SelenideElement ageInput = $("#searchAge");
    private final SelenideElement dateFromInput = $("#searchDateFrom");
    private final SelenideElement dateToInput = $("#searchDateTo");
    private final ElementsCollection skillCheckboxes = $$("input[name='searchSkill']");

    private final SelenideElement searchButton = $("#searchButton");
    private final SelenideElement resetButton = $("#resetSearchButton");

    private final SelenideElement emailFormatAlert = $("#searchEmailFormatError");
    private final SelenideElement dateRangeAlert = $("#searchDateRangeError");

    private final ElementsCollection resultIds = $$("#resultsTable tbody tr td:nth-child(1)");
    private final SelenideElement resultsCount = $("#resultsCount");
    private final SelenideElement emptyResultsMessage = $("#resultsEmpty");
    //endregion

    //region actions
    public SearchPage enterEmail(String email) {
        emailInput.setValue(email);
        return this;
    }

    public SearchPage enterName(String name) {
        nameInput.setValue(name);
        return this;
    }

    public SearchPage selectGender(String gender) {
        genderSelect.selectOption(gender);
        return this;
    }

    public SearchPage enterAge(int age) {
        ageInput.setValue(String.valueOf(age));
        return this;
    }

    public SearchPage enterDateFrom(String isoDate) {
        setDate(dateFromInput, isoDate);
        return this;
    }

    public SearchPage enterDateTo(String isoDate) {
        setDate(dateToInput, isoDate);
        return this;
    }

    public SearchPage selectSkills(String... skills) {
        for (String skill : skills) {
            skillCheckboxes.findBy(value(skill)).setSelected(true);
        }
        return this;
    }

    private void setDate(SelenideElement dateInput, String isoDate) {
        Selenide.executeJavaScript("arguments[0].value = arguments[1]", dateInput, isoDate);
    }

    public SearchPage search() {
        searchButton.click();
        return this;
    }

    public SearchPage reset() {
        resetButton.click();
        return this;
    }

    public LoginPage logout() {
        logoutButton.click();
        return new LoginPage();
    }
    //endregion

    //region verifications
    public SearchPage verifyIsShown() {
        page.shouldBe(visible);
        return this;
    }

    public SearchPage verifyIsHidden() {
        page.shouldNotBe(visible);
        return this;
    }

    public SearchPage verifyLoggedInAs(String email) {
        loggedInEmail.shouldHave(exactText(email));
        return this;
    }

    public SearchPage verifyResultIds(int... expectedIds) {
        String[] expected = Arrays.stream(expectedIds)
                .mapToObj(String::valueOf)
                .toArray(String[]::new);
        resultIds.shouldHave(exactTexts(expected));
        resultsCount.shouldHave(exactText("Найдено: " + expectedIds.length));
        return this;
    }

    public SearchPage verifyNoResults() {
        resultIds.shouldHave(size(0));
        resultsCount.shouldHave(exactText("Найдено: 0"));
        emptyResultsMessage.shouldBe(visible);
        return this;
    }

    public SearchPage verifyEmailFormatAlert() {
        emailFormatAlert.shouldBe(visible).shouldHave(text("Неверный формат Email"));
        return this;
    }

    public SearchPage verifyNoEmailFormatAlert() {
        emailFormatAlert.shouldNot(exist);
        return this;
    }

    public SearchPage verifyDateRangeAlert() {
        dateRangeAlert.shouldBe(visible).shouldHave(text("Дата «с» не может быть позже даты «по»"));
        return this;
    }

    public SearchPage verifyFiltersEmpty() {
        emailInput.shouldBe(empty);
        nameInput.shouldBe(empty);
        genderSelect.getSelectedOption().shouldHave(exactText(ANY_GENDER));
        ageInput.shouldBe(empty);
        dateFromInput.shouldBe(empty);
        dateToInput.shouldBe(empty);
        skillCheckboxes.filterBy(checked).shouldHave(size(0));
        return this;
    }
    //endregion
}