//package com.example.selenium;
//
//import io.github.bonigarcia.wdm.WebDriverManager;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.openqa.selenium.*;
//import org.openqa.selenium.chrome.ChromeDriver;
//import org.openqa.selenium.chrome.ChromeOptions;
//import org.openqa.selenium.support.ui.ExpectedCondition;
//import org.openqa.selenium.support.ui.ExpectedConditions;
//import org.openqa.selenium.support.ui.Select;
//import org.openqa.selenium.support.ui.WebDriverWait;
//
//import java.time.Duration;
//
//class ElectricityBusinessSeleniumTest {
//    private WebDriver driver;
//    private WebDriverWait wait;
//    private final String BASE_URL = System.getenv("BASE_URL") != null ?
//            System.getenv("BASE_URL") :
//            "https://aureliep63.github.io/bc_alternance_angular";
//
//    @BeforeEach
//    public void setUp() {
//        System.out.println("➡️ Lancement du test ");
//        WebDriverManager.chromedriver().setup();
//        ChromeOptions options = new ChromeOptions();
//
//        driver = new ChromeDriver(options);
//        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
//        driver.manage().window().maximize();
//        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
//    }
//
//    @AfterEach
//    public void tearDown() {
//        if (driver != null) {
//            driver.quit();
//        }
//    }
//
//    private void pause(long millis) {
//        try {
//            Thread.sleep(millis);
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//            e.printStackTrace();
//        }
//    }
//
//    private void waitForApiToWakeUp() {
//        System.out.println("⏳ Attente du démarrage de l'API...");
//        try {
//            // Utilise l'URL de base pour faire une requête simple (par exemple, à l'accueil)
//            // L'API mettra du temps à répondre, mais ne renverra pas d'erreur de timeout si elle est en cours de réveil
//            wait.until((ExpectedCondition<Boolean>) driver -> {
//                driver.get(BASE_URL); // Tente d'accéder à la page d'accueil ou une page de base
//                return true; // Le test est simple, on ne vérifie rien, on attend juste que le navigateur charge la page de login
//            });
//            System.out.println(" API démarrée. Poursuite des tests.");
//        } catch (TimeoutException e) {
//            System.err.println(" Timeout : l'API n'a pas répondu dans le temps imparti. Vérifiez son statut sur Render.");
//            throw e;
//        }
//    }
//    @Test
//    public void testLoginAndAddBorne() {
//        // Ajout de l'étape d'attente
//        waitForApiToWakeUp();
//
//        System.out.println(" [STEP 1] Accès à la page d'accueil");
//        driver.get(BASE_URL);
//
//        System.out.println(" [STEP 1.1] Clic sur le lien 'Se connecter' pour ouvrir la modale");
//        WebElement openLoginLink = wait.until(
//                ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(text(), 'Se connecter')]"))
//        );
//        openLoginLink.click();
//
//        System.out.println(" [STEP 2] Remplissage des identifiants dans la modale");
//        WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email")));
//        emailField.sendKeys("aurelie@test.fr");
//
//        WebElement passwordField = driver.findElement(By.id("password"));
//        passwordField.sendKeys("tototo");
//
//        WebElement rememberField = driver.findElement(By.cssSelector("input[type='checkbox']"));
//        JavascriptExecutor executor = (JavascriptExecutor) driver;
//        executor.executeScript("arguments[0].click();", rememberField);
//
//        WebElement loginBtn = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']")));
//        executor.executeScript("arguments[0].click();", loginBtn);
//
//        System.out.println("[STEP 3] Navigation directe vers /profile");
//        WebElement monCompteLink = new WebDriverWait(driver, Duration.ofSeconds(15))
//                .until(ExpectedConditions.presenceOfElementLocated(
//                        By.xpath("//a[contains(., 'Mon Compte')]")
//                ));
//        ((JavascriptExecutor)driver).executeScript("arguments[0].click();", monCompteLink);
//        pause(1000);
//
//        WebElement monProfilLink = new WebDriverWait(driver, Duration.ofSeconds(10))
//                .until(ExpectedConditions.elementToBeClickable(
//                        By.xpath("//a[contains(text(),'Mon profil')]")
//                ));
//        monProfilLink.click();
//        pause(1000);
//
//        System.out.println("[STEP 3.1] Cliquer sur navTab Bornes");
//        WebElement bornesTab = new WebDriverWait(driver, Duration.ofSeconds(10))
//                .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[contains(., 'Mes Bornes')]")));
//        ((JavascriptExecutor)driver).executeScript("arguments[0].click();", bornesTab);
//        pause(1000);
//
//        System.out.println("[STEP 3.2] Bouton Ajouter borne");
//        WebElement addBorneBtn = new WebDriverWait(driver, Duration.ofSeconds(10))
//                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[contains(., 'Ajouter une borne')]")));
//        ((JavascriptExecutor)driver).executeScript("arguments[0].click();", addBorneBtn);
//        pause(1000);
//
//        System.out.println("[STEP 3.2] Ajouter infos");
//        WebElement nomInput = driver.findElement(By.id("nom"));
//        nomInput.sendKeys("Test Selenium");
//
//        WebElement surPiedCheckbox = driver.findElement(By.id("surPied"));
//        if (!surPiedCheckbox.isSelected()) {
//            surPiedCheckbox.click();
//        }
//
//        WebElement puissanceSelect = driver.findElement(By.id("puissance"));
//        Select selectPuissance = new Select(puissanceSelect);
//        selectPuissance.selectByVisibleText("7,4 kW – Normale (recommandée)");
//
//        WebElement prixInput = driver.findElement(By.id("prix"));
//        prixInput.sendKeys("4");
//
//        WebElement instructionTextarea = driver.findElement(By.id("instruction"));
//        instructionTextarea.sendKeys("Test instruction");
//
//        WebElement photoInput = driver.findElement(By.cssSelector("input[type='file']"));
//        photoInput.sendKeys("C:\\Users\\HB\\Desktop\\BC\\photo\\test.jpg");
//        pause(2000);
//
//        System.out.println("[STEP 3.4] Bouton Suivant");
//        WebElement nextStepBtn = driver.findElement(By.xpath("//button[contains(.,'Ajouter le lieu')]"));
//        ((JavascriptExecutor)driver).executeScript("arguments[0].click();", nextStepBtn);
//
//
//        System.out.println("[STEP 4] Fausse info Lieu Borne");
//        WebElement adresseInput = new WebDriverWait(driver, Duration.ofSeconds(5))
//                .until(ExpectedConditions.elementToBeClickable(By.id("adresse")));
//        adresseInput.clear();
//        adresseInput.sendKeys("2025 Rue Inexistante");
//
//        WebElement villeInput = driver.findElement(By.id("ville"));
//        villeInput.clear();
//        villeInput.sendKeys("Porto");
//
//        WebElement codePostalInput = driver.findElement(By.id("codePostal"));
//        codePostalInput.clear();
//        codePostalInput.sendKeys("20000");
//        pause(2000);
//
//        System.out.println("[STEP 4.1] Erreur Adresse");
//        WebElement addBtn = driver.findElement(By.xpath("//button[@type='submit' and contains(., 'Ajouter')]"));
//        ((JavascriptExecutor)driver).executeScript("arguments[0].click();", addBtn);
//        pause(2000);
//
//
//        System.out.println("[STEP 4.2] Vérification de l'erreur");
//        WebElement errorMsg = new WebDriverWait(driver, Duration.ofSeconds(5))
//                .until(ExpectedConditions.visibilityOfElementLocated(
//                        By.xpath("//div[contains(@class,'alert-danger') and contains(.,\"L'adresse saisie n'a pas pu être validée\")]")
//
//                ));
//        System.out.println("Message d'erreur affiché : " + errorMsg.getText());
//        pause(2000);
//
//        System.out.println("[STEP 5] Lieu avec vrai adresse");
//        adresseInput.clear();
//        adresseInput.sendKeys("675 Avenue du Palais de la Mer");
//
//        villeInput.clear();
//        villeInput.sendKeys("Le Grau-du-Roi");
//
//        codePostalInput.clear();
//        codePostalInput.sendKeys("30240");
//
//        System.out.println("[STEP 5.1] Validation de la borne");
//        ((JavascriptExecutor)driver).executeScript("arguments[0].click();", addBtn);
//        pause(10000);
//
//        System.out.println("[STEP 6] Vérification de la présence de la borne dans le tableau");
//        WebElement borneRow = wait.until(ExpectedConditions.visibilityOfElementLocated(
//                By.xpath("//table//td[contains(text(),'Test Selenium')]")
//        ));
//        pause(10000);
//        Assertions.assertNotNull(borneRow, "La borne ajoutée n'est pas trouvée dans le tableau !");
//        System.out.println("Borne ajoutée trouvée dans le tableau : 'Test Selenium'");
//        pause(10000);
//
//
//        System.out.println("[STEP 8] Vérification de l'ajout de la borne sur la map");
//        driver.get(BASE_URL);
//        pause(10000);
//    }
//}