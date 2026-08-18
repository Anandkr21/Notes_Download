package tests;

import base.BaseClass;
import org.testng.annotations.Test;
import pages.JUnitAndMockitoNotesPage;

public class JUnitAndMockitoNotesPageTest extends BaseClass {

    @Test
    public void downloadJUnitAndMockitoNotes() {

        JUnitAndMockitoNotesPage junitAndMockitoNotesPage =
                new JUnitAndMockitoNotesPage(driver);

        // Open Notes
        junitAndMockitoNotesPage.clickNotesNavigation();

        // Open JUnit and Mockito Notes
        junitAndMockitoNotesPage.clickJUnitAndMockitoNotes();

        // Download all pages
        junitAndMockitoNotesPage.downloadAllJUnitAndMockitoPages();
    }
}