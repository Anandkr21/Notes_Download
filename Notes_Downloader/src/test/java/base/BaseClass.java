package base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import utils.ConfigReader;

import java.time.Duration;

public class BaseClass {
    protected WebDriver driver;

    @BeforeMethod
    public void setup(){
        driver = new ChromeDriver();

        driver.manage()
                .window()
                .maximize();

        driver.manage()
                .timeouts()
                .implicitlyWait(
                        Duration.ofSeconds(
                                Long.parseLong(ConfigReader.getProperty("implicitWait"))
                        )
                );

        driver.get(ConfigReader.getProperty("url"));
    }


    @AfterMethod
    public void tearDown(){
        if(driver !=null){
            driver.quit();
        }
    }
}
