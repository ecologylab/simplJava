package ecologylab.translators.javascript;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import ecologylab.generic.HashMapArrayList;
import ecologylab.serialization.ClassDescriptor;
import ecologylab.serialization.ElementState;
import ecologylab.serialization.FieldDescriptor;
import ecologylab.serialization.FieldTypes;
import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.StringFormat;
import ecologylab.serialization.TranslationScope;
import ecologylab.serialization.XMLTools;
import ecologylab.translators.net.DotNetTranslationException;

public class JavascriptTranslator
{
	public static String ElementStateToJSON(ElementState elementState)
	{
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		String returnString = null;
		try
		{
			ecologylab.serialization.TranslationScope.enableGraphSerialization();
			returnString = ClassDescriptor.serialize(elementState, StringFormat.JSON).toString();
		}
		catch (SIMPLTranslationException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return returnString;
	}

	public JavascriptTranslator()
	{

	}

	/*
	 * function player(json,name,strength,speed,skin) { this._simpl_object_name = "player";
	 * this._simpl_collection_types = {}; this._simpl_map_types = {}; if(json) {
	 * jsonConstruct(json,this); return; } else { if(name) this.name = name; if(strength)
	 * this.strength = strength; if(speed) this.speed = speed; if(skin) this.skin = skin; } }
	 */
	private void translateToJavascript(Class<?> inputClass, Appendable appendable)
			throws IOException
	{
		ClassDescriptor<? extends FieldDescriptor> classDescriptor = ClassDescriptor
				.getClassDescriptor(inputClass);

		String classDescriptorJSON = "";
		try
		{
			ecologylab.serialization.TranslationScope.enableGraphSerialization();
			classDescriptorJSON = ClassDescriptor.serialize(classDescriptor, StringFormat.JSON)
					.toString();
			System.out.println();
		}
		catch (SIMPLTranslationException e)
		{
			e.printStackTrace();
		}

		HashMapArrayList<String, ? extends FieldDescriptor> fieldDescriptors = classDescriptor
				.getFieldDescriptorsByFieldName();
		String functionName = XMLTools.getXmlTagName(inputClass.getSimpleName(), "");
		appendable.append("\nfunction " + functionName + "(");
		// itterate through member's names... and add to parameters
		String parameters = "json";
		String constructFields = "";
		String collectionTypes = "";
		String compositeTypes = "";
		String mapTypes = "";
		String mapTypesKeys = "";

		boolean hasCollectionBefore = false;
		String leadingCommaCollection = "";
		boolean hasMapBefore = false;
		String leadingCommaMap = "";
		boolean hasCompositeBefore = false;
		String leadingCommaComposite = "";
		for (FieldDescriptor fieldDescriptor : fieldDescriptors)
		{
			String fieldName = "";

			fieldName = XMLTools.getXmlTagName(fieldDescriptor.getName(), "");

			if (fieldDescriptor.getType() == FieldTypes.MAP_ELEMENT
					|| fieldDescriptor.getType() == FieldTypes.MAP_SCALAR
					|| fieldDescriptor.getType() == FieldTypes.COLLECTION_ELEMENT)
				if (!fieldDescriptor.isWrapped())
					fieldName = XMLTools.getXmlTagName(fieldDescriptor.getCollectionOrMapTagName(), "");
			parameters += "," + fieldName;
			constructFields += "\n        if(" + fieldName + ") this." + fieldName + " = " + fieldName
					+ ";";
			if (fieldName.equals("moves"))
				System.out.println("Found moves");

			if (fieldDescriptor.getType() == FieldTypes.COLLECTION_ELEMENT)
			{
				if (hasCollectionBefore)
					leadingCommaCollection = ",";
				String elementType = XMLTools.getXmlTagName(fieldDescriptor.getElementClassDescriptor()
						.getDescribedClassSimpleName(), "");
				collectionTypes += leadingCommaCollection + "\"" + fieldName + "\":\"" + elementType + "\"";
				hasCollectionBefore = true;
			}
			else if (fieldDescriptor.getType() == FieldTypes.COMPOSITE_ELEMENT)
			{
				if (hasCompositeBefore)
					leadingCommaComposite = ",";
				String elementType = XMLTools.getXmlTagName(fieldDescriptor.getElementClassDescriptor()
						.getDescribedClassSimpleName(), "");
				hasCompositeBefore = true;
				compositeTypes += leadingCommaComposite + "\"" + fieldName + "\":\"" + elementType + "\"";
			}

			if (fieldDescriptor.getType() == FieldTypes.MAP_ELEMENT
					|| fieldDescriptor.getType() == FieldTypes.MAP_SCALAR)
			{
				if (hasMapBefore)
					leadingCommaMap = ",";
				String elementType = "object";// This means that the object has a string value rather than
																			// complex type values.
				if (fieldDescriptor.getElementClassDescriptor() != null)
				{
					elementType = XMLTools.getXmlTagName(fieldDescriptor.getElementClassDescriptor()
							.getDescribedClassSimpleName(), "");
				}
				mapTypes += leadingCommaMap + "\"" + fieldName + "\":\"" + elementType + "\"";
				// /fieldDescriptor.getMapKeyFieldName();
				String mapKeyName = "object";// this means string string hash
				if (fieldDescriptor.getMapKeyFieldName() != null)
					mapKeyName = XMLTools.getXmlTagName(fieldDescriptor.getMapKeyFieldName(), "");
				mapTypesKeys += leadingCommaMap + "\"" + fieldName + "\":\"" + mapKeyName + "\"";
				hasMapBefore = true;
			}

		}
		// fieldDescriptor.
		appendable.append(parameters + ")\n{");
		appendable.append("\n    this._simpl_object_name = \"" + functionName + "\";");
		appendable.append("\n    this._simpl_class_descriptor='" + classDescriptorJSON + "';");

		appendable.append("\n    this._simpl_collection_types = {" + collectionTypes + "};");
		appendable.append("\n    this._simpl_map_types = {" + mapTypes + "};");
		appendable.append("\n    this._simpl_map_types_keys = {" + mapTypesKeys + "};");
		appendable.append("\n    this._simpl_composite_types = {" + compositeTypes + "};");
		appendable.append("\n    if(json)");
		appendable.append("\n    {");
		appendable.append("\n        jsonConstruct(json,this);");
		appendable.append("\n        return;");
		appendable.append("\n    }");
		appendable.append("\n    else");
		appendable.append("\n    {");
		appendable.append(constructFields);
		appendable.append("\n    }");

		for (FieldDescriptor fieldDescriptor : fieldDescriptors)
		{
			if (fieldDescriptor.belongsTo(classDescriptor))
				System.out.println(fieldDescriptor.getName());
			// appendFieldAsCSharpAttribute(fieldDescriptor, classFile);
		}

		// +"{}\n");

		/*
		 * appendHeaderComments(inputClass, header);
		 * 
		 * openNameSpace(inputClass, classFile); openClassFile(inputClass, classFile);
		 * 
		 * if (fieldDescriptors.size() > 0) { classDescriptor.resolveUnresolvedScopeAnnotationFDs();
		 * 
		 * for (FieldDescriptor fieldDescriptor : fieldDescriptors) { if
		 * (fieldDescriptor.belongsTo(classDescriptor)) appendFieldAsCSharpAttribute(fieldDescriptor,
		 * classFile); }
		 * 
		 * appendDefaultConstructor(inputClass, classFile);
		 * 
		 * for (FieldDescriptor fieldDescriptor : fieldDescriptors) { if
		 * (fieldDescriptor.belongsTo(classDescriptor)) appendGettersAndSetters(fieldDescriptor,
		 * classFile); }
		 * 
		 * if (implementMappableInterface) { implementMappableMethods(classFile);
		 * implementMappableInterface = false; } }
		 * 
		 * closeClassFile(classFile); closeNameSpace(classFile);
		 * 
		 * importNameSpaces(header);
		 * 
		 * libraryNamespaces.clear(); setAdditionalImportNamespaces(additionalImportLines);
		 */
		appendable.append("\n}\n\n");
	}

	private File createCSharpFileWithDirectoryStructure(Class<?> inputClass, File directoryLocation)
			throws IOException
	{
		String packageName = XMLTools.getPackageName(inputClass);
		String className = XMLTools.getClassSimpleName(inputClass);
		String currentDirectory = directoryLocation.toString() + "/";

		// String[] arrayPackageNames = packageName.split(PACKAGE_NAME_SEPARATOR);

		// for (String directoryName : arrayPackageNames)
		// {
		// currentDirectory += directoryName + FILE_PATH_SEPARATOR;
		// }

		File directory = new File(currentDirectory);
		directory.mkdirs();

		File currentFile = new File(currentDirectory + className + ".js");

		if (currentFile.exists())
		{
			currentFile.delete();
		}

		currentFile.createNewFile();
		return currentFile;
	}

	private void translateToJavascript(Class<? extends ElementState> inputClass,
			File directoryLocation) throws IOException, DotNetTranslationException
	{

		File outputFile = createCSharpFileWithDirectoryStructure(inputClass, directoryLocation);

		BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outputFile));

		translateToJavascript(inputClass, bufferedWriter);

		bufferedWriter.close();

	}

	/***
	 * Takes in
	 * 
	 * @param fileLocation
	 *          an instantiated file
	 * @param tScope
	 *          the classes to translated into javascript
	 * @throws IOException
	 * @throws DotNetTranslationException
	 */
	public void translateToJavascript(File fileLocation, TranslationScope tScope) throws IOException
	{
		System.out.println("Translating...");
		System.out.println("Parsing source files to extract comments... not yet really");
		TranslationScope anotherScope = TranslationScope.augmentTranslationScope(tScope);// I'm not sure
		// what this
		// does...
		// Parse source files for javadocs
		// if(workSpaceLocation != null)
		// JavaDocParser.parseSourceFileIfExists(anotherScope,
		// workSpaceLocation);

		BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileLocation));

		System.out.println("generating classes...");
		// First we generate class descriptor and field descriptor...
		translateToJavascript(FieldDescriptor.class, bufferedWriter);
		translateToJavascript(ClassDescriptor.class, bufferedWriter);

		// Generate header and implementation files
		ArrayList<Class<?>> classes = anotherScope.getAllClasses();
		int length = classes.size();
		for (int i = 0; i < length; i++)
		{
			Class<?> inputClass = classes.get(i);
			System.out.println("Translating " + inputClass);
			translateToJavascript(inputClass, bufferedWriter);
		}
		bufferedWriter.close();
		System.out.println("DONE !");
	}
}
