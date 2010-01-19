/*
 * Created on Mar 30, 2006
 */
package ecologylab.services.authentication;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import ecologylab.generic.Debug;
import ecologylab.services.authentication.db.AuthenticationDBStrings;

/**
 * Abstracts access to a database as a list of AuthenticationEntry's. Raw passwords are never
 * serialized using this object, only one-way hashes of them (see
 * {@link ecologylab.services.authentication.AuthenticationListEntry AuthenticationListEntry}).
 * 
 * Instances of this should be used by a server to determine valid usernames and passwords.
 * 
 * Most methods in this class are synchronized, so that they cannot be interleaved on multiple
 * threads. This should prevent consistency errors.
 * 
 * Requires a user table with the following schema:
 * 
 * uid | int(11) | NO | MUL | NULL | auto_increment <br>
 * email | varchar(100) | NO | PRI | NULL <br>
 * password | varchar(100) | NO | - | NULL <br>
 * name | varchar(100) | NO | - | NULL <br>
 * level | int(11) | NO | - | 0 <br>
 * 
 * This authentication list assumes that authentication list entry's username field refers to an
 * email address.
 * 
 * @author Zachary O. Toups (zach@ecologylab.net)
 */
public class AuthenticationListDBImpl<E extends EmailAuthenticationListEntry> extends Debug implements
		AuthenticationList<E>, AuthenticationDBStrings
{
	/** Database connection. */
	private Connection						dBC																= null;

	/** Database connection string; combines URL, username, and password. */
	private String								dBConnectString										= null;

	/**
	 * Creates a new AuthenticationList based on a connection to a MySQL database. Lazily instantiates
	 * the database connection as needed.
	 * 
	 * @param dbLocation
	 *          URL for database in the form "mysql://...".
	 * @param username
	 *          username to connect to database.
	 * @param password
	 *          password for database.
	 */
	public AuthenticationListDBImpl(String dbLocation, String username, String password, String db)
	{
		super();

		dBConnectString = "jdbc:"
				+ dbLocation
				+ "/"
				+ db
				+ "?user="
				+ username
				+ "&password="
				+ password;
	}

	protected Connection connection() throws SQLException
	{
		if (dBC == null)
		{
			synchronized (this)
			{
				if (dBC == null)
				{
					try
					{
						Class.forName("com.mysql.jdbc.Driver").newInstance();
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}

					dBC = DriverManager.getConnection(dBConnectString);
				}
			}
		}

		return dBC;
	}

	/**
	 * Adds the given entry to this.
	 */
	public synchronized boolean addEntry(E entry)
	{
		if (!this.contains(entry.getEmail()))
		{
			this.addUser(entry.getEmail(), entry.getPassword(), entry.getUsername());

			return true;
		}

		return false;
	}

	/**
	 * Inserts the information for a new user into the database. Does not check to see if the user
	 * exists; this should be done prior to calling this method.
	 * 
	 * @param email
	 * @param password
	 * @param name
	 * @return
	 */
	protected synchronized void addUser(String email, String password, String name)
	{
		String insertUser = INSERT_USER_PREFIX
				+ email
				+ LIST_SEPARATOR_STRING_TYPE
				+ password
				+ LIST_SEPARATOR_STRING_TYPE
				+ name
				+ STATEMENT_END_STRING_PAREN;

		Statement stmt = null;

		try
		{
			stmt = connection().createStatement();
			stmt.execute(insertUser);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (stmt != null)
			{
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
	}

	/**
	 * Checks to see if this contains the given username; returns true if it does.
	 * 
	 * @param username
	 * @return
	 */
	public synchronized boolean contains(String email)
	{
		String selectUser = SELECT_USER_COUNT_BY_EMAIL_PREFIX + email + STATEMENT_END_STRING;

		Statement stmt = null;
		ResultSet rs = null;
		boolean userExists = false;

		debug(selectUser);

		try
		{
			stmt = connection().createStatement();
			rs = stmt.executeQuery(selectUser);
			rs.next();

			userExists = rs.getInt(1) > 0;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (rs != null)
			{
				try
				{
					rs.close();
				}
				catch (SQLException e)
				{

				}

				rs = null;
			}

			if (stmt != null)
			{
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

		debug("user exists? " + userExists);

		return userExists;
	}

	/**
	 * Cloning AuthenticationLists is not allowed, because it is a security violation.
	 * 
	 * This method just throws an UnsupportedOperationException.
	 */
	@Override
	public final Object clone() throws UnsupportedOperationException
	{
		throw new UnsupportedOperationException(
				"Cannot clone an AuthenticationList, for security reasons.");
	}

	/**
	 * Checks to see if this contains the username given in entry; returns true if it does.
	 * 
	 * @param entry
	 * @return
	 */
	public synchronized boolean contains(E entry)
	{
		return this.contains(entry.getEmail());
	}

	/**
	 * Retrieves the access level for the given entry.
	 * 
	 * @param entry
	 * @return
	 */
	public synchronized int getAccessLevel(E entry)
	{
		String email = entry.getEmail();

		if (this.contains(email))
		{
			String selectUser = SELECT_USER_LEVEL_BY_EMAIL_PREFIX + email + STATEMENT_END_STRING;

			Statement stmt = null;
			ResultSet rs = null;
			int level = -1;

			try
			{
				stmt = connection().createStatement();
				rs = stmt.executeQuery(selectUser);
				rs.next();

				level = rs.getInt(COL_LEVEL);
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
			finally
			{
				if (rs != null)
				{
					try
					{
						rs.close();
					}
					catch (SQLException e)
					{

					}

					rs = null;
				}

				if (stmt != null)
				{
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

			return level;
		}

		return -1;
	}

	/**
	 * Checks entry against the entries contained in this. Verifies that the username exists, and the
	 * password matches; returns true if both are true.
	 * 
	 * @param entry
	 * @return
	 */
	public synchronized boolean isValid(E entry)
	{
		return (this.contains(entry.getEmail()) && entry.getPassword() != null && entry
				.compareHashedPassword(this.retrievePassword(entry.getEmail())));
	}

	/**
	 * Looks up the hashed password in the database and returns it.
	 * 
	 * @param email
	 * @return
	 */
	private String retrievePassword(String email)
	{
		String selectUser = SELECT_USER_BY_EMAIL_PREFIX + email + STATEMENT_END_STRING;

		// debug("MySQL statement: "+selectUser);

		Statement stmt = null;
		ResultSet rs = null;
		String password = null;

		try
		{
			stmt = connection().createStatement();
			rs = stmt.executeQuery(selectUser);
			rs.next();

			password = rs.getString(COL_PASSWORD);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (rs != null)
			{
				try
				{
					rs.close();
				}
				catch (SQLException e)
				{

				}

				rs = null;
			}

			if (stmt != null)
			{
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

		return password;
	}

	/**
	 * Attempts to remove the given object; this will succeed if and only if the following are true:
	 * 
	 * 1.) the Object is of type AuthenticationListEntry 2.) this list contains the
	 * AuthenticationListEntry 3.) the AuthenticationListEntry's username and password both match the
	 * one in this list
	 * 
	 * @param entry
	 *          the AuthenticationListEntry (username / password) to attempt to remove.
	 */
	public synchronized boolean remove(E entry)
	{
		if (this.isValid(entry))
		{
			String deleteUser = DELETE_USER_BY_EMAIL_PREFIX
					+ entry.getEmail()
					+ STATEMENT_END_STRING_PAREN;

			Statement stmt = null;

			try
			{
				stmt = connection().createStatement();
				stmt.execute(deleteUser);
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
			finally
			{
				if (stmt != null)
				{
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

			return true;
		}

		return false;
	}

	/**
	 * Returns a String indicating the number of entries in the AuthenticationList.
	 */
	@Override
	public String toString()
	{
		try
		{
			if (this.connection() != null)
				return "DBAuthenticationList connected.";
			else
				return "DBAuthenticationList unable to connect.";
		}
		catch (SQLException e)
		{
			e.printStackTrace();

			return "DBAuthenticationList unable to connect.";
		}
	}
}
