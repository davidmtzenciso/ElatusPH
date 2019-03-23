/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elatusdev.elatusph.unit;

import com.elatusdev.elatusph.model.TestClassD;
import com.elatusdev.elatusph.transformersimpl.Validator;
import java.lang.reflect.Field;
import java.util.logging.Logger;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author root
 */
@RunWith(JUnitParamsRunner.class)
public class ValidatorTest {
    
    private static Validator validator;    
    
    @BeforeClass
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
           Assert.assertTrue(validator.isValidClass(TestClassD.class));
        }
        catch(NullPointerException e){
            Logger.getGlobal().severe(e.getMessage());
            Assert.fail();
        }
    }
    
    @Test
    @Parameters(method="parametersValidField")
    public void testValidField(String fieldName){
        try{
            Field field = TestClassD.class.getDeclaredField(fieldName);
            Assert.assertTrue(validator.isValidField(field));
        }catch(NoSuchFieldException e){
            Logger.getGlobal().severe(e.getMessage());
            Assert.fail();
        }
    }
    
    @Test
    @Parameters(method="parametersInvalidField")
    public void testInvalidField(String fieldName){
        try{
            Field field = TestClassD.class.getDeclaredField(fieldName);
            Assert.assertFalse(validator.isValidField(field));
        }catch(NoSuchFieldException e){
            Logger.getGlobal().severe(e.getMessage());
            Assert.fail();
        }
    }
    
    @Test(expected=NullPointerException.class)
    public void testIsNonAnnotatedField() throws NullPointerException{
        try{
            Field field = TestClassD.class.getDeclaredField("field8");
            Assert.assertFalse(validator.isValidField(field));
        }catch(NoSuchFieldException e){
            Logger.getGlobal().severe(e.getMessage());
            Assert.fail();
        }
    }
  
}
