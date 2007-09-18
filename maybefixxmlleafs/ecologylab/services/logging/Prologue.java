package ecologylab.services.logging;

import java.util.Date;

import ecologylab.appframework.types.prefs.Pref;
import ecologylab.net.NetTools;
import ecologylab.xml.ElementState;


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
	
	@xml_attribute protected String 	userID				= "0";
	
	@xml_attribute protected String		questionId;
	
	@xml_attribute protected String 	studyName;
	
	@xml_attribute protected String		questionPath;
	
	public Prologue()
	{
		super();
		this.userID 	= Pref.lookupString("uid", "0");
		this.questionId = Pref.lookupString("questionId");
		this.studyName 	= Pref.lookupString("study_name");
	}
	
	public void setUserID(String id)
	{
		this.userID = id;
	}
	
	public String getUserID()
	{
		return this.userID;
	}
	
	public void setStudyName(String studyName)
	{
		this.studyName = studyName;
	}
	
	public String getStudyName()
	{
		return studyName;
	}
	
	public void setQuestionId(String questionId)
	{
		this.questionId = questionId;
	}
	
	public String getQuestionId()
	{
		return questionId;
	}
	
	public String getIp()
	{
		return ip;
	}
	
	public String getDate()
	{
		return date;
	}
	
	public String getQuestionPath()
	{
		return questionPath;
	}
}