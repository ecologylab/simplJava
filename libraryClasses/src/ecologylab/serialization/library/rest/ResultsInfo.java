package ecologylab.serialization.library.rest;

import simpl.annotations.dbal.Hint;
import simpl.annotations.dbal.simpl_hints;
import simpl.annotations.dbal.simpl_scalar;
import ecologylab.serialization.ElementState;

public class ResultsInfo extends ElementState
{
	@simpl_scalar @simpl_hints(Hint.XML_LEAF)	protected int			totalNumResults;
	@simpl_scalar @simpl_hints(Hint.XML_LEAF)	protected int			numSkipped;
	@simpl_scalar @simpl_hints(Hint.XML_LEAF)	protected int			numReturned;
	
	public ResultsInfo() {}
	
	public ResultsInfo(int totalNumResults, int numSkipped, int numReturned)
	{
		this.setTotalNumResults(totalNumResults);
		this.setNumSkipped(numSkipped);
		this.setNumReturned(numReturned);
	}

	/**
	 * @param totalNumResults the totalNumResults to set
	 */
	public void setTotalNumResults(int totalNumResults)
	{
		this.totalNumResults = totalNumResults;
	}

	/**
	 * @return the totalNumResults
	 */
	public int getTotalNumResults()
	{
		return totalNumResults;
	}

	/**
	 * @param numSkipped the numSkipped to set
	 */
	public void setNumSkipped(int numSkipped)
	{
		this.numSkipped = numSkipped;
	}

	/**
	 * @return the numSkipped
	 */
	public int getNumSkipped()
	{
		return numSkipped;
	}

	/**
	 * @param numReturned the numReturned to set
	 */
	public void setNumReturned(int numReturned)
	{
		this.numReturned = numReturned;
	}

	/**
	 * @return the numReturned
	 */
	public int getNumReturned()
	{
		return numReturned;
	}
}
