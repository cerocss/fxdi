package com.cerocss.fxdi;

import java.lang.annotation.*;

/**
 * To avoid unintentional injection, which could cause problems, injected classes need to be annotated with the
 * {@link Singleton} annotation.
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Singleton {
}

