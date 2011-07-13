package ecologylab.translators.javascript;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import ecologylab.generic.HashMapArrayList;
import ecologylab.serialization.ClassDescriptor;
import ecologylab.serialization.ElementState;
import ecologylab.serialization.FieldDescriptor;
import ecologylab.serialization.TranslationScope;
import ecologylab.serialization.XMLTools;
import ecologylab.translators.net.DotNetTranslationException;
import ecologylab.translators.parser.JavaDocParser;

public class JavascriptTranslator
{

	public JavascriptTranslator()
	{

	}

	/*
	 function player(json,name,strength,speed,skin)
{
   this._simpl_object_name = "player";
   this._simpl_collection_types = {};
   this._simpl_map_types = {};
   if(json)
   {
      jsonConstruct(json,this);
      return;
    }
    else
    {
       if(name) this.name = name;
       if(strength) this.strength = strength;
       if(speed) this.speed = speed;
       if(skin) this.skin = skin;
    }
}

	 */
	private void translateToJavascript(Class<? extends ElementState> inputClass, Appendable appendable)
			throws IOException, DotNetTranslationException
	{
		ClassDescriptor<?, ?> classDescriptor = ClassDescriptor.getClassDescriptor(inputClass);

		HashMapArrayList<String, ? extends FieldDescriptor> fieldDescriptors = classDescriptor
				.getFieldDescriptorsByFieldName();
    String functionName = XMLTools.getXmlTagName(inputClass.getSimpleName(),"");
		appendable.append("\nfunction "+functionName+"(");
		//itterate through member's names... and add to parameters
		String parameters = "json";
		String constructFields = "";
		for (FieldDescriptor fieldDescriptor : fieldDescriptors)
		{
			String fieldName = XMLTools.getXmlTagName(fieldDescriptor.getFieldName(),"");
			parameters += ","+fieldName;
			constructFields += "\n        if("+fieldName+") this."+fieldName+" = "+fieldName+";";
		}
		
		appendable.append(parameters+")\n{");
		
		String collectionTypes = "";
		String mapTypes = "";
		appendable.append("\n    this._simpl_object_name = \""+functionName+"\";");
		appendable.append("\n    this._simpl_collection_types = {};");
		appendable.append("\n    this._simpl_map_types = {};");
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
				System.out.println(fieldDescriptor.getFieldName());
				//appendFieldAsCSharpAttribute(fieldDescriptor, classFile);
		}
		
		
		//+"{}\n");
		
		
   /*
		appendHeaderComments(inputClass, header);

		openNameSpace(inputClass, classFile);
		openClassFile(inputClass, classFile);

		if (fieldDescriptors.size() > 0)
		{
			classDescriptor.resolveUnresolvedScopeAnnotationFDs();

			for (FieldDescriptor fieldDescriptor : fieldDescriptors)
			{
				if (fieldDescriptor.belongsTo(classDescriptor))
					appendFieldAsCSharpAttribute(fieldDescriptor, classFile);
			}

			appendDefaultConstructor(inputClass, classFile);

			for (FieldDescriptor fieldDescriptor : fieldDescriptors)
			{
				if (fieldDescriptor.belongsTo(classDescriptor))
					appendGettersAndSetters(fieldDescriptor, classFile);
			}

			if (implementMappableInterface)
			{
				implementMappableMethods(classFile);
				implementMappableInterface = false;
			}
		}

		closeClassFile(classFile);
		closeNameSpace(classFile);

		importNameSpaces(header);

		libraryNamespaces.clear();
		setAdditionalImportNamespaces(additionalImportLines);
*/
		appendable.append("\n}\n\n");
	}

	private File createCSharpFileWithDirectoryStructure(Class<?> inputClass, File directoryLocation)
			throws IOException
	{
		String packageName = XMLTools.getPackageName(inputClass);
		String className = XMLTools.getClassName(inputClass);
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

	public void translateToJavascript(File directoryLocation, TranslationScope tScope)
			throws IOException, DotNetTranslationException
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
		
		BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File("C:/testjs/js/gamething.js")));

		
		

		System.out.println("generating classes...");

		// Generate header and implementation files
		ArrayList<Class<? extends ElementState>> classes = anotherScope.getAllClasses();
		int length = classes.size();
		for (int i = 0; i < length; i++)
		{
			Class<? extends ElementState> inputClass = classes.get(i);
			// if(excludeClassesFromTranslation.contains(inputClass))
			// {
			// System.out.println("Excluding " + inputClass +
			// " from translation as requested");
			// continue;
			// }
			System.out.println("Translating " + inputClass + ".....  but not really :)");
			//translateToJavascript(inputClass, directoryLocation);
			translateToJavascript(inputClass, bufferedWriter);
		}

		// create a folder to put the translation scope getter class
		// File tScopeDirectory =
		// createGetTranslationScopeFolder(directoryLocation);
		// generate translation scope getter class
		// generateTranslationScopeGetterClass(tScopeDirectory, tScope);
		bufferedWriter.close();
		System.out.println("DONE !");
	}
}
