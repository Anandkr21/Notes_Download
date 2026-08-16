package tests;

import base.BaseClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.NotesPage;

public class NotesPageTest extends BaseClass {

    private NotesPage notesPage;

    @BeforeMethod
    public void setUpPage() {
        notesPage = new NotesPage(driver);
    }

    @Test
    public void downloadJavaFundamentalsNotes() {

        notesPage.clickNotesNavigation();

        notesPage.openJavaFundamentals();

        notesPage.downloadAllPages();
    }
}