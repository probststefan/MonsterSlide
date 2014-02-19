package fh.teamproject.utils.debug;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Debug {
	String name();

	boolean isModifiable();
}
