package simpl.descriptions;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import ecologylab.generic.ReflectionTools;


import simpl.annotations.dbal.simpl_collection;
import simpl.annotations.dbal.simpl_scalar;
import simpl.core.ISimplStringMarshaller;
import simpl.core.SimplIssue;
import simpl.exceptions.SIMPLDescriptionException;
import simpl.exceptions.SIMPLTranslationException;
import simpl.tools.XMLTools;


public class EnumerationDescriptor implements ISimplStringMarshaller, IMetaInformationProvider {
	
	/**
	 * Gets an enumeration description for a given class. 
	 * @param enumerationClass The given class to describe. 
	 * @return An EnumerationDescription
	 * @throws SIMPLDescriptionException Whenever the described class has invalid aspects 
	 */
	public static EnumerationDescriptor get(Class<?> enumerationClass) throws SIMPLDescriptionException
	{
		// TODO: Add caching here at some point. 
		
		EnumerationDescriptor ed = new EnumerationDescriptor(enumerationClass);
		
		if(isCustomValuedEnum(enumerationClass))
		{
			
			// Remember: An enum is a collection of static constants of a type...
			Object[] enumerationConstants = enumerationClass.getEnumConstants();
							
			for(Object o : enumerationConstants)
			{
				String enumEntryName = ((Enum<?>)o).name();
				Integer enumEntryValue;
				try 
				{
					// We marshal this value in; sometimes, we need to reset the accessibility
					enumEntryValue = (Integer)ed.getEnumerationCustomValueField().get(o);
					// Interesting aside: It seems that on the first entry of an enumeration, we /always/ need to set accessibility
					// But not on the second. Weird! 
				}
				catch (IllegalArgumentException e) {
					throw new SIMPLDescriptionException("Illegal argument exception while attempting to marshal entry value for enum entry: " + enumEntryName, e);
				}
				catch (IllegalAccessException e) 
				{
					try{
						// Most of the time, we can set the field to accessible to overcome the IllegalAccessException
						ed.getEnumerationCustomValueField().setAccessible(true);
						enumEntryValue = (Integer)ed.getEnumerationCustomValueField().get(o);
					}
					catch(Exception w)
					{
						throw new SIMPLDescriptionException("Illegal access exception while attempting to marshal entry value for enum entry: " + enumEntryName, w);			
					}
				}
				
				EnumerationEntry entry = new EnumerationEntry(enumEntryName, enumEntryValue);
				
				addAllMetaInformation(entry, enumerationClass);
				
				ed.getEnumerationEntries().add(entry);
			}
		}
		else
		{
			

			
			
			// Remember: An enum is a collection of static constants of a type...
			Object[] enumerationConstants = enumerationClass.getEnumConstants();
				
			for(Object o : enumerationConstants)
			{
				String enumEntryName = ((Enum<?>)o).name(); 
				
				EnumerationEntry entry = new EnumerationEntry(enumEntryName);
				
				addAllMetaInformation(entry, enumerationClass);
				
				ed.getEnumerationEntries().add(entry);
			}
		}
		
		return ed;
	}
	
	private static void addAllMetaInformation(EnumerationEntry entry, Class<?> enumerationClass)
	{
		Field entryField = ReflectionTools.getField(enumerationClass, entry.getName());
		AnnotationParser ap = new AnnotationParser();
		Collection<MetaInformation> metaInfo = ap.getAllMetaInformation(entryField);
		for(MetaInformation mi : metaInfo)
		{
			entry.addMetaInformation(mi);
		}
	}

	private String tagName;
	
	public String getTagName()
	{
		return this.tagName;
	}
	
	public void setTagName(String tag)
	{
		this.tagName = tag;
	}
	
	private IMetaInformationProvider metainfo;
	
	
	private ArrayList<String> otherTags;
	
	/**
	 * Initialize the basic data structures in the EnumerationDescription
	 */
	private void basicInitialization()
	{
		this.enumerationEntries = new LinkedList<>();
		this.metainfo = new MetaInformationCollection();
		this.otherTags = new ArrayList<String>();
	}
	
	/**
	 * This constructor is primarily for the sake of SIMPL serialization; you probably don't want to use it.
	 */
	public EnumerationDescriptor()
	{
		basicInitialization();
	}
	
	/**
	 * Creates an EnumerationDescription from a Class<?>... Does not add the EnumerationEntries 
	 * (Leave that to .Get() which will cache the EnumreationDescriptions)
	 * @param describedEnum
	 * @throws SIMPLDescriptionException
	 */
	private EnumerationDescriptor(Class<?> describedEnum) throws SIMPLDescriptionException
	{
		basicInitialization();
		
		if(describedEnum.isEnum())
		{
			this.enumerationClass = describedEnum;
			this.enumerationName = describedEnum.getName();
			this.packageName = describedEnum.getPackage().getName();
		
			// Add all meta information at the enumeration level
			AnnotationParser ap = new AnnotationParser();
			Collection<MetaInformation> metaInfo = ap.getAllMetaInformation(enumerationClass);
			
			for(MetaInformation imo : metaInfo)
			{
				this.addMetaInformation(imo);
			}
			
			this.tagName = XMLTools.getXmlTagName(describedEnum, "");
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
							throw new SIMPLDescriptionException("Error on: " + describedEnum.getName() + " The single field of an enumeration type should be annotated with the simpl_scalar type.");
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
	@simpl_collection("entry")
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

	public ArrayList<String> otherTags() {
		// TODO Auto-generated method stub
		return new ArrayList<String>();
	}

	public String getJavaTypeName() {
		// TODO Auto-generated method stub
		return enumerationName;
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
			// let's fetch a class! :3 
			try{
			Class<?> theClass = Class.forName(this.getJavaTypeName());
			this.enumerationClass = theClass;
			}
			catch(Exception e)
			{
				return null;
			}
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
	
	public Enum<?> getEntryEnumFromValue(Integer value)
	{
		return this.getEntryEnumValue(this.fetchEnumValueToEnumName().get(value));
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
	
	private HashMap<Integer, String> fetchEnumValueToEnumName()
	{
		HashMap<Integer, String> ourHash = new HashMap<>();

		if(isCustomValuedEnum(fetchEnumClass()))
		{
			for(EnumerationEntry ee : this.enumerationEntries)
			{
				ourHash.put(ee.getValue(), ee.getName());
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

	/**
	 * Gets the integer value that corresponds to a custom-valued enumeration
	 * @param string Entry name
 	 * @return Custom value
	 */
	public Integer getEntryEnumIntegerValue(String string) {
		return fetchEnumNameToEnumIntegerValue().get(string);
	}

	@Override
	/**
	 * Marshals a given Object (an enumeration, in our case) to a string representation.
	 */
	public String marshal(Object object) throws SIMPLTranslationException{
		// TODO Auto-generated method stub
		if(object == null)
		{
			throw new SIMPLTranslationException(new SimplIssue("Should not attempt to marshal null values.", null, object));
		}
		
		if(object.getClass().isEnum())
		{
			if(object.getClass().equals(this.fetchEnumClass()))
			{
				//We have a valid object! Let's do this!
				return object.toString(); // Hehehe. This should do the trick.
				// Marshalling enums here is super trivial because toString() is guarenteed to return the name. 
				// Unmarshalling will be a bit more tricksy... but not by much.
			}
			else
			{
				throw new SIMPLTranslationException(new SimplIssue("Could not marshal because enumeration class was not the same as that in description. Was: "+ object.getClass().getName(), null, object));		
			}
		}
		else
		{
			throw new SIMPLTranslationException(new SimplIssue("Could not marshal a non-enumeration type... was: " + object.getClass().getName(), null, object));
		}
	}

	@Override
	public Object unmarshal(String string) throws SIMPLTranslationException{
		// TODO Auto-generated method stub
		if(string == null || string.isEmpty())
		{
			throw new SIMPLTranslationException(new SimplIssue("Could not unmarshal a null or empty string!", string, null));
		}

		if(this.containsEntry(string))
		{
			// We have this entry! Let's try to get it. 
			return this.getEntryEnumValue(string);
		}
		else
		{
			// Can we convert the string to an integer? If yes: try to marshal by value...
			// otherwise, it's not in here! 
			try
			{
				Integer enumValue = Integer.parseInt(string);
				Object value = this.getEntryEnumFromValue(enumValue);
				if(value == null)
				{
					throw new SIMPLTranslationException(new SimplIssue("No enumeration entry exists with given value!", string, null));
				}
				else
				{
					return value;
				}
			}
			catch(NumberFormatException nfe)
			{
				throw new SIMPLTranslationException(new SimplIssue("Could not find the string value in the enumeration!", string, null));
			}
		}
	}

	public Object getSimpleName() {
		// TODO Auto-generated method stub
		return this.getEnumerationClass().getSimpleName();
	}

	@Override
	public void addMetaInformation(MetaInformation imo) {
		this.metainfo.addMetaInformation(imo);	
	}

	@Override
	public Collection<MetaInformation> getMetaInformation() {
		return this.metainfo.getMetaInformation();
	}

	@Override
	public boolean containsMetaInformation(String name) {
		return this.metainfo.containsMetaInformation(name);
	}

	@Override
	public MetaInformation getMetaInformation(String name) {
		return this.metainfo.getMetaInformation(name);
	}
}
