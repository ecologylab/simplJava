package simpl.translation.api;

/**
 * This interface translates lines of comments into source comments
 */
public interface CommentTranslator {
	/**
	 * Translates a set of comments into a comment. 
	 * If there are multiple lines, should use the idiomatic 
	 * representation of a multi-line comment in the target language.
	 * @param comments A collection of comments 
	 * @return A SourceAppender containing entries for the comments. 
	 */
	public SourceAppender translateDocComment(String... comments);
}