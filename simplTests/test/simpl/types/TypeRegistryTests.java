package simpl.types;

import static org.junit.Assert.*;

import org.junit.Test;

import simpl.types.scalar.IntegerType;

public class TypeRegistryTests {

	@Test
	public void testTypeRegistryContainsDefaultTypes() {
		
		TypeRegistry.init();
		new FundamentalTypes();
				
		ScalarType stClass = TypeRegistry.getScalarType(Integer.class);
		assertNotNull("Not marshalling types correctly!", stClass);
		assertEquals(IntegerType.class, stClass.getClass());
		
		
		ScalarType st = TypeRegistry.getScalarType("IntegerType");
		assertNotNull(st);
		assertEquals(IntegerType.class, st.getClass());
	}

}
