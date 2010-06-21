package de.fu_berlin.compilerbau.symbolTable;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * You must not call this interface method outside of
 * its actual implementation. The behavior should be considered
 * undocumented!
 * @author rene
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target({ ElementType.METHOD })
public @interface InternalMethod {

}
