package ecologylab.serialization.deserializers.parsers.bibtex;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ecologylab.serialization.ElementState;
import ecologylab.serialization.ElementStateBibTeXHandler;
import ecologylab.serialization.FieldTypes;
import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.deserializers.parsers.bibtex.entrytypes.AbstractBibTeXEntry;
import ecologylab.serialization.deserializers.parsers.bibtex.entrytypes.BibTeXInProceedings;
import ecologylab.serialization.serializers.enums.Format;

/**
 * The BibTeX parser class.
 * 
 * @author quyin
 *
 */
public class BibTeXParser implements FieldTypes
{

	static enum State
	{
		START, TYPE, BODY_START, KEY_START, KEY, KEY_FINISH, TAG_START, TAG, VALUE_START, VALUE, STOP
	};

	BibTeXEvents			eventListener;

	State							state;

	/**
	 * Constructor.
	 * 
	 * @param eventListener An event listener to handle parsing events.
	 * 
	 */
	public BibTeXParser(BibTeXEvents eventListener)
	{
		this.eventListener = eventListener;
	}

	/**
	 * The entry method to parse BibTeX for deserialization. Accept a char array. Output a list of
	 * ElementState (which has been annotated with S.IM.PL's bibtex annotations) since one BibTeX
	 * file can contain multiple entries.
	 * <p />
	 * The parsing process is controlled by a DFA.
	 * 
	 * @param data
	 * @return
	 * @throws BibTeXFormatException
	 */
	public <ES extends ElementState> List<ES> parse(char[] data) throws BibTeXFormatException
	{
		List<ES> rst = new ArrayList<ES>();
		
		if (data.length <= 0)
			return rst;

		eventListener.startBibTeX();
		state = State.START;

		int p = 0;
		int valueStart = 0;
		while (p < data.length)
		{
			switch (state)
			{
			case START:
				if (data[p] != '@')
				{
					++p;
				}
				else if (data[p] == '@')
				{
					state = State.TYPE;
					++p;
					valueStart = p;
				}
				else
					throw new BibTeXFormatException(data, p, "expecting whitespaces or '@'.");
				break;

			case TYPE:
				if (!(Character.isWhitespace((int) data[p])) && data[p] != '{')
				{
					++p;
				}
				else if (Character.isWhitespace((int) data[p]))
				{
					state = State.BODY_START;
					eventListener.startEntity(new String(data, valueStart, p - valueStart));
					++p;
				}
				else if (data[p] == '{')
				{
					state = State.KEY_START;
					eventListener.startEntity(new String(data, valueStart, p - valueStart));
					++p;
				}
				else
					throw new BibTeXFormatException(data, p, "expecting letters, whitespaces or '{'.");
				break;

			case BODY_START:
				if (Character.isWhitespace((int) data[p]))
				{
					++p;
				}
				else if (data[p] == '{')
				{
					state = State.KEY_START;
					++p;
				}
				else
					throw new BibTeXFormatException(data, p, "expecting whitespaces or '{'.");
				break;

			case KEY_START:
				if (Character.isWhitespace((int) data[p]))
				{
					state = State.KEY;
					valueStart = p;
					++p;
				}
				else if (Character.isLetterOrDigit((int) data[p]))
				{
					state = State.KEY;
					valueStart = p;
					++p;
				}
				else if (data[p] == ',')
				{
					state = State.TAG_START;
					eventListener.key(new String(data, valueStart, p - valueStart));
					++p;
				}
				else
					throw new BibTeXFormatException(data, p, "expecting letters, digits or whitespaces.");
				break;

			case KEY:
				if (!(Character.isWhitespace((int) data[p])) && data[p] != ',')
				{
					++p;
				}
				else if (Character.isWhitespace((int) data[p]))
				{
					state = State.KEY_FINISH;
					eventListener.key(new String(data, valueStart, p - valueStart));
					++p;
				}
				else if (data[p] == ',')
				{
					state = State.TAG_START;
					eventListener.key(new String(data, valueStart, p - valueStart));
					++p;
				}
				else
					throw new BibTeXFormatException(data, p, "expecting letters, digits, whitespaces or ','.");
				break;

			case KEY_FINISH:
				if (Character.isWhitespace((int) data[p]))
				{
					++p;
				}
				else if (data[p] == ',')
				{
					state = State.TAG_START;
					++p;
				}
				else
					throw new BibTeXFormatException(data, p, "expecting whitespaces or ','.");
				break;

			case TAG_START:
				if (Character.isWhitespace((int) data[p]))
				{
					++p;
				}
				else if (Character.isLetterOrDigit((int) data[p]))
				{
					state = State.TAG;
					valueStart = p;
					++p;
				}
				else
					throw new BibTeXFormatException(data, p, "expecting letters, digits or whitespaces.");
				break;

			case TAG:
				if (Character.isLetterOrDigit((int) data[p]) || data[p] == '_')
				{
					++p;
				}
				else if (Character.isWhitespace((int) data[p]))
				{
					state = State.VALUE_START;
					eventListener.startTag(new String(data, valueStart, p - valueStart));
					++p;
				}
				else if (data[p] == '=')
				{
					state = State.VALUE;
					eventListener.startTag(new String(data, valueStart, p - valueStart));
					++p;
				}
				else
					throw new BibTeXFormatException(data, p, "expecting letters, digits, whitespaces or '='.");
				break;

			case VALUE_START:
				if (Character.isWhitespace((int) data[p]))
					++p;
				else if (data[p] == '=')
				{
					state = State.VALUE;
					++p;
				}
				else
					throw new BibTeXFormatException(data, p, "expecting whitespaces or '='.");
				break;

			case VALUE:
				StringBuilder sb = new StringBuilder();
				p += readValueAndWhitespaces(data, p, sb);
				eventListener.value(sb.toString());
				if (data[p] == ',')
				{
					eventListener.endTag();
					state = State.TAG_START;
					++p;
				}
				else if (data[p] == '}')
				{
					eventListener.endTag();
					ES object = (ES) eventListener.getBibTeXObject();
					if (object != null)
						rst.add(object);
					eventListener.endEntity();
					state = State.START;
					++p;
				}
				else
					throw new BibTeXFormatException(data, p, "expecting ',' or '}'.");
				break;
			}
		}

		state = State.STOP;
		eventListener.endBibTeX();
		
		return rst;
	}

	private int readValueAndWhitespaces(char[] data, int p, StringBuilder sb)
			throws BibTeXFormatException
	{
		int p0 = p;

		while (p < data.length && Character.isWhitespace((int) data[p]))
			++p;

		if (data[p] == '"')
		{
			++p;
			int mode = 0;
			while (p < data.length && !(mode == 0 && data[p] == '"'))
			{
				switch (mode)
				{
				case 0:
					if (data[p] == '\\')
						mode = 1;
					else
						sb.append(data[p]);
					break;
				case 1:
					if (data[p] == '"' || data[p] == '\\')
						sb.append(data[p]);
					else
						sb.append('\\').append(data[p]);
					mode = 0;
					break;
				}
				++p;
			}

			if (mode != 0 || p >= data.length)
				throw new BibTeXFormatException(data, p, "unclosed quotes.");
		}
		else if (data[p] == '{')
		{
			++p;
			int count = 1;
			int mode = 0;
			while (p < data.length && count > 0)
			{
				switch (mode)
				{
				case 0:
					if (data[p] == '\\')
						mode = 1;
					else
					{
						if (data[p] == '{')
							++count;
						else if (data[p] == '}')
							--count;
						if (count > 0)
							sb.append(data[p]);
					}
					break;
				case 1:
					if (data[p] == '{' || data[p] == '}' || data[p] == '\\')
						sb.append(data[p]);
					else
						sb.append('\\').append(data[p]);
					mode = 0;
					break;
				}
				++p;
			}
			--p;

			if (count > 0)
				throw new BibTeXFormatException(data, p, "unclosed brackets.");
		}
		else
		{
			// numbers or proper nouns
			while (p < data.length && !Character.isWhitespace((int) data[p]) && data[p] != ',')
			{
					sb.append(data[p]);
					++p;
			}
			--p;
		}

		++p; // skip the closing " or }
		while (p < data.length && Character.isWhitespace((int) data[p]))
			++p;

		return p - p0;
	}

	void testReadValue() throws BibTeXFormatException
	{
		String[] tests =
		{ "\"\"", "\"abc\"", "\"ab\\\"c\"", "\"ab\\\\\\\"c\"", "{}", "{abc}", "{ab\\{c}",
				"{ab\\{abc\\}}", "{ab{abc}}", "1234"};

		for (String test : tests)
		{
			StringBuilder sb = new StringBuilder();
			int d = readValueAndWhitespaces(test.toCharArray(), 0, sb);
			System.out.format("%d: %s\n", d, sb.toString());
		}
	}
	
	void testParser1() throws BibTeXFormatException, SIMPLTranslationException
	{
		String data = "   @inproceedings   {  article1  ,  author   =   \"Author 1\"   ,   title    =   {TITLE 1}     }    @inproceedings {   article2,  author = \"Somebody\", sometag={some value}}   ";
		List<BibTeXInProceedings> entities = parse(data.toCharArray());
		for (BibTeXInProceedings entity : entities)
		{
			System.out.println(entity.serialize().toString());
		}
	}
	
	void testParser2() throws IOException, BibTeXFormatException, SIMPLTranslationException
	{
		FileReader fr = new FileReader("C:/tmp/iis10.bib");
		StringBuilder sb = new StringBuilder();
		char[] buf = new char[4096];
		while (true)
		{
			int len = fr.read(buf);
			if (len < 0)
				break;
			sb.append(buf, 0, len);
		}
		
		List<AbstractBibTeXEntry> entities = parse(sb.toString().toCharArray());
		for (AbstractBibTeXEntry entity : entities)
		{
			entity.serialize(System.out, Format.XML);
			System.out.println();
//			entity.serialize(System.out, FORMAT.BIBTEX);
		}
	}

	public static void main(String[] args) throws BibTeXFormatException, SIMPLTranslationException, IOException
	{
		BibTeXEvents listener = new ElementStateBibTeXHandler(BibTeXEntryTranslationScope.get());
		BibTeXParser parser = new BibTeXParser(listener);
//		parser.testReadValue();
//		parser.testParser1();
		parser.testParser2();
	}

}
