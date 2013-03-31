package simpl.translators.java;

import simpl.translation.api.SourceAppender;
import simpl.translation.api.SourceCodeAppender;
import simpl.translation.api.SourceEntry;
import simpl.translation.api.CommentTranslator;

public class JavaCommentTranslator implements CommentTranslator
{
	@Override
	public SourceAppender translateDocComment(String... comments) {
		SourceAppender source = new SourceCodeAppender();
		
		if(comments != null && comments.length > 0)
		{
			if(comments[0] != null)
			{
				// Start of comment block
				source.append("/**");
				for(String commentLine : comments)
				{
					if(commentLine != null)
					{
						source.append("* " + commentLine);
					}
				}
				source.append("*/");
				return source;
			}else{
				return source;
			}
		}
		else
		{
			return source;
		}
	}
}
