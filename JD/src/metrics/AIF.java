/**
 * Team Bravo, SOEN 6611 Winter 2014
 * Attribute Hiding Factor Metric
 */

package metrics;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import ast.ClassObject;
import ast.FieldObject;
import ast.SystemObject;

public class AIF {
	private int totalNumberOfAttributesInheritedForEachClassObject;
	private int totalNumberOfAttributesInheritedAndDeclaredForEachClassObject;
	private SystemObject system;
	
	public AIF(SystemObject system){
		
		this.system=system;
		Set<ClassObject> classes = system.getClassObjects();
		
		for(ClassObject classObject : classes) {
			int numOfInheritedAttributesInActiveClassObject=getNumberOfAttributesInheritedInActiveClass(classObject);
			totalNumberOfAttributesInheritedForEachClassObject+=numOfInheritedAttributesInActiveClassObject;
			int numOfDeclaredAttributesInActiveClassObject=getNumberOfAttributesDeclaredInActiveClass(classObject);
			totalNumberOfAttributesInheritedAndDeclaredForEachClassObject+=(numOfInheritedAttributesInActiveClassObject+numOfDeclaredAttributesInActiveClassObject);		
		
	}
		}
	
	
	public int getNumberOfAttributesInheritedInActiveClass(ClassObject classObject)
	{
		int numOfInheritedAttributes =0;
				System.out.println("******"+classObject.getName()+"********");
		List<FieldObject> attributes =classObject.getFieldList();
		ArrayList<String> formattedAttributes = new ArrayList<String>();
		Set<String> formattedParentAttributes = new LinkedHashSet<String>();
		ClassObject subClassObject = classObject;
		Set<FieldObject> accessibleParentAttributes = new LinkedHashSet<FieldObject>();
		
		for(FieldObject fieldObject: attributes){

				formattedAttributes.add(fieldObject.getType().toString()+fieldObject.getName());			
		}
		
		
		 while((subClassObject.getSuperclass()!=null) && (system.getClassObject(subClassObject.getSuperclass().getClassType()))!=null){
		 
			 ClassObject superClassObject = system.getClassObject(subClassObject.getSuperclass().getClassType());
			 List<FieldObject> parentAttributes = superClassObject.getFieldList();
			 
				for(FieldObject fieldObject: parentAttributes){
					
					if(!fieldObject.getAccess().toString().equals("private")){
												
						formattedParentAttributes.add(fieldObject.getType().toString()+fieldObject.getName());
					}
				}
			 
			 subClassObject=superClassObject;
		 }
		 
		 int initialSize = formattedParentAttributes.size();
		 
		 for(int i=0; i<formattedAttributes.size();i++){
			 
			 if(formattedParentAttributes.contains(formattedAttributes.get(i))){
				 initialSize--;
			 }
		 }
          
		 System.out.println(formattedAttributes);
		 System.out.println(formattedParentAttributes);

		return initialSize;
		
	}
	
	public int getNumberOfAttributesDeclaredInActiveClass(ClassObject classObject)
	{
		return classObject.getFieldList().size();
	}

	public double getAIF(){

		return ((double)totalNumberOfAttributesInheritedForEachClassObject/totalNumberOfAttributesInheritedAndDeclaredForEachClassObject);
				
	}
	
	public String toString(){
		return "\nAIF for parsed project: "+ getAIF();
	}
	
}
