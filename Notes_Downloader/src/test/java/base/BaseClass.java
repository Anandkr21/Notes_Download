package base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import utils.ConfigReader;

import java.time.Duration;

public class BaseClass {

    protected WebDriver driver;

    @BeforeMethod
    public void setup() {

        // Edge options
        EdgeOptions options = new EdgeOptions();

        // Open Edge in InPrivate mode
        options.addArguments("--inprivate");

        // Start Edge
        driver = new EdgeDriver(options);

        // Maximize browser
        driver.manage()
                .window()
                .maximize();

        // Implicit wait
        driver.manage()
                .timeouts()
                .implicitlyWait(
                        Duration.ofSeconds(
                                Long.parseLong(
                                        ConfigReader.getProperty("implicitWait")
                                )
                        )
                );

        // Open application
        driver.get(
                ConfigReader.getProperty("url")
        );
    }

    @AfterMethod
    public void tearDown() {

        if (driver != null) {
            driver.quit();
        }
    }
}