/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elatusdev.mcmodel.transformersimpl;

import com.elatusdev.mcmodel.annotaciones.Label;
import com.elatusdev.mcmodel.transformers.PresentationTransfomer;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.NoSuchElementException;

/**
 *
 * @author root
 */
public class PresentationTransformerImpl implements PresentationTransfomer {
    
    
    private void insertData(String label, Object value, Object target) 
                                                    throws NoSuchFieldException,
                                                           NoSuchMethodException, 
                                                           IllegalAccessException,
                                                           InvocationTargetException,
                                                           NoSuchElementException{
        Field field;
        Method method;
        Class<?> cls;
         
        cls = target.getClass();
        field = findField(label, cls);
        method = findMethod(formatName("set",field.getName()), field.getType(), cls);
        if(field.getType().equals(value.getClass()))
           method.invoke(target, value);
        else
            throw new NoSuchElementException(new StringBuilder().append("type: ")
                      .append(field.getType().getSimpleName())
                      .append(" not matched param in set method").toString());
    }
    
    private String formatName(String prefix, String fieldName){
        return new StringBuilder().append(prefix).append(String.valueOf(fieldName.charAt(0)).toLowerCase())
                   .append(fieldName.substring(1, fieldName.length())).toString();
    }
    
    private Field findField(String label, Class<?> cls) 
                                                    throws NoSuchFieldException{
        Class<?> superCls;
        Label labelAnnotation;
        
        for(Field field : cls.getDeclaredFields()){
            labelAnnotation = field.getAnnotation(Label.class);
            if(labelAnnotation != null){
                if(labelAnnotation.identifier().equals(label)){
                    return field;
                }
            }
        }
        superCls = cls.getSuperclass();
        if(!superCls.equals(Object.class))
            return findField(label, superCls);
        else
            throw new NoSuchFieldException(new StringBuilder().append("identifier: ")
                     .append(label).append(" not found in the class nor in any parent class")
                     .toString());
    }

    
    private Method findMethod(String name, Class<?> type, Class<?> cls) 
                                                    throws NoSuchMethodException{
        Class<?> superCls;
        
        try{
            return cls.getDeclaredMethod(name, type);
        }catch(NoSuchMethodException e){
            superCls = cls.getSuperclass();
            if(!superCls.equals(Object.class))
                return findMethod(name, type, superCls);
            else
                throw new NoSuchMethodException(new StringBuilder().append("Method: ")
                         .append(name).append(" not found in the class nor in any parent class")
                         .toString());
        }
    }
  
     
    @Override
    public void toObject(List<String> labels, List<Object> data, final Class<?> cls) {
        List<String> errors;
        Object obj;
        
        obj = instanceCreator.apply(cls);
        errors = insertData(obj, labels, data);
        if(errors.isEmpty())
            onSuccessConsumer.accept(obj);
        else
            onErrorConsumer.accept(errors);
    }
    
    private List<String> insertData(Object obj, List<String> labels, List<Object> data){
        List<String> errors;
        
        errors = new ArrayList<>();
        try{
            for(int i=0; i < labels.size(); i++){
                insertData(labels.get(i), data.get(i), obj);
            }
        }catch(NoSuchFieldException | NoSuchMethodException | 
                IllegalAccessException | InvocationTargetException e){
            errors.add(e.getMessage());
        }
        return errors;
    }

    @Override
    public void toObjects(List<String> labels, List<List<Object>> data, final Class<?> cls){
        List<Object> objects;
        List<String> errors;
        Object obj;
        
        objects = new ArrayList<>();
        errors = new ArrayList<>();
        for(List<Object> objData : data){        
            obj = instanceCreator.apply(cls);
            errors.addAll(insertData(obj, labels, objData));   
        }
        if(errors.isEmpty())
            onSuccessListConsumer.accept(objects);
        else
            onErrorConsumer.accept(errors);
        
    }
    
    @Override
    public void setInstanceCreator(Function<Class<?>, Object> function) {
        this.instanceCreator = function;
    }

    @Override
    public void setOnSuccessConsumer(Consumer<Object> onSuccessConsumer) {
        this.onSuccessConsumer = onSuccessConsumer;
    }
    
    @Override
    public void setOnSuccessListConsumer(Consumer<List<Object>> onSuccessConsumer) {
        this.onSuccessListConsumer = onSuccessConsumer;
    }

    @Override
    public void setOnErrorConsumer(Consumer<List<String>> onErrorConsumer) {
        this.onErrorConsumer = onErrorConsumer;
    }

    private Function<Class<?>, Object> instanceCreator;
    private Consumer<Object> onSuccessConsumer;
    private Consumer<List<Object>> onSuccessListConsumer;
    private Consumer<List<String>> onErrorConsumer;
}
