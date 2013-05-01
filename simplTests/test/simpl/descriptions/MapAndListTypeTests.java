package simpl.descriptions;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import simpl.annotations.dbal.simpl_collection;
import simpl.descriptions.ClassDescriptor;
import simpl.descriptions.ClassDescriptors;
import simpl.descriptions.FieldDescriptor;
import simpl.descriptions.FieldType;
import simpl.descriptions.testclasses.basicSuperClass;
import simpl.descriptions.testclasses.declaredScalarCompositeInterfaceClass;
import simpl.descriptions.testclasses.declaredScalarCompositeMapClass;
import simpl.descriptions.testclasses.declaredScalarInterfaceListDescription;
import simpl.descriptions.testclasses.declaredScalarListDescription;
import simpl.interpretation.listOfScalars;
import simpl.types.ListType;
import simpl.types.MapType;

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
		
		@simpl_collection
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
		ClassDescriptor cd = ClassDescriptors.getClassDescriptor(declaredScalarListDescription.class);
		assertEquals(1, cd.allFieldDescriptors().size());
		
		ListType lt = cd.allFieldDescriptors().get(0).getListType();
		assertNotNull("Should have a list type!", lt);
		assertEquals(String.class, lt.getListItemType());
		assertEquals(LinkedList.class, lt.getListType());
		assertEquals(LinkedList.class, lt.getDeclaredListType());
		
		Object instance = lt.createInstance();
		assertEquals(instance.getClass(), LinkedList.class);
		
		FieldDescriptor fd = cd.allFieldDescriptors().get(0);
		assertEquals(FieldType.COLLECTION_SCALAR, fd.getType());
	}
	
	@Test
	public void testListTypeCreatedForInterfaceListType()
	{
		ClassDescriptor cd = ClassDescriptors.getClassDescriptor(declaredScalarInterfaceListDescription.class);
		assertEquals(1, cd.allFieldDescriptors().size());
		
		ListType lt = cd.allFieldDescriptors().get(0).getListType();
		assertNotNull("Should have a list type!", lt);
		assertEquals(String.class, lt.getListItemType());
		assertEquals(List.class, lt.getDeclaredListType());
		assertEquals(LinkedList.class, lt.getListType());

		Object instance = lt.createInstance();
		assertEquals(instance.getClass(), LinkedList.class);
		
		FieldDescriptor fd = cd.allFieldDescriptors().get(0);
		assertEquals(FieldType.COLLECTION_SCALAR, fd.getType());
	}
	
	@Test
	public void testMapTypeCreatedForDeclaredMapType()
	{
		ClassDescriptor cd = ClassDescriptors.getClassDescriptor(declaredScalarCompositeMapClass.class);
		assertEquals(1, cd.allFieldDescriptors().size());
		
		MapType mt = cd.allFieldDescriptors().get(0).getMapType();
		assertNotNull("Should have a map type!", mt);

		assertEquals(HashMap.class, mt.getDeclaredMapType());
		assertEquals(HashMap.class, mt.getMapType());
		
		declaredScalarCompositeMapClass mcd; 
		assertEquals(String.class, mt.getKeyType());
		assertEquals(basicSuperClass.class, mt.getValueType());

		Object instance = mt.createInstance();
		assertEquals(instance.getClass(), HashMap.class);
		
		FieldDescriptor fd = cd.allFieldDescriptors().get(0);
		assertEquals(FieldType.MAP_ELEMENT, fd.getType());
	}
	
	@Test
	public void testMapTypeCreatedForInterfaceMapType()
	{
		ClassDescriptor cd = ClassDescriptors.getClassDescriptor(declaredScalarCompositeInterfaceClass.class);
		assertEquals(1, cd.allFieldDescriptors().size());
		
		MapType mt = cd.allFieldDescriptors().get(0).getMapType();
		assertNotNull("Should have a map type!", mt);

		assertEquals(Map.class, mt.getDeclaredMapType());
		assertEquals(HashMap.class, mt.getMapType());
		
		declaredScalarCompositeMapClass mcd; 
		assertEquals(String.class, mt.getKeyType());
		assertEquals(basicSuperClass.class, mt.getValueType());

		Object instance = mt.createInstance();
		assertEquals(instance.getClass(), HashMap.class);
		
		FieldDescriptor fd = cd.allFieldDescriptors().get(0);
		assertEquals(FieldType.MAP_ELEMENT, fd.getType());
	}
}
