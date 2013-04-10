package simpl.formats.string;

import java.util.HashMap;
import java.util.Map;

import simpl.core.ISimplStringFormat;
import simpl.formats.enums.Format;
import simpl.formats.enums.StringFormat;


public class FormatRegistry {

	private static Map<StringFormat, ISimplStringFormat> formats;
	
	private static Map<Format, ISimplStringFormat> standardFormats;

	// TODO: STructure this more like type registry / simpl type scope... 
	// TODO: Avoid use of enums for extensibility in the future. Possibly leverage multiscope idea (to be implemented)
	static
	{
		formats = new HashMap<StringFormat, ISimplStringFormat>();	
		formats.put(StringFormat.XML, new XMLFormat());
		formats.put(StringFormat.JSON, new JSONFormat());
		
		standardFormats = new HashMap<Format, ISimplStringFormat>();
		standardFormats.put(Format.XML, new XMLFormat());
		standardFormats.put(Format.JSON, new JSONFormat());
	}
	
	public static ISimplStringFormat get(StringFormat sf)
	{
		return formats.get(sf);
	}
	
	public static ISimplStringFormat get(Format f)
	{
		return standardFormats.get(f);
	}
}
