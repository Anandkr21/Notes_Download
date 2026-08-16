package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HomePage {

    // WebDriver
    private WebDriver driver;

    // Constructor
    public HomePage(WebDriver driver){
        this.driver = driver;
    }

    // Locoators
    private By homeNavigation = By.xpath("//a[text()='Home']");



    // Methods
    public void clickHomeNavigation(){
        driver.findElement(homeNavigation).click();
    }
}
