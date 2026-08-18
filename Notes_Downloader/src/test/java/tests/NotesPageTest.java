package tests;

import base.BaseClass;
import org.testng.annotations.Test;
import pages.NoteData;
import pages.NotesPage;

import java.util.Arrays;
import java.util.List;

public class NotesPageTest extends BaseClass {

    @Test
    public void downloadAllNotes() {

        // =====================================================
        // CREATE NOTES PAGE OBJECT
        // =====================================================

        NotesPage notesPage =
                new NotesPage(driver);

        // =====================================================
        // NOTES LIST
        // =====================================================

        List<NoteData> notes =
                Arrays.asList(

//                        new NoteData(
//                                "Playwright Handbook",
//                                "Preview Playwright Handbook",
//                                "Playwright Handbook-Notes"
//                        ),
//                        new NoteData(
//                                "Playwright Framework",
//                                "Preview Playwright Framework",
//                                "Playwright Framework-Notes"
//                        ),
//                        new NoteData(
//                                "Playwright Top 35 Questions",
//                                "Preview Playwright Top 35 Questions",
//                                "Playwright Top 35 Questions"
//                        ),
//                        new NoteData(
//                                "Top 165 real Selenium Interview Questions",
//                                "Preview Top 165 real Selenium Interview Questions",
//                                "Top 165 real Selenium Interview Questions"
//                        ),
//                        new NoteData(
//                                "REST Assured Notes",
//                                "Preview REST Assured Notes",
//                                "REST Assured Notes"
//                        ),
                        new NoteData(
                                "JUnit and Mockito Notes",
                                "Preview JUnit and Mockito Notes",
                                "JUnit and Mockito Notes"
                        )


//        new NoteData(
//                "Low-Level Design (LLD)",
//                "Preview Low-Level Design (LLD)",
//                "Low-Level Design (LLD)"
//        )

//                        new NoteData(
//                                "Maven Notes",
//                                "Preview Maven Notes",
//                                "Maven-Notes"
//                        )
                );

        // =====================================================
        // OPEN NOTES PAGE
        // =====================================================

        notesPage.clickNotesNavigation();

        // =====================================================
        // LOOP THROUGH ALL NOTES
        // =====================================================

        for (NoteData note : notes) {

            System.out.println(
                    "\n===================================="
            );

            System.out.println(
                    "Starting note: " +
                            note.getNoteName()
            );

            System.out.println(
                    "===================================="
            );

            // -------------------------------------------------
            // OPEN NOTE
            // -------------------------------------------------

            notesPage.openNote(note);

            // -------------------------------------------------
            // DOWNLOAD ALL IMAGE PAGES
            // LAST PAGE WILL BE SKIPPED
            // -------------------------------------------------

            notesPage.downloadAllPages(note);

            System.out.println(
                    "Finished note: " +
                            note.getNoteName()
            );
        }

        // =====================================================
        // ALL NOTES COMPLETED
        // =====================================================

        System.out.println(
                "\n===================================="
        );

        System.out.println(
                "ALL NOTES PROCESSED SUCCESSFULLY!"
        );

        System.out.println(
                "===================================="
        );
    }
}