/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elatusdev.elatusph.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author root
 */
public class TestClassC {
    
    public TestClassC(){
        collection = new ArrayList<>();
    }
    
    private final List<TestClassB> collection; 
}
