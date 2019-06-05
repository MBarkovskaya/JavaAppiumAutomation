import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URL;
import java.util.List;

public class SecondLesson {
    private AppiumDriver driver;

    @Before
    public void setUp() throws Exception {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("deviceName", "AndroidTestDevice");
        capabilities.setCapability("platformVersion", "6.0");
        capabilities.setCapability("automationName", "Appium");
        capabilities.setCapability("appPackage", "org.wikipedia");
        capabilities.setCapability("appActivity", ".main.MainActivity");
        capabilities.setCapability("app", "/hdd/Projects_Mariya/study/JavaAppiumAutomation/apks/org.wikipedia_10280_apps.evozi.com.apk");

        driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
    }

    @After
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void findTextSearchIntoInputFieldTest() {
        waitForElementAndClick(By.id("org.wikipedia:id/fragment_onboarding_skip_button"), "Cannot find SKIP button", 5);

        waitForElementAndClick(By.xpath("//*[contains(@text,'Search Wikipedia')]"), "Cannot find search input", 5);
        WebElement titleElement = waitForElementPresent(By.id("org.wikipedia:id/search_src_text"), "Cannot find 'Search Wikipedia' text into the input field");
        String searchWikipediaText = titleElement.getAttribute("text");

        Assert.assertEquals("We see unexpected title", "Search Wikipedia", searchWikipediaText);
    }

    @Test
    public void searchByWordAndCancelSearchingTest() {
        waitForElementAndClick(By.id("org.wikipedia:id/fragment_onboarding_skip_button"), "Cannot find SKIP button", 5);
        waitForElementAndClick(By.xpath("//*[contains(@text,'Search Wikipedia')]"), "Cannot find search input", 5);

        waitForElementAndSendKeys(By.xpath("//*[contains(@text,'Search Wikipedia')]"), "Java","Cannot find search input", 5);
        WebElement listOfResults = waitForElementPresent(By.xpath("//*[@resource-id='org.wikipedia:id/search_results_list']"), "Cannot find list of searching results");
        int size = listOfResults.findElements(By.xpath("//*[@class='android.view.ViewGroup']//*[@resource-id='org.wikipedia:id/page_list_item_title']")).size();

        Assert.assertTrue("The searching finds some articles", size > 0);
        waitForElementAndClick(By.xpath("//*[@resource-id='org.wikipedia:id/search_close_btn']"), "Cannot find close button", 0);
        boolean resultIsEmpty = waitForElementNotPresent(By.xpath("//*[@resource-id='org.wikipedia:id/search_results_list']"), "The list of searching results is still on the page", 5);
        Assert.assertTrue("There are some results of empty searching", resultIsEmpty);
    }

    @Test
    public void searchByWordAndResultsCheckingTest() {
        waitForElementAndClick(By.id("org.wikipedia:id/fragment_onboarding_skip_button"), "Cannot find SKIP button", 5);
        waitForElementAndClick(By.xpath("//*[contains(@text,'Search Wikipedia')]"), "Cannot find search input", 5);

        waitForElementAndSendKeys(By.xpath("//*[contains(@text,'Search Wikipedia')]"), "Java", "Cannot find search input", 5);
        WebElement listOfResults = waitForElementPresent(By.xpath("//*[@resource-id='org.wikipedia:id/search_results_list']"), "Cannot find list of searching results");
        List<WebElement> resultsOfSearching = listOfResults.findElements(By.xpath("//*[@class='android.view.ViewGroup']//*[@resource-id='org.wikipedia:id/page_list_item_title']"));
        for (WebElement webElement : resultsOfSearching) {
            Assert.assertTrue("There aren't any word 'Java' into the each of searching result", webElement.getAttribute("text").toLowerCase().contains("java"));
        }

    }


    private WebElement waitForElementPresent(By locator, String erMsg, long timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
        wait.withMessage(erMsg + "\n");
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    private WebElement waitForElementPresent(By locator, String erMsg) {
        return waitForElementPresent(locator, erMsg, 5);
    }

    private WebElement waitForElementAndClick(By locator, String erMsg, long timeoutInSeconds) {
        WebElement element = waitForElementPresent(locator, erMsg, timeoutInSeconds);
        element.click();
        return element;
    }

    private WebElement waitForElementAndSendKeys(By locator, String value, String erMsg, long timeoutInSeconds) {
        WebElement element = waitForElementPresent(locator, erMsg, timeoutInSeconds);
        element.sendKeys(value);
        return element;
    }

    private boolean waitForElementNotPresent(By locator, String erMsg, long timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
        wait.withMessage(erMsg + "\n");
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

}
