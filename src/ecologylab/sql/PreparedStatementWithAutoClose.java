/**
 * 
 */
package ecologylab.sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
public class PreparedStatementWithAutoClose extends StatementWithAutoClose<PreparedStatement>
{
	public PreparedStatementWithAutoClose(PreparedStatement stmt)
	{
		super(stmt);
	}

	public ResultSet executeQuery() throws SQLException
	{
		ResultSet rs = stmt.executeQuery();

		results().add(rs);

		return rs;
	}

	public int executeUpdate() throws SQLException
	{
		return stmt.executeUpdate();
	}

	/**
	 * @param parameterIndex
	 * @param x
	 * @throws SQLException
	 */
	public void setLong(int parameterIndex, long x) throws SQLException
	{
		this.stmt.setLong(parameterIndex, x);
	}

	/**
	 * @param parameterIndex
	 * @param x
	 * @throws SQLException
	 */
	public void setInt(int parameterIndex, int x) throws SQLException
	{
		this.stmt.setInt(parameterIndex, x);
	}

	/**
	 * @param parameterIndex
	 * @param x
	 * @throws SQLException
	 */
	public void setString(int parameterIndex, String x) throws SQLException
	{
		this.stmt.setString(parameterIndex, x);
	}

	/**
	 * @return
	 * @throws SQLException 
	 */
	public ResultSet getGeneratedKeys() throws SQLException
	{
		ResultSet rs = this.stmt.getGeneratedKeys();
		results().add(rs);

		return rs;
	}
}
