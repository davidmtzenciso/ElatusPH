/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elatusdev.elatusph.unit;

import com.elatusdev.elatusph.model.ListPresentation;
import com.elatusdev.elatusph.model.TablePresentation;
import com.elatusdev.elatusph.model.TestClassA;
import com.elatusdev.elatusph.model.TestClassB;
import com.elatusdev.elatusph.model.TestClassC;
import com.elatusdev.elatusph.model.TestClassD;
import com.elatusdev.elatusph.transformers.ObjectTransformer;
import com.elatusdev.elatusph.transformersimpl.ObjectTransformerImpl;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

/**
 *
 * @author root
 */
@RunWith(MockitoJUnitRunner.class)
public class ObjectTransformerTest {
                
    private final ObjectTransformer objTransformer = new ObjectTransformerImpl();
        
    public TestClassD createInstanceD(){
        TestClassD instanceD;
        
        instanceD = new TestClassD();
        instanceD.setField1(new Long(1));
        instanceD.setField2("aaaaa");
        instanceD.setField3(1234);
        instanceD.setField4(123.1);
        instanceD.setField5(new Float(1.2));
        instanceD.setField6(true);
        return instanceD;
    }
    
    public Object createInstanceB(){
        TestClassB instanceB;
        
        instanceB = new TestClassB();
        instanceB.setFieldClassD1(createInstanceD());
        instanceB.setFieldClassD2(createInstanceD());
        return instanceB;
    }
    
    public List<Object> createList(){
        List<Object> list = new ArrayList<>();
        
        
        for(int i=0; i < 2; i++){
            list.add(createInstanceD());
        }
        return list;
    }
    
    @Test
    public void testToListGetLabelsTestClassB(){
        ListPresentation list;
       
       list = objTransformer.toListPresentation(createInstanceB());
       Assert.assertArrayEquals(
               new String[]{"field 1", "field 2", "field 3", "field 4", "field 5",
                            "field 6", "field ClassD2"}, list.getLabels().toArray());    
    }
   
        
    @Test
    public void testToListGetLablesTestClassD(){
       ListPresentation list;
       
       
       list = objTransformer.toListPresentation(createInstanceD());
       Assert.assertArrayEquals(
               new String[]{"field 1", "field 2", "field 3", "field 4", "field 5",
                            "field 6"}, list.getLabels().toArray());       
    }
    
    @Test
    public void testToListGetDataTestClassD(){
       ListPresentation list;
       
       list = objTransformer.toListPresentation(createInstanceD());
       Assert.assertArrayEquals(
               new Object[]{ new Long(1),"aaaaa",1234, 123.1, new Float(1.2),true}, 
                            list.getData().toArray());
    }
    
    @Test
    public void testToTableGetRowsTestClassD(){
        TablePresentation table;
        
        table = objTransformer.toTablePresentation(createList());
        Assert.assertEquals(2, table.getRows().size());
    }
    
    @Test
    public void testToTableGetLabelsTestClassD(){
        TablePresentation table;
        
        table = objTransformer.toTablePresentation(createList());
        Assert.assertArrayEquals(
               new String[]{"field 1", "field 2", "field 3", "field 4", "field 5",
                            "field 6"}, table.getLabels().toArray());          
    }
}
