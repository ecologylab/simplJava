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

	private void translateToJavascript(Class<? extends ElementState> inputClass, Appendable appendable)
			throws IOException, DotNetTranslationException
	{
		ClassDescriptor<?, ?> classDescriptor = ClassDescriptor.getClassDescriptor(inputClass);

		HashMapArrayList<String, ? extends FieldDescriptor> fieldDescriptors = classDescriptor
				.getFieldDescriptorsByFieldName();

		appendable.append("function "+XMLTools.getXmlTagName(inputClass.getSimpleName()+"(",""));
		//itterate through member's names... and add to parameters
		appendable.append(")\n{\n");
		
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
		appendable.append("\n}");
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
			translateToJavascript(inputClass, directoryLocation);
		}

		// create a folder to put the translation scope getter class
		// File tScopeDirectory =
		// createGetTranslationScopeFolder(directoryLocation);
		// generate translation scope getter class
		// generateTranslationScopeGetterClass(tScopeDirectory, tScope);

		System.out.println("DONE !");
	}
}
