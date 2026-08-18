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

public class Java20TrickyInterviewQ {

    private WebDriver driver;
    private WebDriverWait wait;

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public Java20TrickyInterviewQ(WebDriver driver) {

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

    // Java Top 20 Trick Interview Questions card
    private By javaInterviewProgramsCard =
            By.xpath(
                    "//div[contains(@class,'note-list-card')]" +
                            "[.//h4[normalize-space()='Java Top 20 Trick Interview Questions']]"
            );

    // Preview button
    private By javaInterviewProgramsButton =
            By.cssSelector(
                    "button[aria-label='Preview Java Top 20 Trick Interview Questions']"
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

        System.out.println("Notes page opened successfully.");
    }

    // =========================================================
    // OPEN JAVA TOP 20 TRICK INTERVIEW QUESTIONS
    // =========================================================

    public void clickJavaImportantInterviewPrograms() {

        WebElement card =
                wait.until(
                        ExpectedConditions.visibilityOfElementLocated(
                                javaInterviewProgramsCard
                        )
                );

        // Scroll card into view
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center'});",
                card
        );

        // Find Preview button inside the card
        WebElement previewButton =
                card.findElement(javaInterviewProgramsButton);

        // Scroll button into view
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
                "Java Top 20 Trick Interview Questions preview opened successfully."
        );
    }

    // =========================================================
    // DOWNLOAD ALL PAGES
    // =========================================================

    public void downloadAllJavaInterviewProgramsPages() {

        String downloadFolder =
                "downloaded-notes/Java-Top-20-Trick-Interview-Questions";

        File folder = new File(downloadFolder);

        if (!folder.exists()) {
            folder.mkdirs();
        }

        int downloadedPages = 0;

        while (true) {

            try {

                // -------------------------------------------------
                // WAIT FOR CURRENT IMAGE
                // -------------------------------------------------

                WebElement image =
                        wait.until(
                                ExpectedConditions.visibilityOfElementLocated(
                                        noteImage
                                )
                        );

                // -------------------------------------------------
                // GET PAGE COUNTER
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
                // SPLIT COUNTER
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
                // FILE PATH
                // -------------------------------------------------

                String filePath =
                        downloadFolder +
                                "/Java-Top-20-Trick-Interview-Questions-Page-" +
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
                            "All pages downloaded successfully!"
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

                final int previousPage = currentPage;

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

                // Also wait for image to be visible
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
                        "ERROR while downloading notes."
                );

                e.printStackTrace();

                throw new RuntimeException(
                        "Notes download failed after " +
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