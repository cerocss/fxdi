package com.cerocss.fxdi;

import java.lang.annotation.*;

/**
 * Determines the constructor that should be used for injection, when there are multiple public constructors on a class,
 * that needs to be instantiated.
 */
@Documented
@Target(ElementType.CONSTRUCTOR)
@Retention(RetentionPolicy.RUNTIME)
public @interface InjectionConstructor {
}
