package simpl.descriptions.beiber;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import simpl.descriptions.ClassDescriptor;
import simpl.descriptions.ClassDescriptors;
import simpl.descriptions.FieldDescriptor;
import simpl.descriptions.FieldType;
import simpl.interpretation.listOfScalars;
import simpl.types.ListType;

public class MapAndListTypeTests {

	class mapClass 
	{
		public mapClass()
		{
			ourMap = new HashMap<String, Integer>();
		}
		
		public Map<String, Integer> ourMap; 
	}
	
	class listClass
	{
		public listClass()
		{
			ourList= new LinkedList<String>();
		}
		
		public List<String> ourList;
	}
	
	@Test
	public void testProveOutBasicConceptWithLists() throws Exception {
		Class<?> list = listClass.class;
		
		Class<?> listInterface = list.getField("ourList").getType();
		
		listClass lc = new listClass();
		Class<?> linkedList = lc.ourList.getClass();
		
		
		assertFalse(listInterface.equals(linkedList));
		
		List<String> l = (List<String>)(linkedList.newInstance());
		l.add("a");
		l.add("b");
		l.add("c");
		assertEquals(linkedList, l.getClass());
		assertEquals(3, l.size());		
	}
	
	@Test
	public void testListTypeCreatedForDeclaredListType()
	{
		ClassDescriptor cd = ClassDescriptors.getClassDescriptor(declaredListDescription.class);
		assertEquals(1, cd.allFieldDescriptors().size());
		
		ListType lt = cd.allFieldDescriptors().get(0).getListType();
		assertNotNull("Should have a list type!", lt);
		Object instance = lt.createInstance();
		assertEquals(instance.getClass(), LinkedList.class);
		
		FieldDescriptor fd = cd.allFieldDescriptors().get(0);
		assertEquals(FieldType.COLLECTION_SCALAR, fd.getType());
		
		
	}
	
	@Test
	public void testListTypeCreatedForInterfaceListType()
	{
		
	}
	
	@Test
	public void testMapTypeCreatedForDeclaredMapType()
	{
		
	}
	
	@Test
	public void testMapTypeCreatedForInterfaceMapType()
	{
		
	}
}
