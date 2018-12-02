package com.codetaylor.mc.dropt.modules.dropt.compat.crafttweaker;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ZenDocClass {

  String value();

  String[] description() default {};
}
