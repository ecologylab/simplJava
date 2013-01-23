package ecologylab.translators.hibernate;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Collections;
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
import ecologylab.serialization.SimplTypesScope;
import ecologylab.serialization.formatenums.StringFormat;
import ecologylab.translators.hibernate.hbmxml.HibernateClass;
import ecologylab.translators.hibernate.hbmxml.HibernateClassId;
import ecologylab.translators.hibernate.hbmxml.HibernateClassIdGenerator;
import ecologylab.translators.hibernate.hbmxml.HibernateComposite;
import ecologylab.translators.hibernate.hbmxml.HibernateElement;
import ecologylab.translators.hibernate.hbmxml.HibernateFieldBase;
import ecologylab.translators.hibernate.hbmxml.HibernateIndex;
import ecologylab.translators.hibernate.hbmxml.HibernateJoinedSubclass;
import ecologylab.translators.hibernate.hbmxml.HibernateKey;
import ecologylab.translators.hibernate.hbmxml.HibernateList;
import ecologylab.translators.hibernate.hbmxml.HibernateManyToMany;
import ecologylab.translators.hibernate.hbmxml.HibernateMap;
import ecologylab.translators.hibernate.hbmxml.HibernateMapKey;
import ecologylab.translators.hibernate.hbmxml.HibernateMapping;
import ecologylab.translators.hibernate.hbmxml.HibernateProperty;

/**
 * 
 * @author quyin
 * 
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class HibernateXmlMappingGenerator extends Debug
{
	
	public static final String											XML_HEAD								= "<?xml version=\"1.0\"?>\n<!DOCTYPE hibernate-mapping PUBLIC \"-//Hibernate/Hibernate Mapping DTD 3.0//EN\" \"http://www.hibernate.org/dtd/hibernate-mapping-3.0.dtd\">\n";

	private DbNameGenerator													dbNameGenerator;

	private SimplTypesScope													translationScope;

	/**
	 * this is the map from (full) class names to ORM ID field names. each root class should be in
	 * this map as a key, and the value is the name of the field that will be used as the primary key.
	 * if a root class isn't in this map, we will try to find the ID field using a default name
	 * specified in DbNameGenerator. typically, surrogate keys are recommended, so this could just be
	 * a long integer field.
	 */
	private Map<String, String>											idFieldNameByClass;

	private Map<String, HibernateClass>							allMappings							= new HashMap<String, HibernateClass>();

	public HibernateXmlMappingGenerator()
	{
		this(new DefaultCachedDbNameGenerator());
	}
	
	protected HibernateXmlMappingGenerator(DbNameGenerator dbNameGenerator)
	{
		this.dbNameGenerator = dbNameGenerator;
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
	public List<String> generateMappings(File hbmDir, SimplTypesScope translationScope, Map<String, String> idFieldNameByClass)
			throws FileNotFoundException, SIMPLTranslationException
	{
		PropertiesAndDirectories.createDirsAsNeeded(hbmDir);

		this.translationScope = translationScope;
		this.idFieldNameByClass = idFieldNameByClass;

		List<String> mappingImports = new ArrayList<String>();

		for (ClassDescriptor cd : translationScope.entriesByClassName().values())
		{
			generateClassMapping(cd);
			this.dbNameGenerator.clearCache();
		}
		
		generateMappingsDoneHook();

		for (HibernateClass classMapping : allMappings.values())
		{
			HibernateMapping mappingUnit = new HibernateMapping();
			String className = classMapping.getName();
			if (className == null)
				continue;
			mappingUnit.setMappingPackageName(className.substring(0, className.lastIndexOf('.')));
			mappingUnit.getMappedClasses().put(classMapping.getName(), classMapping);
			
			String entityClassName = className.replace('$', '_');
			File newHbmFile = new File(hbmDir, entityClassName + ".hbm.xml");
			mappingImports.add(String.format("<mapping file=\"%s\" />", newHbmFile.getPath()));
			
			PrintWriter writer = new PrintWriter(newHbmFile);
			writer.println(XML_HEAD);
			SimplTypesScope.serialize(mappingUnit, writer, StringFormat.XML);			
			writer.println();
			writer.close();
		}

		Collections.sort(mappingImports);
		return mappingImports;
	}

	protected void generateMappingsDoneHook()
	{
		// TODO Auto-generated method stub
		
	}

	protected HibernateClass generateClassMapping(ClassDescriptor cd)
	{
		if (cd == null)
			return null;
		
		if (allMappings.containsKey(cd.getDescribedClassName()))
			return null; // this class has been mapped before or being mapped. prevent infinite loops.
		
		HibernateClass thatClass = null;

		ClassDescriptor superCd = cd.getSuperClass();
		if (superCd == null || this.translationScope.getClassDescriptorByClassName(superCd.getDescribedClassName()) == null)
		{
			thatClass = new HibernateClass();

			String idFieldName = findIdFieldName(cd);
			String idColName = findIdColName(cd);
			thatClass.setId(new HibernateClassId(idFieldName, idColName, HibernateClassIdGenerator.identityGenerator));
			
			// this is optional. if second level cache is disabled this will cause an error
//			thatClass.setCache(new HibernateClassCache());
			
			// discriminator isn't working in hibernate in my experiments :(
//			 thatClass.setDiscriminatorValue(cd.getDescribedClassName());
//			 thatClass.setDiscriminator(new HibernateClassDiscriminator(DISCRIMINATOR_COLUMN_NAME));
		}
		else
		{
			String idColName = findIdColName(cd);
			thatClass = new HibernateJoinedSubclass(superCd.getDescribedClassName(), new HibernateKey(idColName));
		}
		if (thatClass != null)
		{
			// put it in the bookkeeping map early to prevent infinite loop
			allMappings.put(cd.getDescribedClassName(), thatClass);
			
			thatClass.setMappedClassDescriptor(cd);
			String className = getClassNameForHbm(cd);
			if (className == null)
				return null;
			thatClass.setName(className);
			thatClass.setTable(dbNameGenerator.getTableName(cd));
			HashMapArrayList<String, HibernateFieldBase> propertyMappings = generatePropertyMappingsForClass(cd);
			thatClass.setProperties(propertyMappings);
		}

		return thatClass;
	}

	protected HashMapArrayList<String, HibernateFieldBase> generatePropertyMappingsForClass(
			ClassDescriptor cd)
	{
		HashMapArrayList<String, HibernateFieldBase> propertyMappings = new HashMapArrayList<String, HibernateFieldBase>();
		cd.resolvePolymorphicAnnotations();
		for (Object fdObj : cd.getDeclaredFieldDescriptorsByFieldName().values())
		{
			FieldDescriptor fd = (FieldDescriptor) fdObj;
			if (fd.getName().equals(findIdFieldName(cd)))
				continue;
			if (fd.getDeclaringClassDescriptor().equals(cd))
			{
				HibernateFieldBase currentField = generateFieldMapping(cd, fd);
				if (currentField != null)
					propertyMappings.put(currentField.getName(), currentField);
			}
		}
		return propertyMappings;
	}
	
	protected String getClassNameForHbm(ClassDescriptor cd)
	{
		return cd.getDescribedClassName();
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
			if (result == null)
				result = DbNameGenerator.DEFAULT_ORM_ID_FIELD_NAME;
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
			thatField = generateListOfElementMapping(cd, fd);
			break;
		case FieldTypes.COLLECTION_SCALAR:
			thatField = generateListOfScalarMapping(cd, fd);
			break;
		case FieldTypes.MAP_ELEMENT:
			thatField = generateMapOfElementMapping(cd, fd);
			break;
		case FieldTypes.MAP_SCALAR:
			thatField = generateMapOfScalarMapping(cd, fd);
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
		prop.setType(translateType(fd.getScalarType().getJavaTypeName()));
		
		// TODO index?
		
		return prop;
	}

	protected HibernateComposite generateCompositeMapping(ClassDescriptor mmd, FieldDescriptor fd)
	{
		ClassDescriptor elementCd = null;
		
		if (fd.isPolymorphic())
		{
			elementCd = ClassDescriptor.getClassDescriptor(fd.getField().getType());
		}
		else
		{
			elementCd = fd.getElementClassDescriptor();
		}

		if (elementCd == null)
		{
			warning("Cannot find or determine element ClassDescriptor: " + fd);
			return null;
		}
		else
		{
			generateClassMapping(elementCd);
		}
		
		HibernateComposite comp = new HibernateComposite();
		comp.setName(fd.getName());
		comp.setColumn(dbNameGenerator.getColumnName(fd));
		comp.setCompositeClassName(elementCd.getDescribedClassName());
		return comp;
	}

	protected HibernateList generateListOfElementMapping(ClassDescriptor cd, FieldDescriptor fd)
	{
		ClassDescriptor elementCd = null;
		
		if (fd.isPolymorphic())
		{
			Type genericType = fd.getField().getGenericType(); // the List<T> type
			if (genericType instanceof ParameterizedType)
			{
				Type elementType = ((ParameterizedType) genericType).getActualTypeArguments()[0];
				if (elementType instanceof ParameterizedType)
					elementType = ((ParameterizedType) elementType).getRawType();
				if (elementType instanceof TypeVariable)
					elementType = ((TypeVariable) elementType).getBounds()[0];
				elementCd = ClassDescriptor.getClassDescriptor((Class) elementType);
			}
		}
		else
		{
			elementCd = fd.getElementClassDescriptor();
		}

		if (elementCd == null)
		{
			warning("Cannot find or determine element ClassDescriptor: " + fd);
			return null;
		}
		else
		{
			generateClassMapping(elementCd);
		}
		
		HibernateList coll = new HibernateList();
		coll.setName(fd.getName());
		coll.setTable(dbNameGenerator.getAssociationTableName(cd, fd));
		coll.setKey(new HibernateKey(findIdColName(cd)));
		coll.setIndex(new HibernateIndex(dbNameGenerator.getAssociationTableIndexName(cd, fd)));
		coll.setAssociation(new HibernateManyToMany(dbNameGenerator.getAssociationTableColumnName(elementCd), elementCd.getDescribedClassName()));

		// TODO reverse mapping

		return coll;
	}

	protected HibernateList generateListOfScalarMapping(ClassDescriptor cd, FieldDescriptor fd)
	{
		HibernateList coll = new HibernateList();
		coll.setName(fd.getName());
		coll.setTable(dbNameGenerator.getAssociationTableName(cd, fd));
		coll.setKey(new HibernateKey(dbNameGenerator.getAssociationTableColumnName(cd)));
		coll.setIndex(new HibernateIndex(dbNameGenerator.getAssociationTableIndexName(cd, fd)));
		coll.setAssociation(new HibernateElement(
				DbNameGenerator.SCALAR_COLLECTION_VALUE_COLUMN_NAME,
				translateType(fd.getScalarType().getJavaTypeName()))
		);
		
		// TODO reverse mapping
		
		return coll;
	}

	protected HibernateFieldBase generateMapOfElementMapping(ClassDescriptor cd, FieldDescriptor fd)
	{
		HibernateMap map = new HibernateMap();
		map.setName(fd.getName());
		map.setTable(dbNameGenerator.getAssociationTableName(cd, fd));
		map.setKey(new HibernateKey(dbNameGenerator.getAssociationTableColumnName(cd)));
		
		Type genericType = fd.getField().getGenericType();
		if (genericType instanceof ParameterizedType)
		{
			ParameterizedType mapParam = (ParameterizedType) genericType;
			Type[] typeArgs = mapParam.getActualTypeArguments();
			if (typeArgs.length == 2)
			{
				// TODO if these args are not Class (only Type), we should be able to ignore type parameters
				Class keyType = (Class) typeArgs[0];
				Class valueType = (Class) typeArgs[1];
				map.setMapKey(new HibernateMapKey(translateType(keyType.getName())));
				map.setAssociation(new HibernateManyToMany(
						dbNameGenerator.getAssociationTableColumnName(ClassDescriptor.getClassDescriptor(valueType)),
						valueType.getName()) // this is not a scalar, no need to translate type
				);
				return map;
			}
			else
			{
				error("expected 2 type argments: " + fd.getField());
			}
		}
		else
		{
			error("expected parameterized type: " + fd.getField());
		}
		return null;
	}
	
	protected HibernateFieldBase generateMapOfScalarMapping(ClassDescriptor cd, FieldDescriptor fd)
	{
		HibernateMap map = new HibernateMap();
		map.setName(fd.getName());
		map.setTable(dbNameGenerator.getAssociationTableName(cd, fd));
		map.setKey(new HibernateKey(dbNameGenerator.getAssociationTableColumnName(cd)));
		
		Type genericType = fd.getField().getGenericType();
		if (genericType instanceof ParameterizedType)
		{
			ParameterizedType mapParam = (ParameterizedType) genericType;
			Type[] typeArgs = mapParam.getActualTypeArguments();
			if (typeArgs.length == 2)
			{
				// TODO if these args are not Class (only Type), we should be able to ignore type parameters
				Class keyType = (Class) typeArgs[0];
				Class valueType = (Class) typeArgs[1];
				map.setMapKey(new HibernateMapKey(translateType(keyType.getName())));
				map.setAssociation(new HibernateElement(
						DbNameGenerator.SCALAR_COLLECTION_VALUE_COLUMN_NAME,
						translateType(valueType.getName())) // this is a scalar, must translate type
				);
				return map;
			}
			else
			{
				error("expected 2 type argments: " + fd.getField());
			}
		}
		else
		{
			error("expected parameterized type: " + fd.getField());
		}
		return null;
	}

	protected String translateType(String typeName)
	{
		return typeName;
	}

	protected Map<String, HibernateClass> getAllMappings()
	{
		return allMappings;
	}
	
}
