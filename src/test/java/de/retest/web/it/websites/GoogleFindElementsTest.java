package de.retest.web.it.websites;

import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import de.retest.recheck.Recheck;
import de.retest.web.RecheckWebImpl;
import de.retest.web.selenium.By;
import de.retest.web.selenium.RecheckDriver;

class GoogleFindElementsTest {

	private WebDriver driver;
	private Recheck re;

	@Before
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

		// Use the RecheckDriver as a wrapper for your usual driver
		driver = new RecheckDriver( new ChromeDriver( opts ) );

		// Use the unbreakable recheck implementation.
		re = new RecheckWebImpl();

		re.startTest( "simple-showcase" );
		driver.get(
				"https://www.google.de/search?q=suche&source=lnms&tbm=isch&sa=X&ved=0ahUKEwiIy5TBmfDgAhUQ3qQKHbJCDYMQ_AUIDigB&biw=1494&bih=1042&dpr=1.25" );
	}

	@Test
	public void findText() throws Exception {
		re.check( driver, "index" );

		driver.findElement( By.linkText( "Alle" ) );
		driver.findElement( By.linkText( "Maps" ) );
		driver.findElement( By.linkText( "News" ) );
		driver.findElement( By.linkText( "Videos" ) );
		driver.findElement( By.linkText( "Mehr" ) );
	}

	@Test
	public void findIDs() throws Exception {
		re.check( driver, "index" );
		driver.findElement( By.id( "searchform" ) );
	}

	@Test
	public void findClassName() throws Exception {
		re.check( driver, "index" );
		driver.findElement( By.className( "logo" ) );
		driver.findElement( By.className( "THL2l" ) );
		driver.findElement( By.className( "col" ) );
	}

	@After
	public void tearDown() {
		driver.quit();
		// Produce the result file.
		//re.cap();
	}

}
