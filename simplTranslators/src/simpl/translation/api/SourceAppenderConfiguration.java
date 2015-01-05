package simpl.translation.api;


public class SourceAppenderConfiguration {

	/**
	 * Character that represents a tab
	 */
	private String tabCharacter;
	
	/**
	 * Character that represents a linebreak
	 */
	private String lineBreak;
	
	/**
	 * Character that represents the start of a block
	 */
	private String blockStart;
	
	/**
	 * Chracter that represents the end of a block
	 */
	private String blockEnd;
	
	/**
	 * Configures with default values.
	 */
	public SourceAppenderConfiguration()
	{
		// Initialize all values to defaults. 
		this.tabCharacter = "\t";
		this.lineBreak = "\r\n";
		this.blockStart = "{";
		this.blockEnd = "}";
	}
	
	/**
	 * Configures a SourceAppender with chosen values.
	 * @param tab Tab Character
	 * @param line LineBreak character
	 * @param blockStart Beginning of a block
	 * @param blockEnd ending of a block
	 */
	public SourceAppenderConfiguration(String tab, String line, String blockStart, String blockEnd)
	{
		this.tabCharacter = tab;
		this.lineBreak = line;
		this.blockStart = blockStart;
		this.blockEnd = blockEnd;
	}

	public String getTabCharacter() {
		return tabCharacter;
	}

	public void setTabCharacter(String tabCharacter) {
		this.tabCharacter = tabCharacter;
	}

	public String getLineBreak() {
		return lineBreak;
	}

	public void setLineBreak(String lineBreak) {
		this.lineBreak = lineBreak;
	}

	public String getBlockStart() {
		return blockStart;
	}

	public void setBlockStart(String blockStart) {
		this.blockStart = blockStart;
	}

	public String getBlockEnd() {
		return blockEnd;
	}

	public void setBlockEnd(String blockEnd) {
		this.blockEnd = blockEnd;
	}
	
	public String translateSourceEntry(SourceEntry entry)
	{
		if(entry.isDelimiter())
		{
			if(entry.equals(SourceEntry.TAB))
			{
				return this.getTabCharacter();
			}
			if(entry.equals(SourceEntry.BREAK))
			{
				return this.getLineBreak();
			}
			if(entry.equals(SourceEntry.BLOCK_BEGIN))
			{
				return this.getBlockStart();
			}
			if(entry.equals(SourceEntry.BLOCK_END))
			{
				return this.getBlockEnd();
			}
			throw new RuntimeException("Invalid SourceEntry. Expected valid delimmiter but did not get it.");
		}else{
			return entry.getLineOfSource();
		}
	}	
}
