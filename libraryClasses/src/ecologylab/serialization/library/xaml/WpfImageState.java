/**
 * 
 */
package ecologylab.serialization.library.xaml;

import simpl.annotations.dbal.simpl_scalar;
import simpl.annotations.dbal.simpl_tag;
import ecologylab.net.ParsedURL;

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
