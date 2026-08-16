package tests;

import base.BaseClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.HomePage;

public class HomePageTest extends BaseClass {

    private HomePage homePage;

    @BeforeMethod
    public void setUpPage(){
        homePage = new HomePage(driver);
    }

    @Test
    public void verifyHomePage(){
        homePage.clickHomeNavigation();
        System.out.println("Clicked");
    }
}
