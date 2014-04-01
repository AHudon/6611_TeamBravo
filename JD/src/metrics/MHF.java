/**
 * Team Bravo, SOEN 6611 Winter 2014
 * @author: Akash Kanaujia (6560180)
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

import ast.ASTReader;
import ast.Access;
import ast.ClassObject;
import ast.MethodObject;
import ast.SystemObject;
import ast.TypeObject;

public class MHF {
	
	Set<ClassObject> classes;
	Map <String,Integer> pkgClasses = new HashMap<String,Integer>();
	
	Map <String,Integer> baseClasses = new HashMap<String,Integer>();
		
	int totalMethods = 0;
	double mthdVisibilty = 0;
	double MHFPercentage = 0;
	double classSizeExculdingContainerClass = 0;
	
		
	public MHF(SystemObject system)
	{
		this.classes = system.getClassObjects();		
		mthdVisibilty = 0;
		classSizeExculdingContainerClass = (double)classes.size() - 1d;
		
		for(ClassObject classObject : classes){	 
						
			getSuperClass(classObject);
			getAllPackages(classObject);
		}
		
		for(ClassObject classObject : classes) {		
			
			
			getClassMethodVisibility(classObject);
		}
		
		MHFPercentage =	getMHFValue();
		
		
	}
	
	private void getClassMethodVisibility(ClassObject classObject)
	{
				
		List<MethodObject> methods = classObject.getMethodList();
		
		for(MethodObject methodObject: methods)
		{			
		    totalMethods ++;		    
		    
			Access methodAccess = methodObject.getAccess();
		
	        String methodType = methodAccess.toString();        
	        
		
           mthdVisibilty = mthdVisibilty + (1d - ((double)numberofClassCallMethod(methodType, classObject)/classSizeExculdingContainerClass));		
		
		}	
		
			
	
	}
	
	private double numberofClassCallMethod(String methodType, ClassObject classObject)
	{
		switch(methodType)
		{
		  case "": 
			 return (double)getPackageClassCount(classObject) - 1d;
			
          case "public": 
        	  return (double)this.classes.size() - 1d;  
        	         	                 
          case "private":        	  
        	  return 0d;        	  
        	  
          case "protected":
        	 return (double)((getPackageClassCount(classObject) - 1d) + getBaseClassCountWithOutPackageBaseClass(classObject));
        			 
          default: return 0;
		}
		
	}
	
		
	
	private void getAllPackages(ClassObject classObject)
	{
		int index = classObject.getIFile().getFullPath().toString().lastIndexOf("/");		
		String pkgPath = classObject.getIFile().getFullPath().toString().substring(0, index);
		
		
		if(pkgClasses.containsKey(pkgPath))
		{					
			pkgClasses.put(pkgPath, pkgClasses.get(pkgPath) + 1);				
		
		}
		else
		{			
			pkgClasses.put(pkgPath, 1);
			
		}	

	}
	
	private int getPackageClassCount(ClassObject classObject)
	{
		int index = classObject.getIFile().getFullPath().toString().lastIndexOf("/");	
		String pkgPath = classObject.getIFile().getFullPath().toString().substring(0, index);
	
		return pkgClasses.get(pkgPath);
	}
	
	
	private double getMHFValue()
	{
		double MHF = (double)(mthdVisibilty/totalMethods)*100;
		
		return MHF;
		
	}
	
	
     public void getSuperClass(ClassObject classObject){
		
		try
			{
			
			//Compare Package of Super class and base class
			// In case package is same, the package visibility will handle visibility factor
			// If not in same package we will increase visibility factor by one for protected modifier
			
			int baseIndex = classObject.getIFile().getFullPath().toString().lastIndexOf("/");		
			String pkgBasePath = classObject.getIFile().getFullPath().toString().substring(0, baseIndex);
			
			//Getting Super class object
			ClassObject superclassObject = ASTReader.getSystemObject().getClassObject(classObject.getSuperclass().getClassType());
			
			int superIndex = superclassObject.getIFile().getFullPath().toString().lastIndexOf("/");	
			String pkgSuperPath = superclassObject.getIFile().getFullPath().toString().substring(0, superIndex);
			
			if(!pkgBasePath.equals(pkgSuperPath))
			{							
				String superClassName = classObject.getSuperclass().getClassType();								
								
				if(baseClasses.containsKey(superClassName))
				{					
					//System.out.println("Outside Package base Class Incremental:" + " " + superClassName);
					baseClasses.put(superClassName, baseClasses.get(superClassName) + 1);				
				
				}
				else
				{					
					//System.out.println("Outside Package base Class New:" + " "  + superClassName);
					baseClasses.put(superClassName, 1);					
				}	
			}	
			
			//System.out.println("Super Class Name : " + classObject.getSuperclass().getClassType());					
							
			}
			catch(NullPointerException e)
			{
				
			}				
		
	}
     
     private int getBaseClassCountWithOutPackageBaseClass(ClassObject classObject)
     {   	 
    	 
    	 try
    	 {
    	   if(baseClasses.containsKey(classObject.getName()))
    	  {
    		 // System.out.println("Base Class Number:" + baseClasses.get(classObject.getName()).toString());
    		  return baseClasses.get(classObject.getName());
    	   }
    	 }
    	 catch(NullPointerException e)
    	{
    		return 0;
    	 }
    	 
    	 return 0;
     }
	
	
	public String toString(){
		return "\nMHF for parsed project: "+ getMHFValue() + " %";
	}
	   


	
	
}
	
	


