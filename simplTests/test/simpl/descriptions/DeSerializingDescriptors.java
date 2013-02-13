package simpl.descriptions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import simpl.core.SimplTypesScope;
import simpl.core.SimplTypesScope.GRAPH_SWITCH;
import simpl.core.SimplTypesScopeFactory;
import simpl.descriptions.ClassDescriptor;
import simpl.descriptions.FieldDescriptor;
import simpl.exceptions.SIMPLTranslationException;
import simpl.formats.enums.Format;
import simpl.formats.enums.StringFormat;
import simpl.tools.JSONTools;


public class DeSerializingDescriptors extends Assert
{

  @Before
  public void init()
  {
    SimplTypesScope.graphSwitch = GRAPH_SWITCH.ON;
  }

  public void deSerializeDescriptorsJSON() throws SIMPLTranslationException, IOException
  {
    SimplTypesScope tscope =(SimplTypesScope) SimplTypesScopeFactory.name("test-de/serialize descriptors in json").translations(
                                                 FieldDescriptor.class,
                                                 ClassDescriptor.class,
                                                 SimplTypesScope.class).create();
    String json = SimplTypesScope.serialize(tscope, StringFormat.JSON).toString();
    System.out.println("serialized json string:\n" + json);
    assertTrue(JSONTools.validate(json));

//    File testScopeFile = new File("data/test-deserialize-descriptors-scope.json");
//    assertTrue(testScopeFile.exists());
//    json = loadJSONStringFromFile(testScopeFile);

     SimplTypesScope newTscope = (SimplTypesScope) tscope.deserialize(json, StringFormat.JSON);
//    SimplTypesScope newTscope = (SimplTypesScope) tscope.deserialize(testScopeFile, Format.JSON);
    assertNotNull(newTscope);
    assertNotNull(newTscope.classDescriptors.by.SimplName.get("FieldDescriptor"));
    assertNotNull(newTscope.classDescriptors.by.SimplName.get("ClassDescriptor"));
    assertNotNull(newTscope.classDescriptors.by.SimplName.get("SimplTypesScope"));
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
