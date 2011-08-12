package ecologylab.translators.hibernate;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ecologylab.appframework.PropertiesAndDirectories;
import ecologylab.generic.Debug;
import ecologylab.generic.HashMapArrayList;
import ecologylab.semantics.metametadata.exceptions.MetaMetadataException;
import ecologylab.serialization.ClassDescriptor;
import ecologylab.serialization.FieldDescriptor;
import ecologylab.serialization.FieldTypes;
import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.TranslationScope;
import ecologylab.translators.hibernate.hbmxml.HibernateClass;
import ecologylab.translators.hibernate.hbmxml.HibernateClassCache;
import ecologylab.translators.hibernate.hbmxml.HibernateClassId;
import ecologylab.translators.hibernate.hbmxml.HibernateClassIdGenerator;
import ecologylab.translators.hibernate.hbmxml.HibernateCollection;
import ecologylab.translators.hibernate.hbmxml.HibernateCollectionScalar;
import ecologylab.translators.hibernate.hbmxml.HibernateComposite;
import ecologylab.translators.hibernate.hbmxml.HibernateFieldBase;
import ecologylab.translators.hibernate.hbmxml.HibernateJoinedSubclass;
import ecologylab.translators.hibernate.hbmxml.HibernateKey;
import ecologylab.translators.hibernate.hbmxml.HibernateManyToMany;
import ecologylab.translators.hibernate.hbmxml.HibernateMapping;
import ecologylab.translators.hibernate.hbmxml.HibernateProperty;

/**
 * 
 * @author quyin
 * 
 */
@SuppressWarnings("rawtypes")
public class HibernateXmlMappingGenerator extends Debug
{

	public static final String						XML_HEAD														= "<?xml version=\"1.0\"?>\n<!DOCTYPE hibernate-mapping PUBLIC \"-//Hibernate/Hibernate Mapping DTD 3.0//EN\" \"http://www.hibernate.org/dtd/hibernate-mapping-3.0.dtd\">\n";

	public static final String						DEFAULT_ORM_ID_FIELD_NAME						= "ormId";

	// public static final String DISCRIMINATOR_COLUMN_NAME = "type_discrim";

	public static final String						ASSOCIATION_TABLE_SEP								= "__";

	public static final String						SCALAR_COLLECTION_VALUE_COLUMN_NAME	= "value";
	
	public static Class										DB_NAME_GENERATOR_CLASS							= DbNameGenerator.class;

	private DbNameGenerator								dbNameGenerator;
	
	private NameTable<ClassDescriptor>		tableNames;

	private NameTable<FieldDescriptor>		columnNames;

	private TranslationScope							translationScope;

	private Map<String, String>						idFieldNameByClass;

	private Map<String, HibernateMapping>	allMapping													= new HashMap<String, HibernateMapping>();

	public HibernateXmlMappingGenerator()
	{
		try
		{
			dbNameGenerator = (DbNameGenerator) DB_NAME_GENERATOR_CLASS.newInstance();
		}
		catch (InstantiationException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IllegalAccessException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		tableNames = new NameTable<ClassDescriptor>() {
			@Override
			public String createName(ClassDescriptor obj)
			{
				return dbNameGenerator.createTableName(obj);
			}
		};
		columnNames = new NameTable<FieldDescriptor>() {
			@Override
			public String createName(FieldDescriptor obj)
			{
				return dbNameGenerator.createColumnName(obj);
			}
		};
	}

	protected String getAssociationTableName(ClassDescriptor cd, FieldDescriptor fd)
	{
		return tableNames.get(cd) + ASSOCIATION_TABLE_SEP + columnNames.get(fd);
	}

	protected String getAssociationTableColumnName(ClassDescriptor cd)
	{
		return tableNames.get(cd) + "_id";
	}

	public List<String> generateMappings(File hbmDir, TranslationScope tscope, Map<String, String> idFieldNameByClass)
			throws FileNotFoundException, SIMPLTranslationException
	{
		PropertiesAndDirectories.createDirsAsNeeded(hbmDir);

		this.translationScope = tscope;
		this.idFieldNameByClass = idFieldNameByClass;

		List<String> mappingImports = new ArrayList<String>();

		for (ClassDescriptor cd : tscope.getClassDescriptors())
		{
			HibernateMapping mapping = new HibernateMapping();
			mapping.setMappingPackageName(cd.getDescribedClassPackageName());
			HibernateClass currentClass = generateClassMapping(cd);
			if (currentClass != null)
			{
				mapping.getMappedClasses().put(currentClass.getName(), currentClass);
				allMapping.put(currentClass.getName(), mapping);
			}
			columnNames.clear();
		}

		for (HibernateMapping mapping : allMapping.values())
		{
			HibernateClass currentClass = mapping.getMappedClasses().get(0);
			String tableName = currentClass.getTable();
			File newHbmFile = new File(hbmDir, tableName + ".hbm.xml");
			mappingImports.add(String.format("<mapping file=\"%s\" />", newHbmFile.getPath()));
			PrintWriter writer = new PrintWriter(newHbmFile);
			writer.println(XML_HEAD);
			mapping.serialize(writer);
			writer.println();
			writer.close();
		}

		return mappingImports;
	}

	protected HibernateClass generateClassMapping(ClassDescriptor cd)
	{
		HibernateClass thatClass = null;

		ClassDescriptor superCd = cd.getSuperClass();
		if (superCd == null || !this.translationScope.getClassDescriptors().contains(superCd))
		{
			thatClass = new HibernateClass();

			String idFieldName = findIdFieldName(cd);
			String idColName = findIdColName(cd);
			thatClass.setId(new HibernateClassId(idFieldName, idColName, HibernateClassIdGenerator.identityGenerator));
			thatClass.setCache(new HibernateClassCache());
			// thatClass.setDiscriminatorValue(cd.getDescribedClassName());
			// thatClass.setDiscriminator(new HibernateClassDiscriminator(DISCRIMINATOR_COLUMN_NAME));
		}
		else
		{
			String idColName = findIdColName(cd);
			thatClass = new HibernateJoinedSubclass(superCd.getDescribedClassName(), new HibernateKey(idColName));
		}
		thatClass.setName(cd.getDescribedClassName());
		thatClass.setTable(tableNames.get(cd));

		thatClass.setProperties(new HashMapArrayList<String, HibernateFieldBase>());
		for (Object fdObj : cd.getDeclaredFieldDescriptorsByFieldName().values())
		{
			FieldDescriptor fd = (FieldDescriptor) fdObj;
			if (fd.getName().equals(findIdFieldName(cd)))
				continue;
			HibernateFieldBase currentField = generateFieldMapping(cd, fd);
			if (currentField != null)
				thatClass.getProperties().put(currentField.getName(), currentField);
		}

		return thatClass;
	}

	protected String findIdColName(ClassDescriptor cd)
	{
		String idFieldName = findIdFieldName(cd);
		return dbNameGenerator.createColumnName(idFieldName);
	}

	protected String findIdFieldName(ClassDescriptor cd)
	{
		if (cd == null)
			return null;
		else if (this.idFieldNameByClass == null)
			return DEFAULT_ORM_ID_FIELD_NAME;
		else if (this.idFieldNameByClass.containsKey(cd.getDescribedClassName()))
			return this.idFieldNameByClass.get(cd.getDescribedClassName());
		else
		{
			String result = findIdFieldName(cd.getSuperClass());
			if (result != null)
				this.idFieldNameByClass.put(cd.getDescribedClassName(), result);
			return result;
		}
	}

	protected HibernateFieldBase generateFieldMapping(ClassDescriptor cd, FieldDescriptor fd)
	{
		HibernateFieldBase thatField = null;

		int typeCode = fd.getType();
		switch (typeCode)
		{
		case FieldTypes.SCALAR:
			thatField = generatePropertyMapping(cd, fd);
			break;
		case FieldTypes.COMPOSITE_ELEMENT:
			thatField = generateCompositeMapping(cd, fd);
			break;
		case FieldTypes.COLLECTION_ELEMENT:
			thatField = generateElementCollectionMapping(cd, fd);
			break;
		case FieldTypes.COLLECTION_SCALAR:
			thatField = generateScalarCollectionMapping(cd, fd);
			break;
		default:
			throw new MetaMetadataException("Unknown field type: " + cd + ": " + fd + ": " + typeCode);
		}

		return thatField;
	}

	protected HibernateProperty generatePropertyMapping(ClassDescriptor cd, FieldDescriptor fd)
	{
		HibernateProperty prop = new HibernateProperty();
		prop.setName(fd.getName());
		prop.setColumn(columnNames.get(fd));
		prop.setType(fd.getScalarType().getJavaTypeName());
		return prop;
	}

	protected HibernateComposite generateCompositeMapping(ClassDescriptor mmd, FieldDescriptor fd)
	{
		ClassDescriptor elementCd = fd.getElementClassDescriptor();
		if (elementCd == null)
		{
			warning("Empty element ClassDescriptor (might be polymorphic field): " + fd);
			return null;
		}

		HibernateComposite comp = new HibernateComposite();
		comp.setName(fd.getName());
		comp.setColumn(columnNames.get(fd));
		comp.setCompositeClassName(elementCd.getDescribedClassName());
		return comp;
	}

	protected HibernateCollection generateElementCollectionMapping(ClassDescriptor cd, FieldDescriptor fd)
	{
		ClassDescriptor elementCd = fd.getElementClassDescriptor();
		if (elementCd == null)
		{
			warning("Empty element ClassDescriptor (might be polymorphic field): " + fd);
			return null;
		}

		HibernateCollection coll = new HibernateCollection();
		coll.setName(fd.getName());
		coll.setTable(getAssociationTableName(cd, fd));
		coll.setKey(new HibernateKey(findIdColName(cd)));
		coll.setManyToMany(new HibernateManyToMany(getAssociationTableColumnName(elementCd), elementCd.getDescribedClassName()));

		// FIXME set reverse mapping?
		// String theOtherTableName = tableNames.get(elementCd);
		// HibernateClass theOtherMappedClass =
		// this.hibernateMappings.getMappedClasses().get(theOtherTableName);
		// if (theOtherMappedClass != null && theOtherMappedClass.isColumnMapped(thisIdColumnName))
		// coll.setInverse(true);

		return coll;
	}

	protected HibernateCollection generateScalarCollectionMapping(ClassDescriptor cd, FieldDescriptor fd)
	{
		HibernateCollection coll = new HibernateCollection();
		coll.setName(fd.getName());
		coll.setTable(getAssociationTableName(cd, fd));
		coll.setKey(new HibernateKey(getAssociationTableColumnName(cd)));
		coll.setElement(new HibernateCollectionScalar(SCALAR_COLLECTION_VALUE_COLUMN_NAME, fd.getScalarType().getJavaTypeName()));
		return coll;
	}

}
