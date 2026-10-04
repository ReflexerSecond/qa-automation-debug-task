package tests;

import fixtures.WebFixture;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.SearchPage;

import java.util.function.Consumer;

public class SearchTest extends WebFixture {

    private static final int[] ALL_EMPLOYEES = {1, 2, 3, 4, 5, 6, 7, 8};
    private static final int[] WOMEN = {2, 4, 6, 8};

    private SearchPage searchPage;

    @BeforeMethod
    public void loginAndOpenSearchPage() {
        searchPage = LoginPage.open().loginAsTestUser();
    }

    @DataProvider
    public Object[][] filters() {
        return new Object[][]{
                row("email", p -> p.enterEmail("ivanov@company.ru"), 1),
                row("name", p -> p.enterName("петрова"), 2),
                row("gender", p -> p.selectGender("Женский"), WOMEN),
                row("age", p -> p.enterAge(28), 1),
                row("hire date from", p -> p.enterDateFrom("2022-01-01"), 1, 3, 6),
                row("hire date to", p -> p.enterDateTo("2020-12-31"), 4, 5, 7),
                row("hire date range", p -> p.enterDateFrom("2021-01-01").enterDateTo("2022-12-31"), 1, 2, 8),
                row("single skill", p -> p.selectSkills("API"), 1, 3, 8),
                row("two skills, employee must have both", p -> p.selectSkills("Java", "API"), 3, 8),
                row("gender and skill", p -> p.selectGender("Женский").selectSkills("Java"), 8),
                row("hire date from is inclusive", p -> p.enterDateFrom("2022-03-15"), 1, 3, 6),
                row("hire date to is inclusive", p -> p.enterDateTo("2022-03-15"), 1, 2, 4, 5, 7, 8),
                row("email ignores case", p -> p.enterEmail("IVANOV@company.ru"), 1)
        };
    }

    private static Object[] row(String title, Consumer<SearchPage> filter, int... expectedIds) {
        return new Object[]{title, filter, expectedIds};
    }

    @Test(dataProvider = "filters")
    public void searchReturnsMatchingEmployees(String title, Consumer<SearchPage> filter, int[] expectedIds) {
        filter.accept(searchPage);

        searchPage.search().verifyResultIds(expectedIds);
    }

    @Test
    public void searchWithoutMatchesShowsEmptyMessage() {
        int ageNobodyHas = 40;

        searchPage.enterAge(ageNobodyHas).search().verifyNoResults();
    }

    @Test
    public void invalidEmailShowsAlertAndKeepsPreviousResults() {
        searchPage.selectGender("Женский").search().verifyResultIds(WOMEN);

        searchPage.enterEmail("ivanov").search()
                .verifyEmailFormatAlert()
                .verifyResultIds(WOMEN);
    }

    @Test
    public void invalidDateRangeShowsAlertAndKeepsPreviousResults() {
        searchPage.selectGender("Женский").search().verifyResultIds(WOMEN);

        searchPage.enterDateFrom("2023-01-01").enterDateTo("2022-01-01").search()
                .verifyDateRangeAlert()
                .verifyResultIds(WOMEN);
    }

    @Test
    public void bothAlertsAreShownTogether() {
        searchPage.enterEmail("ivanov")
                .enterDateFrom("2023-01-01")
                .enterDateTo("2022-01-01")
                .search()
                .verifyEmailFormatAlert()
                .verifyDateRangeAlert();
    }

    @Test
    public void validSearchRemovesPreviousAlert() {
        searchPage.enterEmail("ivanov").search().verifyEmailFormatAlert();

        searchPage.enterEmail("ivanov@company.ru").search()
                .verifyNoEmailFormatAlert()
                .verifyResultIds(1);
    }

    @Test
    public void resetClearsAllFiltersAndShowsAllEmployees() {
        fillAllFilters().search().verifyResultIds(1);

        searchPage.reset()
                .verifyFiltersEmpty()
                .verifyResultIds(ALL_EMPLOYEES);
    }

    @Test
    public void filtersAreClearedAfterRelogin() {
        fillAllFilters().search().verifyResultIds(1);

        searchPage.logout().loginAsTestUser()
                .verifyFiltersEmpty()
                .verifyResultIds(ALL_EMPLOYEES);
    }

    @Test
    public void resetRemovesAlert() {
        searchPage.enterEmail("ivanov").search().verifyEmailFormatAlert();
        searchPage.reset().verifyNoEmailFormatAlert();
    }

    private SearchPage fillAllFilters() {
        return searchPage
                .enterEmail("ivanov@company.ru")
                .enterName("иванов")
                .selectGender("Мужской")
                .enterAge(28)
                .enterDateFrom("2022-01-01")
                .enterDateTo("2022-12-31")
                .selectSkills("Selenium");
    }
}