/**
 * 
 */
package ecologylab.generic;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Tools for manipulating org.w3c.documents
 * 
 * @author andruid
 */
public class DomTools extends Debug
{
	/**
	 * Print your DOM tree in a readable way.
	 * 
	 * @param node
	 */
  public static void prettyPrint(Node node) 
  {
  	prettyPrint(node, 0);
  }
  private static void prettyPrint(Node node, int level) 
  {
    try 
    {
    	for (int i=0; i< level; i++)
    		System.out.print('\t');
    	
      System.out.print("<" + node.getNodeName());
      NamedNodeMap attrMap = node.getAttributes();
      if (attrMap != null)
	      for (int i = 0; i < attrMap.getLength(); i++) 
	      {
	        Node attr = attrMap.item(i);
	        String attrName = attr.getNodeName();
					System.out.print(" " + attrName + "=\"" + attr.getNodeValue() + '"');
	      }
      String value	= node.getNodeValue();
      if (value != null)
      	System.out.print(value);
      System.out.print(">");

      NodeList nl = node.getChildNodes();
      if (nl != null)
      {
	      int numChildren = nl.getLength();
	      if (numChildren > 0)
	      {
	        System.out.print("\n");
					for (int i = 0; i < numChildren; i++) 
		      {
		        Node childNode = nl.item(i);
		        prettyPrint(childNode, level + 1);
		      }
		    	for (int i=0; i< level; i++)
		    		System.out.print('\t');
	      }
      }
      System.out.println("</" + node.getNodeName() + ">");
    } catch (Throwable e) 
    {
      System.out.println("Cannot print!! " + e.getMessage());
      e.printStackTrace();
    }
  }


	
}
