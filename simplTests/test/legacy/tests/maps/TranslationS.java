package legacy.tests.maps;

import java.util.HashMap;

import simpl.annotations.dbal.simpl_map;
import simpl.annotations.dbal.simpl_nowrap;


public class TranslationS 
{
	@simpl_nowrap
	@simpl_map("class_descriptor")
	public HashMap<String, ClassDes>	entriesByTag;
	
	public TranslationS()
	{
		entriesByTag = new HashMap<String, ClassDes>();
	}

}
