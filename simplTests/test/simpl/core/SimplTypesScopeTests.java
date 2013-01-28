package simpl.core;

import static org.junit.Assert.*;

import org.junit.Test;

public class SimplTypesScopeTests {

	@Test
	public void test() {
		ISimplTypesScope ists = SimplTypesScopeFactory.name("test").translations(null).create();
		
		ists.getName();
		ists.setName("some name");
		
	}

}
