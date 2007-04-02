/**
 * 
 */
package ecologylab.services;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashSet;

/**
 * @author Zach
 *
 */
public class ServicesUtilities
{
    public static InetAddress[] getAllInetAddressesForLocalhost() throws UnknownHostException
    {
        HashSet<InetAddress> addresses = new HashSet<InetAddress>();
        
        for (InetAddress a : InetAddress.getAllByName("localhost"))
        {
            addresses.add(a);
        }
        
        for (InetAddress a : InetAddress.getAllByName(InetAddress.getLocalHost().getHostAddress()))
        {
            addresses.add(a);
        }
        
        addresses.add(InetAddress.getLocalHost());
        
        return addresses.toArray(new InetAddress[addresses.size()]);
    }
}
