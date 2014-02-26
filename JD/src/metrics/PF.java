/**
 * Team Bravo, SOEN 6611 Winter 2014
 * Polymorphism Factor Metric
 * @author Alexandre Hudon
 * @date February 26th, 2014
 */

package metrics;

import java.util.List;
import java.util.Set;

import ast.ClassObject;
import ast.MethodObject;
import ast.SystemObject;

public class PF {

	int sumOfOveriddingMethods=0;
	int sumOfNewMethodsTimesDescendingClasses=0;
	
	Set<ClassObject> classes;
	public PF(SystemObject system){
		
		classes = system.getClassObjects();
		
		for(ClassObject classObject : classes){
			
			sumOfOveriddingMethods= sumOfOveriddingMethods + getNumberOfOverridingMethodsInActiveClass(classObject);
			sumOfNewMethodsTimesDescendingClasses = sumOfNewMethodsTimesDescendingClasses + getNumberOfNewMethodsTimesNumberOfDescendingClassesForActiveClass(classObject);
		}
	}
	
	public int getNumberOfOverridingMethodsInActiveClass(ClassObject classObject){
		
		int overiddingMethodsCounter =0;
		List<MethodObject> listOfMethods = classObject.getMethodList();
		
		for(MethodObject methodObject : listOfMethods){
			
			if(methodObject.overridesMethod()){
				overiddingMethodsCounter++;
			}
			
		}
		
		
		return overiddingMethodsCounter;
	}
	
	public int getNumberOfNewMethodsTimesNumberOfDescendingClassesForActiveClass(ClassObject classObject){
		
		int numberOfNewMethods = getNumberOfNewMethodsInActiveClass(classObject);
		int numberOfDescendants = getNumberOfClassesDescendingFromActiveClass(classObject);
		
		return numberOfNewMethods * numberOfDescendants ;
	}
	
	public int getNumberOfNewMethodsInActiveClass(ClassObject classObject){
		
		int newMethodsCounter =0;
		List<MethodObject> listOfMethods = classObject.getMethodList();
		
		for(MethodObject methodObject : listOfMethods){
			
			if(!methodObject.overridesMethod()){
				newMethodsCounter++;
			}
			
		}
		
		
		return newMethodsCounter;
		
	}
	
	public int getNumberOfClassesDescendingFromActiveClass(ClassObject classObject){
		
		int descendantsCounter = 0;
		
		for(ClassObject currentClassObject : classes){
			
			
			try
			{
				if(currentClassObject.getSuperclass().toString().equals(classObject.getName().toString())){
					descendantsCounter++;
				}
			}
			catch(NullPointerException e)
			{
				//Reaching this point indicates that the class had no super class.
			}
		}
		
		return descendantsCounter;
	}
	
	
	public double getPF(){
		return ((double)sumOfOveriddingMethods/sumOfNewMethodsTimesDescendingClasses);
	}
	
	public String toString(){
		return "\nPF for parsed project: "+ getPF();
	}
	
}
