/**
 * 
 */
package ecologylab.serialization.library.xaml;

import ecologylab.serialization.ElementState.xml_tag;
import ecologylab.serialization.TranslationScope;

/**
 * 
 * ecologylab.serialization representation of the Window WPF element for translating to XAML.
 * 
 * @author awebb
 *
 */
@xml_tag("Window")
public class WindowState extends FrameworkElementState
{
	
	static final String NAMESPACE 		= "http://schemas.microsoft.com/winfx/2006/xaml/presentation";
	static final String XAML_NAMESPACE 	= "http://schemas.microsoft.com/winfx/2006/xaml";
	
	@simpl_scalar 						String xmlns 		= NAMESPACE;
	@simpl_scalar @xml_tag("xmlns:x")	String xmlnsXaml 	=  XAML_NAMESPACE;
	@simpl_scalar @xml_tag("Title") 	String title;
	
	public WindowState(String title)
	{
		this.title 		= title;	
	}
	
	public static TranslationScope get()
	{
		return TranslationScope.get("xaml", WindowState.class);
	}
	
}
