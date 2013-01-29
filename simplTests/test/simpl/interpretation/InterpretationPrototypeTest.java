package simpl.interpretation;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import simpl.descriptions.ClassDescriptor;
import simpl.exceptions.SIMPLTranslationException;
import simpl.interpretation.serializerProtos.JsonSerializer;
import simpl.interpretation.serializerProtos.XmlSerializer;

public class InterpretationPrototypeTest {

	
	@Test
	public void testCoreScenario() throws SIMPLTranslationException
	{
		SimplInterpreter si = new SimplInterpreter();
		
		myScalars ms = new myScalars();
		ms.aField = "thisField string";
		ms.aInteger = 2013;
		ms.aDouble = 1.337;
		
		List<ScalarInterpretation> interps = si.interpretInstance(ms);
		assertEquals(3, interps.size());
		
		for(ScalarInterpretation s : interps)
		{
			System.out.println(s.toString());
		}
		
		JsonSerializer sut = new JsonSerializer();
		System.out.println(sut.testInterps(ms));
		
		XmlSerializer xml = new XmlSerializer();
		System.out.println(xml.testInterps(ms));
		
		// some magic happens that converts XML/JSON/ETC into interpretations... 
		
		SimplUnderstander su = new SimplUnderstander();
		myScalars result = (myScalars)su.understandInterpretation(interps, ClassDescriptor.getClassDescriptor(ms.getClass()));
		assertEquals(result.aField, ms.aField);
	}

}
