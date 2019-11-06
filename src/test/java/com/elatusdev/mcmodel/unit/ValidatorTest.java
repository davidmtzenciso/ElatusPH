/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elatusdev.mcmodel.unit;

import com.elatusdev.mcmodel.model.TestClassD;
import com.elatusdev.mcmodel.transformersimpl.Validator;

import java.lang.reflect.Field;
import java.util.logging.Logger;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *
 * @author root
 */
public class ValidatorTest {
    
    private static Validator validator;    
    
    public static void init(){
        validator = new Validator();
    }
    
    public Object[] parametersInvalidField(){
        return new Object[]{
            "field7", "staticField"
        };
    }
    
    public Object[] parametersValidField(){
        return new Object[]{
            "field1", "field2", "field3"
        };
    }
    
    @Test
    public void testValidClass() {
        try{
           Assertions.assertTrue(validator.isValidClass(TestClassD.class));
        }
        catch(NullPointerException e){
            Logger.getGlobal().severe(e.getMessage());
            Assertions.fail();
        }
    }
    
    @Test
    public void testValidField(String fieldName){
        try{
            Field field = TestClassD.class.getDeclaredField(fieldName);
            Assertions.assertTrue(validator.isValidField(field));
        }catch(NoSuchFieldException e){
            Logger.getGlobal().severe(e.getMessage());
            Assertions.fail();
        }
    }
    
    @Test
    public void testInvalidField(String fieldName){
        try{
            Field field = TestClassD.class.getDeclaredField(fieldName);
            Assertions.assertFalse(validator.isValidField(field));
        }catch(NoSuchFieldException e){
            Logger.getGlobal().severe(e.getMessage());
        }
    }
    
    public void testIsNonAnnotatedField() throws NullPointerException{
        try{
            Field field = TestClassD.class.getDeclaredField("field8");
            Assertions.assertFalse(validator.isValidField(field));
        }catch(NoSuchFieldException e){
        }
    }
  
}
