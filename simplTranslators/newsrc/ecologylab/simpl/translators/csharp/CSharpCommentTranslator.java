package ecologylab.simpl.translators.csharp;

/**
 * 
 * @author quyin
 * 
 */
public class CSharpCommentTranslator
{

  /**
   * 
   * @param spacing
   * @param comments
   * @return
   */
  public String translateComment(String spacing, String... comments)
  {
    StringBuilder sb = new StringBuilder();
    sb.append(spacing);
    sb.append("/// <summary>\n");
    if (comments != null && comments.length > 0)
      for (String comment : comments)
        sb.append(spacing).append("/// ").append(comment).append("\n");
    else
      sb.append(spacing).append("/// (missing comments)\n");
    sb.append(spacing).append("/// </summary>\n");
    return sb.toString();
  }

}
