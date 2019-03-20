/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elatusdev.elatusph.transformersimpl;

import com.elatusdev.elatusph.annotaciones.FieldMetadata;
import com.elatusdev.elatusph.annotaciones.MainField;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 *
 * @author root
 */
public class Validator {
    
    public boolean isValidClass(final Class<?> cls) throws NullPointerException{        
        if(!hasComparableInterface(cls.getInterfaces())){
            throw new NullPointerException(new StringBuilder()
                        .append("Comparable interface not found for: ")
                        .append(cls.getSimpleName()).toString());
        }
        else if(cls.getDeclaredAnnotation(MainField.class) == null){
            throw new NullPointerException(new StringBuilder()
                        .append("MainField annotation missing in : ")
                        .append(cls.getSimpleName()).toString());
        }
        else
            return true;
    }
    
    public boolean isValidField(final Field field) throws NullPointerException{
        FieldMetadata metadata;
        
        if(!Modifier.isStatic(field.getModifiers())){
            metadata = field.getDeclaredAnnotation(FieldMetadata.class);
            if(metadata == null){
                throw new NullPointerException(new StringBuilder()
                            .append("FieldMetadata annotation missing in field: ")
                            .append(field.getName()).append(", of class: ")
                            .append(field.getDeclaringClass().getSimpleName())
                            .toString());
            }
            else if(metadata.show())
                return isValidFieldMetadata(metadata, field);
        }
        
        return false;
    }
    
    public boolean isValidFieldMetadata(final FieldMetadata metadata,final Field field)
                                        throws NullPointerException {
        if(metadata.type() == FieldMetadata.FieldType.USER_OBJECT || 
                metadata.type() == FieldMetadata.FieldType.COLLECTION) {
            
            if(metadata.fieldClass() == void.class) {
                throw new NullPointerException(new StringBuilder()
                        .append("fieldClass is void for: ")
                        .append(field.getName()).append("in class: ")
                        .append(field.getDeclaringClass().getSimpleName())
                        .toString());
            }
            else if(metadata.type() == FieldMetadata.FieldType.USER_OBJECT)
                return true;
            
            else if(metadata.type() == FieldMetadata.FieldType.COLLECTION){
                if(metadata.managedClass() == void.class){
                    throw new NullPointerException(new StringBuilder()
                    .append("fieldClass is void for: ")
                    .append(field.getName()).append("in class: ")
                        .append(field.getDeclaringClass().getSimpleName())
                        .toString());
                }
            }
        }
        
        return true;
    }
    
    public boolean hasComparableInterface(final Class<?>[] interfaces){  
        boolean hasIt = false;
        
        for(Class<?> clazz : interfaces){
            if(clazz.equals(Comparable.class))
                hasIt = true;
            else if(clazz.getInterfaces() != null)
                hasIt = hasComparableInterface(clazz.getInterfaces());
        }
        return hasIt;
    }
}
