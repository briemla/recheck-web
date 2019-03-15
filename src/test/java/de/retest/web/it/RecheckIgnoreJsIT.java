package de.retest.web.it;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import java.awt.Rectangle;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import de.retest.recheck.ignore.JSFilterImpl;
import de.retest.recheck.ui.Path;
import de.retest.recheck.ui.descriptors.Element;
import de.retest.recheck.ui.descriptors.IdentifyingAttributes;
import de.retest.recheck.ui.descriptors.MutableAttributes;
import de.retest.recheck.ui.diff.AttributeDifference;

class RecheckIgnoreJsIT {

	final JSFilterImpl cut = new JSFilterImpl( Paths.get( ".retest/recheck.ignore.js" ) );

	@Test
	void should_ignore_outline_5_diff() throws Exception {
		final Element element = Mockito.mock( Element.class );
		assertThat( cut.matches( element, new AttributeDifference( "outline", new Rectangle( 580, 610, 200, -20 ),
				new Rectangle( 578, 605, 203, -16 ) ) ) ).isTrue();
		assertThat( cut.matches( element, new AttributeDifference( "outline", new Rectangle( 580, 610, 200, 20 ),
				new Rectangle( 500, 605, 200, 20 ) ) ) ).isFalse();
	}

	@Test
	void should_ignore_opacity_5_diff() throws Exception {
		final Element element = Mockito.mock( Element.class );
		// opacity: expected="0", actual="0.00566082"
		assertThat( cut.matches( element, new AttributeDifference( "opacity", "0", "0.00566082" ) ) ).isTrue();
		assertThat( cut.matches( element, new AttributeDifference( "opacity", "100", "80" ) ) ).isFalse();
	}

	@Test
	void should_ignore_different_base_URLs() {
		final Element element = Mockito.mock( Element.class );
		assertThat( cut.matches( element, new AttributeDifference( "background-image",
				"url(\"https://www2.test.k8s.bigcct.be/.imaging/default/dam/clients/BT_logo.svg.png/jcr:content.png\")",
				"url(\"http://icullen-website-public-spring4-8:8080/.imaging/default/dam/clients/BT_logo.svg.png/jcr:content.png\")" ) ) )
						.isTrue();
		assertThat( cut.matches( element, new AttributeDifference( "background-image",
				"url(\"https://www2.test.k8s.bigcct.be/.imaging/default/dam/clients/BT_logo.svg.png/jcr:content.png\")",
				"url(\"http://icullen-website-public-spring4-8:8080/some-other-URL.png\")" ) ) ).isFalse();
	}

	@Test
	void should_ignore_same_font_family() {
		final Element element = Mockito.mock( Element.class );
		assertThat( cut.matches( element, new AttributeDifference( "font-family", "Arial", "system-ui" ) ) ).isTrue();
		assertThat( cut.matches( element, new AttributeDifference( "font-family", "Arial", "Courier New" ) ) )
				.isFalse();
	}

	@Test
	void should_ignore_any_5px_diff() {
		final Element element = Mockito.mock( Element.class );
		assertThat( cut.matches( element, //
				new AttributeDifference( "some-css", "10px", "12px" ) ) ).isTrue();
		assertThat( cut.matches( element, //
				new AttributeDifference( "some-css", "10px", "200px" ) ) ).isFalse();
	}

	@Test
	void null_should_not_cause_exc() {
		final Element element = Mockito.mock( Element.class );
		assertThat( cut.matches( element, //
				new AttributeDifference( "some-css", null, "12px" ) ) ).isFalse();
		assertThat( cut.matches( element, //
				new AttributeDifference( "some-css", "10px", null ) ) ).isFalse();
	}

	@Test
	void should_ignore_invisible_elements() {
		final Element element = createElement( "a", false );
		assertThat( cut.matches( element ) ).isTrue();
	}

	@Test
	void should_not_ignore_visible_elements() throws Exception {
		final Element element = createElement( "a", true );
		assertThat( cut.matches( element ) ).isFalse();
	}

	//	@ParameterizedTest
	//	@MethodSource( "specialTags" )
	//	void testName( final String specialTag ) throws Exception {
	//		final WebData webData = createVisibleWebDataForTag( specialTag );
	//		assertThat( WebDataFilter.shouldIgnore( webData ) ).isFalse();
	//	}

	private Element createElement( final String tagName, final boolean shown ) {
		final IdentifyingAttributes identifyingAttributes = new IdentifyingAttributes( IdentifyingAttributes
				.createList( Path.fromString( "HTML[1]/DIV[1]/" + tagName.toUpperCase() + "[1]" ), tagName ) );
		final MutableAttributes attributes = new MutableAttributes();
		attributes.put( "shown", shown );
		return Element.create( "retestId", mock( Element.class ), identifyingAttributes, attributes.immutable() );
	}

	static Stream<String> specialTags() {
		return Arrays.asList( new String[] { "meta", "option", "title" } ).stream();
	}
}
