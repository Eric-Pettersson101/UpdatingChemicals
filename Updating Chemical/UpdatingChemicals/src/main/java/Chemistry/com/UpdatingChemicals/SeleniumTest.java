package Chemistry.com.UpdatingChemicals;
import Chemistry.com.UpdatingChemicals.ExcelReader.*;
import java.util.ArrayList;
import java.util.List;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

public class SeleniumTest {
	
    private static String toLog;
    private static WebDriver driver = new ChromeDriver();
    private static Wait<WebDriver> wait = new FluentWait<>(driver)
            .withTimeout(Duration.ofSeconds(15))
            .pollingEvery(Duration.ofMillis(500));
    public static void main(String[] args) throws IOException {
	    try {

	    // initializing ExcelReader
	     String excelsheetPath = args[0];
        ArrayList<ChemicalData> reader = new ExcelReader().readExcelData(excelsheetPath);

//LOGIN PORTION
    	// Initialize ChromeDriver
            String webDriverPath = args[1];
	        System.setProperty("webdriver.chrome.driver", webDriverPath);

        
        Actions actions = new Actions(driver);
        // Navigate to the login page
        driver.get("https://laveta.ahec.edu/EHSA/login?c=forms&ReturnUrl=%2fehsa");
        
        // Enters credentials and log's in
        String UsernameloginInfo = args[2];
        waitForAndClickElement(By.id("txtLoginId"));
        waitForAndSendKeys(By.id("txtLoginId"), UsernameloginInfo);

        String PasswordloginInfo = args[3];
        waitForAndClickElement(By.id("txtLoginId"));
        waitForAndSendKeys(By.id("txtPassword"), PasswordloginInfo);

        waitForAndClickElement(By.id("btnLogin"));

        // Navigation to data entry & Setup
        waitForAndClickElement(By.id("ancInventory"));
        waitForAndClickElement(By.id("ancChemInventory"));
        

//0-10, 9-50
//If Program halts on a barcode that doesn't exist subtract 1 from the index number of the halted barcode and and run from that starting index
        for (int i = 821; i < 891; i++) {
			toLog += "\n";
            ChemicalData currentRow = reader.get(i);
            //temp code to igngore bad entrys
            String[] badData = {
            		 "MCHE1003A101AP","MCHE1016A100AP", "MCHE1049A111AP",
            		 "MCHE1165A100AP", "MCHE1179A104AP", "MCHE1228A100AP"};
            //MCHE1228A100AP is valid but is in use!!!!!!!!
            boolean skipBarcode = false;
            for ( String selectedBadData : badData) 	{
            	if(currentRow.barcode.toLowerCase().equals(selectedBadData.toLowerCase()))
            		skipBarcode=true;
	    }
            if(skipBarcode) {
                toLog += "\n [" + currentRow.barcode + "]	Skipped due to match in badData\n";
            	continue;
            }
// Navigation to edit screen  
            Thread.sleep(5000);
            waitForAndSendKeys(By.id("chemicalSearch"), currentRow.barcode);
            waitForAndClickElement(By.id("filterSynonymBtn"));
            Thread.sleep(5000);
            waitForAndClickElement(By.xpath("//*[text()='" + currentRow.barcode + "']"));
            waitForAndClickElement(By.id("btnEditRow"));
            toLog +="[" + currentRow.barcode + "]	";
//Data Entry logic
            //important note need to change code so cabinet af will enter
            updateAndLogField(By.name("cboLocation2_input"), currentRow.recordedRoom, currentRow.scannedRoom);
    		Thread.sleep(1500);
            actions.sendKeys(Keys.ENTER).perform();
    		Thread.sleep(1500);
    		//has issue
            updateAndLogField(By.name("cboStorageLocation_input"), currentRow.recordedStorageLocation, currentRow.scannedStorageLocation);
    		Thread.sleep(1500);
            actions.sendKeys(Keys.ENTER).perform();
    		Thread.sleep(2000);
    		//not using updateAndLogField because of issue when  cboSubStorageLocation_listbox gets cleared
        	waitForAndSendKeys(By.cssSelector("input[aria-owns='cboSubStorageLocation_listbox']"), currentRow.scannedSubLocation);
        	if(!currentRow.recordedSubLocation.equals(currentRow.scannedSubLocation)) {
        		toLog += " Replace " + currentRow.recordedSubLocation + " with: " + currentRow.scannedSubLocation;
        }
    		Thread.sleep(500);
            updateAndLogField(By.name("cboStorageDevice_input"), currentRow.recordedContainerMaterial, currentRow.scannedContainerMaterial);
            //Click on something else
    		Thread.sleep(1000);
            waitForAndClickElement(By.id("panel-heading-LocationInformation"));
            //Clicks on save button
            waitForAndClickElement(By.cssSelector("button.btn.btn-primary.btnSave")); 
    		Thread.sleep(5000);
    		checkIfVendorNameNotFilled();
    		checkIfVendorCatalogNotFilled();
            actions.sendKeys(Keys.ENTER).perform();
            Thread.sleep(5000); 
    		waitForAndClickElement(By.id("filterSynonymBtn"));
            Thread.sleep(5000); 
			verifyTableData(currentRow);
        }
        
	    } catch (Exception e) {
			e.printStackTrace();
			System.out.println(toLog);	
	    }  finally {
	        //Setting up logger
            FileWriter writer = new FileWriter("ChemistryDataUpdateLog.txt", true);
			writer.write(toLog);
	        writer.close();
			System.out.println(toLog);		
			System.out.println("Finished!");		

	    }
    }
    private static void waitForAndClickElement(By by) throws IOException, InterruptedException, TimeoutException {
    	try {
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(by));
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".k-loading-image")));
            wait.until(ExpectedConditions.elementToBeClickable(element));
            element.click();
    	} catch(TimeoutException e) {
    		System.out.println(by);
    	} catch(ElementClickInterceptedException e) {
    		Thread.sleep(5000); 
    		By altBy = By.id("panel-heading-BasicInformation");
            WebElement altElement = wait.until(ExpectedConditions.visibilityOfElementLocated(altBy));
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".k-loading-image")));
            wait.until(ExpectedConditions.elementToBeClickable(altElement));
            altElement.click();
    	}
    }
    private static void waitForAndSendKeys(By by, String keys) throws InterruptedException, IOException, TimeoutException {
    	Thread.sleep(1000);
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".k-loading-image")));
        wait.until(ExpectedConditions.elementToBeClickable(element));
        element.clear();
        element.sendKeys(keys);
    }
    private static void updateAndLogField(By by, String oldValue, String newValue) throws InterruptedException, IOException {
    	if(!oldValue.equals(newValue)) {
    		Thread.sleep(2000);
    		toLog += " Replace " + oldValue + " with: " + newValue;
        	waitForAndSendKeys(by, newValue);
    }
}
    public static void verifyTableData( ChemicalData currentRow) throws IOException, InterruptedException {
        // Locate the Table
        WebElement table = driver.findElement(By.tagName("tbody"));
        
        // Iterate Through Rows
        List<WebElement> rows = table.findElements(By.tagName("tr"));
            if (isDataCorrect(rows, currentRow)) {
                    toLog += "\n Data Verified!";
                }
            }
    private static boolean isDataCorrect(List<WebElement> rows, ChemicalData currentRow) throws IOException, InterruptedException {
    	try { 
    	//Finds the correct row and gets its column data
        WebElement row = findCorrectRow(rows, currentRow);
        List<WebElement> cols = row.findElements(By.tagName("td"));
        //Sets up temp variable to check data
        List<String> coldata = new ArrayList<String>();
        String[] dataToCheck = {
        		currentRow.barcode,
        		currentRow.scannedContainerMaterial,
        		currentRow.scannedRoom,
        		currentRow.scannedStorageLocation,
        		currentRow.scannedSubLocation
        		};
        for (WebElement col : cols) {
        	if(!col.getText().equals("")) {
  	  			coldata.add(col.getText());
        	}
	  	 }
        //Code to check if data is updated
        for (String s : dataToCheck) {
        	boolean matched = false;
        	for (String check : coldata) {
        		if (check.toLowerCase().equals(s.toLowerCase())) {
        			matched = true;
        			break;
        		}
        	}
        if (!matched) {
            toLog += "\n Data Mismatch for barcode: " + currentRow.barcode;
            System.out.println("Data Mismatch!!!!!" + s.toLowerCase());
            System.out.println(currentRow);
            return false;
        }
    }
        return true;
    	} catch(NullPointerException e) {
            waitForAndClickElement(By.cssSelector("button.btn.btn-default.btnCancel"));  
            toLog += "\n [" + currentRow.barcode + "]	Data couldn't be verified NullPoint Exception";
            return false;
         }
    	}
    	

    private static void checkIfVendorNameNotFilled() throws TimeoutException, IOException, InterruptedException {
        boolean isVendorNameBlank = isElementDisplayed(driver, By.xpath("//div[@class='bootbox-body' and contains(text(),'Vendor Name is required')]"));
        if(isVendorNameBlank) {
            waitForAndClickElement(By.cssSelector("button[data-bb-handler='ok']"));  
            WebElement vendorName = driver.findElement(By.name("cboVendor_input"));
            String vendorNameValue = vendorName.getAttribute("value");
            waitForAndSendKeys(By.name("cboVendor_input"), "Unknown");
            toLog += "\n vendorName " + vendorNameValue + "    replaced with: 	Unknown";
            System.out.println("Vendor Name:   " + vendorNameValue);
            waitForAndClickElement(By.cssSelector("button.btn.btn-primary.btnSave")); 
    		Thread.sleep(5000); 
        }   
        
    }
    private static void checkIfVendorCatalogNotFilled() throws TimeoutException, IOException, InterruptedException {
        boolean isVendorCatalogBlank = isElementDisplayed(driver, By.xpath("//div[@class='bootbox-body' and contains(text(),'Vendor Catalog # is required')]"));
        if(isVendorCatalogBlank) {
            waitForAndClickElement(By.cssSelector("button[data-bb-handler='ok']"));  
            WebElement vendorCatalog = driver.findElement(By.id("txtVendorCatalog"));
            String vendorCatalogValue = vendorCatalog.getAttribute("value");
            waitForAndSendKeys(By.name("txtVendorCatalog"), "####");
            toLog += "\n VendorCatalog " + vendorCatalogValue + "    replaced with: 	####";
            System.out.println("Vendor Catalog:   " + vendorCatalogValue);
            waitForAndClickElement(By.cssSelector("button.btn.btn-primary.btnSave")); 
    		Thread.sleep(5000); 
        }
    }
    
    public static boolean isElementDisplayed(WebDriver driver, By by) {
        try {
            WebElement element = driver.findElement(by);
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    private static WebElement findCorrectRow(List<WebElement> rows, ChemicalData currentRow) {
    	// temp List for parsing
        List<String> coldata = new ArrayList<String>();
    	boolean correctRow = false;

        //for loop loop that goes over all the rows
    	for (WebElement theRowThatIsBeingChecked : rows) {
    		List<WebElement> cols = theRowThatIsBeingChecked.findElements(By.tagName("td"));
    		//Sets up a List with all the column data
    			for (WebElement col : cols) {
    				if(!col.getText().equals("")) {
    					coldata.add(col.getText());
    				}
    			}
    		//Code to check if theRowThatIsBeingChecked contains barcode
            for (String check : coldata) {
                if (check.toLowerCase().equals(currentRow.barcode.toLowerCase())) {
                	correctRow = true;
                    break;
                }
            }
            if(correctRow) {
            	return theRowThatIsBeingChecked;

        }
        
}
        toLog += "\n No matching row found for barcode: " + currentRow.barcode;
        System.out.println("No matching row found for barcode: " + currentRow.barcode);
		return null;
}
}



