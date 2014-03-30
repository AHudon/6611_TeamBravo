/**
 * Team Bravo, SOEN 6611 Winter 2014
 * Coupling Factor Metric
 */

package metrics;

import java.util.Set;

import ast.ClassObject;
import ast.SystemObject;

public class CF {

	
	Set<ClassObject> classes;
	int totalNumberOfClasses;
	double metricDenominator;
	
	
	public CF(SystemObject system){
		
		classes=system.getClassObjects();
		totalNumberOfClasses=classes.size();
		metricDenominator = Math.pow(totalNumberOfClasses,2) - totalNumberOfClasses;
		
	}
}
