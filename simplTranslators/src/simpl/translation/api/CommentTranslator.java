package simpl.translation.api;

public interface CommentTranslator {
	public SourceAppender translateDocComment(String... comments);
}