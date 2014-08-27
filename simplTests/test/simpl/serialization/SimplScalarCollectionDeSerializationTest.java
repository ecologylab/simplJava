package simpl.serialization;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import legacy.tests.scalar.ScalarCollection;

import org.junit.Test;

import ecologylab.net.ParsedURL;
import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.SimplTypesScope;
import ecologylab.serialization.formatenums.StringFormat;

public class SimplScalarCollectionDeSerializationTest {

	@Test
	public void scalarCollectionDeSerializationTest() throws SIMPLTranslationException
	{
		SimplTypesScope.enableGraphSerialization();
		
		ScalarCollection sc = new ScalarCollection();
		sc.addInt(1);
		sc.addInt(2);
		sc.addInt(3);
		sc.addInt(4);
		sc.addInt(5);

		SimplTypesScope translationScope = SimplTypesScope.get("scalarCollectionTScope", ScalarCollection.class);
		

		//JSON===
		String json = SimplTypesScope.serialize(sc, StringFormat.JSON).toString();
		System.out.println(json);
		assertEquals(json, "{\"scalar_collection\":{\"collection_of_integers\":[\"1\",\"2\",\"3\",\"4\",\"5\"]}}");
		
		ScalarCollection sc1 = (ScalarCollection) translationScope.deserialize(json, StringFormat.JSON);
		assertEquals(5, sc1.getInts().size());
		for (int i = 0; i < 5; ++i)
		{
  		assertEquals(i + 1, (int)sc1.getInts().get(i));
		}
		//===
		

		//XML===
		String xml = SimplTypesScope.serialize(sc, StringFormat.XML).toString();
		System.out.println(json);
		assertEquals(xml, "<scalar_collection><circles>1</circles><circles>2</circles><circles>3</circles><circles>4</circles><circles>5</circles></scalar_collection>");
		
		ScalarCollection sc2 = (ScalarCollection) translationScope.deserialize(xml, StringFormat.XML);
		assertEquals(5, sc2.getInts().size());
		for (int i = 0; i < 5; ++i)
		{
  		assertEquals(i + 1, (int)sc2.getInts().get(i));
		}
		//===
	}
	
	@Test
	public void scalarCollectionsInJsonAlwaysWrapped() throws SIMPLTranslationException
	{
	  ScalarCollection sc = new ScalarCollection();
	  List<ParsedURL> additionalLocations = new ArrayList<ParsedURL>();
	  additionalLocations.add(ParsedURL.getAbsolute("http://example.com/page1"));
	  additionalLocations.add(ParsedURL.getAbsolute("http://example.com/page2"));
    sc.setAdditionalLocations(additionalLocations);
    
    String json = SimplTypesScope.serialize(sc, StringFormat.JSON).toString();
    System.out.println(json);
    assertTrue(json.contains("\"additional_locations\":["));
    assertFalse(json.contains("\"locations\":["));
	}

}
