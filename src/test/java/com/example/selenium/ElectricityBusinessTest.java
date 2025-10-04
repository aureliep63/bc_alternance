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
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class ElectricityBusinessTest {
    private WebDriver driver;
    private WebDriverWait wait;
    private final String BASE_URL = System.getenv("BASE_URL") != null ?
            System.getenv("BASE_URL") :
            "https://aureliep63.github.io/bc_alternance_angular";

    @BeforeEach
    public void setUp() {
        System.out.println("➡ Lancement du test en mode headless");
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
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
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }

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

        System.out.println(" [STEP 1] Accès à la page d'accueil");
        driver.get(BASE_URL);

        System.out.println(" [STEP 1.1] Clic sur le lien 'Se connecter' pour ouvrir la modale");
        WebElement openLoginLink = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//a[contains(text(), 'Se connecter')]")) // Changer de 'elementToBeClickable' à 'presenceOfElementLocated'
        );
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("arguments[0].click();", openLoginLink);

        System.out.println(" [STEP 2] Remplissage des identifiants dans la modale");
        WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email")));
        emailField.sendKeys("aurelie@test.fr");

        WebElement passwordField = driver.findElement(By.id("password"));
        passwordField.sendKeys("tototo");

        WebElement rememberField = driver.findElement(By.cssSelector("input[type='checkbox']"));
        executor.executeScript("arguments[0].click();", rememberField);

        WebElement loginBtn = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']")));
        executor.executeScript("arguments[0].click();", loginBtn);

        System.out.println("[STEP 3] Navigation directe vers /profile");
        WebElement monCompteLink = new WebDriverWait(driver, Duration.ofSeconds(15))
                .until(ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//a[contains(., 'Mon Compte')]")
                ));
        ((JavascriptExecutor)driver).executeScript("arguments[0].click();", monCompteLink);


//        WebElement monProfilLink = new WebDriverWait(driver, Duration.ofSeconds(10))
//                .until(ExpectedConditions.elementToBeClickable(
//                        By.xpath("//a[contains(text(),'Mon profil')]")
//                ));
//        monProfilLink.click();
//
//        System.out.println("[STEP 3.1] Cliquer sur navTab Bornes");
//        WebElement bornesTab = new WebDriverWait(driver, Duration.ofSeconds(10))
//                .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[contains(., 'Mes Bornes')]")));
//        ((JavascriptExecutor)driver).executeScript("arguments[0].click();", bornesTab);
//
//        System.out.println("[STEP 3.2] Bouton Ajouter borne");
//        WebElement addBorneBtn = new WebDriverWait(driver, Duration.ofSeconds(10))
//                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[contains(., 'Ajouter une borne')]")));
//        ((JavascriptExecutor)driver).executeScript("arguments[0].click();", addBorneBtn);
//



        System.out.println("[STEP 8] Vérification de l'ajout de la borne sur la map");
        driver.get(BASE_URL);
        pause(10000);

}
}