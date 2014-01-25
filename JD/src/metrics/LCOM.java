package metrics;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ast.ClassObject;
import ast.FieldInstructionObject;
import ast.MethodObject;
import ast.SystemObject;

public class LCOM {
	
	private Map<String, Integer> cohesionMap;

	public LCOM(SystemObject system) {
		cohesionMap = new HashMap<String, Integer>();
		
		Set<ClassObject> classes = system.getClassObjects();
		
		for(ClassObject classObject : classes) {
			int cohesion = computeCohesion(classObject);
			if(cohesion != -1) {
				cohesionMap.put(classObject.getName(), cohesion);
			}
		}
		
	}
	
	private int computeCohesion(ClassObject classObject) {
		
		List<MethodObject> methods = classObject.getMethodList();
		int p = 0;
		int q = 0;
		
		if(methods.size() < 2) {
			return -1;
		}
		
		for(int i=0; i<methods.size()-1; i++) {
			MethodObject mI = methods.get(i);
			List<FieldInstructionObject> attributesI = mI.getFieldInstructions();
			for(int j=i+1; j<methods.size(); j++) {
				MethodObject mJ = methods.get(j);
				List<FieldInstructionObject> attributesJ = mJ.getFieldInstructions();
				
				Set<FieldInstructionObject> intersection = commonAttributes(attributesI, attributesJ, classObject.getName());
				if(intersection.isEmpty()) {
					p++;
				} else {
					q++;
				}
				
			}
		}
		
		if (p > q) {
			return p - q;
		} else {
			return 0;
		}
	}
	
	private Set<FieldInstructionObject> commonAttributes(List<FieldInstructionObject> attributesI,
			List<FieldInstructionObject> attributesJ, String className) {
		
		Set<FieldInstructionObject> commonAttributes = new HashSet<FieldInstructionObject>();
		for (FieldInstructionObject instructionI : attributesI) {
			if(instructionI.getOwnerClass().equals(className) && attributesJ.contains(instructionI)) {
				commonAttributes.add(instructionI);
			}
		}
		return commonAttributes;
		
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(String key : cohesionMap.keySet()) {
			sb.append(key).append("\t").append(cohesionMap.get(key)).append("\n");
		}
		return sb.toString();
	}
}