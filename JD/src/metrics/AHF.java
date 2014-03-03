/**
 * Team Bravo, SOEN 6611 Winter 2014
 * Author: Akash Kanaujia (6560180)
 * Attribute Hiding Factor(AHF)
 * */

package metrics;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.*;

import ast.Access;
import ast.ClassObject;
import ast.FieldObject;
import ast.SystemObject;
import ast.TypeObject;

public class AHF {
	//private SystemObject system;
	Set<ClassObject> classes;
	Map <String,Integer> subClasses = new HashMap<String,Integer>();
	List<String> pkgClasses = new ArrayList<String>();
	int pkgClassCount = 0;
	int totalAttributes = 0;
	double attrVisibilty = 0;
	double AHFPercentage = 0;
	double classSizeExculdingContainerClass = 0;
	
		
	public AHF(SystemObject system)
	{
		//this.system = system;
		this.classes = system.getClassObjects();		
		attrVisibilty = 0;
		classSizeExculdingContainerClass = (double)classes.size() - 1d;
		
		for(ClassObject classObject : classes){
		
			getAllPackages(classObject);
		}
		
		for(ClassObject classObject : classes) {		
			
			
			getClassAttributeVisibility(classObject);
		}
		
		AHFPercentage =	getAHFValue();
		
		
	}
	
	private void getClassAttributeVisibility(ClassObject classObject)
	{
		//pkgClassCount = getNumberOfClassesInPackage(classObject.getClass().getPackage());
		
		List<FieldObject> attributes = classObject.getFieldList();
		
		for(FieldObject fieldObject: attributes)
		{			
		    totalAttributes ++;		    
		    
			Access fieldAccess = fieldObject.getAccess();
		
	        String fieldType = fieldAccess.toString();        
	        
		
           attrVisibilty = attrVisibilty + (1d - ((double)numberofClassCallAttribute(fieldType, classObject)/classSizeExculdingContainerClass));		
		
		}	
		
			
	
	}
	
	private double numberofClassCallAttribute(String fieldType, ClassObject classObject)
	{
		switch(fieldType)
		{
		  case "": 
			 return (double)getPackageClassCount(classObject) - 1d;
			
          case "public": 
        	  return (double)this.classes.size() - 1d;  
        	         	                 
          case "private":        	  
        	  return 0d;        	  
        	  
          case "protected":
        	 return  (double)getPackageClassCount(classObject) - 1d;
        			 
          default: return 0;
		}
		
	}
	
		
	
	private void getAllPackages(ClassObject classObject)
	{
		int index = classObject.getIFile().getFullPath().toString().lastIndexOf("/");
		//String[] pkgNameArray = classObject.getIFile().getFullPath().toString().substring(0, index);
		String pkgPath = classObject.getIFile().getFullPath().toString().substring(0, index);
		
		System.out.println(pkgPath);
		 
		
		if(subClasses.containsKey(pkgPath))
		{					
			subClasses.put(pkgPath, subClasses.get(pkgPath) + 1);				
		
		}
		else
		{			
			subClasses.put(pkgPath, 1);
			
		}	

	}
	
	private int getPackageClassCount(ClassObject classObject)
	{
		int index = classObject.getIFile().getFullPath().toString().lastIndexOf("/");
		//String[] pkgNameArray = classObject.getIFile().getFullPath().toString().substring(0, index);
		String pkgPath = classObject.getIFile().getFullPath().toString().substring(0, index);
	
		return subClasses.get(pkgPath);
	}
	
	
	private double getAHFValue()
	{
		double AHF = (double)(attrVisibilty/totalAttributes)*100;
		
		return AHF;
		
	}
	
	public String toString(){
		return "\nAHF for parsed project: "+ getAHFValue() + " %";
	}
	   


}
	
	


