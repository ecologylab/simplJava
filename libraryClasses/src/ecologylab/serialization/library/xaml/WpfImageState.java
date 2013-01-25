/**
 * 
 */
package ecologylab.serialization.library.xaml;

import ecologylab.net.ParsedURL;
import ecologylab.serialization.annotations.simpl_scalar;
import ecologylab.serialization.annotations.simpl_tag;

/**
 * ecologylab.serialization representation of the Image WPF element for translating to XAML.
 * 
 * @author andrew
 *
 */
@simpl_tag("Image")
public class WpfImageState extends PanelChildState 
{
    @simpl_scalar @simpl_tag("Source") ParsedURL source;
	
    public WpfImageState(ParsedURL imageSource)
    {
    	this.source 	= imageSource;
    }
}
