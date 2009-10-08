/**
 * 
 */
package ecologylab.xml.library.xaml;

import ecologylab.xml.TranslationScope;
import ecologylab.xml.ElementState.xml_tag;

/**
 * 
 * ecologylab.xml representation of the Window WPF element for translating to XAML.
 * 
 * @author awebb
 *
 */
@xml_tag("Window")
public class WindowState extends FrameworkElementState
{
	
	static final String NAMESPACE 		= "http://schemas.microsoft.com/winfx/2006/xaml/presentation";
	static final String XAML_NAMESPACE 	= "http://schemas.microsoft.com/winfx/2006/xaml";
	
	@xml_attribute 						String xmlns 		= NAMESPACE;
	@xml_attribute @xml_tag("xmlns:x")	String xmlnsXaml 	=  XAML_NAMESPACE;
	@xml_attribute @xml_tag("Title") 	String title;
	
	public WindowState(String title)
	{
		this.title 		= title;	
	}
	
	public static TranslationScope get()
	{
		return TranslationScope.get("xaml", WindowState.class);
	}
	
}
