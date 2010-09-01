package ecologylab.serialization;

public class ElementStateTLVHandler
{
	TranslationScope translationScope;
	
	int type;
	String tagName; 
	
	int length;
	
	
	public ElementStateTLVHandler(TranslationScope translationScope)
	{
		this.translationScope = translationScope;
	}
	
	public ElementState parse(byte[] byteArray)
	{			
		return null;
	}
}