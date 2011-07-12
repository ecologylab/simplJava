package ecologylab.translators.javascript.test;

import ecologylab.serialization.ElementState;
import ecologylab.serialization.ElementState.simpl_scalar;
import ecologylab.serialization.ElementState.xml_tag;
import ecologylab.serialization.types.element.Mappable;

public class Item extends ElementState implements Mappable{
	
	@simpl_scalar float price;
	@simpl_scalar String ownerName;
	@simpl_scalar String name;
	public Item(float price, String ownerName, String name) {
		super();
		this.price = price;
		this.ownerName = ownerName;
		this.name = name;
	}
	@Override
	public Object key() {
		return ownerName;
	}
}
