/**
 * 
 */
package ecologylab.serialization.types.scalar;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.Scanner;
import java.util.regex.Pattern;

import ecologylab.serialization.ScalarUnmarshallingContext;
import ecologylab.serialization.TranslationContext;

/**
 * @author andrew
 *
 */
public class RectangleType extends ScalarType<Rectangle>
{
	private static final Pattern DELIMITER_PATTERN = Pattern.compile("[, ]");
	
	public RectangleType()
	{
		super(Rectangle.class);
	}
	
	@Override
	public Rectangle getInstance(String value, String[] formatStrings,
			ScalarUnmarshallingContext scalarUnmarshallingContext)
	{
		Scanner scanner = new Scanner(value);
		scanner.useDelimiter(DELIMITER_PATTERN);
		
		Rectangle result = null;
		if (scanner.hasNextInt())
		{
			int x =  scanner.nextInt();
			if (scanner.hasNextInt())
			{
				int y = scanner.nextInt();
				if (scanner.hasNextInt())
				{
					int width = scanner.nextInt();
					if (scanner.hasNextInt())
					{
						result = new Rectangle(x, y, width, scanner.nextInt());
					}
				}
			}
		}
		
		return result;
	}
	
	@Override
	public String marshall(Rectangle rectangle, TranslationContext serializationContext)
	{
		return rectangle.x + " " + rectangle.y + " " + rectangle.width + " " + rectangle.height;
	}

	@Override
	public String getObjectiveCType()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCSharptType()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDbType()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getJavaType()
	{
		// TODO Auto-generated method stub
		return MappingConstants.JAVA_RECTANGLE;
	}

}
