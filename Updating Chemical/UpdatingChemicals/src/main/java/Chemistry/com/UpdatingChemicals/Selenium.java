package Chemistry.com.UpdatingChemicals;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import java.time.Duration;
//OLDER VERSION!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
public class Selenium {

    public static void main(String[] args) {

        // Initialize ChromeDriver
        System.setProperty("webdriver.chrome.driver", "C:\\\\Users\\\\Eric Pettersson\\\\Documents\\\\Code\\\\Updating Chemical\\\\UpdatingChemicals\\\\src\\\\Resources\\\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        Wait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(20))
                .pollingEvery(Duration.ofMillis(300));
//LOGIN PORTION
        // Navigate to the login page
        driver.get("https://laveta.ahec.edu/EHSA/login?c=forms&ReturnUrl=%2fehsa");
        //maximizes window
        driver.manage().window().maximize();
        
        // Enters credentials and log's in
        WebElement usernameField = driver.findElement(By.id("txtLoginId"));
        WebElement passwordField = driver.findElement(By.id("txtPassword"));
        WebElement loginButton = driver.findElement(By.id("btnLogin"));
        
        wait.until(d -> usernameField.isDisplayed());
        wait.until(d -> usernameField.isEnabled());

        usernameField.click();
        usernameField.sendKeys("petersone");
        
        wait.until(d -> passwordField.isDisplayed());
        wait.until(d -> passwordField.isEnabled());
        passwordField.click();
        passwordField.sendKeys("msudchemeric");
        
        wait.until(d -> loginButton.isDisplayed());
        wait.until(d -> loginButton.isEnabled());
        loginButton.click();
        
     // Navigation to data entry & Setup
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
        WebElement inventoryLink = driver.findElement(By.id("ancInventory"));
        wait.until(d -> inventoryLink.isDisplayed());
        wait.until(d -> inventoryLink.isEnabled());
        inventoryLink.click();
        

        // Click on the "Quick Chemical Entry" link
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
        WebElement quickChemicalEntryLink = driver.findElement(By.id("ancChemInventory"));
        wait.until(d -> quickChemicalEntryLink.isDisplayed());
        wait.until(d -> quickChemicalEntryLink.isEnabled());
        quickChemicalEntryLink.click();
        
        
        
        // Enters ****Show All PIs****'
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
        WebElement showAllPIsItem = driver.findElement(By.xpath("//li[contains(text(), '****Show All PIs****')]"));
        wait.until(d -> showAllPIsItem.isDisplayed());
        wait.until(d -> showAllPIsItem.isEnabled());
        showAllPIsItem.click();


//DATA ENTRY PORTION
        // Need to loop and make barcode varible dynamic
        String barcode = "09509";
        // Clear's Search
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        WebElement clearButton = driver.findElement(By.id("clearSynonymBtn"));
        wait.until(d -> clearButton.isDisplayed());
        wait.until(d -> clearButton.isEnabled());
        clearButton.click();
        
        // Find the input field by its ID and input the barcode
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
        WebElement searchBar = driver.findElement(By.id("chemicalSearch"));
        wait.until(d -> searchBar.isDisplayed());
        wait.until(d -> searchBar.isEnabled());
        searchBar.sendKeys(barcode);
        
        //Clicks on filter
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
        WebElement searchButton = driver.findElement(By.id("filterSynonymBtn"));
        wait.until(d -> searchButton.isDisplayed());
        wait.until(d -> searchButton.isEnabled());
        searchButton.click();
        
        // Find the grid row by clicking on the text
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        WebElement element = driver.findElement(By.xpath("//*[text()='" + barcode + "']"));

        wait.until(d -> element.isDisplayed());
        wait.until(d -> element.isEnabled());
        element.click();
        //Clicks on editButton
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        WebElement editButton = driver.findElement(By.id("btnEditRow"));
        wait.until(d -> editButton.isDisplayed());
        wait.until(d -> editButton.isEnabled());
        editButton.click();
        //
        
    }
}
