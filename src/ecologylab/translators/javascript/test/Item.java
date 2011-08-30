package ecologylab.translators.javascript.test;

import ecologylab.serialization.ElementState;

public class Item extends ElementState{
	@simpl_scalar float price;
	@simpl_scalar String ownerName;
	@simpl_scalar String name;
	public Item(float price, String ownerName, String name) {
		super();
		this.price = price;
		this.ownerName = ownerName;
		this.name = name;
	}
}
