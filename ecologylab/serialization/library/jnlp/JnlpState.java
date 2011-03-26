/**
 * 
 */
package ecologylab.serialization.library.jnlp;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;

import ecologylab.appframework.types.prefs.PrefSet;
import ecologylab.appframework.types.prefs.PrefSetBaseClassProvider;
import ecologylab.generic.Debug;
import ecologylab.serialization.ElementState;
import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.TranslationScope;
import ecologylab.serialization.library.jnlp.applet.AppletDesc;
import ecologylab.serialization.library.jnlp.application.ApplicationDesc;
import ecologylab.serialization.library.jnlp.information.InformationElement;
import ecologylab.serialization.library.jnlp.resource.Property;
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
	
	@simpl_nowrap
	@simpl_collection("property")
	ArrayList<Property>								properties;

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

	public static void main(String[] args) throws SIMPLTranslationException,
			UnsupportedEncodingException
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
				+ "    <argument>%3Cpref_set%3E%3Cpref_float+name%3D%22WALL_REPULSION%22+value%3D%221000.0%22%2F%3E%3Cpref_float+name%3D%22VALUE_IN%22+value%3D%220.1%22%2F%3E%3Cpref_int+name%3D%22INTERFACE_MODE%22+value%3D%222%22%2F%3E%3Cpref_int+name%3D%22MAX_GOALS%22+value%3D%2212%22%2F%3E%3Cpref_string+name%3D%22TUTORIAL_FILE%22+value%3D%22%2Ftutorial%2Ftutorial.xml%22%2F%3E%3Cpref_boolean+name%3D%22IS_TUTORIAL%22+value%3D%22true%22%2F%3E%3Cpref_color_mapping+name%3D%22SEEKER_COLORS%22%3E%3Cseeker_color+user_id%3D%22tests036%22%2F%3E%3C%2Fpref_color_mapping%3E%3Cpref_boolean+name%3D%22SHOW_WAP_FIELDS%22+value%3D%22true%22%2F%3E%3Cpref_string+name%3D%22APP_ID%22+value%3D%22tteclogTutorialLaunch%22%2F%3E%3Cpref_string+name%3D%22QUESTION_NAME%22+value%3D%22tutorial%22%2F%3E%3Cpref_float+name%3D%22GOAL_REPULSION%22+value%3D%2220.0%22%2F%3E%3Cpref_float+name%3D%22THREAT_KC%22+value%3D%222.5%22%2F%3E%3Cpref_int+name%3D%22debug_global_level%22+value%3D%225%22%2F%3E%3Cpref_int+name%3D%22MAX_3_GOALS%22+value%3D%223%22%2F%3E%3Cpref_int+name%3D%22log_mode%22+value%3D%224%22%2F%3E%3Cpref_int+name%3D%22MAX_CYCLES%22+value%3D%229000%22%2F%3E%3Cpref_int+name%3D%22VALUE_4_GOALS%22+value%3D%221600%22%2F%3E%3Cpref_float+name%3D%22KF%22+value%3D%220.45%22%2F%3E%3Cpref_auth_list+name%3D%22AUTH_LIST_FROM_SERVER%22%3E%3Cvalue+last_u_i_d%3D%222%22%3E%3Cauth_list%3E%3Cuser+user_key%3D%22tests036%22+password%3D%22p9RTXTHTop60vG1x5Ky5omJIoTEYMGzda7l3qSq%2BB5Q%3D%22%2F%3E%3Cuser+user_key%3D%22_coord%22+password%3D%22LPjU%2FOMOf92odzLmTRWqMpCxEE44v8xLjozoWLtTVQA%3D%22+uid%3D%221%22%2F%3E%3C%2Fauth_list%3E%3C%2Fvalue%3E%3C%2Fpref_auth_list%3E%3Cpref_int+name%3D%22logging_port%22+value%3D%2210201%22%2F%3E%3Cpref_boolean+name%3D%22SHOW_RESTORATION_ZONES%22+value%3D%22true%22%2F%3E%3Cpref_float+name%3D%22KC%22+value%3D%220.15%22%2F%3E%3Cpref_int+name%3D%22MAX_2_GOALS%22+value%3D%224%22%2F%3E%3Cpref_float+name%3D%22AVAILABLE_ACCEL%22+value%3D%221.1%22%2F%3E%3Cpref_boolean+name%3D%22SHOW_GPS_FIELDS%22+value%3D%22true%22%2F%3E%3Cpref_float+name%3D%22VIRTUAL_VISCOSITY%22+value%3D%22-0.1%22%2F%3E%3Cpref_boolean+name%3D%22SHOW_GUI%22+value%3D%22true%22%2F%3E%3Cpref_int+name%3D%22MAX_SEEKERS%22+value%3D%221%22%2F%3E%3Cpref_int+name%3D%22MAX_THREATS%22+value%3D%2220%22%2F%3E%3Cpref_int+name%3D%22PENALTY_OUT%22+value%3D%2225%22%2F%3E%3Cpref_float+name%3D%22BASE_REPULSION%22+value%3D%22100.0%22%2F%3E%3Cpref_string+name%3D%22MAP%22+value%3D%22%2Fmaps%2Ftutorial.xml%22%2F%3E%3Cpref_string+name%3D%22STUDY_PASSWORD%22+value%3D%22asdffdsa%22%2F%3E%3Cpref_int+name%3D%22MAX_COLLECT_CYCLES%22+value%3D%2215%22%2F%3E%3Cpref_float+name%3D%22THREAT_KF%22+value%3D%220.02%22%2F%3E%3Cpref_float+name%3D%22KV%22+value%3D%220.45%22%2F%3E%3Cpref_string+name%3D%22STUDY_URL%22+value%3D%22http%3A%2F%2F128.194.128.234%3A8080%2FsampleStudy%22%2F%3E%3Cpref_int+name%3D%22VALUE_1_GOALS%22+value%3D%22100%22%2F%3E%3Cpref_int+name%3D%22VALUE_2_GOALS%22+value%3D%22400%22%2F%3E%3Cpref_float+name%3D%22WORLD_VISCOSITY%22+value%3D%22-0.39999998%22%2F%3E%3Cpref_float+name%3D%22GOAL_ATTRACTION%22+value%3D%2240.0%22%2F%3E%3Cpref_int+name%3D%22VALUE_3_GOALS%22+value%3D%22900%22%2F%3E%3Cpref_boolean+name%3D%22IS_LOCATION_AWARE%22+value%3D%22true%22%2F%3E%3Cpref_float+name%3D%22THREAT_BIAS%22+value%3D%220.90000004%22%2F%3E%3Cpref_long+name%3D%22RANDOM_SEED%22+value%3D%223000%22%2F%3E%3Cpref_boolean+name%3D%22PAUSE_ON_PANIC%22%2F%3E%3Cpref_float+name%3D%22THREAT_KV%22+value%3D%220.1%22%2F%3E%3Cpref_boolean+name%3D%22SHOW_WALLS%22+value%3D%22true%22%2F%3E%3C%2Fpref_set%3E</argument>"
				+ "  </application-desc>\n" + "</jnlp> \n" + "";

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

		String prefSetString = URLDecoder.decode(
				appDesc.getArguments().get(appDesc.getArguments().size() - 1), "UTF-8");
		TranslationScope[] arrayToMakeJavaShutUp = {};
		PrefSet prefs = (PrefSet) TranslationScope.get(PrefSet.PREFS_TRANSLATION_SCOPE,
				arrayToMakeJavaShutUp, PrefSetBaseClassProvider.STATIC_INSTANCE.provideClasses())
				.deserializeCharSequence(prefSetString);

		Debug.println(prefSetString);
		Debug.println(prefs.serialize());

		JnlpState newState = new JnlpState();
		newState.setApplicationDesc(new ApplicationDesc());

		newState.getApplicationDesc().setPrefSet(prefs);

		Debug.println(newState.serialize());
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
