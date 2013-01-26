/**
 * 
 */
package ecologylab.serialization.types.scalar;

import java.awt.Rectangle;
import java.util.Scanner;
import java.util.regex.Pattern;

import simpl.annotations.dbal.simpl_inherit;
import simpl.core.ScalarUnmarshallingContext;
import simpl.core.TranslationContext;
import simpl.types.CrossLanguageTypeConstants;
import simpl.types.ScalarType;


/**
 * @author andrew
 *
 */
@simpl_inherit
public class RectangleType extends ScalarType<Rectangle>
implements CrossLanguageTypeConstants
{
	private static final Pattern DELIMITER_PATTERN = Pattern.compile("[, ]");
	
	public RectangleType()
	{
		super(Rectangle.class, null, null, null);
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

}
