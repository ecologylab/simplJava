package ecologylab.xml;

/**
 * There are certain rules one has to follow while using this framework for translation
 * from Java to XML and back. This exception class enforces those rules on the user. So,
 * if the user fails to follow any of the rule, this exception is thrown with appropriate
 * error message encapsulated in it. This exception is thrown in the following conditions:
 * <ul>
 * <li>
 * If the user is translating a class which has some Collection type object in it 
 * (e.g Vector, Hashtable)and the class does not overrides the methods 
 * <code>getCollection()</code> and <code>addElement()</code> inherited from 
 * <code>ElementState.</code>
 * </li>
 * <li>
 * If the user is translating a class which has some Collection type object in it and the
 * collection does not contain items of objects derived from <code>ElementState</code> type.
 * </li>
 * <li>
 * The classes to be translated does not provide a constructor with zero arguments. In this
 * case the exception is thrown only when the user is building Java classes from xml.
 * </li>
 * <li>
 * The class to be translated does not provide setter method for setting the values of the
 * primitive type variables which are translated. In this case the exception is thrown only 
 * when the user is building Java classes from xml.
 * </li>
 * </ul> 
 * @author      Andruid Kerne
 * @author      Madhur Khandelwal
 * @version     0.5
 */

public class XmlTranslationException extends Exception 
{
	public XmlTranslationException()
	{
		super();
	}
	
	public XmlTranslationException(String msg)
	{
		super(msg);
	}
}
