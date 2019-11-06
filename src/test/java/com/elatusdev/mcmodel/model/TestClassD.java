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
@MainField(value="field1")
public class TestClassD implements Comparable<TestClassD>{
    
    @FieldMetadata(show=true)
    private Long field1;
    
    @FieldMetadata(show=true)
    private String field2;
    
    @FieldMetadata(show=true)
    private Integer field3;
    
    @FieldMetadata(show=true)
    private Double field4;
    
    @FieldMetadata(show=true)
    private Float field5;
    
    @FieldMetadata(show=true)
    private Boolean field6;
    
    @FieldMetadata(show=false)
    private Long field7; 
    
    private static int staticField;
     
    

    public Long getField1() {
        return field1;
    }

    public void setField1(Long field1) {
        this.field1 = field1;
    }

    public String getField2() {
        return field2;
    }

    public void setField2(String field2) {
        this.field2 = field2;
    }

    public Integer getField3() {
        return field3;
    }

    public void setField3(Integer field3) {
        this.field3 = field3;
    }

    public Double getField4() {
        return field4;
    }

    public void setField4(Double field4) {
        this.field4 = field4;
    }

    public Float getField5() {
        return field5;
    }

    public void setField5(Float field5) {
        this.field5 = field5;
    }

    public Boolean getField6() {
        return field6;
    }

    public void setField6(Boolean field6) {
        this.field6 = field6;
    }
 
    @Override
    public int compareTo(TestClassD instanceD){
        return this.field1.compareTo(instanceD.getField1());
    }
    
   
    
}
