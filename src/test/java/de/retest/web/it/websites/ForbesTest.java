package de.retest.web.it.websites;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import de.retest.recheck.Recheck;
import de.retest.recheck.RecheckImpl;

class ForbesTest {

	private WebDriver driver;
	private Recheck re;

	@BeforeEach
	public void setup() {
		// If ChromeDriver (http://chromedriver.chromium.org/downloads/) is not in your PATH, uncomment this and point to your installation.
		// System.setProperty( "webdriver.chrome.driver", "path/to/chromedriver" );

		final ChromeOptions opts = new ChromeOptions();
		opts.addArguments(
				// Enable headless mode for faster execution.
				"--headless",
				// Use Chrome in container-based Travis CI enviroment (see
				// https://docs.travis-ci.com/user/chrome#Sandboxing).
				"--no-sandbox",
				// Fix window size for stable results.
				"--window-size=1200,800" );
		driver = new ChromeDriver( opts );

		// Use the default implementation.
		re = new RecheckImpl();
	}

	@Test
	public void index() throws Exception {
		// Set the file name of the Golden Master.
		re.startTest( "simple-showcase" );

		// Do your Selenium stuff.
		driver.get( "https://www.forbes.com/billionaires/" );

		Thread.sleep( 1000 );

		// Single call instead of multiple assertions (doesn't fail on differences).
		re.check( driver, "index" );
		// Conclude the test case (fails on differences).
		re.capTest();
	}

	@AfterEach
	public void tearDown() {
		driver.quit();

		// Produce the result file.
		re.cap();
	}

}
