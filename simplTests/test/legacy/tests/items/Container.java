package legacy.tests.items;

import java.util.ArrayList;

import legacy.tests.TestCase;
import legacy.tests.TestingUtils;

import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.SimplTypesScope;
import ecologylab.serialization.annotations.simpl_collection;
import ecologylab.serialization.annotations.simpl_nowrap;
import ecologylab.serialization.annotations.simpl_scope;
import ecologylab.serialization.formatenums.Format;

public class Container implements TestCase
{

	@simpl_nowrap
	@simpl_scope("itemScope1")
	@simpl_collection
	ArrayList<ItemBase>	itemCollection1;

	// @simpl_scope("itemScope2")
	// @simpl_collection
	ArrayList<ItemBase>	itemCollection2;

	public Container()
	{

	}

	public void populateContainer()
	{
		itemCollection1 = new ArrayList<ItemBase>();
		itemCollection2 = new ArrayList<ItemBase>();

		itemCollection1.add(new ItemOne(1, 1));
		itemCollection1.add(new ItemOne(1, 2));
		itemCollection1.add(new ItemOne(1, 3));
		itemCollection1.add(new ItemTwo("one", 1));
		itemCollection1.add(new ItemTwo("two", 2));
		itemCollection1.add(new ItemTwo("three", 3));

		itemCollection2.add(new ItemTwo("one", 1));
		itemCollection2.add(new ItemTwo("two", 2));
		itemCollection2.add(new ItemTwo("three", 3));
		itemCollection2.add(new ItemRandom("four", 4));
		itemCollection2.add(new ItemRandom("five", 5));
		itemCollection2.add(new ItemRandom("six", 6));
	}

	@Override
	public void runTest() throws SIMPLTranslationException
	{

		Container c = new Container();
		c.populateContainer();

//		TranslationScope itemTranslationScope = TranslationScope.get("itemScope1", ItemBase.class,
//				ItemOne.class, ItemTwo.class);
//
//		TranslationScope itemTranslationScope2 = TranslationScope.get("itemScope2", ItemBase.class,
//				ItemRandom.class, ItemTwo.class);

		SimplTypesScope containerTranslationScope = SimplTypesScope.get("containerTScope",
				Container.class, ItemBase.class, ItemOne.class, ItemTwo.class, ItemRandom.class);
		
		TestingUtils.test(c, containerTranslationScope, Format.XML);
		TestingUtils.test(c, containerTranslationScope, Format.JSON);
		TestingUtils.test(c, containerTranslationScope, Format.TLV);

	}
}
