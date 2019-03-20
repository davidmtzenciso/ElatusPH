/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elatusdev.elatusph.transformers;

import com.elatusdev.elatusph.model.ListPresentation;
import com.elatusdev.elatusph.model.TablePresentation;
import java.util.List;

/**
 *
 * @author root
 */
public interface ObjectTransformer {
    
    
    public TablePresentation toTablePresentation(final List<Object> objects);
    
    public ListPresentation toListPresentation(final Object object);
    
    public void setSymbolForEmpty(String symbol);
}
