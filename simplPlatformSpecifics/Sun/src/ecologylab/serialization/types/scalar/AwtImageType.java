/*******************************************************************************
 * Revision History:<br>
 * SRL Member - File created
 * 
 * <p>
 * 
 * <pre>
 *  This work is released under the BSD License:
 *  (C) 2011 Sketch Recognition Lab, Texas A&M University (hereafter SRL @ TAMU)
 *  All rights reserved.
 * 
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *      * Redistributions of source code must retain the above copyright
 *        notice, this list of conditions and the following disclaimer.
 *      * Redistributions in binary form must reproduce the above copyright
 *        notice, this list of conditions and the following disclaimer in the
 *        documentation and/or other materials provided with the distribution.
 *      * Neither the name of the Sketch Recognition Lab, Texas A&M University 
 *        nor the names of its contributors may be used to endorse or promote 
 *        products derived from this software without specific prior written 
 *        permission.
 *  
 *  THIS SOFTWARE IS PROVIDED BY SRL @ TAMU ``AS IS'' AND ANY
 *  EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED. IN NO EVENT SHALL SRL @ TAMU BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *  (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *  LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *  SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * </pre>
 * 
 *******************************************************************************/
package ecologylab.serialization.types.scalar;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import simpl.types.scalar.ImageType;

import ecologylab.serialization.ScalarUnmarshallingContext;
import ecologylab.serialization.TranslationContext;

public class AwtImageType<I extends Image> extends ImageType<I>
{

	public AwtImageType()
	{
		this(Image.class);
	}

	protected AwtImageType(Class thatClass)
	{
		super(thatClass);
	}

	@Override
	public I getInstance(String value, String[] formatStrings,
			ScalarUnmarshallingContext scalarUnmarshallingContext)
	{

		byte[] data = stringToByteArray(value);
		
		try
		{
			return (I) ImageIO.read(new ByteArrayInputStream(data));
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public String marshall(I i, TranslationContext suc)
	{
		try
		{
			BufferedImage img = (BufferedImage) i;

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(img, "png", baos);

			return byteArrayToString(baos.toByteArray());
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (ClassCastException e)
		{
			e.printStackTrace();
		}

		return null;
	}

}
