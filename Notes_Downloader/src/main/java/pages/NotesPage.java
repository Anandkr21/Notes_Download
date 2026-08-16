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

    public NotesPage(WebDriver driver) {
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

    private By javaFundamentalsCard =
            By.xpath(
                    "//div[contains(@class,'note-list-card')]" +
                            "[.//*[normalize-space()='Java Fundamentals']]"
            );

    private By javaFundamentalsButton =
            By.cssSelector(
                    "button[aria-label='Preview Java Fundamentals']"
            );

    private By noteImage =
            By.cssSelector(".note-preview-page img");

    private By pageCounter =
            By.cssSelector(".note-preview-navigation span");

    private By nextButton =
            By.cssSelector(
                    ".note-preview-navigation button:last-child"
            );


    // =========================================================
    // DOWNLOAD FOLDER
    // =========================================================

    private File getDownloadFolder() {

        String folderPath =
                System.getProperty("user.dir")
                        + File.separator
                        + "downloaded-notes"
                        + File.separator
                        + "Java-Fundamentals";

        File folder = new File(folderPath);

        if (!folder.exists()) {
            folder.mkdirs();
        }

        return folder;
    }


    // =========================================================
    // CLICK NOTES
    // =========================================================

    public void clickNotesNavigation() {

        WebElement notes = wait.until(
                ExpectedConditions.elementToBeClickable(
                        notesNavigation
                )
        );

        notes.click();

        wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        notesGrid
                )
        );

        System.out.println("Notes page opened.");
    }


    // =========================================================
    // OPEN JAVA FUNDAMENTALS
    // =========================================================

    public void openJavaFundamentals() {

        try {

            WebElement card = wait.until(
                    ExpectedConditions.presenceOfElementLocated(
                            javaFundamentalsCard
                    )
            );

            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].scrollIntoView({block:'center'});",
                    card
            );

            Thread.sleep(500);

            WebElement button = wait.until(
                    ExpectedConditions.presenceOfElementLocated(
                            javaFundamentalsButton
                    )
            );

            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].scrollIntoView({block:'center'});",
                    button
            );

            Thread.sleep(500);

            /*
             * JavaScript click avoids the
             * ElementClickInterceptedException
             * we encountered earlier.
             */
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].click();",
                    button
            );

            System.out.println(
                    "Java Fundamentals opened."
            );

            wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            noteImage
                    )
            );

        } catch (Exception e) {

            System.out.println(
                    "Could not open Java Fundamentals."
            );

            e.printStackTrace();

            throw new RuntimeException(e);
        }
    }


    // =========================================================
    // FIND LAST DOWNLOADED PAGE
    // =========================================================

    private int getLastDownloadedPage(File folder) {

        int lastPage = 0;

        File[] files = folder.listFiles();

        if (files == null) {
            return 0;
        }

        for (File file : files) {

            String fileName =
                    file.getName();

            if (!fileName.startsWith("Page-")) {
                continue;
            }

            if (!fileName.toLowerCase().endsWith(".png")) {
                continue;
            }

            try {

                String number =
                        fileName
                                .replace("Page-", "")
                                .replace(".png", "");

                int page =
                        Integer.parseInt(number);

                if (page > lastPage) {
                    lastPage = page;
                }

            } catch (NumberFormatException ignored) {
                // Ignore files that don't follow Page-XX.png
            }
        }

        return lastPage;
    }


    // =========================================================
    // DOWNLOAD ALL PAGES WITH RESUME
    // =========================================================

    public void downloadAllPages() {

        try {

            File folder =
                    getDownloadFolder();

            int lastDownloaded =
                    getLastDownloadedPage(folder);

            int startPage =
                    lastDownloaded + 1;


            System.out.println();
            System.out.println(
                    "=========================================="
            );

            System.out.println(
                    "DOWNLOAD FOLDER:"
            );

            System.out.println(
                    folder.getAbsolutePath()
            );

            System.out.println(
                    "LAST DOWNLOADED PAGE: "
                            + lastDownloaded
            );

            System.out.println(
                    "STARTING FROM PAGE: "
                            + startPage
            );

            System.out.println(
                    "=========================================="
            );


            // =================================================
            // DOWNLOAD LOOP
            // =================================================

            while (true) {

                // ---------------------------------------------
                // GET CURRENT PAGE COUNTER
                // ---------------------------------------------

                String currentCounter =
                        wait.until(
                                        ExpectedConditions
                                                .visibilityOfElementLocated(
                                                        pageCounter
                                                )
                                )
                                .getText();

                System.out.println();
                System.out.println(
                        "Current page: "
                                + currentCounter
                );


                int currentPage =
                        extractCurrentPage(
                                currentCounter
                        );

                int totalPages =
                        extractTotalPages(
                                currentCounter
                        );


                // ---------------------------------------------
                // IF CURRENT PAGE IS ALREADY DOWNLOADED
                // MOVE FORWARD
                // ---------------------------------------------

                if (currentPage < startPage) {

                    System.out.println(
                            "Page "
                                    + currentPage
                                    + " already downloaded."
                    );

                    clickNextAndWait(
                            currentCounter
                    );

                    continue;
                }


                // ---------------------------------------------
                // GET CURRENT IMAGE
                // ---------------------------------------------

                WebElement image =
                        wait.until(
                                ExpectedConditions
                                        .visibilityOfElementLocated(
                                                noteImage
                                        )
                        );


                // ---------------------------------------------
                // WAIT FOR IMAGE TO FINISH LOADING
                // ---------------------------------------------

                wait.until(driver -> {

                    try {

                        WebElement img =
                                driver.findElement(
                                        noteImage
                                );

                        Boolean loaded =
                                (Boolean)
                                        ((JavascriptExecutor)
                                                driver)
                                                .executeScript(
                                                        "return arguments[0].complete " +
                                                                "&& arguments[0].naturalWidth > 0;",
                                                        img
                                                );

                        return Boolean.TRUE.equals(
                                loaded
                        );

                    } catch (Exception e) {

                        return false;
                    }
                });


                // ---------------------------------------------
                // DOWNLOAD IMAGE
                // ---------------------------------------------

                downloadImage(
                        image,
                        folder,
                        currentPage
                );


                // ---------------------------------------------
                // CHECK LAST PAGE
                // ---------------------------------------------

                if (currentPage >= totalPages) {

                    System.out.println();
                    System.out.println(
                            "=========================================="
                    );

                    System.out.println(
                            "ALL PAGES DOWNLOADED SUCCESSFULLY"
                    );

                    System.out.println(
                            "TOTAL PAGES: "
                                    + totalPages
                    );

                    System.out.println(
                            "=========================================="
                    );

                    break;
                }


                // ---------------------------------------------
                // CLICK NEXT
                // ---------------------------------------------

                clickNextAndWait(
                        currentCounter
                );
            }


        } catch (Exception e) {

            System.out.println();
            System.out.println(
                    "=========================================="
            );

            System.out.println(
                    "DOWNLOAD FAILED"
            );

            System.out.println(
                    "=========================================="
            );

            e.printStackTrace();

            throw new RuntimeException(e);
        }
    }


    // =========================================================
    // CLICK NEXT AND WAIT FOR PAGE NUMBER TO CHANGE
    // =========================================================

    private void clickNextAndWait(
            String oldCounter
    ) throws InterruptedException {

        WebElement next =
                wait.until(
                        ExpectedConditions
                                .presenceOfElementLocated(
                                        nextButton
                                )
                );


        // Check whether Next is disabled
        String disabled =
                next.getAttribute("disabled");

        if (!next.isEnabled()
                || disabled != null) {

            return;
        }


        ((JavascriptExecutor) driver)
                .executeScript(
                        "arguments[0].click();",
                        next
                );


        // ---------------------------------------------
        // WAIT FOR COUNTER TO CHANGE
        // ---------------------------------------------

        wait.until(driver -> {

            try {

                String newCounter =
                        driver.findElement(
                                pageCounter
                        ).getText();

                return !newCounter.equals(
                        oldCounter
                );

            } catch (Exception e) {

                return false;
            }
        });


        Thread.sleep(500);
    }


    // =========================================================
    // EXTRACT CURRENT PAGE
    // =========================================================

    private int extractCurrentPage(
            String counter
    ) {

        /*
         * Example:
         *
         * 44 / 58
         */

        String current =
                counter.split("/")[0].trim();

        return Integer.parseInt(current);
    }


    // =========================================================
    // EXTRACT TOTAL PAGES
    // =========================================================

    private int extractTotalPages(
            String counter
    ) {

        /*
         * Example:
         *
         * 44 / 58
         */

        String total =
                counter.split("/")[1].trim();

        return Integer.parseInt(total);
    }


    // =========================================================
    // DOWNLOAD IMAGE
    // =========================================================

    private void downloadImage(
            WebElement image,
            File folder,
            int pageNumber
    ) throws Exception {


        String fileName =
                String.format(
                        "Page-%02d.png",
                        pageNumber
                );


        File outputFile =
                new File(
                        folder,
                        fileName
                );


        // ---------------------------------------------
        // DON'T DOWNLOAD AGAIN IF FILE EXISTS
        // ---------------------------------------------

        if (outputFile.exists()
                && outputFile.length() > 0) {

            System.out.println(
                    "Already exists: "
                            + fileName
            );

            return;
        }


        // ---------------------------------------------
        // JAVASCRIPT TO GET BLOB IMAGE
        // ---------------------------------------------

        String javascript =
                "var image = arguments[0];" +
                        "var callback = arguments[arguments.length - 1];" +

                        "fetch(image.src)" +

                        ".then(function(response) {" +
                        "    return response.blob();" +
                        "})" +

                        ".then(function(blob) {" +

                        "    var reader = new FileReader();" +

                        "    reader.onloadend = function() {" +
                        "        callback(reader.result);" +
                        "    };" +

                        "    reader.readAsDataURL(blob);" +

                        "})" +

                        ".catch(function(error) {" +

                        "    callback('ERROR:' + error);" +

                        "});";


        Object result =
                ((JavascriptExecutor) driver)
                        .executeAsyncScript(
                                javascript,
                                image
                        );


        if (result == null) {

            throw new RuntimeException(
                    "Image download returned NULL."
            );
        }


        String base64Data =
                result.toString();


        if (base64Data.startsWith(
                "ERROR:"
        )) {

            throw new RuntimeException(
                    base64Data
            );
        }


        // ---------------------------------------------
        // REMOVE DATA URL HEADER
        // ---------------------------------------------

        int commaIndex =
                base64Data.indexOf(",");


        if (commaIndex == -1) {

            throw new RuntimeException(
                    "Invalid image data received."
            );
        }


        String pureBase64 =
                base64Data.substring(
                        commaIndex + 1
                );


        // ---------------------------------------------
        // CONVERT BASE64 → BYTES
        // ---------------------------------------------

        byte[] imageBytes =
                Base64.getDecoder().decode(
                        pureBase64
                );


        // ---------------------------------------------
        // WRITE PNG FILE
        // ---------------------------------------------

        try (
                FileOutputStream outputStream =
                        new FileOutputStream(
                                outputFile
                        )
        ) {

            outputStream.write(
                    imageBytes
            );
        }


        System.out.println(
                "Downloaded: "
                        + fileName
        );
    }
}