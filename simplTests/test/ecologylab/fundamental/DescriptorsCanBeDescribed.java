package ecologylab.fundamental;

import org.junit.Before;
import org.junit.Test;

import ecologylab.serialization.ClassDescriptor;
import ecologylab.serialization.FieldDescriptor;
import ecologylab.serialization.SimplTypesScope;

public class DescriptorsCanBeDescribed{


	@Before
	public void ResetSTS()
	{
		SimplTypesScope.ResetAllTypesScopes();
	}

	@Test
	public void FieldDescriptorCanBeDescribed()
	{
		ClassDescriptor cd = ClassDescriptor.getClassDescriptor(FieldDescriptor.class);
	}

	@Test
	public void ClassDescriptorCanBeDescribed()
	{
		ClassDescriptor cd = ClassDescriptor.getClassDescriptor(ClassDescriptor.class);
	}

	
	@Test
	public void YinsCase()
	{
		SimplTypesScope tscope = SimplTypesScope.get("test-de/serialize descriptors in json",
                FieldDescriptor.class,
                ClassDescriptor.class,
                SimplTypesScope.class);

	}
	//TODO: Better validation on these.
	//They're sparse because they're just trying to catch silly exceptions.



}