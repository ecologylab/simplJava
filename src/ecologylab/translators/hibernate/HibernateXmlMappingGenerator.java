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
	
	public static final String											XML_HEAD								= "<?xml version=\"1.0\"?>\n<!DOCTYPE hibernate-mapping PUBLIC \"-//Hibernate/Hibernate Mapping DTD 3.0//EN\" \"http://www.hibernate.org/dtd/hibernate-mapping-3.0.dtd\">\n";

	/**
	 * the implementation class of DbNameGenerator. when a new HibernateXmlMappingGenerator is newed,
	 * this static member will be used to create the generator.
	 */
	public static Class<? extends DbNameGenerator>	DB_NAME_GENERATOR_CLASS	= DefaultCachedDbNameGenerator.class;

	private DbNameGenerator													dbNameGenerator;

	private TranslationScope												translationScope;

	private Map<String, String>											idFieldNameByClass;

	private Map<String, HibernateMapping>						allMapping							= new HashMap<String, HibernateMapping>();

	public HibernateXmlMappingGenerator()
	{
		try
		{
			dbNameGenerator = DB_NAME_GENERATOR_CLASS.newInstance();
		}
		catch (InstantiationException e)
		{
			e.printStackTrace();
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * the entry method for generating native hibernate mappings from a translation scope.
	 * 
	 * @param hbmDir
	 *          the destination dir of generated hibernate mapping files. each class (entity) will
	 *          have one corresponding mapping file.
	 * @param translationScope
	 *          the translation scope from which we want to generate hibernate mapping files.
	 * @param idFieldNameByClass
	 *          the map from class (qualified) names to ORM ID field names. ORM ID field will be used
	 *          as the primary key in the database tables. currently, this id field must be
	 *          <i>surrogate id</i> and managed by the database service (no writes allowed on this
	 *          field, but reads are ok). only "root" classes need to be in this map (inherited
	 *          classes will use the ORM ID field in the base class).
	 *          <p>
	 *          NOTE that this map might be modified during the generating process as a cache.
	 * @return a list of configurations you should add to your hibernate config file (typically,
	 *         hibernate.cfg.xml).
	 * @throws FileNotFoundException
	 * @throws SIMPLTranslationException
	 */
	public List<String> generateMappings(File hbmDir, TranslationScope translationScope, Map<String, String> idFieldNameByClass)
			throws FileNotFoundException, SIMPLTranslationException
	{
		PropertiesAndDirectories.createDirsAsNeeded(hbmDir);

		this.translationScope = translationScope;
		this.idFieldNameByClass = idFieldNameByClass;

		List<String> mappingImports = new ArrayList<String>();

		for (ClassDescriptor cd : translationScope.entriesByClassName().values())
		{
			HibernateMapping mapping = new HibernateMapping();
			mapping.setMappingPackageName(cd.getDescribedClassPackageName());
			HibernateClass currentClass = generateClassMapping(cd);
			if (currentClass != null)
			{
				mapping.getMappedClasses().put(currentClass.getName(), currentClass);
				allMapping.put(currentClass.getName(), mapping);
			}
			this.dbNameGenerator.clearCache();
		}

		for (HibernateMapping mapping : allMapping.values())
		{
			HibernateClass currentClass = mapping.getMappedClasses().get(0);
			File newHbmFile = new File(hbmDir, currentClass.getName() + ".hbm.xml");
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
			
			// this is optional. if second level cache is disabled this will cause an error
//			thatClass.setCache(new HibernateClassCache());
			
			// thatClass.setDiscriminatorValue(cd.getDescribedClassName());
			// thatClass.setDiscriminator(new HibernateClassDiscriminator(DISCRIMINATOR_COLUMN_NAME));
		}
		else
		{
			String idColName = findIdColName(cd);
			thatClass = new HibernateJoinedSubclass(superCd.getDescribedClassName(), new HibernateKey(idColName));
		}
		thatClass.setName(cd.getDescribedClassName());
		thatClass.setTable(dbNameGenerator.getTableName(cd));

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
		return dbNameGenerator.getColumnName(idFieldName);
	}

	protected String findIdFieldName(ClassDescriptor cd)
	{
		if (cd == null)
			return null;
		else if (this.idFieldNameByClass == null)
			return DbNameGenerator.DEFAULT_ORM_ID_FIELD_NAME;
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
		prop.setColumn(dbNameGenerator.getColumnName(fd));
		prop.setType(fd.getScalarType().getJavaTypeName());
		
		// TODO index?
		
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
		comp.setColumn(dbNameGenerator.getColumnName(fd));
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
		coll.setTable(dbNameGenerator.getAssociationTableName(cd, fd));
		coll.setKey(new HibernateKey(findIdColName(cd)));
		coll.setManyToMany(new HibernateManyToMany(dbNameGenerator.getAssociationTableColumnName(elementCd), elementCd.getDescribedClassName()));

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
		coll.setTable(dbNameGenerator.getAssociationTableName(cd, fd));
		coll.setKey(new HibernateKey(dbNameGenerator.getAssociationTableColumnName(cd)));
		coll.setElement(new HibernateCollectionScalar(DbNameGenerator.SCALAR_COLLECTION_VALUE_COLUMN_NAME, fd.getScalarType().getJavaTypeName()));
		return coll;
	}

}
