package ecologylab.translators.hibernate.hbmxml;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import ecologylab.serialization.ClassDescriptor;
import ecologylab.serialization.ElementState;
import ecologylab.serialization.FieldDescriptor;
import ecologylab.serialization.TranslationContext;
import ecologylab.serialization.annotations.simpl_inherit;

/**
 * the root class for hibernate mappings.
 * 
 * @author quyin
 * 
 */
@simpl_inherit
public abstract class HibernateBasic extends ElementState
{

	/**
	 * decide the order of serialization using a double. field with smaller values should be
	 * serialized earlier.
	 * 
	 * @author quyin
	 * 
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Inherited
	public @interface simpl_serialization_order
	{
		
		public static final double DEFAULT_VALUE = 0;

		/**
		 * the weight of a field in terms of the serialization order. the smaller, the earlier.
		 * 
		 * @return
		 */
		double value() default 0;

	}

	public HibernateBasic()
	{
		super();
	}

	@Override
	public void serializationPreHook(TranslationContext translationContext)
	{
		super.serializationPreHook(translationContext);

		ClassDescriptor cd = classDescriptor();
		ArrayList<FieldDescriptor> fieldDescriptors = cd.elementFieldDescriptors();
		Collections.sort(fieldDescriptors, new Comparator<FieldDescriptor>() {
			@Override
			public int compare(FieldDescriptor fd1, FieldDescriptor fd2)
			{
				simpl_serialization_order orderAnnotation1 = fd1.getField().getAnnotation(simpl_serialization_order.class);
				double order1 = orderAnnotation1 == null ? simpl_serialization_order.DEFAULT_VALUE : orderAnnotation1.value();
				simpl_serialization_order orderAnnotation2 = fd2.getField().getAnnotation(simpl_serialization_order.class);
				double order2 = orderAnnotation2 == null ? simpl_serialization_order.DEFAULT_VALUE : orderAnnotation2.value();
				
				return Double.compare(order1, order2);
			}
		});
	}

}
