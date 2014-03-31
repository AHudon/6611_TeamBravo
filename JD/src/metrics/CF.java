/**
 * Team Bravo, SOEN 6611 Winter 2014
 * Coupling Factor Metric
 */

package metrics;

import java.util.ArrayList;
import java.util.Set;

import ast.ClassObject;
import ast.SystemObject;

public class CF {

	
	ArrayList<ClassObject> classes;
	int totalNumberOfClasses;
	double metricDenominator;
	
	
	public CF(SystemObject system){
		
		classes.addAll(system.getClassObjects());
		
		totalNumberOfClasses=classes.size();
		metricDenominator = Math.pow(totalNumberOfClasses,2) - totalNumberOfClasses;
		System.out.println(metricDenominator);
		
		for(int i=0; i<totalNumberOfClasses; i++)
		{
			ClassObject classI = classes.get(i);
			for(int j=0; j<totalNumberOfClasses; j++){
				
				ClassObject classJ = classes.get(j);
				
				if(!(classI.getName().equals(classJ.getName()))){
					CalculateClient(classI, classJ);
				}
			}
		}
	}
	
	
	public int CalculateClient (ClassObject clientClass, ClassObject potentialSupplier){
		return 0;
	}
	
}
