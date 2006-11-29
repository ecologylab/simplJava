package ecologylab.xml.rss;

import ecologylab.xml.*;

/**
 * {@link ecologylab.xml.ElementState ElementState} for the root element of the RSS parser.
 * In particular, this supports RSS versions such as .91, .92, .93, .94, and 2.0.
 *
 * @author andruid
 */
public class RssState extends ElementState
{
	@xml_attribute	float		version;
   
	@xml_nested		Channel		channel;

	/**
	 * @return Returns the channel.
	 */
	public Channel getChannel()
	{
		return channel;
	}

	/**
	 * @param channel The channel to set.
	 */
	public void setChannel(Channel channel)
	{
		this.channel = channel;
	}

	/**
	 * @return Returns the version.
	 */
	public float getVersion()
	{
		return version;
	}

	/**
	 * @param version The version to set.
	 */
	public void setVersion(float version)
	{
		this.version = version;
	}
}
