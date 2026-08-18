package tests;

import base.BaseClass;
import org.testng.annotations.Test;
import pages.Java20TrickyInterviewQ;

public class Java20TrickyInterviewQTest extends BaseClass {

    @Test
    public void downloadJava20TrickyInterviewQuestions() {

        Java20TrickyInterviewQ java20TrickyInterviewQ =
                new Java20TrickyInterviewQ(driver);

        // Open Notes
        java20TrickyInterviewQ.clickNotesNavigation();

        // Open Java Top 20 Trick Interview Questions
        java20TrickyInterviewQ.clickJavaImportantInterviewPrograms();

        // Download all 58 pages
        java20TrickyInterviewQ.downloadAllJavaInterviewProgramsPages();
    }
}