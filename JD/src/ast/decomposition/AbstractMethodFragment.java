package ast.decomposition;

import ast.ArrayCreationObject;
import ast.ClassInstanceCreationObject;
import ast.CreationObject;
import ast.FieldInstructionObject;
import ast.LiteralObject;
import ast.LocalVariableDeclarationObject;
import ast.LocalVariableInstructionObject;
import ast.MethodInvocationObject;
import ast.SuperFieldInstructionObject;
import ast.SuperMethodInvocationObject;
import ast.TypeObject;
import ast.decomposition.cfg.AbstractVariable;
import ast.decomposition.cfg.PlainVariable;
import util.MethodDeclarationUtility;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.core.dom.ArrayCreation;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.PostfixExpression;
import org.eclipse.jdt.core.dom.PrefixExpression;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SuperFieldAccess;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;
import org.eclipse.jdt.core.dom.ThisExpression;
import org.eclipse.jdt.core.dom.ThrowStatement;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.VariableDeclaration;

public abstract class AbstractMethodFragment {
	private AbstractMethodFragment parent;
	
	private List<MethodInvocationObject> methodInvocationList;
	private List<SuperMethodInvocationObject> superMethodInvocationList;
	private List<FieldInstructionObject> fieldInstructionList;
	private List<SuperFieldInstructionObject> superFieldInstructionList;
	private List<LocalVariableDeclarationObject> localVariableDeclarationList;
	private List<LocalVariableInstructionObject> localVariableInstructionList;
	private List<CreationObject> creationList;
	private List<LiteralObject> literalList;
	private Set<String> exceptionsInThrowStatements;
	//private Map<AbstractVariable, LinkedHashSet<MethodInvocationObject>> invokedMethodsThroughFields;
	private Map<AbstractVariable, ArrayList<MethodInvocationObject>> nonDistinctInvokedMethodsThroughFields;
	//private Map<AbstractVariable, LinkedHashSet<MethodInvocationObject>> invokedMethodsThroughParameters;
	private Map<AbstractVariable, ArrayList<MethodInvocationObject>> nonDistinctInvokedMethodsThroughParameters;
	//private Map<AbstractVariable, LinkedHashSet<MethodInvocationObject>> invokedMethodsThroughLocalVariables;
	private Map<AbstractVariable, ArrayList<MethodInvocationObject>> nonDistinctInvokedMethodsThroughLocalVariables;
	//private Set<MethodInvocationObject> invokedMethodsThroughThisReference;
	private List<MethodInvocationObject> nonDistinctInvokedMethodsThroughThisReference;
	private Set<MethodInvocationObject> invokedStaticMethods;
	
	//private Set<AbstractVariable> definedFieldsThroughFields;
	private List<AbstractVariable> nonDistinctDefinedFieldsThroughFields;
	//private Set<AbstractVariable> usedFieldsThroughFields;
	private List<AbstractVariable> nonDistinctUsedFieldsThroughFields;
	//private Set<AbstractVariable> definedFieldsThroughParameters;
	private List<AbstractVariable> nonDistinctDefinedFieldsThroughParameters;
	//private Set<AbstractVariable> usedFieldsThroughParameters;
	private List<AbstractVariable> nonDistinctUsedFieldsThroughParameters;
	//private Set<AbstractVariable> definedFieldsThroughLocalVariables;
	private List<AbstractVariable> nonDistinctDefinedFieldsThroughLocalVariables;
	//private Set<AbstractVariable> usedFieldsThroughLocalVariables;
	private List<AbstractVariable> nonDistinctUsedFieldsThroughLocalVariables;
	//private Set<PlainVariable> definedFieldsThroughThisReference;
	private List<PlainVariable> nonDistinctDefinedFieldsThroughThisReference;
	//private Set<PlainVariable> usedFieldsThroughThisReference;
	private List<PlainVariable> nonDistinctUsedFieldsThroughThisReference;
	
	private Set<PlainVariable> declaredLocalVariables;
	private Set<PlainVariable> definedLocalVariables;
	private Set<PlainVariable> usedLocalVariables;
	private Map<PlainVariable, LinkedHashSet<MethodInvocationObject>> parametersPassedAsArgumentsInMethodInvocations;
	private Map<PlainVariable, LinkedHashSet<SuperMethodInvocationObject>> parametersPassedAsArgumentsInSuperMethodInvocations;
	
	protected AbstractMethodFragment(AbstractMethodFragment parent) {
		this.parent = parent;
		this.methodInvocationList = new ArrayList<MethodInvocationObject>();
		this.superMethodInvocationList = new ArrayList<SuperMethodInvocationObject>();
		this.fieldInstructionList = new ArrayList<FieldInstructionObject>();
		this.superFieldInstructionList = new ArrayList<SuperFieldInstructionObject>();
		this.localVariableDeclarationList = new ArrayList<LocalVariableDeclarationObject>();
		this.localVariableInstructionList = new ArrayList<LocalVariableInstructionObject>();
		this.creationList = new ArrayList<CreationObject>();
		this.literalList = new ArrayList<LiteralObject>();
		this.exceptionsInThrowStatements = new LinkedHashSet<String>();
		//this.invokedMethodsThroughFields = new LinkedHashMap<AbstractVariable, LinkedHashSet<MethodInvocationObject>>();
		this.nonDistinctInvokedMethodsThroughFields = new LinkedHashMap<AbstractVariable, ArrayList<MethodInvocationObject>>();
		//this.invokedMethodsThroughParameters = new LinkedHashMap<AbstractVariable, LinkedHashSet<MethodInvocationObject>>();
		this.nonDistinctInvokedMethodsThroughParameters = new LinkedHashMap<AbstractVariable, ArrayList<MethodInvocationObject>>();
		//this.invokedMethodsThroughLocalVariables = new LinkedHashMap<AbstractVariable, LinkedHashSet<MethodInvocationObject>>();
		this.nonDistinctInvokedMethodsThroughLocalVariables = new LinkedHashMap<AbstractVariable, ArrayList<MethodInvocationObject>>();
		//this.invokedMethodsThroughThisReference = new LinkedHashSet<MethodInvocationObject>();
		this.nonDistinctInvokedMethodsThroughThisReference = new ArrayList<MethodInvocationObject>();
		this.invokedStaticMethods = new LinkedHashSet<MethodInvocationObject>();
		
		//this.definedFieldsThroughFields = new LinkedHashSet<AbstractVariable>();
		this.nonDistinctDefinedFieldsThroughFields = new ArrayList<AbstractVariable>();
		//this.usedFieldsThroughFields = new LinkedHashSet<AbstractVariable>();
		this.nonDistinctUsedFieldsThroughFields = new ArrayList<AbstractVariable>();
		//this.definedFieldsThroughParameters = new LinkedHashSet<AbstractVariable>();
		this.nonDistinctDefinedFieldsThroughParameters = new ArrayList<AbstractVariable>();
		//this.usedFieldsThroughParameters = new LinkedHashSet<AbstractVariable>();
		this.nonDistinctUsedFieldsThroughParameters = new ArrayList<AbstractVariable>();
		//this.definedFieldsThroughLocalVariables = new LinkedHashSet<AbstractVariable>();
		this.nonDistinctDefinedFieldsThroughLocalVariables = new ArrayList<AbstractVariable>();
		//this.usedFieldsThroughLocalVariables = new LinkedHashSet<AbstractVariable>();
		this.nonDistinctUsedFieldsThroughLocalVariables = new ArrayList<AbstractVariable>();
		//this.definedFieldsThroughThisReference = new LinkedHashSet<PlainVariable>();
		this.nonDistinctDefinedFieldsThroughThisReference = new ArrayList<PlainVariable>();
		//this.usedFieldsThroughThisReference = new LinkedHashSet<PlainVariable>();
		this.nonDistinctUsedFieldsThroughThisReference = new ArrayList<PlainVariable>();
		
		this.declaredLocalVariables = new LinkedHashSet<PlainVariable>();
		this.definedLocalVariables = new LinkedHashSet<PlainVariable>();
		this.usedLocalVariables = new LinkedHashSet<PlainVariable>();
		this.parametersPassedAsArgumentsInMethodInvocations = new LinkedHashMap<PlainVariable, LinkedHashSet<MethodInvocationObject>>();
		this.parametersPassedAsArgumentsInSuperMethodInvocations = new LinkedHashMap<PlainVariable, LinkedHashSet<SuperMethodInvocationObject>>();
	}

    public AbstractMethodFragment getParent() {
    	return this.parent;
    }

	protected void processVariables(List<Expression> variableInstructions, List<Expression> assignments,
			List<Expression> postfixExpressions, List<Expression> prefixExpressions) {
		for(Expression variableInstruction : variableInstructions) {
			SimpleName simpleName = (SimpleName)variableInstruction;
			IBinding binding = simpleName.resolveBinding();
			if(binding != null && binding.getKind() == IBinding.VARIABLE) {
				IVariableBinding variableBinding = (IVariableBinding)binding;
				if(variableBinding.isField()) {
					if(variableBinding.getDeclaringClass() != null) {
						String originClassName = variableBinding.getDeclaringClass().getQualifiedName();
						String qualifiedName = variableBinding.getType().getQualifiedName();
						TypeObject fieldType = TypeObject.extractTypeObject(qualifiedName);
						String fieldName = variableBinding.getName();
						if(!originClassName.equals("")) {
							if(simpleName.getParent() instanceof SuperFieldAccess) {
								SuperFieldInstructionObject superFieldInstruction = new SuperFieldInstructionObject(originClassName, fieldType, fieldName);
								superFieldInstruction.setSimpleName(simpleName);
								if((variableBinding.getModifiers() & Modifier.STATIC) != 0)
									superFieldInstruction.setStatic(true);
								addSuperFieldInstruction(superFieldInstruction);
							}
							else {
								FieldInstructionObject fieldInstruction = new FieldInstructionObject(originClassName, fieldType, fieldName);
								fieldInstruction.setSimpleName(simpleName);
								if((variableBinding.getModifiers() & Modifier.STATIC) != 0)
									fieldInstruction.setStatic(true);
								addFieldInstruction(fieldInstruction);
								Set<Assignment> fieldAssignments = getMatchingAssignments(simpleName, assignments);
								Set<PostfixExpression> fieldPostfixAssignments = getMatchingPostfixAssignments(simpleName, postfixExpressions);
								Set<PrefixExpression> fieldPrefixAssignments = getMatchingPrefixAssignments(simpleName, prefixExpressions);
								AbstractVariable variable = MethodDeclarationUtility.createVariable(simpleName, null);
								if(!fieldAssignments.isEmpty()) {
									handleDefinedField(variable);
									for(Assignment assignment : fieldAssignments) {
										Assignment.Operator operator = assignment.getOperator();
										if(!operator.equals(Assignment.Operator.ASSIGN))
											handleUsedField(variable);
									}
								}
								if(!fieldPostfixAssignments.isEmpty()) {
									handleDefinedField(variable);
									handleUsedField(variable);
								}
								if(!fieldPrefixAssignments.isEmpty()) {
									handleDefinedField(variable);
									handleUsedField(variable);
								}
								if(fieldAssignments.isEmpty() && fieldPostfixAssignments.isEmpty() && fieldPrefixAssignments.isEmpty()) {
									handleUsedField(variable);
								}
							}
						}
					}
				}
				else {
					if(variableBinding.getDeclaringClass() == null) {
						String variableBindingKey = variableBinding.getKey();
						String variableName = variableBinding.getName();
						String variableType = variableBinding.getType().getQualifiedName();
						TypeObject localVariableType = TypeObject.extractTypeObject(variableType);
						boolean isField = variableBinding.isField();
						boolean isParameter = variableBinding.isParameter();
						PlainVariable variable = new PlainVariable(variableBindingKey, variableName, variableType, isField, isParameter);
						if(simpleName.isDeclaration()) {
							LocalVariableDeclarationObject localVariable = new LocalVariableDeclarationObject(localVariableType, variableName);
							VariableDeclaration variableDeclaration = (VariableDeclaration)simpleName.getParent();
							localVariable.setVariableDeclaration(variableDeclaration);
							addLocalVariableDeclaration(localVariable);
							addDeclaredLocalVariable(variable);
						}
						else {
							LocalVariableInstructionObject localVariable = new LocalVariableInstructionObject(localVariableType, variableName);
							localVariable.setSimpleName(simpleName);
							addLocalVariableInstruction(localVariable);
							Set<Assignment> localVariableAssignments = getMatchingAssignments(simpleName, assignments);
							Set<PostfixExpression> localVariablePostfixAssignments = getMatchingPostfixAssignments(simpleName, postfixExpressions);
							Set<PrefixExpression> localVariablePrefixAssignments = getMatchingPrefixAssignments(simpleName, prefixExpressions);
							if(!localVariableAssignments.isEmpty()) {
								addDefinedLocalVariable(variable);
								for(Assignment assignment : localVariableAssignments) {
									Assignment.Operator operator = assignment.getOperator();
									if(!operator.equals(Assignment.Operator.ASSIGN))
										addUsedLocalVariable(variable);
								}
							}
							if(!localVariablePostfixAssignments.isEmpty()) {
								addDefinedLocalVariable(variable);
								addUsedLocalVariable(variable);
							}
							if(!localVariablePrefixAssignments.isEmpty()) {
								addDefinedLocalVariable(variable);
								addUsedLocalVariable(variable);
							}
							if(localVariableAssignments.isEmpty() && localVariablePostfixAssignments.isEmpty() && localVariablePrefixAssignments.isEmpty()) {
								addUsedLocalVariable(variable);
							}
						}
					}
				}
			}
		}
	}

	private void addFieldInstruction(FieldInstructionObject fieldInstruction) {
		fieldInstructionList.add(fieldInstruction);
		if(parent != null) {
			parent.addFieldInstruction(fieldInstruction);
		}
	}

	private void addSuperFieldInstruction(SuperFieldInstructionObject superFieldInstruction) {
		superFieldInstructionList.add(superFieldInstruction);
		if(parent != null) {
			parent.addSuperFieldInstruction(superFieldInstruction);
		}
	}

	private void addLocalVariableDeclaration(LocalVariableDeclarationObject localVariable) {
		localVariableDeclarationList.add(localVariable);
		if(parent != null) {
			parent.addLocalVariableDeclaration(localVariable);
		}
	}

	private void addLocalVariableInstruction(LocalVariableInstructionObject localVariable) {
		localVariableInstructionList.add(localVariable);
		if(parent != null) {
			parent.addLocalVariableInstruction(localVariable);
		}
	}

	private void addDeclaredLocalVariable(PlainVariable variable) {
		declaredLocalVariables.add(variable);
		if(parent != null) {
			parent.addDeclaredLocalVariable(variable);
		}
	}

	private void addDefinedLocalVariable(PlainVariable variable) {
		definedLocalVariables.add(variable);
		if(parent != null) {
			parent.addDefinedLocalVariable(variable);
		}
	}

	private void addUsedLocalVariable(PlainVariable variable) {
		usedLocalVariables.add(variable);
		if(parent != null) {
			parent.addUsedLocalVariable(variable);
		}
	}

	protected void processMethodInvocations(List<Expression> methodInvocations) {
		for(Expression expression : methodInvocations) {
			if(expression instanceof MethodInvocation) {
				MethodInvocation methodInvocation = (MethodInvocation)expression;
				IMethodBinding methodBinding = methodInvocation.resolveMethodBinding();
				String originClassName = methodBinding.getDeclaringClass().getQualifiedName();
				String methodInvocationName = methodBinding.getName();
				String qualifiedName = methodBinding.getReturnType().getQualifiedName();
				TypeObject returnType = TypeObject.extractTypeObject(qualifiedName);
				MethodInvocationObject methodInvocationObject = new MethodInvocationObject(originClassName, methodInvocationName, returnType);
				methodInvocationObject.setMethodInvocation(methodInvocation);
				ITypeBinding[] parameterTypes = methodBinding.getParameterTypes();
				for(ITypeBinding parameterType : parameterTypes) {
					String qualifiedParameterName = parameterType.getQualifiedName();
					TypeObject typeObject = TypeObject.extractTypeObject(qualifiedParameterName);
					methodInvocationObject.addParameter(typeObject);
				}
				ITypeBinding[] thrownExceptionTypes = methodBinding.getExceptionTypes();
				for(ITypeBinding thrownExceptionType : thrownExceptionTypes) {
					methodInvocationObject.addThrownException(thrownExceptionType.getQualifiedName());
				}
				if((methodBinding.getModifiers() & Modifier.STATIC) != 0)
					methodInvocationObject.setStatic(true);
				addMethodInvocation(methodInvocationObject);
				AbstractVariable invoker = MethodDeclarationUtility.processMethodInvocationExpression(methodInvocation.getExpression());
				if(invoker != null) {
					PlainVariable initialVariable = invoker.getInitialVariable();
					if(initialVariable.isField()) {
						//addInvokedMethodThroughField(invoker, methodInvocationObject);
						addNonDistinctInvokedMethodThroughField(invoker, methodInvocationObject);
					}
					else if(initialVariable.isParameter()) {
						//addInvokedMethodThroughParameter(invoker, methodInvocationObject);
						addNonDistinctInvokedMethodThroughParameter(invoker, methodInvocationObject);
					}
					else {
						//addInvokedMethodThroughLocalVariable(invoker, methodInvocationObject);
						addNonDistinctInvokedMethodThroughLocalVariable(invoker, methodInvocationObject);
					}
				}
				else {
					if(methodInvocationObject.isStatic())
						addStaticallyInvokedMethod(methodInvocationObject);
					else if (methodInvocation.getExpression() == null || methodInvocation.getExpression() instanceof ThisExpression) {
						//addInvokedMethodThroughThisReference(methodInvocationObject);
						addNonDistinctInvokedMethodThroughThisReference(methodInvocationObject);
					}
				}
				List<Expression> arguments = methodInvocation.arguments();
				for(Expression argument : arguments) {
					if(argument instanceof SimpleName) {
						SimpleName argumentName = (SimpleName)argument;
						IBinding binding = argumentName.resolveBinding();
						if(binding != null && binding.getKind() == IBinding.VARIABLE) {
							IVariableBinding variableBinding = (IVariableBinding)binding;
							if(variableBinding.isParameter()) {
								String variableBindingKey = variableBinding.getKey();
								String variableName = variableBinding.getName();
								String variableType = variableBinding.getType().getQualifiedName();
								boolean isField = variableBinding.isField();
								boolean isParameter = variableBinding.isParameter();
								PlainVariable variable = new PlainVariable(variableBindingKey, variableName, variableType, isField, isParameter);
								addParameterPassedAsArgumentInMethodInvocation(variable, methodInvocationObject);
							}
						}
					}
				}
			}
			else if(expression instanceof SuperMethodInvocation) {
				SuperMethodInvocation superMethodInvocation = (SuperMethodInvocation)expression;
				IMethodBinding methodBinding = superMethodInvocation.resolveMethodBinding();
				String originClassName = methodBinding.getDeclaringClass().getQualifiedName();
				String methodInvocationName = methodBinding.getName();
				String qualifiedName = methodBinding.getReturnType().getQualifiedName();
				TypeObject returnType = TypeObject.extractTypeObject(qualifiedName);
				SuperMethodInvocationObject superMethodInvocationObject = new SuperMethodInvocationObject(originClassName, methodInvocationName, returnType);
				superMethodInvocationObject.setSuperMethodInvocation(superMethodInvocation);
				ITypeBinding[] parameterTypes = methodBinding.getParameterTypes();
				for(ITypeBinding parameterType : parameterTypes) {
					String qualifiedParameterName = parameterType.getQualifiedName();
					TypeObject typeObject = TypeObject.extractTypeObject(qualifiedParameterName);
					superMethodInvocationObject.addParameter(typeObject);
				}
				ITypeBinding[] thrownExceptionTypes = methodBinding.getExceptionTypes();
				for(ITypeBinding thrownExceptionType : thrownExceptionTypes) {
					superMethodInvocationObject.addThrownException(thrownExceptionType.getQualifiedName());
				}
				if((methodBinding.getModifiers() & Modifier.STATIC) != 0)
					superMethodInvocationObject.setStatic(true);
				addSuperMethodInvocation(superMethodInvocationObject);
				List<Expression> arguments = superMethodInvocation.arguments();
				for(Expression argument : arguments) {
					if(argument instanceof SimpleName) {
						SimpleName argumentName = (SimpleName)argument;
						IBinding binding = argumentName.resolveBinding();
						if(binding != null && binding.getKind() == IBinding.VARIABLE) {
							IVariableBinding variableBinding = (IVariableBinding)binding;
							if(variableBinding.isParameter()) {
								String variableBindingKey = variableBinding.getKey();
								String variableName = variableBinding.getName();
								String variableType = variableBinding.getType().getQualifiedName();
								boolean isField = variableBinding.isField();
								boolean isParameter = variableBinding.isParameter();
								PlainVariable variable = new PlainVariable(variableBindingKey, variableName, variableType, isField, isParameter);
								addParameterPassedAsArgumentInSuperMethodInvocation(variable, superMethodInvocationObject);
							}
						}
					}
				}
			}
		}
	}

	private void addMethodInvocation(MethodInvocationObject methodInvocationObject) {
		methodInvocationList.add(methodInvocationObject);
		if(parent != null) {
			parent.addMethodInvocation(methodInvocationObject);
		}
	}

	private void addSuperMethodInvocation(SuperMethodInvocationObject superMethodInvocationObject) {
		superMethodInvocationList.add(superMethodInvocationObject);
		if(parent != null) {
			parent.addSuperMethodInvocation(superMethodInvocationObject);
		}
	}

	protected void processClassInstanceCreations(List<Expression> classInctanceCreations) {
		for(Expression classInstanceCreationExpression : classInctanceCreations) {
			ClassInstanceCreation classInstanceCreation = (ClassInstanceCreation)classInstanceCreationExpression;
			IMethodBinding constructorBinding = classInstanceCreation.resolveConstructorBinding();
			Type type = classInstanceCreation.getType();
			ITypeBinding typeBinding = type.resolveBinding();
			String qualifiedTypeName = typeBinding.getQualifiedName();
			TypeObject typeObject = TypeObject.extractTypeObject(qualifiedTypeName);
			ClassInstanceCreationObject creationObject = new ClassInstanceCreationObject(typeObject);
			creationObject.setClassInstanceCreation(classInstanceCreation);
			ITypeBinding[] parameterTypes = constructorBinding.getParameterTypes();
			for(ITypeBinding parameterType : parameterTypes) {
				String qualifiedParameterName = parameterType.getQualifiedName();
				TypeObject parameterTypeObject = TypeObject.extractTypeObject(qualifiedParameterName);
				creationObject.addParameter(parameterTypeObject);
			}
			addCreation(creationObject);
		}
	}

	protected void processArrayCreations(List<Expression> arrayCreations) {
		for(Expression arrayCreationExpression : arrayCreations) {
			ArrayCreation arrayCreation = (ArrayCreation)arrayCreationExpression;
			Type type = arrayCreation.getType();
			ITypeBinding typeBinding = type.resolveBinding();
			String qualifiedTypeName = typeBinding.getQualifiedName();
			TypeObject typeObject = TypeObject.extractTypeObject(qualifiedTypeName);
			ArrayCreationObject creationObject = new ArrayCreationObject(typeObject);
			creationObject.setArrayCreation(arrayCreation);
			addCreation(creationObject);
		}
	}

	private void addCreation(CreationObject creationObject) {
		creationList.add(creationObject);
		if(parent != null) {
			parent.addCreation(creationObject);
		}
	}
	
	protected void processLiterals(List<Expression> literals) {
		for(Expression literal : literals) {
			LiteralObject literalObject = new LiteralObject(literal);
			addLiteral(literalObject);
		}
	}

	private void addLiteral(LiteralObject literalObject) {
		literalList.add(literalObject);
		if(parent != null) {
			parent.addLiteral(literalObject);
		}
	}

	protected void processThrowStatement(ThrowStatement throwStatement) {
		Expression expression = throwStatement.getExpression();
		if(expression instanceof ClassInstanceCreation) {
			ClassInstanceCreation creation = (ClassInstanceCreation)expression;
			ITypeBinding typeBinding = creation.getType().resolveBinding();
			addExceptionInThrowStatement(typeBinding.getQualifiedName());
		}
	}

	private void addExceptionInThrowStatement(String exception) {
		exceptionsInThrowStatements.add(exception);
		if(parent != null) {
			parent.addExceptionInThrowStatement(exception);
		}
	}
/*
	private void addInvokedMethodThroughField(AbstractVariable field, MethodInvocationObject methodInvocation) {
		if(invokedMethodsThroughFields.containsKey(field)) {
			LinkedHashSet<MethodInvocationObject> methodInvocations = invokedMethodsThroughFields.get(field);
			methodInvocations.add(methodInvocation);
		}
		else {
			LinkedHashSet<MethodInvocationObject> methodInvocations = new LinkedHashSet<MethodInvocationObject>();
			methodInvocations.add(methodInvocation);
			invokedMethodsThroughFields.put(field, methodInvocations);
		}
		if(parent != null) {
			parent.addInvokedMethodThroughField(field, methodInvocation);
		}
	}
*/
	private void addNonDistinctInvokedMethodThroughField(AbstractVariable field, MethodInvocationObject methodInvocation) {
		if(nonDistinctInvokedMethodsThroughFields.containsKey(field)) {
			ArrayList<MethodInvocationObject> methodInvocations = nonDistinctInvokedMethodsThroughFields.get(field);
			methodInvocations.add(methodInvocation);
		}
		else {
			ArrayList<MethodInvocationObject> methodInvocations = new ArrayList<MethodInvocationObject>();
			methodInvocations.add(methodInvocation);
			nonDistinctInvokedMethodsThroughFields.put(field, methodInvocations);
		}
		if(parent != null) {
			parent.addNonDistinctInvokedMethodThroughField(field, methodInvocation);
		}
	}
/*
	private void addInvokedMethodThroughParameter(AbstractVariable parameter, MethodInvocationObject methodInvocation) {
		if(invokedMethodsThroughParameters.containsKey(parameter)) {
			LinkedHashSet<MethodInvocationObject> methodInvocations = invokedMethodsThroughParameters.get(parameter);
			methodInvocations.add(methodInvocation);
		}
		else {
			LinkedHashSet<MethodInvocationObject> methodInvocations = new LinkedHashSet<MethodInvocationObject>();
			methodInvocations.add(methodInvocation);
			invokedMethodsThroughParameters.put(parameter, methodInvocations);
		}
		if(parent != null) {
			parent.addInvokedMethodThroughParameter(parameter, methodInvocation);
		}
	}
*/
	private void addNonDistinctInvokedMethodThroughParameter(AbstractVariable parameter, MethodInvocationObject methodInvocation) {
		if(nonDistinctInvokedMethodsThroughParameters.containsKey(parameter)) {
			ArrayList<MethodInvocationObject> methodInvocations = nonDistinctInvokedMethodsThroughParameters.get(parameter);
			methodInvocations.add(methodInvocation);
		}
		else {
			ArrayList<MethodInvocationObject> methodInvocations = new ArrayList<MethodInvocationObject>();
			methodInvocations.add(methodInvocation);
			nonDistinctInvokedMethodsThroughParameters.put(parameter, methodInvocations);
		}
		if(parent != null) {
			parent.addNonDistinctInvokedMethodThroughParameter(parameter, methodInvocation);
		}
	}
/*
	private void addInvokedMethodThroughLocalVariable(AbstractVariable localVariable, MethodInvocationObject methodInvocation) {
		if(invokedMethodsThroughLocalVariables.containsKey(localVariable)) {
			LinkedHashSet<MethodInvocationObject> methodInvocations = invokedMethodsThroughLocalVariables.get(localVariable);
			methodInvocations.add(methodInvocation);
		}
		else {
			LinkedHashSet<MethodInvocationObject> methodInvocations = new LinkedHashSet<MethodInvocationObject>();
			methodInvocations.add(methodInvocation);
			invokedMethodsThroughLocalVariables.put(localVariable, methodInvocations);
		}
		if(parent != null) {
			parent.addInvokedMethodThroughLocalVariable(localVariable, methodInvocation);
		}
	}
*/
	private void addNonDistinctInvokedMethodThroughLocalVariable(AbstractVariable localVariable, MethodInvocationObject methodInvocation) {
		if(nonDistinctInvokedMethodsThroughLocalVariables.containsKey(localVariable)) {
			ArrayList<MethodInvocationObject> methodInvocations = nonDistinctInvokedMethodsThroughLocalVariables.get(localVariable);
			methodInvocations.add(methodInvocation);
		}
		else {
			ArrayList<MethodInvocationObject> methodInvocations = new ArrayList<MethodInvocationObject>();
			methodInvocations.add(methodInvocation);
			nonDistinctInvokedMethodsThroughLocalVariables.put(localVariable, methodInvocations);
		}
		if(parent != null) {
			parent.addNonDistinctInvokedMethodThroughLocalVariable(localVariable, methodInvocation);
		}
	}
/*
	private void addInvokedMethodThroughThisReference(MethodInvocationObject methodInvocation) {
		invokedMethodsThroughThisReference.add(methodInvocation);
		if(parent != null) {
			parent.addInvokedMethodThroughThisReference(methodInvocation);
		}
	}
*/
	private void addNonDistinctInvokedMethodThroughThisReference(MethodInvocationObject methodInvocation) {
		nonDistinctInvokedMethodsThroughThisReference.add(methodInvocation);
		if(parent != null) {
			parent.addNonDistinctInvokedMethodThroughThisReference(methodInvocation);
		}
	}

	private void addStaticallyInvokedMethod(MethodInvocationObject methodInvocation) {
		invokedStaticMethods.add(methodInvocation);
		if(parent != null) {
			parent.addStaticallyInvokedMethod(methodInvocation);
		}
	}

	private void addParameterPassedAsArgumentInMethodInvocation(PlainVariable parameter, MethodInvocationObject methodInvocation) {
		if(parametersPassedAsArgumentsInMethodInvocations.containsKey(parameter)) {
			LinkedHashSet<MethodInvocationObject> methodInvocations = parametersPassedAsArgumentsInMethodInvocations.get(parameter);
			methodInvocations.add(methodInvocation);
		}
		else {
			LinkedHashSet<MethodInvocationObject> methodInvocations = new LinkedHashSet<MethodInvocationObject>();
			methodInvocations.add(methodInvocation);
			parametersPassedAsArgumentsInMethodInvocations.put(parameter, methodInvocations);
		}
		if(parent != null) {
			parent.addParameterPassedAsArgumentInMethodInvocation(parameter, methodInvocation);
		}
	}

	private void addParameterPassedAsArgumentInSuperMethodInvocation(PlainVariable parameter, SuperMethodInvocationObject methodInvocation) {
		if(parametersPassedAsArgumentsInSuperMethodInvocations.containsKey(parameter)) {
			LinkedHashSet<SuperMethodInvocationObject> methodInvocations = parametersPassedAsArgumentsInSuperMethodInvocations.get(parameter);
			methodInvocations.add(methodInvocation);
		}
		else {
			LinkedHashSet<SuperMethodInvocationObject> methodInvocations = new LinkedHashSet<SuperMethodInvocationObject>();
			methodInvocations.add(methodInvocation);
			parametersPassedAsArgumentsInSuperMethodInvocations.put(parameter, methodInvocations);
		}
		if(parent != null) {
			parent.addParameterPassedAsArgumentInSuperMethodInvocation(parameter, methodInvocation);
		}
	}

	private Set<Assignment> getMatchingAssignments(SimpleName simpleName, List<Expression> assignments) {
		Set<Assignment> matchingAssignments = new LinkedHashSet<Assignment>();
		for(Expression expression : assignments) {
			Assignment assignment = (Assignment)expression;
			Expression leftHandSide = assignment.getLeftHandSide();
			SimpleName leftHandSideName = MethodDeclarationUtility.getRightMostSimpleName(leftHandSide);
			if(leftHandSideName != null && leftHandSideName.equals(simpleName)) {
				matchingAssignments.add(assignment);
			}
		}
		return matchingAssignments;
	}

	private Set<PostfixExpression> getMatchingPostfixAssignments(SimpleName simpleName, List<Expression> postfixExpressions) {
		Set<PostfixExpression> matchingPostfixAssignments = new LinkedHashSet<PostfixExpression>();
		for(Expression expression : postfixExpressions) {
			PostfixExpression postfixExpression = (PostfixExpression)expression;
			Expression operand = postfixExpression.getOperand();
			SimpleName operandName = MethodDeclarationUtility.getRightMostSimpleName(operand);
			if(operandName != null && operandName.equals(simpleName)) {
				matchingPostfixAssignments.add(postfixExpression);
			}
		}
		return matchingPostfixAssignments;
	}

	private Set<PrefixExpression> getMatchingPrefixAssignments(SimpleName simpleName, List<Expression> prefixExpressions) {
		Set<PrefixExpression> matchingPrefixAssignments = new LinkedHashSet<PrefixExpression>();
		for(Expression expression : prefixExpressions) {
			PrefixExpression prefixExpression = (PrefixExpression)expression;
			Expression operand = prefixExpression.getOperand();
			PrefixExpression.Operator operator = prefixExpression.getOperator();
			SimpleName operandName = MethodDeclarationUtility.getRightMostSimpleName(operand);
			if(operandName != null && operandName.equals(simpleName) &&
					(operator.equals(PrefixExpression.Operator.INCREMENT) ||
					operator.equals(PrefixExpression.Operator.DECREMENT))) {
				matchingPrefixAssignments.add(prefixExpression);
			}
		}
		return matchingPrefixAssignments;
	}

	private void handleDefinedField(AbstractVariable variable) {
		if(variable != null) {
			PlainVariable initialVariable = variable.getInitialVariable();
			if(variable instanceof PlainVariable) {
				//definedFieldsThroughThisReference.add((PlainVariable)variable);
				nonDistinctDefinedFieldsThroughThisReference.add((PlainVariable)variable);
			}
			else {
				if(initialVariable.isField()) {
					//definedFieldsThroughFields.add(variable);
					nonDistinctDefinedFieldsThroughFields.add(variable);
				}
				else if(initialVariable.isParameter()) {
					//definedFieldsThroughParameters.add(variable);
					nonDistinctDefinedFieldsThroughParameters.add(variable);
				}
				else {
					//definedFieldsThroughLocalVariables.add(variable);
					nonDistinctDefinedFieldsThroughLocalVariables.add(variable);
				}
			}
			if(parent != null) {
				parent.handleDefinedField(variable);
			}
		}
	}

	private void handleUsedField(AbstractVariable variable) {
		if(variable != null) {
			PlainVariable initialVariable = variable.getInitialVariable();
			if(variable instanceof PlainVariable) {
				//usedFieldsThroughThisReference.add((PlainVariable)variable);
				nonDistinctUsedFieldsThroughThisReference.add((PlainVariable)variable);
			}
			else {
				if(initialVariable.isField()) {
					//usedFieldsThroughFields.add(variable);
					nonDistinctUsedFieldsThroughFields.add(variable);
				}
				else if(initialVariable.isParameter()) {
					//usedFieldsThroughParameters.add(variable);
					nonDistinctUsedFieldsThroughParameters.add(variable);
				}
				else {
					//usedFieldsThroughLocalVariables.add(variable);
					nonDistinctUsedFieldsThroughLocalVariables.add(variable);
				}
			}
			if(parent != null) {
				parent.handleUsedField(variable);
			}
		}
	}

	public List<FieldInstructionObject> getFieldInstructions() {
		return fieldInstructionList;
	}

	public List<SuperFieldInstructionObject> getSuperFieldInstructions() {
		return superFieldInstructionList;
	}

	public List<LocalVariableDeclarationObject> getLocalVariableDeclarations() {
		return localVariableDeclarationList;
	}

	public List<LocalVariableInstructionObject> getLocalVariableInstructions() {
		return localVariableInstructionList;
	}

	public List<MethodInvocationObject> getMethodInvocations() {
		return methodInvocationList;
	}

	public List<SuperMethodInvocationObject> getSuperMethodInvocations() {
		return superMethodInvocationList;
	}

	public List<CreationObject> getCreations() {
		return creationList;
	}

	public List<ClassInstanceCreationObject> getClassInstanceCreations() {
		List<ClassInstanceCreationObject> classInstanceCreations = new ArrayList<ClassInstanceCreationObject>();
		for(CreationObject creation : creationList) {
			if(creation instanceof ClassInstanceCreationObject) {
				classInstanceCreations.add((ClassInstanceCreationObject)creation);
			}
		}
		return classInstanceCreations;
	}

	public List<ArrayCreationObject> getArrayCreations() {
		List<ArrayCreationObject> arrayCreations = new ArrayList<ArrayCreationObject>();
		for(CreationObject creation : creationList) {
			if(creation instanceof ArrayCreationObject) {
				arrayCreations.add((ArrayCreationObject)creation);
			}
		}
		return arrayCreations;
	}

	public List<LiteralObject> getLiterals() {
		return literalList;
	}

	public Set<String> getExceptionsInThrowStatements() {
		return exceptionsInThrowStatements;
	}

	public boolean containsMethodInvocation(MethodInvocationObject methodInvocation) {
		return methodInvocationList.contains(methodInvocation);
	}

	public boolean containsFieldInstruction(FieldInstructionObject fieldInstruction) {
		return fieldInstructionList.contains(fieldInstruction);
	}

	public boolean containsSuperMethodInvocation(SuperMethodInvocationObject superMethodInvocation) {
		return superMethodInvocationList.contains(superMethodInvocation);
	}

	public boolean containsLocalVariableDeclaration(LocalVariableDeclarationObject lvdo) {
		return localVariableDeclarationList.contains(lvdo);
	}

	public Map<AbstractVariable, LinkedHashSet<MethodInvocationObject>> getInvokedMethodsThroughFields() {
		Map<AbstractVariable, LinkedHashSet<MethodInvocationObject>> invokedMethodsThroughFields =
				new LinkedHashMap<AbstractVariable, LinkedHashSet<MethodInvocationObject>>();
		for(AbstractVariable key : nonDistinctInvokedMethodsThroughFields.keySet()) {
			invokedMethodsThroughFields.put(key, new LinkedHashSet<MethodInvocationObject>(nonDistinctInvokedMethodsThroughFields.get(key)));
		}
		return invokedMethodsThroughFields;
	}

	public Map<AbstractVariable, LinkedHashSet<MethodInvocationObject>> getInvokedMethodsThroughParameters() {
		Map<AbstractVariable, LinkedHashSet<MethodInvocationObject>> invokedMethodsThroughParameters =
				new LinkedHashMap<AbstractVariable, LinkedHashSet<MethodInvocationObject>>();
		for(AbstractVariable key : nonDistinctInvokedMethodsThroughParameters.keySet()) {
			invokedMethodsThroughParameters.put(key, new LinkedHashSet<MethodInvocationObject>(nonDistinctInvokedMethodsThroughParameters.get(key)));
		}
		return invokedMethodsThroughParameters;
	}

	public Map<AbstractVariable, ArrayList<MethodInvocationObject>> getNonDistinctInvokedMethodsThroughFields() {
		return nonDistinctInvokedMethodsThroughFields;
	}

	public Map<AbstractVariable, ArrayList<MethodInvocationObject>> getNonDistinctInvokedMethodsThroughParameters() {
		return nonDistinctInvokedMethodsThroughParameters;
	}

	public Map<AbstractVariable, LinkedHashSet<MethodInvocationObject>> getInvokedMethodsThroughLocalVariables() {
		Map<AbstractVariable, LinkedHashSet<MethodInvocationObject>> invokedMethodsThroughLocalVariables =
				new LinkedHashMap<AbstractVariable, LinkedHashSet<MethodInvocationObject>>();
		for(AbstractVariable key : nonDistinctInvokedMethodsThroughLocalVariables.keySet()) {
			invokedMethodsThroughLocalVariables.put(key, new LinkedHashSet<MethodInvocationObject>(nonDistinctInvokedMethodsThroughLocalVariables.get(key)));
		}
		return invokedMethodsThroughLocalVariables;
	}

	public Map<AbstractVariable, ArrayList<MethodInvocationObject>> getNonDistinctInvokedMethodsThroughLocalVariables() {
		return nonDistinctInvokedMethodsThroughLocalVariables;
	}

	public Set<MethodInvocationObject> getInvokedMethodsThroughThisReference() {
		return new LinkedHashSet<MethodInvocationObject>(nonDistinctInvokedMethodsThroughThisReference);
	}

	public List<MethodInvocationObject> getNonDistinctInvokedMethodsThroughThisReference() {
		return nonDistinctInvokedMethodsThroughThisReference;
	}

	public Set<MethodInvocationObject> getInvokedStaticMethods() {
		return invokedStaticMethods;
	}

	public Set<AbstractVariable> getDefinedFieldsThroughFields() {
		return new LinkedHashSet<AbstractVariable>(nonDistinctDefinedFieldsThroughFields);
	}

	public Set<AbstractVariable> getUsedFieldsThroughFields() {
		return new LinkedHashSet<AbstractVariable>(nonDistinctUsedFieldsThroughFields);
	}

	public List<AbstractVariable> getNonDistinctDefinedFieldsThroughFields() {
		return nonDistinctDefinedFieldsThroughFields;
	}

	public List<AbstractVariable> getNonDistinctUsedFieldsThroughFields() {
		return nonDistinctUsedFieldsThroughFields;
	}

	public Set<AbstractVariable> getDefinedFieldsThroughParameters() {
		return new LinkedHashSet<AbstractVariable>(nonDistinctDefinedFieldsThroughParameters);
	}

	public Set<AbstractVariable> getUsedFieldsThroughParameters() {
		return new LinkedHashSet<AbstractVariable>(nonDistinctUsedFieldsThroughParameters);
	}

	public List<AbstractVariable> getNonDistinctDefinedFieldsThroughParameters() {
		return nonDistinctDefinedFieldsThroughParameters;
	}

	public List<AbstractVariable> getNonDistinctUsedFieldsThroughParameters() {
		return nonDistinctUsedFieldsThroughParameters;
	}

	public Set<AbstractVariable> getDefinedFieldsThroughLocalVariables() {
		return new LinkedHashSet<AbstractVariable>(nonDistinctDefinedFieldsThroughLocalVariables);
	}

	public Set<AbstractVariable> getUsedFieldsThroughLocalVariables() {
		return new LinkedHashSet<AbstractVariable>(nonDistinctUsedFieldsThroughLocalVariables);
	}

	public List<AbstractVariable> getNonDistinctDefinedFieldsThroughLocalVariables() {
		return nonDistinctDefinedFieldsThroughLocalVariables;
	}

	public List<AbstractVariable> getNonDistinctUsedFieldsThroughLocalVariables() {
		return nonDistinctUsedFieldsThroughLocalVariables;
	}

	public Set<PlainVariable> getDefinedFieldsThroughThisReference() {
		return new LinkedHashSet<PlainVariable>(nonDistinctDefinedFieldsThroughThisReference);
	}

	public List<PlainVariable> getNonDistinctDefinedFieldsThroughThisReference() {
		return nonDistinctDefinedFieldsThroughThisReference;
	}

	public Set<PlainVariable> getUsedFieldsThroughThisReference() {
		return new LinkedHashSet<PlainVariable>(nonDistinctUsedFieldsThroughThisReference);
	}

	public List<PlainVariable> getNonDistinctUsedFieldsThroughThisReference() {
		return nonDistinctUsedFieldsThroughThisReference;
	}

	public Set<PlainVariable> getDeclaredLocalVariables() {
		return declaredLocalVariables;
	}

	public Set<PlainVariable> getDefinedLocalVariables() {
		return definedLocalVariables;
	}

	public Set<PlainVariable> getUsedLocalVariables() {
		return usedLocalVariables;
	}

	public Map<PlainVariable, LinkedHashSet<MethodInvocationObject>> getParametersPassedAsArgumentsInMethodInvocations() {
		return parametersPassedAsArgumentsInMethodInvocations;
	}

	public Map<PlainVariable, LinkedHashSet<SuperMethodInvocationObject>> getParametersPassedAsArgumentsInSuperMethodInvocations() {
		return parametersPassedAsArgumentsInSuperMethodInvocations;
	}
}
