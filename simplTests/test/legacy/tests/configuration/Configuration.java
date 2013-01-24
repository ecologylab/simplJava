package legacy.tests.configuration;

import java.util.ArrayList;

import legacy.tests.TestCase;
import legacy.tests.TestingUtils;

import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.SimplTypesScope;
import ecologylab.serialization.annotations.simpl_classes;
import ecologylab.serialization.annotations.simpl_collection;
import ecologylab.serialization.annotations.simpl_composite;
import ecologylab.serialization.formatenums.Format;

public class Configuration implements TestCase
{
	@simpl_classes(
	{ Pref.class, PrefDouble.class, PrefInteger.class })
	@simpl_composite
	public Pref							pref;

	@simpl_classes(
	{ Pref.class, PrefDouble.class, PrefInteger.class })
	@simpl_collection
	public ArrayList<Pref>	prefs;

	public Configuration()
	{

	}

	@Override
	public void runTest() throws SIMPLTranslationException
	{
		SimplTypesScope translationScope = SimplTypesScope.get("configurationTScope", Configuration.class, Pref.class, 
				PrefInteger.class, PrefDouble.class, Pref.class);

		Configuration configuration = new Configuration();

		PrefInteger prefInteger = new PrefInteger();
		prefInteger.name = "integer_pref";
		prefInteger.intValue = 2;

		PrefDouble prefDouble = new PrefDouble();
		prefDouble.name = "double_pref";
		prefDouble.doubleValue = 5;

		Pref pref = new Pref();
		pref.name = "only_pref";

		ArrayList<Pref> prefList = new ArrayList<Pref>();
		prefList.add(pref);
		prefList.add(prefInteger);
		prefList.add(prefDouble);

		configuration.pref = prefInteger;
		configuration.prefs = prefList;
		
		TestingUtils.test(configuration, translationScope, Format.XML);
		TestingUtils.test(configuration, translationScope, Format.JSON);
		TestingUtils.test(configuration, translationScope, Format.TLV);

	}
}
