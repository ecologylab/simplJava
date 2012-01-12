/**
 * 
 */
package ecologylab.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Wraps a java.sql.PreparedStatement, tracking all of its open ResultSets. On close, automatically
 * closes any open ResultSets.
 * 
 * Used by ecologylab.sql.ConnectionWithAutoClose to wrap all prepareStatement() results.
 * 
 * TODO Ultimately, this class should implement java.sql.PreparedStatement, but for now, it only
 * implements a few select methods; adding others as needed should be straightforward.
 * 
 * @author Zachary O. Toups (zach@ecologylab.net)
 */
public class StatementWithAutoClose<S extends Statement>
{
	protected S	stmt;

	protected List<ResultSet>		results	= null;

	public StatementWithAutoClose(S stmt)
	{
		this.stmt = stmt;
	}

	public ResultSet executeQuery(String sql) throws SQLException
	{
		ResultSet rs = stmt.executeQuery(sql);

		results().add(rs);

		return rs;
	}

	public int executeUpdate(String sql) throws SQLException
	{
		return stmt.executeUpdate(sql);
	}

	protected List<ResultSet> results()
	{
		if (results == null)
		{
			synchronized (this)
			{
				if (results == null)
				{
					results = new LinkedList<ResultSet>();
				}
			}
		}

		return results;
	}

	/**
	 * Wraps PreparedStatement.close() and eats its exceptions. Before invoking stmt.close(), closes
	 * all open ResultSets that were returned by this.
	 */
	public void close()
	{
		if (results != null)
		{
			Iterator<ResultSet> resultSetIterator = results.iterator();

			while (resultSetIterator.hasNext())
			{
				ResultSet resultSet = resultSetIterator.next();
				resultSetIterator.remove();

				try
				{
					resultSet.close();
				}
				catch (SQLException e)
				{
				}
			}
		}

		try
		{
			stmt.close();
		}
		catch (SQLException e)
		{
		}

		stmt = null;
	}
}
