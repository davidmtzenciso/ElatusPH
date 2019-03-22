/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elatusdev.elatusph.transformersimpl;

import com.elatusdev.elatusph.model.BadConstructionException;
import com.elatusdev.elatusph.model.ListPresentation;
import com.elatusdev.elatusph.model.TablePresentation;
import com.elatusdev.elatusph.transformers.ElatusPH;
import com.elatusdev.elatusph.transformers.ObjectTransformer;
import com.elatusdev.elatusph.transformers.PresentationTransfomer;
import java.util.List;
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
    public ElatusPH fromPresentationToObject(List<String> labels, List<Object> data, Class<?> cls){
        this.labels = labels;
        this.data = data;
        this.cls = cls;
        return this;
    }
    
    @Override
    public ElatusPH fromPresentationToCollection(List<String> labels, List<List<Object>> data, Class<?> cls){
        this.labels = labels;
        this.dataCollection = data;
        this.cls = cls;
        return this;
    }
    
    @Override
    public ElatusPH setInstanceCreator(Function<Class<?>, Object> function){
        this.instanceCreator = function; 
        return this;
    }
    
    @Override
    public ElatusPH onCollectionCreation(Consumer<List<Object>> consumer){
        collectionConsumer = consumer;
        return this;
    }
    
    @Override
    public ElatusPH onObjCreation(Consumer<Object> consumer){
        objConsumer = consumer;
        return this;
    }
    
    @Override
    public ElatusPH onError(Consumer<List<String>> consumer){
        errorConsumer = consumer;
        return this;
    }
    
    @Override
    public void transforme() throws BadConstructionException {
        String msg;
        
        msg = labels == null ? "labels not defined" :
              (cls == null ? "Class not defined" :
              (instanceCreator == null ? "instance creator not defined" :
              (errorConsumer == null ? "on error not defined" : null)));
        if(msg == null){
            if(data != null){
                if(objConsumer != null)
                    this.presentationTransformer.toObject(labels, data, cls);
                else
                    msg = "onObjCretion not defined";
            }
            else if(dataCollection != null){
                if(collectionConsumer != null)
                    this.presentationTransformer.toObjects(labels, dataCollection, cls);
                else
                    msg = "onCollectionCreation not defined";   
            }
        }
            
        if(msg != null)
            throw new BadConstructionException(msg);
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
    private List<Object> data;
    private List<List<Object>> dataCollection;
    private List<String> labels;
    private Consumer<Object> objConsumer;
    private Consumer<List<Object>> collectionConsumer;
    private Consumer<List<String>> errorConsumer;
    private Function<Class<?>, Object> instanceCreator;
    private final PresentationTransfomer presentationTransformer;
    private final ObjectTransformer objectTransformer;
}
