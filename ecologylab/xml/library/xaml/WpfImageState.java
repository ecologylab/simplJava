/**
 * 
 */
package ecologylab.xml.library.xaml;

import ecologylab.net.ParsedURL;
import ecologylab.xml.ElementState.xml_tag;

/**
 * ecologylab.xml representation of the Image WPF element for translating to XAML.
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
