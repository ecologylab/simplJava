package simpl.translation.api;


public interface SourceAppender {
	
	/**
	 * Sets the configuration for this Source Appender
	 * @param s
	 */
	void setConfiguration(SourceAppenderConfiguration s);
	
	/**
	 * Gets the configuration for this SourceAppender
	 * @return
	 */
	SourceAppenderConfiguration getConfiguration();
	
	
	/**
	 * Implicitly converts the string into a SourceEntry
	 */
	SourceAppender append(String s);
	
	/**
	 * Appends a source entry to the SourceAppender
	 * @param s The entry of source code to append. (Like public class ClassName)
	 * @return the soruce appender; this is a fluent API
	 */
	SourceAppender append(SourceEntry s);
	
	/**
	 * Appends the contents of one SourceAppender to another
	 * @param s The source appender. {a, b, etc.}
	 * @return {thisSourceAppender, a, b, etc.}
	 */
	SourceAppender append(SourceAppender s);
	
	/**
	 * Converts this appender to the source representation
	 * @return A string representing the source code constructed
	 */
	String toSource();
	
	/**
	 * Obtains the entries in this sourceAppender
	 * @return
	 */
	Iterable<SourceEntry> getEntries();
	
	/**
	 * Obatins the number of entries in this sourceAppender
	 * @return
	 */
	int size();
}
