/*
 * Created on Oct 31, 2006
 */
package ecologylab.services.authentication;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import ecologylab.services.authentication.db.AuthenticationDBStrings;

/**
 * Encapsulates all authentication actions (tracking who is online, etc.), so that Servers don't
 * need to. Requires a backend database of users with passwords (an AuthenticationList).
 * 
 * Database implementation.
 * 
 * @author Zachary O. Toups (zach@ecologylab.net)
 */
public class OnlineAuthenticatorDBImpl<E extends EmailAuthenticationListEntry> extends
		AuthenticationListDBImpl<E> implements OnlineAuthenticator<E>, AuthenticationDBStrings
{
	/**
	 * Creates a new OnlineAuthenticatorDBImpl based on a connection to a MySQL database. Lazily
	 * instantiates the database connection as needed.
	 * 
	 * @param dbLocation
	 *          URL for database in the form "mysql://...".
	 * @param username
	 *          username to connect to database.
	 * @param password
	 *          password for database.
	 */
	public OnlineAuthenticatorDBImpl(String dbLocation, String username, String password, String db)
	{
		super(dbLocation, username, password, db);
	}

	/**
	 * @see ecologylab.services.authentication.OnlineAuthenticator#login(A, java.lang.String)
	 */
	public boolean login(E entry, String sessionId)
	{
		System.out.println("*****************************************");
		System.out.println("entry: " + entry.toString());

		boolean loggedInSuccessfully = false;

		// first see if the username exists
		if (entry != null)
		{
			// check password
			if (super.isValid(entry))
			{
				// regardless of whether the user is already online, update their information in the
				// database.
				// mark login successful
				loggedInSuccessfully = true;

				this.performLoginOrLogoutOnDB(entry.getEmail(), sessionId, true);
			}
			else
			{
				debug("invalid entry");
			}
		}
		else
		{
			debug("<null> attempted login.");
			loggedInSuccessfully = false;
		}

		return loggedInSuccessfully;
	}

	/**
	 * @see ecologylab.services.authentication.OnlineAuthenticator#lookupUserLevel(A)
	 */
	public int lookupUserLevel(E entry)
	{
		if (super.isValid(entry))
		{
			return super.getAccessLevel(entry);
		}
		else
		{
			return -1;
		}
	}

	/**
	 * Returns the list of email addresses of users currently logged-in to the system.
	 * 
	 * @see ecologylab.services.authentication.OnlineAuthenticator#usersLoggedIn(A)
	 */
	public Set<String> usersLoggedIn(E administrator)
	{
		if (this.lookupUserLevel(administrator) >= AuthLevels.ADMINISTRATOR)
		{
			return this.performLookupOnlineUsersInDB();
		}
		else
		{
			return null;
		}
	}

	/**
	 * @see ecologylab.services.authentication.OnlineAuthenticator#logout(A, java.lang.String)
	 */
	public boolean logout(E entry, String sessionId)
	{
		try
		{
			if (entry.getEmail().equals(this.performLookupEmailInDB(sessionId)))
			{
				this.performLoginOrLogoutOnDB(entry.getEmail(), sessionId, false);
				return true;
			}
			else
			{
				return false;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * @see ecologylab.services.authentication.OnlineAuthenticator#isLoggedIn(java.lang.String)
	 */
	public boolean isLoggedIn(String email)
	{
		return (this.performIsOnlineDB(email));
	}

	/**
	 * @see ecologylab.services.authentication.OnlineAuthenticator#getSessionId(ecologylab.services.authentication.AuthenticationListEntry)
	 */
	@Override
	public String getSessionId(E entry)
	{
		return performLookupSessionIdDB(entry.getEmail());
	}

	/**
	 * @see ecologylab.services.authentication.OnlineAuthenticator#isLoggedIn(ecologylab.services.authentication.AuthenticationListEntry)
	 */
	@Override
	public boolean isLoggedIn(E entry)
	{
		return performIsOnlineDB(entry.getEmail());
	}

	/**
	 * @see ecologylab.services.authentication.OnlineAuthenticator#logoutBySessionId(java.lang.String)
	 */
	@Override
	public void logoutBySessionId(String sessionId)
	{
		this.performLogoutOnDB(sessionId);
	}

	/**
	 * @see ecologylab.services.authentication.OnlineAuthenticator#sessionValid(java.lang.String)
	 */
	@Override
	public boolean sessionValid(String sessionId)
	{
		return this.performLookupEmailInDB(sessionId) != null;
	}

	/**
	 * @param email
	 * @return true if the email address is online; false if it is offline or does not exist
	 */
	private boolean performIsOnlineDB(String email)
	{
		String selectUser = SELECT_USER_BY_EMAIL_PREFIX + email + STATEMENT_END_STRING;

		Statement stmt = null;
		ResultSet rs = null;
		boolean isOnline = false;

		try
		{
			stmt = connection().createStatement();
			rs = stmt.executeQuery(selectUser);

			if (rs.next())
				isOnline = rs.getBoolean(COL_ONLINE);
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

		return isOnline;
	}

	private synchronized Set<String> performLookupOnlineUsersInDB()
	{
		Statement stmt = null;
		ResultSet rs = null;

		Set<String> onlineUsers = new HashSet<String>();

		try
		{
			stmt = connection().createStatement();
			rs = stmt.executeQuery(SELECT_ALL_ONLINE_USERS);

			while (rs.next())
			{
				onlineUsers.add(rs.getString(COL_EMAIL));
			}
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

		return onlineUsers;

	}

	/**
	 * @param email
	 */
	private void performLoginOrLogoutOnDB(String email, String sessionId, boolean login)
	{
		String updateUser = (login ? (LOGIN_USER_BY_EMAIL_PREFIX + sessionId + LOGIN_USER_BY_EMAIL_SESSION_WHERE_CLAUSE)
				: LOGOUT_USER_BY_EMAIL_PREFIX)
				+ email
				+ STATEMENT_END_STRING;

		debug("update user command: "+updateUser);
		
		Statement stmt = null;

		try
		{
			stmt = connection().createStatement();
			stmt.execute(updateUser);
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
	 * @param email
	 */
	private void performLogoutOnDB(String sessionId)
	{
		String updateUser = LOGOUT_USER_BY_SESSION_ID_PREFIX + sessionId + STATEMENT_END_STRING_PAREN;

		Statement stmt = null;

		try
		{
			stmt = connection().createStatement();
			stmt.execute(updateUser);
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
	 * @param email
	 * @return the current session id for the given email; null if email is not in system or if user
	 *         is offline
	 */
	private synchronized String performLookupSessionIdDB(String email)
	{
		Statement stmt = null;
		ResultSet rs = null;

		String sessionId = null;

		try
		{
			stmt = connection().createStatement();
			rs = stmt.executeQuery(SELECT_USER_BY_EMAIL_PREFIX + email + STATEMENT_END_STRING);

			if (rs.next())
				sessionId = rs.getString(COL_SESSION_ID);
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

		return sessionId;
	}

	/**
	 * @param email
	 * @return the email address for the given session identifier, or null if there is no session with
	 *         that identifier
	 */
	private synchronized String performLookupEmailInDB(String sessionId)
	{
		Statement stmt = null;
		ResultSet rs = null;

		String email = null;

		try
		{
			stmt = connection().createStatement();
			rs = stmt.executeQuery(SELECT_USER_BY_SESSION_ID_PREFIX + sessionId + STATEMENT_END_STRING);

			if (rs.next())
				email = rs.getString(COL_EMAIL);
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

		return email;
	}
}
