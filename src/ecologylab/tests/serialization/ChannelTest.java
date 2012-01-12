package ecologylab.tests.serialization;

import java.util.ArrayList;

import ecologylab.serialization.annotations.simpl_collection;
import ecologylab.serialization.annotations.simpl_nowrap;
import ecologylab.serialization.annotations.simpl_tag;
import ecologylab.serialization.library.rss.Item;

@simpl_tag("channel")
public class ChannelTest extends Base
{
  @simpl_nowrap @simpl_collection("item") ArrayList<Item> items;

	public ChannelTest()
	{
		// TODO Auto-generated constructor stub
	}

}
