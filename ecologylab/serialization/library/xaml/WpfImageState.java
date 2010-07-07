/**
 * 
 */
package ecologylab.serialization.library.xaml;

import ecologylab.net.ParsedURL;
import ecologylab.serialization.ElementState.xml_tag;

/**
 * ecologylab.serialization representation of the Image WPF element for translating to XAML.
 * 
 * @author andrew
 *
 */
@xml_tag("Image")
public class WpfImageState extends PanelChildState 
{
    @simpl_scalar @xml_tag("Source") ParsedURL source;
	
    public WpfImageState(ParsedURL imageSource)
    {
    	this.source 	= imageSource;
    }
}
