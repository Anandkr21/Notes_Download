package tests;

import base.BaseClass;
import org.testng.annotations.Test;
import pages.JavaOOPsPage;

public class JavaOOPsPageTest extends BaseClass {

    @Test
    public void downloadOOPsConcepts() {

        JavaOOPsPage javaOOPsPage =
                new JavaOOPsPage(driver);

        // Open Notes
        javaOOPsPage.clickNotesNavigation();

        // Open OOPS Concepts
        javaOOPsPage.clickOOPsConcepts();

        // Download all pages
        javaOOPsPage.downloadAllOOPsPages();
    }
}