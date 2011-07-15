package ecologylab.serialization.types;

import java.util.ArrayList;
import java.util.HashMap;

import ecologylab.collections.Scope;
import ecologylab.generic.HashMapArrayList;

public class FundamentalTypes
implements CrossLanguageTypeConstants
{
	public static final CollectionType ARRAYLIST_TYPE				= 
		new CollectionType(ArrayList.class, DOTNET_ARRAYLIST, OBJC_ARRAYLIST);

	public static final CollectionType HASHMAP_TYPE					= 
		new CollectionType(HashMap.class, DOTNET_HASHMAP, OBJC_HASHMAP);

	public static final CollectionType HASHMAPARRAYLIST_TYPE	= 
		new CollectionType( HashMapArrayList.class, DOTNET_HASHMAPARRAYLIST, OBJC_HASHMAPARRAYLIST);

	public static final CollectionType SCOPE_TYPE						= 
		new CollectionType(Scope.class, DOTNET_SCOPE, OBJC_SCOPE);
	
	
}