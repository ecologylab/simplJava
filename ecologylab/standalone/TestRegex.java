package ecologylab.standalone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 */

/**
 * @author Zachary O. Toups (zach@ecologylab.net)
 *
 */
public class TestRegex
{

    /**
     * @param args
     */
    public static void main(String[] args)
    {
        String header = 
//            "asdfasdfasdfasdfasdfContent-Length: 87\nasdfasdfasdfasdfasdf";
//            "POST /%3Curl_message%20uid=%2210%22%20collection=%22Select%20TestCollection%22%20url=%22http://www.yahoo.com/%22/%3E HTTP/1.1\nHost: localhost:10200\nUser-Agent: Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.8.1.1) Gecko/20061204 Firefox/2.0.0.1\nAccept: text/xml,application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5\nAccept-Language: en-us,en;q=0.5\nAccept-Encoding: gzip,deflate\nAccept-Charset: ISO-8859-1,utf-8;q=0.7,*;q=0.7\nKeep-Alive: 300\nConnection: keep-alive\nContent-Type: text/xml\nContent-Length: 87\nPragma: no-cache\nCache-Control: no-cache";
"POST /%3Curl_message%20uid=%2210%22%20collection=%22Select%20TestCollection%22%20url=%22about:blank%22/%3E HTTP/1.1\nHost: csdll.cs.tamu.edu:10200\nUser-Agent: Mozilla/5.0 (Windows; U; Windows NT 5.0; en-US; rv:1.8.1.1) Gecko/20061204 Firefox/2.0.0.1\nAccept: text/xml,application/xml,application/xhtml+xml,text/html;q= 0.9,text/plain;q=0.8,image/png,*/*;q=0.5\nAccept-Language: en-us,en;q=0.5\nAccept-Encoding: gzip,deflate\nAccept-Charset: ISO-8859-1,utf-8;q=0.7,*;q=0.7\nKeep-Alive: 300\nConnection: keep-alive\nContent-Type: text/xml\nContent-Length: 77\nCookie: s_pers=%20s_vsn_nytimesglobal_1%3D6738512885858%7C1474161904926%3B%20s_vnum1%3D1158542704942%257C1%7C1474161904942%3B; CP=*\nPragma: no-cache\nCache-Control: no-cache";
        
        Pattern p = Pattern.compile("\\p{ASCII}*content-length\\s*:\\s*(\\d*)\\p{ASCII}*");

        Matcher m = p.matcher(header.toLowerCase());
        
        try
        {
            m.matches();
            System.out.println(Integer.parseInt(m.group(1)));
        }
        catch (NumberFormatException e)
        {
e.printStackTrace();        }
        catch (IllegalStateException e)
        {
            System.out.println("regex was: "+p.pattern());
            System.out.println("string was: "+header.toLowerCase());
            System.out.println("***");
            
            throw e;
        }
    
    }

}
