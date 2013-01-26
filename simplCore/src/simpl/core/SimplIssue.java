package simpl.core;

/**
 * A more friendly representation of any "issues" that may occur while simple de/serializing data.
 */
public class SimplIssue {

	/**
	 * A concise explanation of an error that caused an issue with SIMPL.
	 */
	public String errorExplanation;
	/**
	 * The problematic string which caused a given issue
	 */
	public String problematicString;
	/**
	 * The problematic object which cased a given issue. 
	 */
	public Object problematicObject;
	
	public Exception exception; 

	/**
	 * Creates an instance of simpl issue to describe an issue that occurred in simpl de/serialization
	 * @param explanation A nice explanation of the issue. For example: "Invalid format for Date"
 	 * @param problemString The string that may have caused the issue. Empty if no string. Example: "132/41/23rg" => A malformatted date.
	 * @param problemObject The object that may have caused the issue. Null if no object.
	 */
	public SimplIssue(String explanation, String problemString, Object problemObject)
	{
		this.errorExplanation = explanation;
		this.problematicString = problemString;
		this.problematicObject = problemObject;
	}

	public SimplIssue exception(Exception ex) {
		this.exception = ex;
		return this;
	}

}
