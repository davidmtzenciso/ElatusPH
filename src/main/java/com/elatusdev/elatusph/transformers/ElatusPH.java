/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elatusdev.elatusph.transformers;

import com.elatusdev.elatusph.model.BadConstructionException;
import com.elatusdev.elatusph.model.ListPresentation;
import com.elatusdev.elatusph.model.TablePresentation;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 *
 * @author root
 */
public interface ElatusPH {
    
    public ElatusPH fromPresentationToObject(List<String> labels, List<Object> data, Class<?> cls);
    
    public ElatusPH fromPresentationToCollection(List<String> labels, List<List<Object>> data, Class<?> cls);
    
    public ElatusPH setInstanceCreator(Function<Class<?>, Object> function);
    
    public ElatusPH onObjCreation(Consumer<Object> consumer);
    
    public ElatusPH onCollectionCreation(Consumer<List<Object>> consumer);
    
    public ElatusPH onError(Consumer<List<String>> consumer);
    
    public void transforme() throws BadConstructionException;
    
    public ListPresentation fromObjectToList(Object obj);
    
    public TablePresentation fromObjectsToTable(List<Object> objects);
    
    
    
}
