package ecologylab.xml.rss;

import ecologylab.xml.*;

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
