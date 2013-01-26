package ecologylab.oodss.logging;

import java.util.Date;

import simpl.annotations.dbal.simpl_scalar;

import ecologylab.appframework.types.prefs.Pref;
import ecologylab.net.NetTools;
import ecologylab.serialization.ElementState;

/**
 * request message for the Logging server to open new log file and write the header.
 * 
 * @author eunyee
 */
public class Prologue extends ElementState
{
	public static final String	STUDY_NAME	= "study_name";

	@simpl_scalar
	protected String						date				= new Date(System.currentTimeMillis()).toString();

	@simpl_scalar
	protected String						ip					= NetTools.localHost();

	@simpl_scalar
	protected String						userID			= "0";

	@simpl_scalar
	protected String						questionId;

	@simpl_scalar
	protected String						studyName;

	@simpl_scalar
	protected String						questionPath;

	public Prologue()
	{
		super();
		this.userID = Pref.lookupString("uid", "0");
		this.questionId = Pref.lookupString("questionId");
		this.studyName = Pref.lookupString(STUDY_NAME);
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