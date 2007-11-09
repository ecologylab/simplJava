/**
 * 
 */
package ecologylab.services.authentication;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.xml.transform.stream.StreamResult;

import ecologylab.xml.ElementState;
import ecologylab.xml.XMLTools;
import ecologylab.xml.XMLTranslationException;

/**
 * This program allows users to create and modify AuthenticationList files so
 * that they do not have to be stored as plaintext.
 * 
 * @author toupsz
 * 
 */
public class AuthListAdmin
{

    /**
     * @param args
     * @throws IOException
     * @throws XMLTranslationException
     */
    public static void main(String[] args) throws IOException,
            SecurityException, XMLTranslationException
    {
        if (args.length < 1)
        {
            System.out
                    .println("You must supply at least one parameter. Parameter list is as follows:");
            System.out
                    .println("AuthListAdmin <authentication list filename> <administrator username> <new users file> | <new username>");
            System.exit(1);
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String resp = "";
        String filename = args[0];
        boolean newFile = false;
        AuthenticationListEntry userEntry;
        boolean loggedIn = false;

        // first we need to open up the authentcation list
        AuthenticationList authList = null;

        File xmlFile = new File(filename);

        if (xmlFile.exists())
        {
            try
            {
                authList = (AuthenticationList) ElementState.translateFromXML(
                        xmlFile, AuthenticationTranslations.get());
            }
            catch (XMLTranslationException e)
            {
                System.err
                        .println("There was an error translating the authentication list: "
                                + filename);

                e.printStackTrace();
            }
        }
        else
        {
            // no file with that name, ask if user wants to create
            while (resp.length() < 1
                    || (resp.charAt(0) != 'y' && resp.charAt(0) != 'n'))
            {
                System.out
                        .println("The file "
                                + filename
                                + " that you specified does not exist, would you like to create a new authentication list with this filename (Y/N)?");

                resp = br.readLine();

                resp = resp.toLowerCase();
            }

            if (resp.charAt(0) == 'n')
            {
                System.out.println("No file created.");
                System.exit(1);
            }
            else
            {
                System.out.println("Creating file....");
                boolean success;

                success = xmlFile.createNewFile();

                if (!success)
                {
                    System.out.println("File exists somehow.");
                    System.exit(1);
                }
                else
                {
                    System.out.println("...file created.");
                    newFile = true;
                }
            }

            // now the file exists, because we'd have exited by now. We also
            // don't yet have an authlist object
            authList = new AuthenticationList();
        }

        if (authList != null)
        {
            System.out.println("authentication list created.");
            // the user must now authenticate
            String username = null;
            if (args[1] != null)
            {
                username = args[1];
            }

            while (!loggedIn)
            {
                // we need a username
                if (!newFile)
                {
                    System.out
                            .println("Please supply a username and password that has administrator privileges for this file (not the computer containing this file):");
                    username = br.readLine();
                }
                else if (username == null)
                {
                    System.out
                            .println("Please supply a username that will be an administrator user for this file:");
                    username = br.readLine();
                }

                String password = null;

                while (password == null)
                {
                    System.out.println("Enter password: ");

                    password = br.readLine();

                    if (newFile)
                    {
                        System.out.println("Re-enter password: ");

                        String secondPass = br.readLine();

                        if (!secondPass.equals(password))
                        {
                            password = null;
                            System.out
                                    .println("Passwords do not match, try again.");
                        }
                    }
                }

                userEntry = new AuthenticationListEntry(username, password);

                // have password and username!
                if (!newFile
                        && authList.isValid(userEntry)
                        && authList.getAccessLevel(userEntry) == AuthLevels.ADMINISTRATOR)
                {
                    // user is valid!
                    loggedIn = true;
                }
                else if (newFile)
                {
                    loggedIn = true;
                    System.out.println("adding administrator " + username);

                    userEntry.setLevel(AuthLevels.ADMINISTRATOR);

                    authList.add(userEntry);
                }
            }

            // now logged in and ready to make changes!
            boolean doneEntering = false;

            while (!doneEntering)
            {
                System.out.println("enter user name to add (\\q to quit): ");
                String newUser = br.readLine();

                if ("\\q".equals(newUser))
                {
                    doneEntering = true;
                }

                if (!newUser.contains("/") && !newUser.contains("\\"))
                {
                    String password = null;

                    while (password == null)
                    {
                        System.out.println("Enter password (\\q to quit): ");

                        password = br.readLine();

                        if ("\\q".equals(password))
                        {
                            System.out.println("Username " + newUser
                                    + " not added.");

                            doneEntering = true;
                            break;
                        }

                        System.out.println("Re-enter password: ");

                        String secondPass = br.readLine();

                        if (!secondPass.equals(password))
                        {
                            password = null;
                            System.out
                                    .println("Passwords do not match, try again.");
                        }
                    }

                    if (username != null && password != null)
                    {
                        System.out.println("adding user: " + newUser);

                        authList.add(new AuthenticationListEntry(newUser,
                                password));
                    }
                }
                else
                {
                    System.out
                            .println("Error in username; cannot contain / or \\.");
                }
            }

            System.out.println("Saving file: " + filename);

            XMLTools.writePrettyXML(authList.translateToDOM(), xmlFile);

            System.out.println("Finished.");
        }
        else
        {
            System.err.println("There was an error: " + filename);
        }
    }
}
