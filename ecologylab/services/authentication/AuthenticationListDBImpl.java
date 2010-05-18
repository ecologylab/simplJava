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
import ecologylab.services.exceptions.SaveFailedException;

/**
 * Abstracts access to a database as a list of AuthenticationEntry's. Raw passwords are never
 * serialized using this object, only one-way hashes of them (see
 * {@link ecologylab.services.authentication.User AuthenticationListEntry}).
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
public class AuthenticationListDBImpl<E extends UserWithEmail> extends Debug implements AuthenticationDBStrings
//AuthenticationList<E>,
//FIXME: Zach how does this change ? 
{
	/** Database connection string; combines URL, username, and password. */
	private String	dBConnectString	= null;

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

	public AuthenticationListDBImpl()
	{
		super();
	}

	protected Connection connection() throws SQLException
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return DriverManager.getConnection(dBConnectString);
	}

	/**
	 * Adds the given entry to this.
	 * 
	 * @throws SaveFailedException
	 */
	public synchronized boolean addUser(E entry) throws SaveFailedException
	{
		if (!this.contains(entry))
		{
			entry.setUid(this
					.performInsertUser(entry.getEmail(), entry.getPassword(), entry.getUserKey()));

			return true;
		}

		return false;
	}

	/**
	 * Inserts the information for a new user into the database. Does not check to see if the user
	 * exists; this should be done prior to calling this method.
	 * 
	 * Returns the auto-increment UID for the user; this should be attached to the
	 * AuthenticationListEntry that triggered the call to addUser.
	 * 
	 * @param email
	 * @param password
	 * @param name
	 * @return the auto-increment UID for the user. -1 if there was an error.
	 * @throws SaveFailedException
	 */
	protected synchronized long performInsertUser(String email, String password, String userKey)
			throws SaveFailedException
	{
		String insertUser = INSERT_USER_PREFIX
				+ userKey
				+ LIST_SEPARATOR_STRING_TYPE
				+ password
				+ LIST_SEPARATOR_STRING_TYPE
				+ email
				+ STATEMENT_END_STRING_PAREN;

		Statement stmt = null;

		long uid = -1;

		Connection connection = null;

		try
		{
			connection = connection();
		}
		catch (SQLException e1)
		{
			e1.printStackTrace();

			throw new SaveFailedException(e1);
		}

		try
		{
			stmt = connection.createStatement();
			stmt.execute(insertUser, Statement.RETURN_GENERATED_KEYS);

			ResultSet autoGenKeys = stmt.getGeneratedKeys();

			if (autoGenKeys.next())
				uid = autoGenKeys.getLong(1);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new SaveFailedException("SQLException occurred when attempting to add user.", e);
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

			try
			{
				connection.close();
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
		}

		return uid;
	}

	/**
	 * Looks up the given user in the database and constructs a UserWithEmail object from the data.
	 * Does not populate the password field of the UserWithEmail object.
	 * 
	 * @param userKey
	 * @return
	 */
	protected synchronized UserWithEmail retrieveUserFromDB(String userKey)
	{
		String selectUser = SELECT_USER_BY_USER_KEY_PREFIX + userKey + STATEMENT_END_STRING;
		debug(selectUser);

		Statement stmt = null;
		ResultSet rs = null;

		UserWithEmail foundUser = null;

		Connection connection = null;

		try
		{
			connection = connection();
		}
		catch (SQLException e1)
		{
			e1.printStackTrace();

			return null;
		}

		try
		{
			stmt = connection.createStatement();
			rs = stmt.executeQuery(selectUser);

			if (rs.next())
			{
				foundUser = new UserWithEmail(rs.getString(COL_USER_KEY), null, rs.getString(COL_EMAIL));
				foundUser.setLevel(rs.getInt(COL_LEVEL));
				foundUser.setUid(rs.getLong(COL_UID));
			}
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

			try
			{
				connection.close();
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
		}

		return foundUser;
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
		return this.retrieveUserFromDB(entry.getUserKey()) != null;
	}

	/**
	 * Retrieves the access level for the given entry.
	 * 
	 * @param entry
	 * @return
	 */
	public synchronized int getAccessLevel(E entry)
	{
		UserWithEmail foundUser = this.retrieveUserFromDB(entry.getUserKey());

		if (foundUser != null)
			return foundUser.getLevel();

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
		UserWithEmail foundUser = this.retrieveUserFromDB(entry.getUserKey());

		return ((foundUser != null) && (entry.getPassword() != null) && entry
				.compareHashedPassword(this.retrievePassword(entry.getUserKey())));
	}

	/**
	 * Looks up the hashed password in the database and returns it.
	 * 
	 * @param email
	 * @return
	 */
	private String retrievePassword(String userKey)
	{
		String selectUser = SELECT_USER_BY_USER_KEY_PREFIX + userKey + STATEMENT_END_STRING;

		Statement stmt = null;
		ResultSet rs = null;
		String password = null;

		Connection connection = null;

		try
		{
			connection = connection();
		}
		catch (SQLException e1)
		{
			e1.printStackTrace();

			return null;
		}

		try
		{
			stmt = connection.createStatement();
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

			try
			{
				connection.close();
			}
			catch (SQLException e)
			{
				e.printStackTrace();
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
	 * @throws SaveFailedException
	 */
	public synchronized boolean removeUser(E entry) throws SaveFailedException
	{
		if (this.isValid(entry))
		{
			String deleteUser = DELETE_USER_BY_USER_KEY_PREFIX
					+ entry.getUserKey()
					+ STATEMENT_END_STRING_PAREN;

			Statement stmt = null;

			Connection connection = null;

			try
			{
				connection = connection();
			}
			catch (SQLException e1)
			{
				e1.printStackTrace();

				throw new SaveFailedException(e1);
			}

			try
			{
				stmt = connection.createStatement();
				stmt.execute(deleteUser);
			}
			catch (SQLException e)
			{
				e.printStackTrace();
				throw new SaveFailedException("SQLException occurred when attempting to remove user.", e);
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

				try
				{
					connection.close();
				}
				catch (SQLException e)
				{
					e.printStackTrace();
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

	/**
	 * @see ecologylab.services.authentication.AuthenticationList#setUID(ecologylab.services.authentication.User)
	 */
	public void setUID(E entry)
	{
		UserWithEmail foundUser = this.retrieveUserFromDB(entry.getUserKey());
		entry.setUid(foundUser.getUid());
	}

	/**
	 * This method does nothing, as all of the add / remove methods are automatically written to the
	 * backing store (database) when they are called.
	 */
	public void save() throws SaveFailedException
	{
	}
}
