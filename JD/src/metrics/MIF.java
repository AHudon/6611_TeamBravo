/**
 * Team Bravo, SOEN 6611 Winter 2014
 * Method Inheritance Factor Metric
 * @author Alexandre Hudon
 * @date February 17th, 2014
 * 
 * Method Inheritance Factor gives information about the use of overriding versus not declaring any methods (redundancy in inheritance).
 */

package metrics;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import ast.ClassObject;
import ast.MethodObject;
import ast.SystemObject;

public class MIF {
	
	private int totalNumberOfMethodsInheritedForEachClassObject;
	private int totalNumberOfMethodsInheritedAndDeclaredForEachClassObject;
	private SystemObject system;
	
	public MIF(SystemObject system){
		
		this.system=system;
		Set<ClassObject> classes = system.getClassObjects();
		
		for(ClassObject classObject : classes) {
			int numOfInheritedMethodInActiveClassObject=getNumberOfMethodsInheritedInActiveClass(classObject);
			totalNumberOfMethodsInheritedForEachClassObject+=numOfInheritedMethodInActiveClassObject;
			int numOfDeclaredMethodsInActiveClassObject=getNumberOfMethodsDeclaredInActiveClass(classObject);
			totalNumberOfMethodsInheritedAndDeclaredForEachClassObject+=(numOfInheritedMethodInActiveClassObject+numOfDeclaredMethodsInActiveClassObject);		
		}
	}
	
	
	public int getNumberOfMethodsInheritedInActiveClass(ClassObject classObject)
	{
		int numOfInheritedMethods =0;
				
		List<MethodObject> methods=classObject.getMethodList();
		List<MethodObject> overridesList = new ArrayList<MethodObject>();
		List<MethodObject> superMethods =  new ArrayList<MethodObject>();
		ClassObject subClassObject = classObject;
		
		for(MethodObject methodObject : methods){
			
			if(methodObject.overridesMethod()){
				overridesList.add(methodObject);
			}
		}
		
		 while((subClassObject.getSuperclass()!=null) && (system.getClassObject(subClassObject.getSuperclass().getClassType()))!=null){
		 
			 ClassObject superClassObject = system.getClassObject(subClassObject.getSuperclass().getClassType());
			 superMethods.addAll(superClassObject.getMethodList());
			 subClassObject=superClassObject;
		 }
		 
		for(MethodObject methodObject : superMethods){
			
			boolean isContainedInOverridesList=false;
			
			for(int i=0; i<overridesList.size();i++){
				if(overridesList.get(i).getSignature().equals(methodObject.getSignature())){
					isContainedInOverridesList=true;
				}
			}
			
			if(!isContainedInOverridesList){
				numOfInheritedMethods++;
			}
		}
		
		return numOfInheritedMethods;
	}
	
	public int getNumberOfMethodsDeclaredInActiveClass(ClassObject classObject)
	{
		return classObject.getNumberOfMethods();
	}

	public double getMIF(){

		return ((double)totalNumberOfMethodsInheritedForEachClassObject/totalNumberOfMethodsInheritedAndDeclaredForEachClassObject);
				
	}
	
	public String toString(){
		return "\nMIF for parsed project: "+ getMIF();
	}
	
}
