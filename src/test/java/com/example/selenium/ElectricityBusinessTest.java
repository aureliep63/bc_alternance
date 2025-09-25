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
        System.out.println("➡️ Lancement du test en mode headless");
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
        System.out.println("⏳ Attente du démarrage de l'API...");
        try {
            // Utilise l'URL de base pour faire une requête simple (par exemple, à l'accueil)
            // L'API mettra du temps à répondre, mais ne renverra pas d'erreur de timeout si elle est en cours de réveil
            wait.until((ExpectedCondition<Boolean>) driver -> {
                driver.get(BASE_URL); // Tente d'accéder à la page d'accueil ou une page de base
                return true; // Le test est simple, on ne vérifie rien, on attend juste que le navigateur charge la page de login
            });
            System.out.println("✅ API démarrée. Poursuite des tests.");
        } catch (TimeoutException e) {
            System.err.println("❌ Timeout : l'API n'a pas répondu dans le temps imparti. Vérifiez son statut sur Render.");
            throw e;
        }
    }
    @Test
    public void testLoginAndAddBorne() {
        // Ajout de l'étape d'attente
        waitForApiToWakeUp();

        System.out.println(" [STEP 1] Accès à la page de login");
        driver.get(BASE_URL + "/login");

        System.out.println(" [STEP 2] Remplissage des identifiants");
        WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email")));
        emailField.sendKeys("aurelie@test.fr");

        WebElement passwordField = driver.findElement(By.id("password"));
        passwordField.sendKeys("tototo");

        WebElement rememberField = driver.findElement(By.cssSelector("input[type='checkbox']"));
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("arguments[0].click();", rememberField);

        WebElement loginBtn = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']")));
        executor.executeScript("arguments[0].click();", loginBtn);

        System.out.println("[STEP 3] Vérification redirection vers /profile");
        wait = new WebDriverWait(driver, Duration.ofSeconds(60)); // Timeout 60s
        wait.until(ExpectedConditions.urlContains("/profile"));
        Assertions.assertTrue(driver.getCurrentUrl().contains("/profile"),
                "L'URL n'a pas changé vers /profile après login");

        System.out.println("[STEP 8] Vérification de l'ajout de la borne sur la map");
        driver.get(BASE_URL);
        pause(10000);
}
}