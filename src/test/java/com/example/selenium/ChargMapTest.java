package com.example.selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ChargMapTest {

    private WebDriver driver;
    private WebDriverWait wait;
    private final String BASE_URL = "http://localhost:4200";

//    @BeforeEach
//    public void setUp() {
//        System.out.println("➡️ Lancement du test pour l'ajout d'une borne");
//        // Télécharge et configure automatiquement ChromeDriver
//        WebDriverManager.chromedriver().setup();
//        driver = new ChromeDriver();
//        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
//        driver.manage().window().maximize();
//        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//    }

    @BeforeEach
    public void setUp() {
        System.out.println("➡️ Lancement du test en mode headless");

        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");             // Mode headless
        options.addArguments("--disable-gpu");          // Désactive GPU (utile sur Windows)
        options.addArguments("--window-size=1920,1080");// Taille d'écran simulée
        options.addArguments("--remote-allow-origins=*"); // Évite certaines erreurs CORS

        driver = new ChromeDriver(options);             // Utilise Chrome avec ces options
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    private void pause(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLoginAndAddBorne() {
        System.out.println(" [STEP 1] Accès à la page de login");
        driver.get(BASE_URL + "/login");

        //  Remplir et soumettre le formulaire de login
        System.out.println(" [STEP 2] Remplissage des identifiants");
        WebElement emailField = wait.until(ExpectedConditions.
                visibilityOfElementLocated(By.id("email")));
        emailField.sendKeys("aurelie@test.fr");

        WebElement passwordField = driver.findElement(By.id("password"));
        passwordField.sendKeys("tototo");

        WebElement rememberField = driver.findElement(By.cssSelector("input[type='checkbox']"));
        rememberField.click();

        WebElement loginBtn = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']")));
        JavascriptExecutor executor = (JavascriptExecutor)driver;
        executor.executeScript("arguments[0].click();", loginBtn);

        //  Attendre redirection vers /profile
        System.out.println("[STEP 3] Vérification redirection vers /profile");
        wait.until(ExpectedConditions.urlContains("/profile"));
        Assertions.assertTrue(driver.getCurrentUrl().contains("/profile"),
                "L'URL n'a pas changé vers /profile après login");
        //pause(1000);

        //  4. Ouvrir le modal d’ajout de borne
        System.out.println("[STEP 4] Ajout de la borne");
        WebElement addBorneBtn = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#titreBorne button")));
        JavascriptExecutor executor2 = (JavascriptExecutor) driver;
        executor2.executeScript("arguments[0].click();", addBorneBtn);
        pause(1000);

        //  5. Remplir les champs du formulaire de borne
        System.out.println("[STEP 5] Remplissage des champs de la borne");
        WebElement nomField = wait.until(ExpectedConditions
                .visibilityOfElementLocated(By.id("nom")));
        nomField.sendKeys("Borne Test");

        WebElement puissanceField = driver.findElement(By.id("puissance"));
        puissanceField.sendKeys("22");

        WebElement prixField = driver.findElement(By.id("prix"));
        prixField.sendKeys("5");

        WebElement uploadInput = driver.findElement(By.id("photo"));
        String filePath = "C:\\Users\\HB\\Desktop\\BC\\BC_alternance\\upload\\voitureBornedevant.png";
        uploadInput.sendKeys(filePath);

        WebElement instructionField = driver.findElement(By.id("instruction"));
        instructionField.sendKeys("Brancher et charger");

        //  6. Aller à l’étape suivante (lieu)
        WebElement nextStepBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(),'Ajouter le lieux')]")));
        JavascriptExecutor executor3 = (JavascriptExecutor) driver;
        executor3.executeScript("arguments[0].click();", nextStepBtn);

        //  7. Ajout d'un lieu
        System.out.println("[STEP 6] Ajout d'un lieu");
        WebElement adresseField = wait.until(ExpectedConditions
                .visibilityOfElementLocated(By.id("adresse")));
        adresseField.sendKeys("18 rue Vauban");

        WebElement villeField = wait.until(ExpectedConditions
                .visibilityOfElementLocated(By.id("ville")));
        villeField.sendKeys("Collioure");

        WebElement cpField = wait.until(ExpectedConditions.
                visibilityOfElementLocated(By.id("codePostal")));
        cpField.sendKeys("66190");

        //  8. Soumettre
        WebElement submitBtn = driver.findElement(By.cssSelector("form button[type='submit']"));
        submitBtn.click();

        //  9. Vérifier que la borne apparaît dans le tableau
        System.out.println("[STEP 7] Vérification de l'ajout de la borne dans le profil");
        WebDriverWait longWait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement borneRow = longWait.until(ExpectedConditions.
                visibilityOfElementLocated(By.xpath("//td[contains(text(),'Borne Test')]")));

        Assertions.assertTrue(driver.getPageSource().contains("Borne Test"));
        Assertions.assertNotNull(borneRow, "'Borne Test' n'a pas été trouvée !");
        System.out.println("La borne 'Borne Test' est bien affichée !");

        //  10. Vérifier que la borne apparaît sur la map
        System.out.println("[STEP 8] Vérification de l'ajout de la borne sur la map");
        driver.get("http://localhost:4200/");
        pause(10000);
    }
}
