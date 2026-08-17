package tests;

import base.BaseClass;
import org.testng.annotations.Test;
import pages.JavaHandBookNotes;

public class JavaHandBookNotesTest extends BaseClass {

    @Test
    public void downloadJavaCollectionsHandbook() {

        JavaHandBookNotes notes =
                new JavaHandBookNotes(driver);

        // Navigate to Notes
        notes.clickNotesNavigation();

        // Open Java Collections Handbook
        notes.clickJavaCollectionsHandbook();

        // Download all pages
        notes.downloadAllJavaCollectionsPages();
    }
}