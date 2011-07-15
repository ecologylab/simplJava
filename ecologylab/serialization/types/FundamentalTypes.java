package ecologylab.serialization.types;

import java.util.ArrayList;
import java.util.HashMap;

import ecologylab.collections.Scope;
import ecologylab.generic.HashMapArrayList;

public interface FundamentalTypes
extends MappingConstants
{

	public static final CollectionType ARRAYLIST_TYPE				= 
		new CollectionType(JAVA_ARRAYLIST, ArrayList.class, DOTNET_ARRAYLIST, OBJC_ARRAYLIST, false);

	public static final CollectionType HASHMAP_TYPE					= 
		new CollectionType(JAVA_HASHMAP, HashMap.class, DOTNET_HASHMAP, OBJC_HASHMAP, true);

	public static final CollectionType HASHMAPARRAYLIST_TYPE	= 
		new CollectionType( JAVA_HASHMAPARRAYLIST, HashMapArrayList.class, DOTNET_HASHMAPARRAYLIST, OBJC_HASHMAPARRAYLIST, true);

	public static final CollectionType SCOPE_TYPE						= 
		new CollectionType(JAVA_SCOPE, Scope.class, DOTNET_SCOPE, OBJC_SCOPE, true);

}