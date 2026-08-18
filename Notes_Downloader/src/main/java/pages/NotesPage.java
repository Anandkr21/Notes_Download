package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.FileOutputStream;
import java.time.Duration;
import java.util.Base64;

public class NotesPage {

    private WebDriver driver;
    private WebDriverWait wait;

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public NotesPage(WebDriver driver) {

        this.driver = driver;

        this.wait =
                new WebDriverWait(
                        driver,
                        Duration.ofSeconds(20)
                );
    }

    // =========================================================
    // COMMON LOCATORS
    // =========================================================

    private By notesNavigation =
            By.xpath("//a[normalize-space()='Notes']");

    private By notesGrid =
            By.cssSelector(".notes-list-grid");

    // Image inside preview
    private By noteImage =
            By.cssSelector(".note-preview-page img");

    // Example: 1 / 58
    private By pageCounter =
            By.cssSelector(
                    ".note-preview-navigation span"
            );

    // Next button
    private By nextButton =
            By.cssSelector(
                    ".note-preview-navigation button:last-child"
            );

    // =========================================================
    // NAVIGATE TO NOTES
    // =========================================================

    public void clickNotesNavigation() {

        wait.until(
                ExpectedConditions.elementToBeClickable(
                        notesNavigation
                )
        ).click();

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        notesGrid
                )
        );

        System.out.println(
                "Notes page opened successfully."
        );
    }

    // =========================================================
    // OPEN NOTE
    // =========================================================

    public void openNote(NoteData note) {

        // ---------------------------------------------------------
        // DYNAMIC CARD LOCATOR
        // ---------------------------------------------------------

        By noteCard =
                By.xpath(
                        "//div[contains(@class,'note-list-card')]" +
                                "[.//h4[normalize-space()='" +
                                note.getNoteName() +
                                "']]"
                );

        // ---------------------------------------------------------
        // FIND CARD
        // ---------------------------------------------------------

        WebElement card =
                wait.until(
                        ExpectedConditions.visibilityOfElementLocated(
                                noteCard
                        )
                );

        // ---------------------------------------------------------
        // SCROLL CARD INTO VIEW
        // ---------------------------------------------------------

        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center'});",
                card
        );

        // ---------------------------------------------------------
        // DYNAMIC BUTTON LOCATOR
        // ---------------------------------------------------------

        By previewButton =
                By.cssSelector(
                        "button[aria-label='" +
                                note.getButtonAriaLabel() +
                                "']"
                );

        // ---------------------------------------------------------
        // FIND BUTTON INSIDE CARD
        // ---------------------------------------------------------

        WebElement button =
                card.findElement(
                        previewButton
                );

        // ---------------------------------------------------------
        // SCROLL BUTTON INTO VIEW
        // ---------------------------------------------------------

        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center'});",
                button
        );

        // ---------------------------------------------------------
        // WAIT UNTIL CLICKABLE
        // ---------------------------------------------------------

        wait.until(
                ExpectedConditions.elementToBeClickable(
                        button
                )
        );

        // ---------------------------------------------------------
        // CLICK BUTTON
        // ---------------------------------------------------------

        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].click();",
                button
        );

        // ---------------------------------------------------------
        // WAIT FOR PREVIEW IMAGE
        // ---------------------------------------------------------

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        noteImage
                )
        );

        // ---------------------------------------------------------
        // WAIT FOR PAGE COUNTER
        // ---------------------------------------------------------

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        pageCounter
                )
        );

        System.out.println(
                "Opened note: " +
                        note.getNoteName()
        );
    }

    // =========================================================
    // DOWNLOAD ALL IMAGE PAGES
    // LAST PAGE WILL BE SKIPPED
    // =========================================================

    public void downloadAllPages(NoteData note) {

        // ---------------------------------------------------------
        // DOWNLOAD FOLDER
        // ---------------------------------------------------------

        String downloadFolder =
                "downloaded-notes/" +
                        note.getFolderName();

        // ---------------------------------------------------------
        // CREATE FOLDER
        // ---------------------------------------------------------

        File folder =
                new File(downloadFolder);

        if (!folder.exists()) {

            boolean created =
                    folder.mkdirs();

            if (!created) {

                throw new RuntimeException(
                        "Unable to create folder: " +
                                downloadFolder
                );
            }
        }

        int downloadedPages = 0;

        // =========================================================
        // PAGE LOOP
        // =========================================================

        while (true) {

            try {

                // -------------------------------------------------
                // GET PAGE COUNTER FIRST
                // -------------------------------------------------
                // IMPORTANT:
                // We check the page number BEFORE looking for
                // an image.
                //
                // This allows us to skip the last page because
                // the last page is not an image.
                // -------------------------------------------------

                String counter =
                        wait.until(
                                ExpectedConditions.visibilityOfElementLocated(
                                        pageCounter
                                )
                        ).getText();

                System.out.println(
                        "Current page: " +
                                counter
                );

                // -------------------------------------------------
                // SPLIT PAGE COUNTER
                // Example:
                //
                // 1 / 58
                // -------------------------------------------------

                String[] parts =
                        counter.split("/");

                if (parts.length < 2) {

                    throw new RuntimeException(
                            "Invalid page counter: " +
                                    counter
                    );
                }

                int currentPage =
                        Integer.parseInt(
                                parts[0].trim()
                        );

                int totalPages =
                        Integer.parseInt(
                                parts[1].trim()
                        );

                System.out.println(
                        "Current page: " +
                                currentPage +
                                " of " +
                                totalPages
                );

                // =================================================
                // LAST PAGE CHECK
                // =================================================
                //
                // Example:
                //
                // Current page = 58
                // Total pages  = 58
                //
                // We do NOT search for image.
                // We simply skip this page and finish.
                // =================================================

                if (currentPage >= totalPages) {

                    System.out.println(
                            "===================================="
                    );

                    System.out.println(
                            "Last page detected."
                    );

                    System.out.println(
                            "Skipping last page because " +
                                    "it is not an image."
                    );

                    System.out.println(
                            "Note: " +
                                    note.getNoteName()
                    );

                    System.out.println(
                            "Total pages downloaded: " +
                                    downloadedPages
                    );

                    System.out.println(
                            "Download completed successfully!"
                    );

                    System.out.println(
                            "===================================="
                    );

                    break;
                }

                // -------------------------------------------------
                // WAIT FOR CURRENT IMAGE
                // -------------------------------------------------

                WebElement image =
                        wait.until(
                                ExpectedConditions.visibilityOfElementLocated(
                                        noteImage
                                )
                        );

                System.out.println(
                        "Downloading page " +
                                currentPage +
                                " of " +
                                totalPages
                );

                // -------------------------------------------------
                // GET IMAGE AS BASE64
                // -------------------------------------------------

                String base64Image =
                        getImageAsBase64(image);

                if (base64Image == null ||
                        base64Image.startsWith("ERROR:")) {

                    throw new RuntimeException(
                            "Failed to get image for page " +
                                    currentPage +
                                    ". " +
                                    base64Image
                    );
                }

                // -------------------------------------------------
                // CONVERT BASE64 TO BYTES
                // -------------------------------------------------

                byte[] imageBytes =
                        Base64.getDecoder().decode(
                                base64Image
                        );

                // -------------------------------------------------
                // CREATE FILE PATH
                // -------------------------------------------------

                String filePath =
                        downloadFolder +
                                "/" +
                                note.getFolderName() +
                                "-Page-" +
                                currentPage +
                                ".png";

                // -------------------------------------------------
                // SAVE IMAGE
                // -------------------------------------------------

                try (FileOutputStream output =
                             new FileOutputStream(filePath)) {

                    output.write(imageBytes);
                }

                downloadedPages++;

                System.out.println(
                        "Downloaded: " +
                                filePath
                );

                // -------------------------------------------------
                // GET NEXT BUTTON
                // -------------------------------------------------

                WebElement next =
                        wait.until(
                                ExpectedConditions.presenceOfElementLocated(
                                        nextButton
                                )
                        );

                // -------------------------------------------------
                // SCROLL NEXT BUTTON INTO VIEW
                // -------------------------------------------------

                ((JavascriptExecutor) driver).executeScript(
                        "arguments[0].scrollIntoView({block:'center'});",
                        next
                );

                // -------------------------------------------------
                // CLICK NEXT
                // -------------------------------------------------

                ((JavascriptExecutor) driver).executeScript(
                        "arguments[0].click();",
                        next
                );

                // -------------------------------------------------
                // WAIT FOR PAGE NUMBER TO CHANGE
                // -------------------------------------------------

                final int previousPage =
                        currentPage;

                wait.until(driver -> {

                    try {

                        WebElement counterElement =
                                driver.findElement(
                                        pageCounter
                                );

                        String newCounter =
                                counterElement.getText();

                        if (newCounter == null ||
                                !newCounter.contains("/")) {

                            return false;
                        }

                        String[] newParts =
                                newCounter.split("/");

                        int newPage =
                                Integer.parseInt(
                                        newParts[0].trim()
                                );

                        return newPage > previousPage;

                    } catch (Exception e) {

                        return false;
                    }
                });

                System.out.println(
                        "Next page loaded successfully."
                );

            } catch (Exception e) {

                System.out.println(
                        "ERROR while downloading note: " +
                                note.getNoteName()
                );

                e.printStackTrace();

                throw new RuntimeException(
                        "Download failed for: " +
                                note.getNoteName() +
                                " after " +
                                downloadedPages +
                                " pages.",
                        e
                );
            }
        }
    }

    // =========================================================
    // CONVERT IMAGE TO BASE64
    // JAVA 11 COMPATIBLE
    // =========================================================

    private String getImageAsBase64(
            WebElement image) {

        try {

            JavascriptExecutor js =
                    (JavascriptExecutor) driver;

            String script =
                    "const image = arguments[0];" +
                            "const callback = arguments[arguments.length - 1];" +

                            "fetch(image.src)" +
                            ".then(response => response.blob())" +

                            ".then(blob => {" +

                            "    const reader = new FileReader();" +

                            "    reader.onloadend = function() {" +

                            "        const result = " +
                            "reader.result.split(',')[1];" +

                            "        callback(result);" +

                            "    };" +

                            "    reader.readAsDataURL(blob);" +

                            "})" +

                            ".catch(error => {" +

                            "    callback('ERROR:' + error);" +

                            "});";

            Object result =
                    js.executeAsyncScript(
                            script,
                            image
                    );

            if (result == null) {

                return "ERROR: Image conversion returned null";
            }

            return result.toString();

        } catch (Exception e) {

            e.printStackTrace();

            return "ERROR: " +
                    e.getMessage();
        }
    }
}