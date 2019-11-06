/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elatusdev.mcmodel.model;

import java.util.List;

/**
 *
 * @author root
 */
public class TablePresentation {
    private final List<String> labels;
    private final List<List<Object>> rows;

    public TablePresentation(List<String> labels, List<List<Object>> rows) {
        this.labels = labels;
        this.rows = rows;
    }

    public List<String> getLabels() {
        return labels;
    }

    public List<List<Object>> getRows() {
        return rows;
    }
    
    
}
