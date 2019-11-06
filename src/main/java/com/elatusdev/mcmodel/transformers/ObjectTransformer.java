/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elatusdev.mcmodel.transformers;

import java.util.List;

import com.elatusdev.mcmodel.model.ListPresentation;
import com.elatusdev.mcmodel.model.TablePresentation;

/**
 *
 * @author root
 */
public interface ObjectTransformer {
    
    
    public TablePresentation toTablePresentation(final List<Object> objects);
    
    public ListPresentation toListPresentation(final Object object);
    
    public void setSymbolForEmpty(String symbol);
}
