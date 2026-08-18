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

public class JavaOOPsPage {

    private WebDriver driver;
    private WebDriverWait wait;

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public JavaOOPsPage(WebDriver driver) {

        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    // =========================================================
    // LOCATORS
    // =========================================================

    private By notesNavigation =
            By.xpath("//a[normalize-space()='Notes']");

    private By notesGrid =
            By.cssSelector(".notes-list-grid");

    // OOPS Concepts card
    private By javaOOPsCard =
            By.xpath(
                    "//div[contains(@class,'note-list-card')]" +
                            "[.//h4[normalize-space()='OOPS Concepts']]"
            );

    // Preview button
    private By javaOOPsButton =
            By.cssSelector(
                    "button[aria-label='Preview OOPS Concepts']"
            );

    // Image inside preview
    private By noteImage =
            By.cssSelector(".note-preview-page img");

    // Example: 1 / 58
    private By pageCounter =
            By.cssSelector(".note-preview-navigation span");

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
    // OPEN OOPS CONCEPTS
    // =========================================================

    public void clickOOPsConcepts() {

        WebElement card =
                wait.until(
                        ExpectedConditions.visibilityOfElementLocated(
                                javaOOPsCard
                        )
                );

        // Scroll card into view
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center'});",
                card
        );

        // Find Preview button inside card
        WebElement previewButton =
                card.findElement(javaOOPsButton);

        // Scroll Preview button into view
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center'});",
                previewButton
        );

        // Wait until clickable
        wait.until(
                ExpectedConditions.elementToBeClickable(
                        previewButton
                )
        );

        // Click Preview
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].click();",
                previewButton
        );

        // Wait for preview image
        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        noteImage
                )
        );

        // Wait for page counter
        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        pageCounter
                )
        );

        System.out.println(
                "OOPS Concepts preview opened successfully."
        );
    }

    // =========================================================
    // DOWNLOAD ALL PAGES
    // =========================================================

    public void downloadAllOOPsPages() {

        // Folder where notes will be saved
        String downloadFolder =
                "downloaded-notes/OOPS-Concepts";

        // Create folder
        File folder =
                new File(downloadFolder);

        if (!folder.exists()) {
            folder.mkdirs();
        }

        int downloadedPages = 0;

        while (true) {

            try {

                // -------------------------------------------------
                // WAIT FOR CURRENT PAGE IMAGE
                // -------------------------------------------------

                WebElement image =
                        wait.until(
                                ExpectedConditions.visibilityOfElementLocated(
                                        noteImage
                                )
                        );

                // -------------------------------------------------
                // GET CURRENT PAGE NUMBER
                // Example: 1 / 58
                // -------------------------------------------------

                String counter =
                        wait.until(
                                ExpectedConditions.visibilityOfElementLocated(
                                        pageCounter
                                )
                        ).getText();

                System.out.println(
                        "Current page: " + counter
                );

                // -------------------------------------------------
                // SPLIT PAGE COUNTER
                // -------------------------------------------------

                String[] parts =
                        counter.split("/");

                if (parts.length < 2) {

                    throw new RuntimeException(
                            "Invalid page counter: " + counter
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
                        "Downloading page " +
                                currentPage +
                                " of " +
                                totalPages
                );

                // -------------------------------------------------
                // GET IMAGE DATA
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
                                "/OOPS-Concepts-Page-" +
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
                // CHECK LAST PAGE
                // -------------------------------------------------

                if (currentPage >= totalPages) {

                    System.out.println(
                            "===================================="
                    );

                    System.out.println(
                            "All OOPS Concepts pages downloaded!"
                    );

                    System.out.println(
                            "Total pages downloaded: " +
                                    downloadedPages
                    );

                    System.out.println(
                            "===================================="
                    );

                    break;
                }

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
                // WAIT FOR PAGE COUNTER TO CHANGE
                // -------------------------------------------------

                final int previousPage =
                        currentPage;

                wait.until(driver -> {

                    try {

                        WebElement counterElement =
                                driver.findElement(pageCounter);

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

                // -------------------------------------------------
                // WAIT FOR NEXT IMAGE
                // -------------------------------------------------

                wait.until(
                        ExpectedConditions.visibilityOfElementLocated(
                                noteImage
                        )
                );

                System.out.println(
                        "Next page loaded successfully."
                );

            } catch (Exception e) {

                System.out.println(
                        "ERROR while downloading OOPS Concepts."
                );

                e.printStackTrace();

                throw new RuntimeException(
                        "OOPS Concepts download failed after " +
                                downloadedPages +
                                " pages.",
                        e
                );
            }
        }
    }

    // =========================================================
    // CONVERT BLOB IMAGE TO BASE64
    // JAVA 11 COMPATIBLE
    // =========================================================

    private String getImageAsBase64(WebElement image) {

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

            return "ERROR: " + e.getMessage();
        }
    }
}