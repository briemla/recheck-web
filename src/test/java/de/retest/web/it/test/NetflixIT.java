package de.retest.web.it.test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.WebDriver;

import de.retest.recheck.Recheck;
import de.retest.recheck.RecheckImpl;
import de.retest.web.testutils.PageFactory;

public class NetflixIT {

	WebDriver driver;
	Recheck re;

	@BeforeEach
	void setUp() {
		re = new RecheckImpl();
	}

	@ParameterizedTest
	@MethodSource( "de.retest.web.testutils.WebDriverFactory#drivers" )
	void netflix_html_should_be_checked( final WebDriver driver ) throws Exception {
		this.driver = driver;
		re.startTest( "Netflix-" + driver.getClass().getSimpleName() );

		driver.get( PageFactory.toPageUrlString( "test/netflix.html" ) );

		Thread.sleep( 1000 );

		re.check( driver, "open" );

		re.capTest();
	}

	@AfterEach
	void tearDown() {
		driver.quit();
		re.cap();
	}
}
