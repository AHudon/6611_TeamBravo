/**
 * Team Bravo, SOEN 6611 Winter 2014
 * Coupling Factor Metric
 * @author Alexandre Hudon
 * @date March 31st, 2014
 */
package metrics;

import java.util.ArrayList;
import java.util.List;

import ast.ClassObject;
import ast.FieldObject;
import ast.MethodObject;
import ast.SystemObject;

public class CF {

	
	ArrayList<ClassObject> classes = new ArrayList<ClassObject>();
	int totalNumberOfClasses;
	double metricDenominator;
	double numerator =0;
	
	
	public CF(SystemObject system){
		
		classes.addAll(system.getClassObjects());
		totalNumberOfClasses=classes.size();
		metricDenominator = Math.pow(totalNumberOfClasses,2) - totalNumberOfClasses;
		
		for(int i=0; i<totalNumberOfClasses; i++)
		{
			ClassObject classI = classes.get(i);
			for(int j=0; j<totalNumberOfClasses; j++){
				
				ClassObject classJ = classes.get(j);
				
		
				if(!(classI.getName().equals(classJ.getName()))){
					numerator = numerator + CalculateClient(classI, classJ);
				}
			}
		}
	}
	
	
	public double CalculateClient (ClassObject clientClass, ClassObject potentialSupplier){

		List<MethodObject> methods = clientClass.getMethodList();
        List<FieldObject> fieldsInMethods = new ArrayList<FieldObject>();
		List<String> fieldTypes = new ArrayList<String>();
		String supplierName = potentialSupplier.getName();
		
		for(int i=0; i<methods.size();i++)
		{
			fieldsInMethods.addAll(clientClass.getFieldsAccessedInsideMethod(methods.get(i)));
		}

		for(int i=0; i<fieldsInMethods.size();i++)
		{
			fieldTypes.add(fieldsInMethods.get(i).getClassName());
		}
		
		if(clientClass.hasFieldType(potentialSupplier.getName()) || clientClass.isFriend(supplierName)|| fieldTypes.contains(supplierName))
		{
				return 1;
		}
		
		return 0;
	}
	
	public double getCF(){
		return 100*(numerator/metricDenominator);
	}
	
	public String toString(){
		return "\nCF for parsed project: "+ getCF()+ " %";
	}
}
