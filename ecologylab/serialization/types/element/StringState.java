package ecologylab.serialization.types.element;

import ecologylab.serialization.ElementState;

public class StringState extends ElementState
{
    @simpl_scalar public String string;
    
    public StringState()
    {
        super();
    }
    
    public StringState(String string)
    {
        this.string = string;
    }

    /**
     * @return the string
     */
    public String getString()
    {
        return string;
    }

		/**
		 * @see ecologylab.generic.Debug#toString()
		 */
		@Override
		public String toString()
		{
			return string;
		}
}
