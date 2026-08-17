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

public class JavaHandBookNotes {

    private WebDriver driver;
    private WebDriverWait wait;

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public JavaHandBookNotes(WebDriver driver) {
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

    // Java Collections Handbook card
    private By javaCollectionsCard =
            By.xpath(
                    "//div[contains(@class,'note-list-card')]" +
                            "[.//*[normalize-space()='Java Collections Handbook']]"
            );

    // Preview button
    private By javaCollectionsButton =
            By.cssSelector(
                    "button[aria-label='Preview Java Collections Handbook']"
            );

    // Image inside preview
    private By noteImage =
            By.cssSelector(".note-preview-page img");

    // Example: "1 / 58"
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
    }

    // =========================================================
    // OPEN JAVA COLLECTIONS HANDBOOK
    // =========================================================

    public void clickJavaCollectionsHandbook() {

        // Find Java Collections Handbook card
        WebElement card = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        javaCollectionsCard
                )
        );

        // Scroll card into view
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center'});",
                card
        );

        // Find Preview button inside the card
        WebElement previewButton =
                card.findElement(javaCollectionsButton);

        // Scroll Preview button into view
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center'});",
                previewButton
        );

        // Wait until button is clickable
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

        System.out.println(
                "Java Collections Handbook preview opened successfully."
        );
    }

    // =========================================================
    // DOWNLOAD ALL PAGES
    // =========================================================

    public void downloadAllJavaCollectionsPages() {

        // Folder where notes will be saved
        String downloadFolder =
                "downloaded-notes/Java-Collections";

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

                    System.out.println(
                            "Invalid page counter: " + counter
                    );

                    break;
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

                    System.out.println(
                            "Failed to get image for page " +
                                    currentPage
                    );

                    break;
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
                                "/Java-Collections-Page-" +
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
                            "All pages downloaded!"
                    );

                    System.out.println(
                            "Total pages: " +
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
                // STORE CURRENT IMAGE SOURCE
                // -------------------------------------------------

                String oldSrc =
                        image.getAttribute("src");

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
                // WAIT FOR NEXT PAGE
                // -------------------------------------------------

                final String previousSrc = oldSrc;

                wait.until(driver -> {

                    try {

                        WebElement newImage =
                                driver.findElement(noteImage);

                        String newSrc =
                                newImage.getAttribute("src");

                        return newSrc != null &&
                                !newSrc.equals(previousSrc);

                    } catch (Exception e) {

                        return false;
                    }
                });

                System.out.println(
                        "Next page loaded successfully."
                );

            } catch (Exception e) {

                System.out.println(
                        "ERROR while downloading page."
                );

                e.printStackTrace();

                break;
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

                            "        const result =" +
                            "            reader.result.split(',')[1];" +

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

            return "ERROR:" + e.getMessage();
        }
    }
}