package ecologylab.translators.javascript.test;

import java.io.File;
import java.io.IOException;
import ecologylab.serialization.ElementState.FORMAT;

import ecologylab.serialization.ClassDescriptor;
import ecologylab.serialization.ElementState;
import ecologylab.serialization.FieldDescriptor;
import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.TranslationContext;
import ecologylab.serialization.TranslationScope;
import ecologylab.serialization.XMLTools;
import ecologylab.translators.cocoa.CocoaTranslationException;
import ecologylab.translators.cocoa.CocoaTranslator;
import ecologylab.translators.java.JavaTranslator;
import ecologylab.translators.javascript.JavascriptTranslator;
import ecologylab.translators.net.DotNetTranslationException;
import ecologylab.translators.net.DotNetTranslator;

public class JavascriptTranslatorTest {
//"{"refer_to_self":{"some_data":"here is some stringness","refer_to_self":{"refer_to_self":{"simpl.ref":"65584869"}},"simpl.id":"65584869"}}"
//"{"refer_to_self":{"some_data":"here is some stringness","refer_to_self":{"simpl.ref":"98475151"},"simpl.id":"98475151"}}"
	public static void main(String args[]) throws IOException, SIMPLTranslationException, DotNetTranslationException, CocoaTranslationException
	{
		System.out.println("Javascript Translator");
		
		//c.tra
		//xmltools.getc get xmltag name
		
		TranslationScope ts = new TranslationScope();
		ts.addTranslation(Player.class);
		
		ts = TranslationScope.get("somegame",Bank.class,Computer.class,Human.class,Item.class,Move.class, Movements.class,Player.class, ReferToSelf.class);
		
		//System.out.println(ts.getClassByName("player"));
		//DotNetTranslator c = new DotNetTranslator();
		//c.translateToCSharp(new File("C:/testjs/cs"),ts);
		//CocoaTranslator coco = new CocoaTranslator();
		
		JavascriptTranslator jst = new JavascriptTranslator();
		jst.translateToJavascript(new File("jscode/gamething.js"), ts);
		
		ReferToSelf self1 = new ReferToSelf();
		ReferToSelf self2 = new ReferToSelf(1);
		
		System.out.println("var self1 = '"+self1.serialize(FORMAT.JSON).toString()+"';");
		System.out.println("var self1 = '"+self2.serialize(FORMAT.JSON).toString()+"';");
		
		//String fromJs =   "{\"refer_to_self\":{\"some_data\":\"here is some stringness\",\"simpl.id\":\"77909673\",\"reference_to_self\":{\"simpl.ref\":\"77909673\"}}}";
		//String fromSelf = "{\"refer_to_self\":{\"some_data\":\"I haver referenceToSelf\", \"simpl.id\":\"270071071\", \"reference_to_self\":{\"simpl.ref\":\"270071071\"}}}";
		//String fffuu =    "{\"refer_to_self\":{\"some_data\":\"here is some stringness\",\"reference_to_self\":{\"refer_to_self\":{\"simpl.ref\":\"1351295\"}},\"simpl.id\":\"1351295\"}}";
		//ElementState.
	  // ElementState es = ts.deserializeByteArray(fromJs.getBytes(),FORMAT.JSON, new TranslationContext());
		//  es.toString();

		//not sure why this is failing
		//coco.translateToObjC(new File("C:/testjs/coco"), ts);
		
		
	}
}
