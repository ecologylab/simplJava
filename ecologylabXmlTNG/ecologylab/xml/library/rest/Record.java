package ecologylab.xml.library.rest;

import ecologylab.xml.ElementState;

public class Record extends ElementState
{
	@xml_leaf	protected	int		position;
	@xml_leaf	protected	double	score;
	@xml_nested protected	Header	header;
	@xml_nested protected 	Fields	fields;
	
	public Record() {}
	
	public Record(int position, double score, Header header, Fields fields)
	{
		this.position 	= position;
		this.score		= score;
		this.header		= header;
		this.fields		= fields;
	}
	
	/**
	 * @param fields the fields to set
	 */
	public void setFields(Fields fields)
	{
		this.fields = fields;
	}
	/**
	 * @return the fields
	 */
	public Fields getFields()
	{
		return fields;
	}
}
