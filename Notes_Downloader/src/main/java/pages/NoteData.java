package pages;

public class NoteData {

    private String noteName;
    private String buttonAriaLabel;
    private String folderName;

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public NoteData(
            String noteName,
            String buttonAriaLabel,
            String folderName) {

        this.noteName = noteName;
        this.buttonAriaLabel = buttonAriaLabel;
        this.folderName = folderName;
    }

    // =========================================================
    // GETTERS
    // =========================================================

    public String getNoteName() {
        return noteName;
    }

    public String getButtonAriaLabel() {
        return buttonAriaLabel;
    }

    public String getFolderName() {
        return folderName;
    }
}