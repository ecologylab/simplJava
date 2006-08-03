package cf.services.messages;

import cf.app.CMShellApplication;
import cf.app.CollageMachine;
import ecologylab.generic.ConsoleUtils;
import ecologylab.generic.Debug;
import ecologylab.generic.ObjectRegistry;
import ecologylab.services.messages.OkResponse;
import ecologylab.services.messages.RequestMessage;
import ecologylab.services.messages.ResponseMessage;
import ecologylab.xml.ElementState;
import ecologylab.xml.TranslationSpace;
import ecologylab.xml.XmlTranslationException;

/**
 * The message sent by CFServicesClientApplet to combinFormation at startup to configure preferences.
 *
 * @author blake
 * @author andruid
 */
public class SetPreferences 
extends RequestMessage
{
	static boolean			firstTime		= true;
	
	public PreferencesSet	preferencesSet	= new PreferencesSet();
	
	
	public SetPreferences()
	{
		super();
	}

	public SetPreferences(PreferencesSet preferencesSet)
	{
		super();
		this.preferencesSet		= preferencesSet;
	}
	public SetPreferences(String preferencesSetString, TranslationSpace nameSpace)
	throws XmlTranslationException
	{
		this((PreferencesSet) translateFromXMLString(preferencesSetString, nameSpace));
	}

	public ResponseMessage performService(ObjectRegistry objectRegistry) 
	{
		debug("cf services: received new preferences: " + preferencesSet +" " + preferencesSet.size());
		if (firstTime)
		{
			firstTime		= false;
	    	//now internally set the preferences
			preferencesSet.processPreferences();
			//print the prefs
			debug("performService() Received and loaded preferences: " + preferencesSet);
	
			
			CollageMachine collageMachine	= CMShellApplication.setupCollageMachine(objectRegistry);
			
			collageMachine.start(Thread.NORM_PRIORITY - 1); // build in new Thread to enable concurrency with seed transmission.
			//collageMachine.run();
	        ConsoleUtils.obtrusiveConsoleOutput("SetPreferences.sending ResponseMessage(OK)");
		}
		else
			debug("IGNORING: preferences were previously loaded.");
		
		return OkResponse.get();
	}
	static final String test = "<preferences_set><preference name=\"desc\" value=\"Auto\"/><preference name=\"codeBaseAppend\" value=\"cf/\"/><preference name=\"code_base\" value=\"http://localhost/ecologylab/code/java/cf/\"/><preference name=\"spatial_grid\" value=\"1\"/><preference name=\"graphics_device\" value=\"1\"/><preference name=\"limit_traversal\" value=\"1\"/><preference name=\"crawl\" value=\"1\"/><preference name=\"download_images_automatically\" value=\"true\"/><preference name=\"study_format\" value=\"0\"/><preference name=\"html_output_format\" value=\"0\"/><preference name=\"html_highlight_onRollover\" value=\"true\"/><preference name=\"html_metadata_onRollover\" value=\"true\"/><preference name=\"play_on_start\" value=\"1\"/><preference name=\"popupcolor\" value=\"cccccc\"/><preference name=\"images_only\" value=\"false\"/><preference name=\"draw_grid\" value=\"0\"/><preference name=\"screen_size\" value=\"2\"/><preference name=\"pulse_timeout\" value=\"50000\"/><preference name=\"dead_width\" value=\"51\"/><preference name=\"dead_height\" value=\"58\"/><preference name=\"debug_global_level\" value=\"0\"/><preference name=\"unstuck_debugger\" value=\"false\"/><preference name=\"sleep_min\" value=\"100\"/><preference name=\"sleep_max\" value=\"10000\"/><preference name=\"sleep\" value=\"900\"/><preference name=\"recursion_sleep\" value=\"10000\"/><preference name=\"go_prefix\" value=\"/ecology/combinFormation/popup.html?location=\"/><preference name=\"single_step\" value=\"0\"/><preference name=\"browserV\" value=\"6\"/><preference name=\"pcormac\" value=\"false\"/><preference name=\"mac\" value=\"false\"/><preference name=\"userinterface\" value=\"mistrot_interface\"/><preference name=\"cool_space_in_center\" value=\"true\"/><preference name=\"coolSpaceRatio\" value=\"0.2\"/><preference name=\"elements_per_square_inch\" value=\"0.7\"/><preference name=\"browser\" value=\"IE\"/><preference name=\"os\" value=\"Windows XP\"/><preference name=\"txt_img_weight_ratio\" value=\"25\"/><preference name=\"min_opacity_txt\" value=\"88\"/><preference name=\"min_opacity_img\" value=\"34.375\"/><preference name=\"aging_txt\" value=\"true\"/><preference name=\"fade_rate_txt_fg_bg\" value=\"0.75\"/><preference name=\"aging_img\" value=\"true\"/><preference name=\"blur_negative_interest\" value=\"false\"/><preference name=\"undo_levels\" value=\"32\"/><preference name=\"log_mode\" value=\"1\"/><preference name=\"synth_google_query\" value=\"1\"/><preference name=\"auto_expand_info_space\" value=\"true\"/><preference name=\"show_dev_prefs\" value=\"true\"/><preference name=\"rolloverVisualTools\" value=\"true\"/><preference name=\"white_text_bg_for_user_elements\" value=\"false\"/><preference name=\"javaplugin\" value=\"true\"/><preference name=\"big_images\" value=\"true\"/><preference name=\"pixelation\" value=\"true\"/></preferences_set>";
	public static void main(String[] a)
	{
		try
		{
			SetPreferences sp	= new SetPreferences(test, CFServicesTranslations.get());
			Debug.println("preferences recoded: " + sp.translateToXML(false));
		}
		catch (XmlTranslationException e)
		{
			Debug.println("ERROR: Translation of startup stuff to XML failed!!\n" +
					e.getMessage() + "\nQuitting.");
			return;
		}
		
	}
}
