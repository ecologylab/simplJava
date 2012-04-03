package ecologylab.translators.sql.testing.ecologylabXmlTest;

/**
This is a generated code. DO NOT edit or modify it.
 @author MetadataCompiler 

**/ 



import java.util.ArrayList;

//import ecologylab.semantics.generated.library.search.SearchResult;
import ecologylab.semantics.metadata.builtins.Document;
import ecologylab.semantics.metametadata.MetaMetadata;
import ecologylab.serialization.annotations.simpl_collection;
import ecologylab.serialization.annotations.simpl_inherit;


/**
	null
**/ 

@simpl_inherit
//rhema:  getting rid of SearchResult, I replaced SearchResult with document throughout
public class  AcmProceedingTest
extends  Document 
{

	@simpl_collection("search_result") private ArrayList<Document>	proceedings;
	@simpl_collection("search_result") private ArrayList<Document>	papers;

/**
	Constructor
**/ 

public AcmProceedingTest()
{
 super();
}

/**
	Constructor
**/ 

public AcmProceedingTest(MetaMetadata metaMetadata)
{
super(metaMetadata);
}

/**
	Lazy Evaluation for proceedings
**/ 

public  ArrayList<Document>	proceedings()
{
 ArrayList<Document>	result	=this.proceedings;
if(result == null)
{
result = new  ArrayList<Document>();
this.proceedings	=	 result;
}
return result;
}

/**
	Set the value of field proceedings
**/ 

public void setProceedings(  ArrayList<Document> proceedings )
{
this.proceedings = proceedings ;
}

/**
	Get the value of field proceedings
**/ 

public  ArrayList<Document> getProceedings(){
return this.proceedings;
}

/**
	Lazy Evaluation for papers
**/ 

public  ArrayList<Document>	papers()
{
 ArrayList<Document>	result	=this.papers;
if(result == null)
{
result = new  ArrayList<Document>();
this.papers	=	 result;
}
return result;
}

/**
	Set the value of field papers
**/ 

public void setPapers(  ArrayList<Document> papers )
{
this.papers = papers ;
}

/**
	Get the value of field papers
**/ 

public  ArrayList<Document> getPapers(){
return this.papers;
}



}

