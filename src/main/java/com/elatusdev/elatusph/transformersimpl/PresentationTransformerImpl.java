/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elatusdev.elatusph.transformersimpl;

import com.elatusdev.elatusph.transformers.PresentationTransfomer;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.function.Function;
import com.elatusdev.elatusph.annotaciones.Label;

/**
 *
 * @author root
 */
public class PresentationTransformerImpl implements PresentationTransfomer {
    
    
    private void insertData(String identifierName, String value, Object target) 
                                                    throws NoSuchFieldException,
                                                           NoSuchMethodException, 
                                                           IllegalAccessException,
                                                           InvocationTargetException{
        Field field;
        Method method;
        Class<?> cls;
         
        cls = target.getClass();
        field = findField(identifierName, cls);
        method = findMethod(formatName("set",field.getName()), field.getType(), cls);
        method.invoke(target, parseValue(field.getType(), value));
    }
    
    private String formatName(String prefix, String fieldName){
        return new StringBuilder().append(prefix).append(String.valueOf(fieldName.charAt(0)).toLowerCase())
                   .append(fieldName.substring(1, fieldName.length())).toString();
    }
    
    private Field findField(String identiferName, Class<?> cls) 
                                                    throws NoSuchFieldException{
        Class<?> superCls;
        Label identifier;
        
        for(Field field : cls.getDeclaredFields()){
            identifier = field.getAnnotation(Label.class);
            if(identifier != null){
                if(identifier.value().equals(identiferName)){
                    return field;
                }
            }
        }
        superCls = cls.getSuperclass();
        if(!superCls.equals(Object.class))
            return findField(identiferName, superCls);
        else
            throw new NoSuchFieldException(new StringBuilder().append("identifier: ")
                     .append(identiferName).append(" not found in the class nor in any parent class")
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
    
     private Object parseValue(Class<?> type, String value) 
                                            throws NumberFormatException{
        try{
        if(type.equals(Integer.class))
            return Integer.parseInt(value);
        else if(type.equals(Double.class))
            return Double.parseDouble(value);
        else if(type.equals(Long.class))
            return Long.parseLong(value);
        else if(type.equals(Boolean.class))
            return Integer.parseInt(value) == 1;
        else
            return value;
        }catch(NumberFormatException e){
            throw new NumberFormatException(new StringBuilder().append("type: ")
                      .append(type.getSimpleName()).append(" not supported").toString());
        }
    }
     
    @Override
    public void toObjects(Map<String,String> data, final Class<?> cls) {
        List<Object> objects;
        List<String> errors;
        Object obj;
        
        objects = new ArrayList<>();
        errors = new ArrayList<>();
        for(Entry<String,String> entry : data.entrySet()){
            obj = instanceCreator.apply(cls);
            try{
                insertData(entry.getKey(), entry.getValue(), obj);
                objects.add(obj);
            }catch(NoSuchFieldException | NoSuchMethodException | 
                    IllegalAccessException | InvocationTargetException e){
                errors.add(e.getMessage());
            }
        }
        if(errors.isEmpty())
            onSuccessConsumer.accept(objects);
        else
            onErrorConsumer.accept(errors);
    }
    
    @Override
    public void setInstanceCreator(Function<Class<?>, Object> function) {
        this.instanceCreator = function;
    }

    @Override
    public void setOnSuccessConsumer(Consumer<List<Object>> onSuccessConsumer) {
        this.onSuccessConsumer = onSuccessConsumer;
    }

    @Override
    public void setOnErrorConsumer(Consumer<List<String>> onErrorConsumer) {
        this.onErrorConsumer = onErrorConsumer;
    }

    private Function<Class<?>, Object> instanceCreator;
    private Consumer<List<Object>> onSuccessConsumer;
    private Consumer<List<String>> onErrorConsumer;
}
