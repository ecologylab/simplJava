package simpl.translation.api;

import java.util.LinkedList;
import java.util.List;

public class SourceCodeAppender implements SourceAppender {

	private SourceAppenderConfiguration ourConfig;
	
	public SourceCodeAppender()
	{
		// use default config. 
		this.ourConfig = new SourceAppenderConfiguration();
	}
	
	private List<SourceEntry> ourEntries = new LinkedList<SourceEntry>();
	
	@Override
	public void setConfiguration(SourceAppenderConfiguration s) {
		this.ourConfig = s;
	}

	@Override
	public SourceAppenderConfiguration getConfiguration() {
		// TODO Auto-generated method stub
		return this.ourConfig;
	}

	@Override
	public SourceAppender append(String s) {
		// TODO Auto-generated method stub
		SourceEntry se = new SourceEntry(s);
		ourEntries.add(se);
		return this;
	}

	@Override
	public SourceAppender append(SourceEntry s) {
		// TODO Auto-generated method stub
		ourEntries.add(s);
		return this;
	}

	@Override
	public SourceAppender append(SourceAppender s) {
		// TODO Auto-generated method stub
		for(SourceEntry se : s.getEntries())
		{
			this.append(se);
		}
		return this;
	}

	@Override
	public Iterable<SourceEntry> getEntries()
	{
		return this.ourEntries;
	}
	
	
	
	private String repeatTab(int times)
	{
		String tab = ourConfig.translateSourceEntry(SourceEntry.TAB);
		
		String tabbed = "";
		for(int i = 0; i < times; i++){
			tabbed = tabbed + tab;
		}
		
		return tabbed;
	}
	
	
	@Override
	public String toSource() {
		// This is where the magic happens! :D
		int tabDepth = 0;
			
		StringBuilder sb = new StringBuilder();
		// In general: 
		// Each line is tabCharacter * tabDepth Times + LineofSource + lineBreak; 
		for(SourceEntry se : this.ourEntries)
		{
			if(se.equals(SourceEntry.BLOCK_END))
			{
				tabDepth--;
			}
			
			sb.append(repeatTab(tabDepth));
			sb.append(ourConfig.translateSourceEntry(se));
			
			if(!se.equals(SourceEntry.BREAK))
			{
				sb.append(ourConfig.translateSourceEntry(SourceEntry.BREAK));
			}
			
			if(se.equals(SourceEntry.BLOCK_BEGIN))
			{
				tabDepth++;
			}
					
			
			if(se.equals(SourceEntry.TAB))
			{
				throw new RuntimeException("Attempting to append tab; reconsider this behavior.");
			}			
		}
		
		return sb.toString();
	}
	
	@Override
	public int size()
	{
		return this.ourEntries.size();
	}
}
