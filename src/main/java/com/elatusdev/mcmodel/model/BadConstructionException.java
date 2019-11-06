/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elatusdev.mcmodel.model;

/**
 *
 * @author root
 */
public class BadConstructionException extends Exception{
    
    private static final long serialVersionUID = 1L;
    private String message;
    
    public BadConstructionException(String msg){
        super(msg);
    }
    
    @Override
    public String getMessage(){
        return this.message;
    }
}
