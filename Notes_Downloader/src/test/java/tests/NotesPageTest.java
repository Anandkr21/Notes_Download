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

        NotesPage notesPage =
                new NotesPage(driver);

        // =====================================================
        // NOTES TO DOWNLOAD
        // =====================================================

        List<NoteData> notes =
                Arrays.asList(

                        new NoteData(
                                "Maven Notes",
                                "Preview Maven Notes",
                                "Maven-Notes"
                        )
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
                    "===================================="
            );

            System.out.println(
                    "Processing note: " +
                            note.getNoteName()
            );

            System.out.println(
                    "===================================="
            );

            // Open note
            notesPage.openNote(note);

            // Download all pages
            notesPage.downloadAllPages(note);
        }
    }
}