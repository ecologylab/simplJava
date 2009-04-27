/**
 * 
 */
package ecologylab.generic;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

/**
 * 
 *
 * @author andruid 
 */
public class CharsetDecoderPool extends ResourcePool<CharsetDecoder>
{
	Charset				charset;
	
	protected CharsetDecoderPool(Charset charset, int initialPoolSize)
	{
		super(false, initialPoolSize, NEVER_CONTRACT);
		this.charset			= charset;
	}

	@Override
	protected void clean(CharsetDecoder decoder)
	{
		decoder.reset();
	}

	@Override
	protected CharsetDecoder generateNewResource()
	{
		return charset.newDecoder();
	}


}
