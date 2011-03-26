package ecologylab.serialization.library.bibtex;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import ecologylab.net.ParsedURL;
import ecologylab.serialization.ElementState;
import ecologylab.serialization.ElementState.bibtex_type;
import ecologylab.serialization.SIMPLTranslationException;

@bibtex_type("article")
public class Entry extends ElementState
{
	@bibtex_key
	@simpl_scalar
	private String						citationKey;

	@bibtex_tag("title")
	@simpl_scalar
	private String						title;

	@bibtex_tag("authors")
	@simpl_nowrap
	@simpl_collection("author")
	private ArrayList<String>	authors;

	@bibtex_tag("journals")
	@simpl_scalar
	private String						journal;

	@bibtex_tag("volume")
	@simpl_scalar
	private Integer						volume;

	@bibtex_tag("issue")
	@simpl_scalar
	private Integer						issue;

	@bibtex_tag("month")
	@simpl_scalar
	private String						month;

	@bibtex_tag("year")
	@simpl_scalar
	private String						year;

	@bibtex_tag("issn")
	@simpl_scalar
	private String						issn;

	@bibtex_tag("pages")
	@simpl_scalar
	private String						pages;

	@bibtex_tag("numPages")
	@simpl_scalar
	private String						numPages;

	@bibtex_tag("url")
	@simpl_scalar
	private ParsedURL					url;

	@bibtex_tag("acmid")
	@simpl_scalar
	private String						acmid;

	@bibtex_tag("publisher")
	@simpl_scalar
	private String						publisher;

	@bibtex_tag("address")
	@simpl_scalar
	private String						address;

	@bibtex_tag("keywords")
	@simpl_collection("keywords")
	private ArrayList<String>	keywords;

	public Entry()
	{

	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getTitle()
	{
		return title;
	}

	public void setAuthors(ArrayList<String> authors)
	{
		this.authors = authors;
	}

	public ArrayList<String> getAuthors()
	{
		return authors;
	}

	public void setJournal(String journal)
	{
		this.journal = journal;
	}

	public String getJournal()
	{
		return journal;
	}

	public void setVolume(Integer volume)
	{
		this.volume = volume;
	}

	public Integer getVolume()
	{
		return volume;
	}

	public void setIssue(Integer issue)
	{
		this.issue = issue;
	}

	public Integer getIssue()
	{
		return issue;
	}

	public void setMonth(String month)
	{
		this.month = month;
	}

	public String getMonth()
	{
		return month;
	}

	public void setYear(String year)
	{
		this.year = year;
	}

	public String getYear()
	{
		return year;
	}

	public void setPages(String pages)
	{
		this.pages = pages;
	}

	public String getPages()
	{
		return pages;
	}

	public void setNumPages(String numPages)
	{
		this.numPages = numPages;
	}

	public String getNumPages()
	{
		return numPages;
	}

	public void setUrl(ParsedURL url)
	{
		this.url = url;
	}

	public ParsedURL getUrl()
	{
		return url;
	}

	public void setAcmid(String acmid)
	{
		this.acmid = acmid;
	}

	public String getAcmid()
	{
		return acmid;
	}

	public void setPublisher(String publisher)
	{
		this.publisher = publisher;
	}

	public String getPublisher()
	{
		return publisher;
	}

	public void setAddress(String address)
	{
		this.address = address;
	}

	public String getAddress()
	{
		return address;
	}

	public void setKeywords(ArrayList<String> keywords)
	{
		this.keywords = keywords;
	}

	public ArrayList<String> getKeywords()
	{
		return keywords;
	}

	public void setIssn(String issn)
	{
		this.issn = issn;
	}

	public String getIssn()
	{
		return issn;
	}
	
	public void setCitationKey(String citationKey)
	{
		this.citationKey = citationKey;
	}

	public String getCitationKey()
	{
		return citationKey;
	}

	public static void main(String args[]) throws SIMPLTranslationException, MalformedURLException
	{		
		/*		  
		   @article{Yu:1995:ECK:627296.627650,
			 author = {Yu, Sheng-Ke},
			 title = {Errata: Comments on 'Knowledge Representation Using Fuzzy Petri Nets'},
			 journal = {IEEE Trans. on Knowl. and Data Eng.},
			 volume = {7},
			 issue = {1},
			 month = {February},
			 year = {1995},
			 issn = {1041-4347},
			 pages = {190--192},
			 numpages = {3},
			 url = {http://portal.acm.org/citation.cfm?id=627296.627650},
			 acmid = {627650},
			 publisher = {IEEE Educational Activities Department},
			 address = {Piscataway, NJ, USA},
			 keywords = {Fuzzy Petri Net, fuzzy production rule, fuzzy reasoning, immediate reachability set, knowledge representation, reachability set.},
			}
		 */
		
		Entry e = new Entry();

		ArrayList<String> authors = new ArrayList<String>();
		ArrayList<String> keywords = new ArrayList<String>();

		authors.add("Yu");
		authors.add("Sheng-Ke");

		keywords.add("Fuzzy Petri Nets");
		keywords.add("fuzzy production rule");
		keywords.add("fuzzy reasoning");
		keywords.add("immediate reachability set");
		keywords.add("knowledge representation");
		keywords.add("reachability set");

		e.setCitationKey("Yu:1995:ECK:627296.627650");
		e.setAuthors(authors);
		e.setTitle("Errata: Comments on Knowledge Representation Using Fuzzy Petri Nets");
		e.setJournal("IEEE Trans. on Knowl. and Data Eng.");
		e.setVolume(1);
		e.setIssue(1);
		e.setMonth("Feburary");
		e.setYear("1995");
		e.setIssn("1041-4347");
		e.setPages("190--192");
		e.setNumPages("3");
		e.setUrl(new ParsedURL(new URL("http://portal.acm.org/citation.cfm?id=627296.627650")));
		e.setAcmid("627650");
		e.setPublisher("IEEE Educational Activities Department");
		e.setAddress("Piscataway, NJ, USA");
		e.setKeywords(keywords);

		e.serialize(System.out, FORMAT.BIBTEX);
	}



}
