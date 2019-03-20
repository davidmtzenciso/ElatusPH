/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elatusdev.elatusph.transformersimpl;

import com.elatusdev.elatusph.model.ListPresentation;
import com.elatusdev.elatusph.model.TablePresentation;
import com.elatusdev.elatusph.transformers.ElatusPH;
import com.elatusdev.elatusph.transformers.ObjectTransformer;
import com.elatusdev.elatusph.transformers.PresentationTransfomer;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 *
 * @author root
 */
public class ElatusPHImpl implements ElatusPH {
    
    public ElatusPHImpl(){
        presentationTransformer = new PresentationTransformerImpl();
        objectTransformer = new ObjectTransformerImpl();
    }
    
    @Override
    public ElatusPH fromPresentation(Map<String,String> data, Class<?> cls){
        this.data = data;
        this.cls = cls;
        return this;
    }
    
    @Override
    public ElatusPH setInstanceCreator(Function<Class<?>, Object> function){
        presentationTransformer.setInstanceCreator(function);
        return this;
    }
    
    @Override
    public ElatusPH onCreationSuccess(Consumer<List<Object>> consumer){
        presentationTransformer.setOnSuccessConsumer(consumer);
        return this;
    }
    
    @Override
    public ElatusPH onCreationError(Consumer<List<String>> consumer){
        presentationTransformer.setOnErrorConsumer(consumer);
        return this;
    }
    
    @Override
    public void transformeToObjects(){
        presentationTransformer.toObjects(data, cls);
    }

    @Override
    public ListPresentation fromObjectToList(Object obj){
        return objectTransformer.toListPresentation(obj);
    }
    
    @Override
    public TablePresentation fromObjectsToTable(List<Object> objects){
        return objectTransformer.toTablePresentation(objects);
    }
        
    private Class<?> cls;
    private Map<String, String> data;
    private final PresentationTransfomer presentationTransformer;
    private final ObjectTransformer objectTransformer;
}
