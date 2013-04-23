package simpl.descriptions.testclasses;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)

public @interface myRandomParametizedAnnotation {
	public String value();	
}
