package simpl.tutorials.java;

import ecologylab.serialization.annotations.simpl_other_tags;
import ecologylab.serialization.annotations.simpl_scalar;
import ecologylab.serialization.annotations.simpl_tag;

public class Book {

	@simpl_scalar
	String title; 
	
	@simpl_scalar
	String authorName;
	
	@simpl_scalar
	@simpl_tag("book_number")
	Integer bookId;
	
	public Book()
	{
	}
	
	public String getTitle()
	{
		return this.title;
	}
	
	public void setTitle(String value)
	{
		this.title = value;
	}
	
	public String getAuthorName()
	{
		return this.authorName;
	}
	
	public void setAuthorName(String value)
	{
		this.authorName = value;
	}
	
	public Integer getBookId()
	{
		return this.bookId;
	}
	
	public void setBookId(Integer value)
	{
		this.bookId = value;
	}
}
