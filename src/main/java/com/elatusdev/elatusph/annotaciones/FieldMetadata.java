/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elatusdev.elatusph.annotaciones;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author root
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FieldMetadata {
    
    public enum FieldType{
        PRIMITIVE, COLLECTION, USER_OBJECT, MULTI_VALUE
    }
    
    boolean show();
    
    FieldType type() default FieldType.PRIMITIVE;
    
    boolean expand() default false;
    
    String mainField();
        
    Class<?> managedClass() default void.class;
    
    Class<?> fieldClass() default void.class;
    
}