package simpl.translation.api;

/**
 * A class representing a single entry of sourcecode
 * Examples include "public class Something" 
 * or "void SomeFunction()" 
 * 
 * Translators generate sourceEntries, the SourceAppender handles the task of tabbing and line breaking
 * So that code is generated with nice indentation, etc.
 * @author twhite
 *
 */
public class SourceEntry {
	String lineOfSource = ""; 
	public boolean isBreak() {
		return isBreak;
	}

	public boolean isBlockBegin() {
		return isBlockBegin;
	}

	public boolean isBlockEnd() {
		return isBlockEnd;
	}

	public boolean isTab() {
		return isTab;
	}

	boolean isBreak = false;
	boolean isBlockBegin = false;
	boolean isBlockEnd = false;
	boolean isTab = false;
	
	public static SourceEntry BREAK = new SourceEntry(true, false, false, false);
	public static SourceEntry BLOCK_BEGIN = new SourceEntry(false, true, false, false);
	public static SourceEntry BLOCK_END = new SourceEntry(false, false, true, false);
	public static SourceEntry TAB = new SourceEntry(false, false, false, true);
	
	private SourceEntry(boolean isBreak, boolean isBlockBegin, boolean isBlockEnd, boolean isTab){
		this.isBreak = isBreak;
		this.isBlockBegin = isBlockBegin;
		this.isBlockEnd = isBlockEnd;
		this.isTab = isTab;
	};
	
	public SourceEntry(String lineOfSource)
	{
		this.lineOfSource = lineOfSource;
	}
	
	public String getLineOfSource()
	{
		return this.lineOfSource;
	}
	
	public boolean isDelimiter()
	{
		return isBreak || isBlockEnd || isBlockBegin || isTab;
	}
	
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		if(obj != null && obj instanceof SourceEntry)
		{
			SourceEntry other = (SourceEntry)obj;
			
			if(this.isDelimiter())
			{
				if(other.isDelimiter())
				{
					// Compare the delimiter values. All must match.
					return ((this.isTab() == other.isTab())&&
					   (this.isBreak() == other.isBreak())&&
					   (this.isBlockBegin() == other.isBlockBegin())&&
					   (this.isBlockEnd() == other.isBlockEnd()));
				}else{
					return false;
				}
			}else{
				if(other.isDelimiter())
				{
					return false;
				}else{
					return this.getLineOfSource().equals(other.getLineOfSource());
				}
			}
		}else{
			return false;
		}
	}
	
	@Override
	public int hashCode() 
	{
		if(lineOfSource.isEmpty())
		{
			int b = isBreak ? 1:0;
			int bb = isBlockBegin ? 2:0;
			int be = isBlockEnd ? 4:0;
			int tt = isTab ? 8:0;
			return b+bb+be+tt;
		}else{
			return lineOfSource.hashCode();
		}
	}
}
