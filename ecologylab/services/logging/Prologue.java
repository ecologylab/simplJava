package ecologylab.services.logging;

import java.util.Date;

import ecologylab.net.NetTools;
import ecologylab.xml.ElementState;
import ecologylab.xml.XmlTranslationException;
import ecologylab.xml.XmlTools;


/**
 * request message for the Logging server to open new log file
 * and write the header.
 * 
 * @author eunyee
 */
public class Prologue extends ElementState
{
	@xml_attribute protected String	date					= new Date(System.currentTimeMillis()).toString();
	
	@xml_attribute protected String	ip						= NetTools.localHost();
	
	@xml_attribute protected int 		userID					= 0;
	
	@xml_attribute protected String 	studyName;
	
	public Prologue()
	{
		super();
	}
	
	public void setUserID(int id)
	{
		this.userID = id;
	}
	
	public void setStudyName(String studyName)
	{
		this.studyName = studyName;
	}
	
	public String getStudyName()
	{
		return studyName;
	}
}