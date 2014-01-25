package ast;

import ast.decomposition.MethodBodyObject;
import ast.decomposition.cfg.AbstractVariable;
import ast.decomposition.cfg.PlainVariable;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.core.dom.MethodDeclaration;

public class ConstructorObject implements AbstractMethodDeclaration {

    protected String name;
	protected List<ParameterObject> parameterList;
    protected Access access;
    protected String className;
    protected MethodBodyObject methodBody;
    //protected MethodDeclaration methodDeclaration;
    protected ASTInformation methodDeclaration;
    private volatile int hashCode = 0;

    public ConstructorObject() {
		this.parameterList = new ArrayList<ParameterObject>();
        this.access = Access.NONE;
    }

    public void setMethodDeclaration(MethodDeclaration methodDeclaration) {
    	//this.methodDeclaration = methodDeclaration;
    	this.methodDeclaration = ASTInformationGenerator.generateASTInformation(methodDeclaration);
    }

    public MethodDeclaration getMethodDeclaration() {
    	//return this.methodDeclaration;
    	return (MethodDeclaration)this.methodDeclaration.recoverASTNode();
    }

    public void setMethodBody(MethodBodyObject methodBody) {
    	this.methodBody = methodBody;
    }

    public MethodBodyObject getMethodBody() {
    	return this.methodBody;
    }

    public void setAccess(Access access) {
        this.access = access;
    }

    public Access getAccess() {
        return access;
    }

    public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassName() {
        return this.className;
    }

	public boolean addParameter(ParameterObject parameter) {
		return parameterList.add(parameter);
	}

    public ListIterator<ParameterObject> getParameterListIterator() {
		return parameterList.listIterator();
	}

    public ParameterObject getParameter(int position) {
    	if(position >= 0 && position < parameterList.size())
    		return parameterList.get(position);
    	else
    		return null;
    }

	public List<MethodInvocationObject> getMethodInvocations() {
		if(methodBody != null)
			return methodBody.getMethodInvocations();
		else
			return new ArrayList<MethodInvocationObject>();
	}

	public List<SuperMethodInvocationObject> getSuperMethodInvocations() {
		if(methodBody != null)
			return methodBody.getSuperMethodInvocations();
		else
			return new ArrayList<SuperMethodInvocationObject>();
	}

    public List<FieldInstructionObject> getFieldInstructions() {
    	if(methodBody != null)
    		return methodBody.getFieldInstructions();
    	else
    		return new ArrayList<FieldInstructionObject>();
    }

    public List<SuperFieldInstructionObject> getSuperFieldInstructions() {
    	if(methodBody != null)
    		return methodBody.getSuperFieldInstructions();
    	else
    		return new ArrayList<SuperFieldInstructionObject>();
    }

    public List<LocalVariableDeclarationObject> getLocalVariableDeclarations() {
    	if(methodBody != null)
    		return methodBody.getLocalVariableDeclarations();
    	else
    		return new ArrayList<LocalVariableDeclarationObject>();
    }

    public List<LocalVariableInstructionObject> getLocalVariableInstructions() {
    	if(methodBody != null)
    		return methodBody.getLocalVariableInstructions();
    	else
    		return new ArrayList<LocalVariableInstructionObject>();
    }

	public List<CreationObject> getCreations() {
		if(methodBody != null)
			return methodBody.getCreations();
		else
			return new ArrayList<CreationObject>();
	}

	public List<LiteralObject> getLiterals() {
		if(methodBody != null)
			return methodBody.getLiterals();
		else
			return new ArrayList<LiteralObject>();
	}

    public Set<String> getExceptionsInThrowStatements() {
    	if(methodBody != null)
			return methodBody.getExceptionsInThrowStatements();
		else
			return new LinkedHashSet<String>();
    }

    public boolean containsMethodInvocation(MethodInvocationObject methodInvocation) {
    	if(methodBody != null)
    		return methodBody.containsMethodInvocation(methodInvocation);
    	else
    		return false;
    }

    public boolean containsFieldInstruction(FieldInstructionObject fieldInstruction) {
    	if(methodBody != null)
    		return methodBody.containsFieldInstruction(fieldInstruction);
    	else
    		return false;
    }

    public boolean containsSuperMethodInvocation(SuperMethodInvocationObject superMethodInvocation) {
    	if(methodBody != null)
    		return methodBody.containsSuperMethodInvocation(superMethodInvocation);
    	else
    		return false;
    }

	public Map<AbstractVariable, LinkedHashSet<MethodInvocationObject>> getInvokedMethodsThroughFields() {
		if(methodBody != null)
			return methodBody.getInvokedMethodsThroughFields();
		else
			return new LinkedHashMap<AbstractVariable, LinkedHashSet<MethodInvocationObject>>();
	}

	public Map<AbstractVariable, ArrayList<MethodInvocationObject>> getNonDistinctInvokedMethodsThroughFields() {
		if(methodBody != null)
			return methodBody.getNonDistinctInvokedMethodsThroughFields();
		else
			return new LinkedHashMap<AbstractVariable, ArrayList<MethodInvocationObject>>();
	}

	public Map<AbstractVariable, LinkedHashSet<MethodInvocationObject>> getInvokedMethodsThroughParameters() {
		if(methodBody != null)
			return methodBody.getInvokedMethodsThroughParameters();
		else
			return new LinkedHashMap<AbstractVariable, LinkedHashSet<MethodInvocationObject>>();
	}

	public Map<AbstractVariable, ArrayList<MethodInvocationObject>> getNonDistinctInvokedMethodsThroughParameters() {
		if(methodBody != null)
			return methodBody.getNonDistinctInvokedMethodsThroughParameters();
		else
			return new LinkedHashMap<AbstractVariable, ArrayList<MethodInvocationObject>>();
	}

	public Map<AbstractVariable, LinkedHashSet<MethodInvocationObject>> getInvokedMethodsThroughLocalVariables() {
		if(methodBody != null)
			return methodBody.getInvokedMethodsThroughLocalVariables();
		else
			return new LinkedHashMap<AbstractVariable, LinkedHashSet<MethodInvocationObject>>();
	}

	public Set<MethodInvocationObject> getInvokedMethodsThroughThisReference() {
		if(methodBody != null)
			return methodBody.getInvokedMethodsThroughThisReference();
		else
			return new LinkedHashSet<MethodInvocationObject>();
	}

	public List<MethodInvocationObject> getNonDistinctInvokedMethodsThroughThisReference() {
		if(methodBody != null)
			return methodBody.getNonDistinctInvokedMethodsThroughThisReference();
		else
			return new ArrayList<MethodInvocationObject>();
	}

	public Set<MethodInvocationObject> getInvokedStaticMethods() {
		if(methodBody != null)
			return methodBody.getInvokedStaticMethods();
		else
			return new LinkedHashSet<MethodInvocationObject>();
	}

	public Set<AbstractVariable> getDefinedFieldsThroughFields() {
		if(methodBody != null)
			return methodBody.getDefinedFieldsThroughFields();
		else
			return new LinkedHashSet<AbstractVariable>();
	}

	public Set<AbstractVariable> getUsedFieldsThroughFields() {
		if(methodBody != null)
			return methodBody.getUsedFieldsThroughFields();
		else
			return new LinkedHashSet<AbstractVariable>();
	}

	public List<AbstractVariable> getNonDistinctDefinedFieldsThroughFields() {
		if(methodBody != null)
			return methodBody.getNonDistinctDefinedFieldsThroughFields();
		else
			return new ArrayList<AbstractVariable>();
	}

	public List<AbstractVariable> getNonDistinctUsedFieldsThroughFields() {
		if(methodBody != null)
			return methodBody.getNonDistinctUsedFieldsThroughFields();
		else
			return new ArrayList<AbstractVariable>();
	}

	public Set<AbstractVariable> getDefinedFieldsThroughParameters() {
		if(methodBody != null)
			return methodBody.getDefinedFieldsThroughParameters();
		else
			return new LinkedHashSet<AbstractVariable>();
	}

	public Set<AbstractVariable> getUsedFieldsThroughParameters() {
		if(methodBody != null)
			return methodBody.getUsedFieldsThroughParameters();
		else
			return new LinkedHashSet<AbstractVariable>();
	}

	public List<AbstractVariable> getNonDistinctDefinedFieldsThroughParameters() {
		if(methodBody != null)
			return methodBody.getNonDistinctDefinedFieldsThroughParameters();
		else
			return new ArrayList<AbstractVariable>();
	}

	public List<AbstractVariable> getNonDistinctUsedFieldsThroughParameters() {
		if(methodBody != null)
			return methodBody.getNonDistinctUsedFieldsThroughParameters();
		else
			return new ArrayList<AbstractVariable>();
	}

	public Set<AbstractVariable> getDefinedFieldsThroughLocalVariables() {
		if(methodBody != null)
			return methodBody.getDefinedFieldsThroughLocalVariables();
		else
			return new LinkedHashSet<AbstractVariable>();
	}

	public Set<AbstractVariable> getUsedFieldsThroughLocalVariables() {
		if(methodBody != null)
			return methodBody.getUsedFieldsThroughLocalVariables();
		else
			return new LinkedHashSet<AbstractVariable>();
	}

	public Set<PlainVariable> getDefinedFieldsThroughThisReference() {
		if(methodBody != null)
			return methodBody.getDefinedFieldsThroughThisReference();
		else
			return new LinkedHashSet<PlainVariable>();
	}

	public List<PlainVariable> getNonDistinctDefinedFieldsThroughThisReference() {
		if(methodBody != null)
			return methodBody.getNonDistinctDefinedFieldsThroughThisReference();
		else
			return new ArrayList<PlainVariable>();
	}

	public Set<PlainVariable> getUsedFieldsThroughThisReference() {
		if(methodBody != null)
			return methodBody.getUsedFieldsThroughThisReference();
		else
			return new LinkedHashSet<PlainVariable>();
	}

	public List<PlainVariable> getNonDistinctUsedFieldsThroughThisReference() {
		if(methodBody != null)
			return methodBody.getNonDistinctUsedFieldsThroughThisReference();
		else
			return new ArrayList<PlainVariable>();
	}

	public Set<PlainVariable> getDeclaredLocalVariables() {
		if(methodBody != null)
			return methodBody.getDeclaredLocalVariables();
		else
			return new LinkedHashSet<PlainVariable>();
	}

	public Set<PlainVariable> getDefinedLocalVariables() {
		if(methodBody != null)
			return methodBody.getDefinedLocalVariables();
		else
			return new LinkedHashSet<PlainVariable>();
	}

	public Set<PlainVariable> getUsedLocalVariables() {
		if(methodBody != null)
			return methodBody.getUsedLocalVariables();
		else
			return new LinkedHashSet<PlainVariable>();
	}

	public Map<PlainVariable, LinkedHashSet<MethodInvocationObject>> getParametersPassedAsArgumentsInMethodInvocations() {
		if(methodBody != null)
			return methodBody.getParametersPassedAsArgumentsInMethodInvocations();
		else
			return new LinkedHashMap<PlainVariable, LinkedHashSet<MethodInvocationObject>>();
	}

	public Map<PlainVariable, LinkedHashSet<SuperMethodInvocationObject>> getParametersPassedAsArgumentsInSuperMethodInvocations() {
		if(methodBody != null)
			return methodBody.getParametersPassedAsArgumentsInSuperMethodInvocations();
		else
			return new LinkedHashMap<PlainVariable, LinkedHashSet<SuperMethodInvocationObject>>();
	}

    public boolean containsSuperMethodInvocation() {
    	if(methodBody != null)
    		return methodBody.containsSuperMethodInvocation();
    	else
    		return false;
    }

    public boolean containsSuperFieldAccess() {
    	if(methodBody != null)
    		return methodBody.containsSuperFieldAccess();
    	else
    		return false;
    }

    public List<TypeObject> getParameterTypeList() {
    	List<TypeObject> list = new ArrayList<TypeObject>();
    	for(ParameterObject parameterObject : parameterList)
    		list.add(parameterObject.getType());
    	return list;
    }

    public List<String> getParameterList() {
    	List<String> list = new ArrayList<String>();
    	for(ParameterObject parameterObject : parameterList)
    		list.add(parameterObject.getType().toString());
    	return list;
    }

    public boolean equals(ClassInstanceCreationObject creationObject) {
    	return this.className.equals(creationObject.getType().getClassType()) && this.getParameterTypeList().equals(creationObject.getParameterTypeList());
    }

    public boolean equals(Object o) {
        if(this == o) {
			return true;
		}

		if (o instanceof ConstructorObject) {
			ConstructorObject constructorObject = (ConstructorObject)o;

			return this.className.equals(constructorObject.className) && this.name.equals(constructorObject.name) &&
				this.parameterList.equals(constructorObject.parameterList);
		}
		return false;
    }

    public int hashCode() {
    	if(hashCode == 0) {
    		int result = 17;
    		result = 37*result + className.hashCode();
    		result = 37*result + name.hashCode();
    		result = 37*result + parameterList.hashCode();
    		hashCode = result;
    	}
    	return hashCode;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        if(!access.equals(Access.NONE))
            sb.append(access.toString()).append(" ");
        sb.append(name);
        sb.append("(");
        if(!parameterList.isEmpty()) {
            for(int i=0; i<parameterList.size()-1; i++)
                sb.append(parameterList.get(i).toString()).append(", ");
            sb.append(parameterList.get(parameterList.size()-1).toString());
        }
        sb.append(")");
        if(methodBody != null)
        	sb.append("\n").append(methodBody.toString());
        return sb.toString();
    }
}