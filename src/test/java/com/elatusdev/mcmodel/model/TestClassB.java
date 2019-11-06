/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elatusdev.mcmodel.model;

import com.elatusdev.mcmodel.annotaciones.FieldMetadata;
import com.elatusdev.mcmodel.annotaciones.Label;
import com.elatusdev.mcmodel.annotaciones.MainField;

/**
 *
 * @author root
 */
@MainField(value="fieldClassD")
public class TestClassB implements Comparable<TestClassB>{
    
    @FieldMetadata(show=true, type=FieldMetadata.FieldType.USER_OBJECT,
            fieldClass=TestClassD.class, expand=true)
    private TestClassD fieldClassD1;
    
    @FieldMetadata(show=true, type=FieldMetadata.FieldType.USER_OBJECT,
            fieldClass=TestClassD.class)
    private TestClassD fieldClassD2;

    public TestClassD getFieldClassD1() {
        return fieldClassD1;
    }

    public void setFieldClassD1(TestClassD fieldClassD1) {
        this.fieldClassD1 = fieldClassD1;
    }

    public TestClassD getFieldClassD2() {
        return fieldClassD2;
    }

    public void setFieldClassD2(TestClassD fieldClassD2) {
        this.fieldClassD2 = fieldClassD2;
    }
   
    @Override
    public int compareTo(TestClassB instanceB){
        return this.fieldClassD1.compareTo(instanceB.getFieldClassD1());
    }
    
}
