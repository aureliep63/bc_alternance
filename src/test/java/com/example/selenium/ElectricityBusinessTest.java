package com.example.selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.time.Duration;

public class ElectricityBusinessTest {
    private WebDriver driver; //obj pour controller le navigateur
    private WebDriverWait wait; // obj pour attendre qu'un element soit prêt
   // url a tester
    private final String BASE_URL = System.getenv("BASE_URL") != null ?
            System.getenv("BASE_URL") :
            "https://aureliep63.github.io/bc_alternance_angular";

    @BeforeEach // Méthode exécutée avant chaque test pour initialiser le navigateur
    public void setUp() {
        System.out.println("➡ Lancement du test en mode headless");

        // Configure automatiquement le driver Chrome avec WebDriverManager
        WebDriverManager.chromedriver().setup();

        // Options pour Chrome
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new"); // exécute en mode en HL
        options.addArguments("--disable-gpu"); // désactive l’accélération GPU
        options.addArguments("--window-size=1920,1080"); // taille de la fenêtre du navigateur
        options.addArguments("--remote-allow-origins=*"); // permet certaines connexions cross-origin
        options.addArguments("--no-sandbox"); // option recommandée pour CI/CD
        options.addArguments("--disable-dev-shm-usage"); // évite les erreurs de mémoire

        driver = new ChromeDriver(options); // Initialise le navigateur avec les options
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));// Définit un délai implicite
        driver.manage().window().maximize();  // Maximise la fenêtre du navigateur
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));  // Utilisée pour attendre certaines conditions
    }

    @AfterEach // Exécutée après chaque test pour fermer le navigateur
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
    // Faire une pause manuelle
    private void pause(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }
    // Attendre que l'API et le site soient accessible
    private void waitForApiToWakeUp() {
        System.out.println(" Attente du démarrage de l'API...");
        try {
            wait.until((ExpectedCondition<Boolean>) driver -> {
                driver.get(BASE_URL);
                return true;
            });
            System.out.println(" API démarrée. Poursuite des tests.");
        } catch (TimeoutException e) {
            System.err.println(" Timeout : l'API n'a pas répondu dans le temps imparti. Vérifiez son statut sur Render.");
            throw e;
        }
    }
    @Test
    public void testLoginAndAddBorne() {
        // Ajout de l'étape d'attente
        waitForApiToWakeUp();

        System.out.println("[STEP 1] Accès à la page d'accueil");
        driver.get(BASE_URL);

        System.out.println("    [STEP 1.1] Clic sur le lien 'Se connecter' pour ouvrir la modale");
        WebElement openLoginLink = wait.until(d -> d.findElement(
                By.xpath("//a[contains(text(), 'Se connecter')]")
        ));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", openLoginLink);

        System.out.println("[STEP 2] Remplissage des identifiants dans la modale");
        WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email")));
        emailField.sendKeys("aurelie@test.fr");

        WebElement passwordField = driver.findElement(By.id("password"));
        passwordField.sendKeys("tototo");

        WebElement rememberField = driver.findElement(By.cssSelector("input[type='checkbox']"));
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("arguments[0].click();", rememberField);

        WebElement loginBtn = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']")));
        executor.executeScript("arguments[0].click();", loginBtn);

        System.out.println("[STEP 3] Navigation vers /profile");
        WebElement monCompteLink = new WebDriverWait(driver, Duration.ofSeconds(120))
                .until(ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//a[contains(., 'Mon Compte')]")
                ));
        ((JavascriptExecutor)driver).executeScript("arguments[0].click();", monCompteLink);
        pause(1000);

        WebElement monProfilLink = wait.until(d -> d.findElement(
                By.xpath("//a[contains(text(),'Mon profil')]")
        ));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", monProfilLink);
        pause(1000);

        System.out.println("    [STEP 3.1] Cliquer sur navTab Bornes");
        WebElement bornesTab = new WebDriverWait(driver, Duration.ofSeconds(30))
                .until(ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//a[contains(translate(., 'MESBORNES', 'mesbornes'), 'mes bornes')]")
                ));
        ((JavascriptExecutor)driver).executeScript("arguments[0].click();", bornesTab);
        pause(1000);

        System.out.println("    [STEP 3.2] Bouton Ajouter borne");
        WebElement addBorneBtn = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[contains(., 'Ajouter une borne')]")));
        ((JavascriptExecutor)driver).executeScript("arguments[0].click();", addBorneBtn);
        pause(1000);

        System.out.println("    [STEP 3.3] Ajouter infos");
        WebElement nomInput = driver.findElement(By.id("nom"));
        nomInput.sendKeys("Test Selenium");

        WebElement surPiedCheckbox = driver.findElement(By.id("surPied"));
        if (!surPiedCheckbox.isSelected()) {
            surPiedCheckbox.click();
        }

        WebElement puissanceSelect = driver.findElement(By.id("puissance"));
        Select selectPuissance = new Select(puissanceSelect);
        selectPuissance.selectByVisibleText("7,4 kW – Normale (recommandée)");

        WebElement prixInput = driver.findElement(By.id("prix"));
        prixInput.sendKeys("4");

        WebElement instructionTextarea = driver.findElement(By.id("instruction"));
        instructionTextarea.sendKeys("Test instruction");

        WebElement photoInput = driver.findElement(By.cssSelector("input[type='file']"));
        File testPhoto = new File("src/test/resources/test.jpg");
        photoInput.sendKeys(testPhoto.getAbsolutePath());
        pause(2000);

        System.out.println("    [STEP 3.4] Bouton Suivant");
        WebElement nextStepBtn = driver.findElement(By.xpath("//button[contains(.,'Ajouter le lieu')]"));
        ((JavascriptExecutor)driver).executeScript("arguments[0].click();", nextStepBtn);


        System.out.println("[STEP 4] Fausse info Lieu Borne");
        WebElement adresseInput = new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.elementToBeClickable(By.id("adresse")));
        adresseInput.clear();
        adresseInput.sendKeys("2025 Rue Inexistante");

        WebElement villeInput = driver.findElement(By.id("ville"));
        villeInput.clear();
        villeInput.sendKeys("Porto");

        WebElement codePostalInput = driver.findElement(By.id("codePostal"));
        codePostalInput.clear();
        codePostalInput.sendKeys("20000");
        pause(2000);

        System.out.println("    [STEP 4.1] Erreur Adresse");
        WebElement addBtn = driver.findElement(By.xpath("//button[@type='submit' and contains(., 'Ajouter')]"));
        ((JavascriptExecutor)driver).executeScript("arguments[0].click();", addBtn);
        pause(2000);


        System.out.println("    [STEP 4.2] Vérification de l'erreur");
        WebElement errorMsg = new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//div[contains(@class,'alert-danger') and contains(.,\"L'adresse saisie n'a pas pu être validée\")]")

                ));
        System.out.println("Message d'erreur affiché : " + errorMsg.getText());
        pause(2000);

        System.out.println("[STEP 5] Lieu avec vrai adresse");
        adresseInput.clear();
        adresseInput.sendKeys("675 Avenue du Palais de la Mer");

        villeInput.clear();
        villeInput.sendKeys("Le Grau-du-Roi");

        codePostalInput.clear();
        codePostalInput.sendKeys("30240");

        System.out.println("    [STEP 5.1] Validation de la borne");
        ((JavascriptExecutor)driver).executeScript("arguments[0].click();", addBtn);
        pause(10000);

        System.out.println("[STEP 6] Vérification de la présence de la borne dans le tableau");
        WebElement borneRow = wait.until(d ->
                d.findElement(By.xpath("//table//tr[td[contains(text(),'Test instruction')]]"))
        );
        Assertions.assertNotNull(borneRow, "La borne ajoutée n'est pas trouvée dans le tableau !");
        System.out.println("Borne ajoutée trouvée dans le tableau : 'Test instruction'");

        System.out.println("[STEP 7] Suppression de la borne");
        System.out.println("    [STEP 7.1] Cliquer sur le btn supp");
        WebElement deleteBtn = borneRow.findElement(By.cssSelector("button.btn-outline-danger"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", deleteBtn);

        System.out.println("    [STEP 7.2] Confirmer avec la modale");
        WebElement confirmDeleteBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[@id='confirmDeleteModal']//button[contains(text(),'Supprimer')]")
        ));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", confirmDeleteBtn);

        System.out.println("[STEP 8] Vérification de l'absence de la borne dans le tableau");
        boolean borneDeleted = wait.until(d -> d.findElements(
                By.xpath("//table//tr[td[contains(text(),'Test instruction')]]")
        ).isEmpty());

        Assertions.assertTrue(borneDeleted, "La borne n'a pas été supprimée !");
        System.out.println("Borne supprimée avec succès ");
        pause(2000);
    }
}