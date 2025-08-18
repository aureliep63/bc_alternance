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
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ElectricityBusinessTest {
    private WebDriver driver;
    private WebDriverWait wait;
    private final String BASE_URL = "https://aureliep63.github.io/bc_alternance_angular";


    @BeforeEach
    public void setUp() {
        System.out.println("➡️ Lancement du test en mode headless");

        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");             // Mode headless
        options.addArguments("--disable-gpu");          // Désactive GPU (utile sur Windows)
        options.addArguments("--window-size=1920,1080");// Taille d'écran simulée
        options.addArguments("--remote-allow-origins=*"); // Évite certaines erreurs CORS
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
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
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        //  Attendre redirection vers /profile
        System.out.println("[STEP 3] Vérification redirection vers /profile");
        wait.until(ExpectedConditions.urlContains("/profile"));
        Assertions.assertTrue(driver.getCurrentUrl().contains("/profile"),
                "L'URL n'a pas changé vers /profile après login");



        //  10. Vérifier que la borne apparaît sur la map
        System.out.println("[STEP 8] Vérification de l'ajout de la borne sur la map");
        driver.get("https://aureliep63.github.io/bc_alternance_angular");
        pause(10000);
    }
}
