package ecologylab.serialization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import ecologylab.serialization.annotations.simpl_scalar;

public class EnumerationDescription extends DescriptorBase {
	
	public static EnumerationDescription get(Class<?> enumerationClass) throws SIMPLDescriptionException
	{
		
		EnumerationDescription ed = new EnumerationDescription(enumerationClass);
		
		// Remember: An enum is a collection of static constants of a type...
		Object[] enumerationConstants = enumerationClass.getEnumConstants();
		// If we customized the type (to select our own integer values...) 
		// We'll have fields!
			
		for(Object o : enumerationConstants)
		{
			String enumEntryName = ((Enum<?>)o).toString();
			
			ed.getEnumerationEntries().add(new EnumerationEntry(enumEntryName));
		}
		
		return ed;
	}
	
	private void basicInitialization()
	{
		this.enumerationEntries = new LinkedList<>();
		this.metaInfo = new LinkedList<>();
		this.otherTags = new ArrayList<>();
	}
	/**
	 * This constructor is primarily for the sake of SIMPL serialization; you probably don't want to use it.
	 */
	public EnumerationDescription()
	{
		basicInitialization();
	}
	
	public EnumerationDescription(Class<?> describedEnum) throws SIMPLDescriptionException
	{
		super(XMLTools.getXmlTagName(describedEnum, null), describedEnum.getSimpleName());
		basicInitialization();
		
		if(describedEnum.isEnum())
		{
			this.enumerationClass = describedEnum;
			this.enumerationName = describedEnum.getName();
			this.packageName = describedEnum.getPackage().getName();
			
		}else{
			throw new SIMPLDescriptionException("Cannot create an enumeration description for a non-enumeration type.");
		}
	}
	
	@simpl_scalar 
	private String packageName; 
	
	@simpl_scalar
	private String enumerationName;
	
	/**
	 * The class that represents the enumeration class.
	 */
	private Class<?> enumerationClass;
	
	/**
	 * A list of entires in this given enumeration. 
	 */
	private List<EnumerationEntry> enumerationEntries;
	

	public Class<?> getEnumerationClass() {
		return enumerationClass;
	}

	public void setEnumerationClass(Class<?> enumerationClass) {
		this.enumerationClass = enumerationClass;
	}

	public List<EnumerationEntry> getEnumerationEntries() {
		return enumerationEntries;
	}

	public void setEnumerationEntries(List<EnumerationEntry> enumerationEntries) {
		this.enumerationEntries = enumerationEntries;
	}

	@Override
	public ArrayList<String> otherTags() {
		// TODO Auto-generated method stub
		return new ArrayList<String>();
	}

	@Override
	public String getJavaTypeName() {
		// TODO Auto-generated method stub
		return enumerationName;
	}

	@Override
	public String getCSharpTypeName() {
		// TODO Auto-generated method stub
		return enumerationName;
    }

	@Override
	public String getCSharpNamespace() {
		// TODO Auto-generated method stub
		return packageName;
	}

	@Override
	public String getObjectiveCTypeName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDbTypeName() {
		// TODO Auto-generated method stub
		return null ;
	}
	
	/**
	 * Determines if this enumeration contains an entry with the given entry name
	 * @param entryName Name of the entry (case sensitive) 
	 * @return True if the entry is in the list of entries
	 */
	public boolean containsEntry(String entryName)
	{
		for(EnumerationEntry ee : this.enumerationEntries)
		{
			if(ee.getName().equals(entryName))
			{
				return true;
			}
		}
		
		return false;
	}

	private Class<?> fetchEnumClass()
	{
		return null;
	}
	
	private HashMap<String, Enum<?>> enumNameToEnumValueHash = null;
	private HashMap<Integer, Enum<?>> enumIntegerToEnumValueHash = null;
	
	public Enum<?> getEntryEnumValue(String string) {
		
		// lazy initialize some underlying hashes from the data we have. 
		if(enumNameToEnumValueHash == null)
		{
			if(this.enumerationClass == null)
			{
				this.enumerationClass = fetchEnumClass();
			}
			this.enumNameToEnumValueHash = fetchEnumNameToEnumValueHash();
		}
		
		return this.enumNameToEnumValueHash.get(string);
	}

	private HashMap<String, Enum<?>> fetchEnumNameToEnumValueHash() {
		if(this.enumerationClass == null)
		{
			this.enumerationClass = fetchEnumClass();
		}

		HashMap<String, Enum<?>> ourHash = new HashMap<>();
		
		for(Object o : this.enumerationClass.getEnumConstants())
		{
			Enum<?> aValue = (Enum<?>)o;
			String aName = aValue.toString();
			ourHash.put(aName, aValue);
		}
		
		return ourHash;
	}

	public static boolean isCustomValuedEnum(Class<?> enumClass) {
		if(enumClass.isEnum())
		{
			
			return false;
		}else{
			return false; // not even an enum to begin with!
		}
	}
}
