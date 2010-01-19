/**
 * 
 */
package ecologylab.services.authentication.db;

/**
 * @author Zachary O. Toups (zach@ecologylab.net)
 * 
 */
public interface AuthenticationDBStrings
{
	/** End of an SQL statement that concludes with a string (includes single quotation mark). */
	static final String	STATEMENT_END_STRING											= "';";

	/** End of an SQL statement that concludes with a string inside a parenthesis. */
	static final String	STATEMENT_END_STRING_PAREN								= "');";

	/**
	 * 
	 */
	static final String	LIST_SEPARATOR_STRING_TYPE								= "', '";

	static final String	TABLE_USER																= "user";

	static final String	COL_UID																		= "uid";

	static final String	COL_EMAIL																	= "email";

	static final String	COL_PASSWORD															= "password";

	static final String	COL_NAME																	= "name";

	static final String	COL_LEVEL																	= "level";

	static final String	COL_ONLINE																= "online";

	static final String	COL_LAST_ONLINE														= "lastOnline";

	static final String	COL_LAST_ACTIVITY													= "lastActivity";

	static final String	COL_SESSION_ID														= "sessionId";

	static final String	INSERT_USER_PREFIX												= "INSERT INTO "
																																		+ TABLE_USER
																																		+ " ("
																																		+ COL_EMAIL
																																		+ ", "
																																		+ COL_PASSWORD
																																		+ ", "
																																		+ COL_NAME
																																		+ ") VALUES ('";

	static final String	SELECT_USER_COUNT_BY_EMAIL_PREFIX					= "SELECT COUNT(*) FROM "
																																		+ TABLE_USER
																																		+ " WHERE "
																																		+ COL_EMAIL
																																		+ " = '";

	static final String	SELECT_USER_BY_EMAIL_PREFIX								= "SELECT * FROM "
																																		+ TABLE_USER
																																		+ " WHERE "
																																		+ COL_EMAIL
																																		+ " = '";

	static final String	SELECT_USER_BY_SESSION_ID_PREFIX					= "SELECT * FROM "
																																		+ TABLE_USER
																																		+ " WHERE "
																																		+ COL_SESSION_ID
																																		+ " = '";

	static final String	SELECT_USER_LEVEL_BY_EMAIL_PREFIX					= "SELECT "
																																		+ COL_LEVEL
																																		+ " FROM "
																																		+ TABLE_USER
																																		+ " WHERE "
																																		+ COL_EMAIL
																																		+ " = '";

	static final String	DELETE_USER_BY_EMAIL_PREFIX								= "DELETE FROM "
																																		+ TABLE_USER
																																		+ " WHERE "
																																		+ COL_EMAIL
																																		+ " = '";

	static final String	LOGIN_USER_BY_EMAIL_PREFIX								= "UPDATE "
																																		+ TABLE_USER
																																		+ " SET "
																																		+ COL_ONLINE
																																		+ "=TRUE, "
																																		+ COL_LAST_ONLINE
																																		+ "=now(), "
																																		+ COL_LAST_ACTIVITY
																																		+ "=now(), "
																																		+ COL_SESSION_ID
																																		+ " = '";

	static final String	LOGIN_USER_BY_EMAIL_SESSION_WHERE_CLAUSE	= "' WHERE " + COL_EMAIL + "='";

	static final String	LOGOUT_USER_BY_EMAIL_PREFIX								= "UPDATE "
																																		+ TABLE_USER
																																		+ " SET "
																																		+ COL_ONLINE
																																		+ "=FALSE WHERE "
																																		+ COL_EMAIL
																																		+ "='";

	static final String	LOGOUT_USER_BY_SESSION_ID_PREFIX					= "UPDATE "
																																		+ TABLE_USER
																																		+ " SET "
																																		+ COL_ONLINE
																																		+ "=FALSE, "
																																		+ COL_SESSION_ID
																																		+ "=NULL WHERE "
																																		+ COL_SESSION_ID
																																		+ "='";

	static final String	SELECT_ALL_ONLINE_USERS										= "SELECT * FROM "
																																		+ TABLE_USER
																																		+ " WHERE "
																																		+ COL_ONLINE
																																		+ "=TRUE;";
}
