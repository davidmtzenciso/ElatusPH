/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elatusdev.elatusph.model;

import java.util.List;

/**
 *
 * @author root
 */
public class ListPresentation {
    
    private final List<String> labels;
    private final List<Object> data;

    public ListPresentation(List<String> labels, List<Object> data) {
        this.labels = labels;
        this.data = data;
    }

    public List<String> getLabels() {
        return labels;
    }
    
    public List<Object> getData() {
        return data;
    }
    
}

