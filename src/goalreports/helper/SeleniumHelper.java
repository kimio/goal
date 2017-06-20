package goalreports.helper;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 *
 * @author felipekimionishikaku
 */
public class SeleniumHelper {
    public enum Browser {
        Firefox("Firefox");
        public String browser;
        Browser(String valor) {
            browser = valor;
        }
    }
    public WebDriver driver;
    public SeleniumHelper(Browser browser,String pathFirefox){
        if(browser.toString().equals("Firefox")){
            System.setProperty("webdriver.firefox.bin",pathFirefox);
            System.setProperty("webdriver.gecko.driver","geckodriver");
            driver = new FirefoxDriver();
        }
    }
    public SeleniumHelper(WebDriver driver){
        this.driver = driver;
    }
    protected void openUrl(String url){
        driver.get(url);
    }
    protected void setValueByIdElement(String id,String value){
        callJs("document.getElementById('"+id+"').value='"+value+"';");
    }
    /**
     * Focus and send int keyevent 
     * @param id id from element id
     * @param key keyevent
     */
    protected void setValueByIdElement(String id,int key){
        Robot robot;
        try {
            callJs("document.getElementById('"+id+"').focus();");
            robot = new Robot();
            robot.keyPress(key);
        } catch (AWTException ex) {
            Logger.getLogger(SeleniumHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    protected WebElement fluentWait(final By locator) {
        WebElement foo = null;
        try {
            Wait<WebDriver> wait = new FluentWait<>(driver)
                    .withTimeout(240, TimeUnit.SECONDS)
                    .pollingEvery(3, TimeUnit.SECONDS)
                    .ignoring(NoSuchElementException.class);

            foo = wait.until((WebDriver driver1) -> driver1.findElement(locator));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return  foo;
    }
    
    protected void waitForPageLoad() {
        Wait<WebDriver> wait = new WebDriverWait(driver, 30);
        wait.until((WebDriver driver1) -> {
            System.out.println("Current Window State: " + String.valueOf(((JavascriptExecutor) driver1).executeScript("return document.readyState")));
            return String.valueOf(((JavascriptExecutor) driver1).executeScript("return document.readyState")).equals("complete");
        });
    }
    protected void callJs(String js){
        ((JavascriptExecutor) driver).executeScript(js);
    }
    protected String captureScreen(By locator,String currentReport) {
        String path;
        try {
            WebElement ele = driver.findElement(locator);

            // Get entire page screenshotpath = source.getName();
            File screenshot = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
            
            BufferedImage  fullImg = ImageIO.read(screenshot);

            // Get the location of element on the page
            Point point = ele.getLocation();

            // Get width and height of the element
            int eleWidth = ele.getSize().getWidth();
            int eleHeight = ele.getSize().getHeight();

            // Crop the entire page screenshot to get only element screenshot
            BufferedImage eleScreenshot = fullImg.getSubimage(point.getX(), point.getY(),
                eleWidth, eleHeight);
            ImageIO.write(eleScreenshot, "png", screenshot);

            path = currentReport+screenshot.getName();
            
            // Copy the element screenshot to disk
            FileUtils.copyFile(screenshot, new File(path));
        }
        catch(IOException | WebDriverException e) {
            path = null;
        }
        return path;
    }
}
