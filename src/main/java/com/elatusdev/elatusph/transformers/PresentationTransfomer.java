/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elatusdev.elatusph.transformers;

import com.elatusdev.elatusph.transformersimpl.PresentationTransformerImpl;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 *
 * @author root
 */
public interface PresentationTransfomer {
    
    public void toObject(List<String> labels, List<Object> data, final Class<?> cls);
    
    public void toObjects(List<String> labels, List<List<Object>> data, final Class<?> cls);
    
    public void setInstanceCreator(Function<Class<?>, Object> createObj);

    public void setOnSuccessConsumer(Consumer<Object> consumeOnSuccess);
    
    public void setOnSuccessListConsumer(Consumer<List<Object>> consumeOnSuccess);

    public void setOnErrorConsumer(Consumer<List<String>> consumerOnError);
}
