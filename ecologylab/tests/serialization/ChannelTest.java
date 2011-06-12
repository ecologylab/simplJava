package ecologylab.tests.serialization;

import java.util.ArrayList;

import ecologylab.serialization.ElementState;
import ecologylab.serialization.ElementState.xml_tag;
import ecologylab.serialization.library.rss.Item;

@xml_tag("channel")
public class ChannelTest extends Base
{
  @simpl_nowrap @simpl_collection("item") ArrayList<Item> items;

	public ChannelTest()
	{
		// TODO Auto-generated constructor stub
	}

}
