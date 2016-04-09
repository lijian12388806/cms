package com.xkw.utils;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
 
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
 
/**
 *
 * @author BAMBOO
 */
public class MyUUID {
	
    public List<String> uuidlist=new ArrayList<String>();
    
    private static MyUUID instance = new MyUUID();  
     private MyUUID (){}
     public static MyUUID getInstance() {  
     return instance;  
     }  
    public  HashSet<String> createUUID(Integer number){
    	
    	HashSet<String> uuidSet = new HashSet<String>();
  		for(int i=0;i<number;i++){
  			uuidSet.add(UUID.randomUUID().toString().replaceAll("-", "").substring(0, 12).toUpperCase());
  		}
  		return uuidSet;

    }
}
