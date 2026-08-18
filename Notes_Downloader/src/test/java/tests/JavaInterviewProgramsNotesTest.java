package tests;

import base.BaseClass;
import org.testng.annotations.Test;
import pages.JavaInterviewProgramsNotes;

public class JavaInterviewProgramsNotesTest extends BaseClass {

    @Test
    public void downloadJavaImportantInterviewPrograms() {

        JavaInterviewProgramsNotes notes =
                new JavaInterviewProgramsNotes(driver);

        notes.clickNotesNavigation();

        notes.clickJavaImportantInterviewPrograms();

        notes.downloadAllJavaInterviewProgramsPages();
    }
}