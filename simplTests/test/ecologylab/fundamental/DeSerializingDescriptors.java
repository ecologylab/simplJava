package ecologylab.fundamental;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import simpl.core.JSONTools;
import simpl.core.SimplTypesScope;
import simpl.core.SimplTypesScope.GRAPH_SWITCH;
import simpl.descriptions.ClassDescriptor;
import simpl.descriptions.FieldDescriptor;
import simpl.exceptions.SIMPLTranslationException;
import simpl.formats.enums.Format;
import simpl.formats.enums.StringFormat;


public class DeSerializingDescriptors extends Assert
{

  @Before
  public void init()
  {
    SimplTypesScope.graphSwitch = GRAPH_SWITCH.ON;
  }

  public void deSerializeDescriptorsJSON() throws SIMPLTranslationException, IOException
  {
    SimplTypesScope tscope = SimplTypesScope.get("test-de/serialize descriptors in json",
                                                 FieldDescriptor.class,
                                                 ClassDescriptor.class,
                                                 SimplTypesScope.class);
    String json = SimplTypesScope.serialize(tscope, StringFormat.JSON).toString();
    System.out.println("serialized json string:\n" + json);
    assertTrue(JSONTools.validate(json));

//    File testScopeFile = new File("data/test-deserialize-descriptors-scope.json");
//    assertTrue(testScopeFile.exists());
//    json = loadJSONStringFromFile(testScopeFile);

     SimplTypesScope newTscope = (SimplTypesScope) tscope.deserialize(json, StringFormat.JSON);
//    SimplTypesScope newTscope = (SimplTypesScope) tscope.deserialize(testScopeFile, Format.JSON);
    assertNotNull(newTscope);
    assertNotNull(newTscope.getClassDescriptorBySimpleName("FieldDescriptor"));
    assertNotNull(newTscope.getClassDescriptorBySimpleName("ClassDescriptor"));
    assertNotNull(newTscope.getClassDescriptorBySimpleName("SimplTypesScope"));
  }


  static String loadJSONStringFromFile(File file) throws IOException
  {
    StringBuilder sb = new StringBuilder();
    BufferedReader br = new BufferedReader(new FileReader(file));
    while (true)
    {
      String line = br.readLine();
      if (line == null)
        break;
      sb.append(line.trim());
    }
    return sb.toString();
  }

}
