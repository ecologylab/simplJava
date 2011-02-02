package ecologylab.serialization.bibtex;

public class BibTeXFormatException extends Exception
{
	
	private String msg;

	public BibTeXFormatException(char[] data, int index, String message)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("BibTeX format error:\n");
		for (int i = 0; i < data.length && i < index + 2; ++i)
		{
			if (Character.isWhitespace((int) data[i]))
			{
				sb.append(' ');
			}
			else
			{
				sb.append(data[i]);
			}
		}
		sb.append("...\n");
		for (int i = 0; i < data.length && i < index; ++i)
			sb.append(' ');
		sb.append("^ ").append(message);
		msg = sb.toString();
	}

	@Override
	public String getMessage()
	{
		return msg;
	}
	
}
