package ecologylab.tutorials.polymorphic.rogue.entity;

import ecologylab.xml.xml_inherit;
import ecologylab.xml.ElementState.xml_tag;

@xml_inherit
@xml_tag("las")
public class LocationAwareSeekerAvatar extends SeekerAvatar
{
	@xml_attribute
	protected double	gpsUncertainty								= 0.0;
	
	@xml_attribute
	protected boolean gpsGood										= true;

	public LocationAwareSeekerAvatar()
	{
		super();
	}
}
