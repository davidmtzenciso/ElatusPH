package com.elatusdev.elatusph.transformersimpl;

import com.elatusdev.elatusph.annotaciones.FieldMetadata;
import com.elatusdev.elatusph.annotaciones.FieldMetadata.FieldType;
import com.elatusdev.elatusph.annotaciones.MainField;
import com.elatusdev.elatusph.model.ListPresentation;
import com.elatusdev.elatusph.model.TablePresentation;
import com.elatusdev.elatusph.transformers.ObjectTransformer;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.elatusdev.elatusph.annotaciones.Label;

/**
 *
 * @author david martinez enciso
 */
public class ObjectTransformerImpl implements ObjectTransformer{
        
    public ObjectTransformerImpl(){     
        emptySymbol = " ";
        validator = new Validator();
    }
    
    private List<String> getLabels(Object target){
        FieldMetadata metadata;
        List<String> list;
        Class<?> cls;
        
        try{
            cls = target.getClass();
            list = new LinkedList<>();
            for(Field field : cls.getDeclaredFields()){
                if(validator.isValidField(field)){
                    metadata = field.getDeclaredAnnotation(FieldMetadata.class);
                    if(metadata.expand()){
                        list.addAll(getLabels(cls.getMethod(
                                getMethodName(field.getName())).invoke(target)));
                    }
                    else
                        list.add(field.getDeclaredAnnotation(Label.class).value());
                }
            }
            return list;
        }catch (NoSuchMethodException | IllegalAccessException 
            | InvocationTargetException  e) {
            LOG.log(Level.SEVERE, "Reflection Error in object to row: {0}", e);
        }
        return null;
    }
    
    private LinkedList<List<Object>> toRows(final List<Object> objects)
                                                    throws NullPointerException{ 
        LinkedList<List<Object>> rows;
        Class<?> cls;

        try{
            cls = objects.get(0).getClass();
            if(validator.isValidClass(cls)){
                rows = new LinkedList<>();
                for(Object object : objects){
                   rows.add(processFieldsOf(object));
                }
                return rows;
            }
            else
                LOG.log(Level.SEVERE, "in toRows method invalid class: {0}",cls.getSimpleName());
        }catch (NoSuchMethodException | IllegalAccessException 
            | InvocationTargetException | NoSuchFieldException e) {
            LOG.log(Level.SEVERE, "Reflection Error in object to row: {0}", e);
        }
        return null;
    }
    
    private List<Object> toRow(final Object object){
        try{
            return processFieldsOf(object);
        }catch (NoSuchMethodException | IllegalAccessException 
            | InvocationTargetException | NoSuchFieldException e) {
            LOG.log(Level.SEVERE, "Reflection Error in object to row: {0}", e);
        }
        return null;
    }
   
    private LinkedList<Object> processFieldsOf(final Object target)
                            throws NoSuchMethodException, NullPointerException,
                                   IllegalAccessException, InvocationTargetException,
                                   NoSuchFieldException {
        LinkedList<Object> row;
        FieldMetadata metadata;
        final Class<?> cls;

        row = new LinkedList<>();
        cls = target.getClass();
        for(Field field : cls.getDeclaredFields()){
            LOG.info(field.getName());
            if(validator.isValidField(field)){
                metadata = field.getDeclaredAnnotation(FieldMetadata.class);
                if(metadata.type() == FieldType.COLLECTION) 
                    row.addAll(transformeCollection(target,field));
                else if(metadata.expand()){
                    row.addAll(processFieldsOf(cls.getMethod(
                            getMethodName(field.getName())).invoke(target)));
                }
                else
                    row.add(getValue(target, field));
            }
            else
                LOG.log(Level.SEVERE, "invalid field: {0}",field.getName());
        }
        return row;
    }
    
    
    
    private void addToRow(final List<Object> row, final Object value,
                       final  List<Object>elements, final int position, 
                       final List<Object> multiValue) throws NullPointerException{
        if(value != null) {
            if(isDuplicate(elements, position)) {
                multiValue.add(value);
                if(!isDuplicate(elements, position+1))
                     row.add(concatenateValues(multiValue));
           }
           else 
               row.add(value);
        }
    }
    
    @SuppressWarnings("unchecked")
    private List<Object> transformeCollection(final Object element, final Field field)
                            throws NoSuchMethodException,IllegalAccessException, 
                                   InvocationTargetException, NoSuchFieldException,
                                   NullPointerException{
        List<Object> collection;
        List<Object> multiValue;
        MainField mainfield;
        List<Object> row;
        Field objField;
        Class<?> cls;
        Object value;

        collection = (List<Object>)element.getClass().getMethod(
                        getMethodName(field.getName())).invoke(element);
        row = new LinkedList<>();
        if(!collection.isEmpty()){
            cls = collection.get(0).getClass();
            if(validator.isValidClass(cls)){
                multiValue= new LinkedList<>();
                for(int i=0; i < collection.size(); i++){
                    cls = collection.get(i).getClass();
                    mainfield = cls.getDeclaredAnnotation(MainField.class);
                    objField = cls.getDeclaredField(mainfield.value());
                    if(validator.isValidField(objField)){
                        value = getValue(collection.get(i),objField);
                        addToRow(row, value, collection, i, multiValue);
                    }
                }
            }
            else
                LOG.log(Level.SEVERE, "in transformeCollection invalid class: {0}",cls.getSimpleName());
        }
        else
            row.add(emptySymbol);
        return row;
    }
    
    @SuppressWarnings("unchecked")
    public <E extends Comparable<E>> boolean isDuplicate(final List<Object> elements,final int i) {
        E element;
        E previous;
        E next;

        element = (E)elements.get(i);
        if(elements.size() > 1){
            previous = (E) (i-1 > -1 ? elements.get(i-1) : null);
            next = (E) (i+i < elements.size() ? elements.get(i+1) : null);
            return  previous != null ? element.compareTo(previous) == 0 : 
                            (next != null ? element.compareTo(next) == 0 : false);
        }
        else
            return false;
    }

    /*
     * The list will be sorted before concatenate
     * and it will be cleared afterwards.
     */
    private Object concatenateValues(final List<Object> multiValue) {
        String delimiter;
        Object[] values;
        String value;

        value = "";
        delimiter = " ,";
        values = multiValue.stream().sorted().toArray();
        for(int i = 0; i < values.length ;i++){
           if(i+1 < values.length)
               value += multiValue.remove(0)+delimiter;
           else
               value += multiValue.remove(0);
        }
        multiValue.clear();
        return value;
    }
    
    /*
     *  The format is for the purpose of invoking a method.
     *  Either get or set concatenated from left to right.
     */
    private String getMethodName(final String name){
        return "get"+(name.substring(0,1).toUpperCase() + name.substring(1, name.length()));
    }

    private Object getValue(final Object target, final Field field)
                                        throws NoSuchMethodException, IllegalAccessException, 
                                               InvocationTargetException,NoSuchFieldException,
                                               NullPointerException{
        FieldMetadata metadata;
        MainField mainField;
        Class<?> fieldClass;
        String methodName;
        Object value;
        Class<?> cls;

        methodName = getMethodName(field.getName());
        if(target != null){
            cls = target.getClass();
            metadata = field.getAnnotation(FieldMetadata.class);
            switch (metadata.type()) {
                case PRIMITIVE:
                    value = cls.getDeclaredMethod(methodName).invoke(target);    
                    return value != null ? value : emptySymbol;
                case USER_OBJECT:
                    fieldClass = metadata.fieldClass();
                    if(validator.isValidClass(fieldClass)){
                        mainField = fieldClass.getDeclaredAnnotation(MainField.class);
                        return getValue(cls.getDeclaredMethod(methodName).invoke(target),
                                fieldClass.getDeclaredField(mainField.value()));
                    }
                    else
                        return null;
                default:
                    throw new NullPointerException(
                            new StringBuilder().append("FieldType not supporte")
                                .append(metadata.type().toString()).toString());
            }
        }  
        return emptySymbol;
    }
    
    @Override
    public TablePresentation toTablePresentation(List<Object> objects){
        return new TablePresentation(
                getLabels(objects.get(0)),toRows(objects));
    }
    
    @Override
    public ListPresentation toListPresentation(Object obj){
        return new ListPresentation(getLabels(obj), toRow(obj));
    }
    
    @Override
    public void setSymbolForEmpty(String symbol){
        this.emptySymbol = symbol;
    }

    private String emptySymbol;
    private final Validator validator;
    private static final Logger LOG = Logger.getLogger(ObjectTransformerImpl.class.getName());
}
