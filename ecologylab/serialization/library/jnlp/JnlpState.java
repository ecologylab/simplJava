/**
 * 
 */
package ecologylab.serialization.library.jnlp;

import java.util.ArrayList;

import ecologylab.serialization.ElementState;
import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.library.jnlp.applet.AppletDesc;
import ecologylab.serialization.library.jnlp.application.ApplicationDesc;
import ecologylab.serialization.library.jnlp.information.InformationElement;
import ecologylab.serialization.library.jnlp.resource.ResourceElementArray;

/**
 * Parses JNLP files for Java web launch.
 * 
 * Field comments from:
 * 
 * http://java.sun.com/j2se/1.5.0/docs/guide/javaws/developersguide/syntax.html
 * 
 * @author Zachary O. Toups (toupsz@cs.tamu.edu)
 * 
 */
public class JnlpState extends ElementState implements Cloneable
{
	/**
	 * This attribute must be 1.0 or higher to work with this release. The default value is "1.0+".
	 * Thus, it can typically be omited. Note that this version supports both spec version 1.0 and
	 * version 1.5, whereas previous versions support only 1.0. A jnlp file specifying spec="1.5+"
	 * will work with this version, but not previous versions of Java Web Start.
	 */
	@simpl_scalar
	String														spec;

	/**
	 * All relative URLs specified in href attributes in the JNLP file are using this URL as a base.
	 */
	@simpl_scalar
	String														codebase;

	/** This is a URL pointing to the location of the JNLP file itself. */
	@simpl_scalar
	String														href;

	@simpl_nowrap
	@simpl_collection("information")
	ArrayList<InformationElement>			informations	= new ArrayList<InformationElement>();

	// @xml_nested private InformationElement information;

	@simpl_collection("all-permissions")
	ArrayList<AllPermissionsElement>	security;

	@simpl_composite
	@xml_tag("resources")
	ResourceElementArray							resources;

	@simpl_composite
	@xml_tag("application-desc")
	ApplicationDesc										applicationDesc;

	@simpl_nowrap
	@simpl_collection("applet-desc")
	ArrayList<AppletDesc>							appletDesc		= new ArrayList<AppletDesc>();

	/**
	 * No-arg constructor for XML translation.
	 */
	public JnlpState()
	{
		super();
	}

	/**
	 * @return the applet description, if any (returns null if there is not one)
	 */
	public AppletDesc getAppletDesc()
	{
		return (appletDesc == null || appletDesc.size() == 0 ? null : appletDesc.get(0));
	}

	/**
	 * Sets the applet description and clears the application description (since there can only be one
	 * or the other) and any previous applet descriptions.
	 * 
	 * @param appletDesc
	 *          the appletDesc to set
	 */
	public void setAppletDesc(AppletDesc appletDesc)
	{
		if (this.appletDesc == null)
		{
			this.appletDesc = new ArrayList<AppletDesc>();
		}

		this.appletDesc.clear();
		this.appletDesc.add(appletDesc);
	}

	/**
	 * @return the application description, if any (returns null if there is not one)
	 */
	public ApplicationDesc getApplicationDesc()
	{
		return applicationDesc;
	}

	/**
	 * Sets the application description and clears the applet description (since there can only be one
	 * or the other) and any previous application descriptions.
	 * 
	 * @param applicationDesc
	 *          the applicationDesc to set
	 */
	public void setApplicationDesc(ApplicationDesc applicationDesc)
	{
		this.applicationDesc = applicationDesc;
	}

	/**
	 * @return the href
	 */
	public String getHref()
	{
		return href;
	}

	/**
	 * @param href
	 *          the href to set
	 */
	public void setHref(String href)
	{
		this.href = href;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (!(obj instanceof JnlpState))
		{
			return false;
		}
		else
		{
			String thisXml;
			try
			{
				thisXml = this.serialize().toString();
				String thatXml = ((ElementState) obj).serialize().toString();

				return thisXml.equals(thatXml);
			}
			catch (SIMPLTranslationException e)
			{
				e.printStackTrace();

				return false;
			}
		}
	}

	public static void main(String[] args) throws SIMPLTranslationException
	{
		String jnlpContents = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
				+ "<!-- JNLP File for combinFormation launcher Application -->\n"
				+ "<jnlp spec=\"1.0+\"\n"
				+ "  codebase=\"http://localhost:8080/rogue/lib/\"\n"
				+ "  href=\"rogue.jnlp\">\n"
				+ "  <information>\n"
				+ "    <title>Teaching Team Coordination through Location-Aware Games</title>\n"
				+ "    <vendor>Interface Ecology Lab | Center for Study of Digital Libraries | Texas A&amp;M University</vendor>\n"
				+ "    <homepage href=\"ecologylab.cs.tamu.edu\"/>\n"
				+ "    <description>A multi-player team game encouraging team coordination through information differential</description>\n"
				+ "    <description kind=\"short\">A multi-player team game</description>\n"
				+ "    <icon href=\"images/swingset2.jpg\"/>\n"
				+ "    <offline-allowed/>\n"
				+ "  </information>\n"
				+ "  <security>\n"
				+ "      <all-permissions/>\n"
				+ "  </security>\n"
				+ "  <resources>\n"
				+ "    <j2se version=\"1.5+\" java-vm-args=\"-Xincgc -XX:NewSize=128M -XX:MaxNewSize=128M \" initial-heap-size=\"520M\"  max-heap-size=\"520M\"/>\n"
				+ "    <jar href=\"TTeCLoG.jar\"/>\n"
				+ "    <jar href=\"ecologylabFundamental.jar\"/>\n"
				+ "    <jar href=\"ecologylabGame.jar\"/>\n"
				+ "    <jar href=\"ecologylabGraphics.jar\"/>\n"
				+ "    <jar href=\"JavaOSC.jar\"/>\n"
				+ "    <jar href=\"RXTXcomm.jar\"/>\n"
				+ "  </resources>\n"
				+ "  <application-desc main-class=\"rogue.executables.ClientExec\">\n"
				+ "    <argument>JNLP</argument>\n"
				+ "    <argument>http://localhost:8080/rogue/lib/</argument>\n"
				+ "  </application-desc>\n"
				+ "</jnlp> \n"
				+ "";

		JnlpState j = (JnlpState) JnlpTranslations.get().deserializeCharSequence(jnlpContents);

		ArrayList<InformationElement> infos = j.getInformations();
		for (InformationElement i : infos)
		{
			System.out.println("-=-=-=-");
			System.out.println(i.getTitle());
			System.out.println(i.getVendor());
		}

		ApplicationDesc appDesc = j.getApplicationDesc();

		for (String a : appDesc.getArguments())
		{
			System.out.println("arg: " + a);
		}

		j.serialize(System.out);
	}

	/**
	 * @param codebase
	 *          the codebase to set
	 */
	public void setCodebase(String codebase)
	{
		this.codebase = codebase;
	}

	/**
	 * @see ecologylab.serialization.types.element.ArrayListState#clone()
	 */
	@Override
	public JnlpState clone()
	{
		// a bit of a hack, but it's easy! :D
		try
		{
			return (JnlpState) JnlpTranslations.get().deserializeCharSequence(this.serialize());
		}
		catch (SIMPLTranslationException e)
		{
			e.printStackTrace();
		}

		return new JnlpState();
	}

	public ArrayList<InformationElement> getInformations()
	{
		return informations;
	}

	@Override
	public String toString()
	{
		return this.getHref();
	}
}
