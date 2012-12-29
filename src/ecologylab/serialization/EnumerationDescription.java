package ecologylab.serialization;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import ecologylab.serialization.annotations.simpl_scalar;

public class EnumerationDescription extends DescriptorBase {
	
	public static EnumerationDescription get(Class<?> enumerationClass) throws SIMPLDescriptionException
	{
		
		EnumerationDescription ed = new EnumerationDescription(enumerationClass);
		
		if(isCustomValuedEnum(enumerationClass))
		{
			// Remember: An enum is a collection of static constants of a type...
			Object[] enumerationConstants = enumerationClass.getEnumConstants();
							
			for(Object o : enumerationConstants)
			{
				String enumEntryName = ((Enum<?>)o).toString();
				Integer enumEntryValue;
				try 
				{
					enumEntryValue = (Integer)ed.getEnumerationCustomValueField().get(o);
					System.out.println(enumEntryName + " = " + enumEntryValue == null ? "null" : enumEntryValue.toString());
				}
				catch (IllegalArgumentException e) {
					throw new SIMPLDescriptionException("Illegal argument exception while attempting to marshal entry value for enum entry: " + enumEntryName, e);
				}
				catch (IllegalAccessException e) 
				{
					try{
						
						ed.getEnumerationCustomValueField().setAccessible(true);
						enumEntryValue = (Integer)ed.getEnumerationCustomValueField().get(o);
					}
					catch(Exception w)
					{
						throw new SIMPLDescriptionException("Illegal access exception while attempting to marshal entry value for enum entry: " + enumEntryName, w);			
					}
				}
				
				ed.getEnumerationEntries().add(new EnumerationEntry(enumEntryName, enumEntryValue));
			}
		}
		else
		{
			// Remember: An enum is a collection of static constants of a type...
			Object[] enumerationConstants = enumerationClass.getEnumConstants();
				
			for(Object o : enumerationConstants)
			{
				String enumEntryName = ((Enum<?>)o).toString();
				
				ed.getEnumerationEntries().add(new EnumerationEntry(enumEntryName));
			}
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
			
			if(isCustomValuedEnum(describedEnum))
			{
				List<Field> enumFields = getEnumerationFields(describedEnum);
				
				if(enumFields.size() == 1)
				{
					Field theField = enumFields.get(0);
				
					if(!(theField.getType().equals(Integer.class) || theField.getType().equals(int.class)))
					{
						throw new SIMPLDescriptionException("To facilitate cross-platform compatability, any custom valued enumeration must be an Integer or an int type.");
					}
					else
					{
						if(theField.getAnnotation(simpl_scalar.class) != null)
						{
							this.enumerationCustomValueField = theField;
						}
						else
						{
							throw new SIMPLDescriptionException("The single field of an enumeration type should be annotated with the simpl_scalar type.");
						}
					}
				}
				else
				{					
					// In java, our enumerations can be super fancy... not the case in most languages. (specifically statically typed languages w/ enum types)
					throw new SIMPLDescriptionException("To facilitate cross-platform compatibility, any custom-valued enumeration must have only a single value field.");
				}
			}
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
	 * The field that represents the custom value for this enumeration.
	 */
	private Field enumerationCustomValueField;
	
	
	public Field getEnumerationCustomValueField() {
		return enumerationCustomValueField;
	}

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
		if(this.enumerationClass == null)
		{
			return null;
		}
		// TODO: Standard simpl code for ressurecting the class of a simpl descriptor / field? 
		return this.enumerationClass;
	}
	
	private HashMap<String, Enum<?>> enumNameToEnumValueHash = null;
	private HashMap<Integer, Enum<?>> enumIntegerToEnumValueHash = null;
	
	/**
	 * Fetches the Enum Value from the name of a given enrty, or null if the given string does not correspond to a value.
	 * @param string The entry name to retrieve
	 * @return The Enum value for the entry name, or null if none exists. 
	 */
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

	/**
	 * Fetches a name to EnumValue hash, lazy initializes behind the scenes.
	 * @return
	 */
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
	
	private HashMap<String, Integer> fetchEnumNameToEnumIntegerValue() { 

		HashMap<String, Integer> ourHash = new HashMap<>();

		if(isCustomValuedEnum(fetchEnumClass()))
		{
			for(EnumerationEntry ee : this.enumerationEntries)
			{
				ourHash.put(ee.getName(), ee.getValue());
			}
		}
		
		return ourHash;
	}
	
	
	/**
	 * Filters out all extraneous fields and gets only enumeration-entry level fields
	 * @param enumClass
	 * @return
	 */
	private static List<Field> getEnumerationFields(Class<?> enumClass)
	{
		List<Field> filteredFields = new LinkedList<Field>();
		for(Field f:enumClass.getDeclaredFields())
		{
			if(f.isEnumConstant())
			{
				continue; // exclude enum constants
			}
			
			if(Modifier.isStatic(f.getModifiers()))
			{
				continue; // exclude static
			}
			
			filteredFields.add(f);
		}
		return filteredFields;
	}

	/**
	 * Determines if a given class represents a "Custom Valued" enumeration
	 * @param enumClass
	 * @return
	 */
	public static boolean isCustomValuedEnum(Class<?> enumClass) {
		if(enumClass.isEnum())
		{
			// So, an enumeration creates public static final fields for each entry
			// and a private static final values() field...
			// any additional fields have different signatures; so let's filter out all of those other fields and procede accordingly. 
			
			List<Field> filteredFields = getEnumerationFields(enumClass);
			
			if(filteredFields.isEmpty())
			{
				return false;
			}
			else
			{
				return true;
			}
		}else{
			return false; // not even an enum to begin with!
		}
	}

	public Integer getEntryEnumIntegerValue(String string) {
		return fetchEnumNameToEnumIntegerValue().get(string);
	}
}
