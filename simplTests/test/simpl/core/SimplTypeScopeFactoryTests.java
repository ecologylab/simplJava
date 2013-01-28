package simpl.core;

import static org.junit.Assert.*;

import org.junit.Test;

public class SimplTypeScopeFactoryTests {

	@Test
	public void testBasicFlow() {
		SimplTypesScopeFactory.name("mySTS").inherits(null).translations(null).create();
		SimplTypesScopeFactory.name("anotherSTS").translations(null).create();
	}

}
