package ast.decomposition;

import ast.CreationObject;
import ast.FieldInstructionObject;
import ast.LiteralObject;
import ast.LocalVariableDeclarationObject;
import ast.LocalVariableInstructionObject;
import ast.MethodInvocationObject;
import ast.SuperFieldInstructionObject;
import ast.SuperMethodInvocationObject;
import ast.decomposition.cfg.PlainVariable;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.core.dom.Statement;

/*
 * CompositeStatementObject represents the following AST Statement subclasses:
 * 1.	Block
 * 2.	DoStatement
 * 3.	EnhancedForStatement
 * 4.	ForStatement
 * 5.	IfStatement
 * 6.	LabeledStatement
 * 7.	SwitchStatement
 * 8.	SynchronizedStatement
 * 9.	TryStatement
 * 10.	WhileStatement
 */

public class CompositeStatementObject extends AbstractStatement {
	
	private List<AbstractStatement> statementList;
	private List<AbstractExpression> expressionList;

	public CompositeStatementObject(Statement statement, StatementType type, AbstractMethodFragment parent) {
		super(statement, type, parent);
		this.statementList = new ArrayList<AbstractStatement>();
		this.expressionList = new ArrayList<AbstractExpression>();
	}

	public void addStatement(AbstractStatement statement) {
		statementList.add(statement);
		//statement.setParent(this);
	}

	public List<AbstractStatement> getStatements() {
		return statementList;
	}

	public void addExpression(AbstractExpression expression) {
		expressionList.add(expression);
		//expression.setParent(this);
	}

	public List<AbstractExpression> getExpressions() {
		return expressionList;
	}

	public List<FieldInstructionObject> getFieldInstructionsInExpressions() {
		List<FieldInstructionObject> fieldInstructions = new ArrayList<FieldInstructionObject>();
		for(AbstractExpression expression : expressionList) {
			fieldInstructions.addAll(expression.getFieldInstructions());
		}
		return fieldInstructions;
	}

	public List<SuperFieldInstructionObject> getSuperFieldInstructionsInExpressions() {
		List<SuperFieldInstructionObject> superFieldInstructions = new ArrayList<SuperFieldInstructionObject>();
		for(AbstractExpression expression : expressionList) {
			superFieldInstructions.addAll(expression.getSuperFieldInstructions());
		}
		return superFieldInstructions;
	}

	public List<LocalVariableDeclarationObject> getLocalVariableDeclarationsInExpressions() {
		List<LocalVariableDeclarationObject> localVariableDeclarations = new ArrayList<LocalVariableDeclarationObject>();
		for(AbstractExpression expression : expressionList) {
			localVariableDeclarations.addAll(expression.getLocalVariableDeclarations());
		}
		return localVariableDeclarations;
	}

	public List<LocalVariableInstructionObject> getLocalVariableInstructionsInExpressions() {
		List<LocalVariableInstructionObject> localVariableInstructions = new ArrayList<LocalVariableInstructionObject>();
		for(AbstractExpression expression : expressionList) {
			localVariableInstructions.addAll(expression.getLocalVariableInstructions());
		}
		return localVariableInstructions;
	}

	public List<MethodInvocationObject> getMethodInvocationsInExpressions() {
		List<MethodInvocationObject> methodInvocations = new ArrayList<MethodInvocationObject>();
		for(AbstractExpression expression : expressionList) {
			methodInvocations.addAll(expression.getMethodInvocations());
		}
		return methodInvocations;
	}

	public List<SuperMethodInvocationObject> getSuperMethodInvocationsInExpressions() {
		List<SuperMethodInvocationObject> superMethodInvocations = new ArrayList<SuperMethodInvocationObject>();
		for(AbstractExpression expression : expressionList) {
			superMethodInvocations.addAll(expression.getSuperMethodInvocations());
		}
		return superMethodInvocations;
	}

	public List<CreationObject> getCreationsInExpressions() {
		List<CreationObject> creations = new ArrayList<CreationObject>();
		for(AbstractExpression expression : expressionList) {
			creations.addAll(expression.getCreations());
		}
		return creations;
	}

	public List<LiteralObject> getLiteralsInExpressions() {
		List<LiteralObject> literals = new ArrayList<LiteralObject>();
		for(AbstractExpression expression : expressionList) {
			literals.addAll(expression.getLiterals());
		}
		return literals;
	}

	public Set<MethodInvocationObject> getInvokedStaticMethodsInExpressions() {
		Set<MethodInvocationObject> staticMethodInvocations = new LinkedHashSet<MethodInvocationObject>();
		for(AbstractExpression expression : expressionList) {
			staticMethodInvocations.addAll(expression.getInvokedStaticMethods());
		}
		return staticMethodInvocations;
	}

	public Set<PlainVariable> getUsedFieldsThroughThisReferenceInExpressions() {
		Set<PlainVariable> usedFieldsThroughThisReference = new LinkedHashSet<PlainVariable>();
		for(AbstractExpression expression : expressionList) {
			usedFieldsThroughThisReference.addAll(expression.getUsedFieldsThroughThisReference());
		}
		return usedFieldsThroughThisReference;
	}

	public Set<MethodInvocationObject> getInvokedMethodsThroughThisReferenceInExpressions() {
		Set<MethodInvocationObject> invokedMethodsThroughThisReference = new LinkedHashSet<MethodInvocationObject>();
		for(AbstractExpression expression : expressionList) {
			invokedMethodsThroughThisReference.addAll(expression.getInvokedMethodsThroughThisReference());
		}
		return invokedMethodsThroughThisReference;
	}

	public List<String> stringRepresentation() {
		List<String> stringRepresentation = new ArrayList<String>();
		stringRepresentation.add(this.toString());
		for(AbstractStatement statement : statementList) {
			stringRepresentation.addAll(statement.stringRepresentation());
		}
		return stringRepresentation;
	}

	public List<CompositeStatementObject> getIfStatements() {
		List<CompositeStatementObject> ifStatements = new ArrayList<CompositeStatementObject>();
		if(this.getType().equals(StatementType.IF))
			ifStatements.add(this);
		for(AbstractStatement statement : statementList) {
			if(statement instanceof CompositeStatementObject) {
				CompositeStatementObject composite = (CompositeStatementObject)statement;
				ifStatements.addAll(composite.getIfStatements());
			}
		}
		return ifStatements;
	}

	public List<CompositeStatementObject> getSwitchStatements() {
		List<CompositeStatementObject> switchStatements = new ArrayList<CompositeStatementObject>();
		if(this.getType().equals(StatementType.SWITCH))
			switchStatements.add(this);
		for(AbstractStatement statement : statementList) {
			if(statement instanceof CompositeStatementObject) {
				CompositeStatementObject composite = (CompositeStatementObject)statement;
				switchStatements.addAll(composite.getSwitchStatements());
			}
		}
		return switchStatements;
	}

	public List<TryStatementObject> getTryStatements() {
		List<TryStatementObject> tryStatements = new ArrayList<TryStatementObject>();
		if(this.getType().equals(StatementType.TRY))
			tryStatements.add((TryStatementObject)this);
		for(AbstractStatement statement : statementList) {
			if(statement instanceof CompositeStatementObject) {
				CompositeStatementObject composite = (CompositeStatementObject)statement;
				tryStatements.addAll(composite.getTryStatements());
			}
		}
		return tryStatements;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getType().toString());
		if(expressionList.size() > 0) {
			sb.append("(");
			for(int i=0; i<expressionList.size()-1; i++) {
				sb.append(expressionList.get(i).toString()).append("; ");
			}
			sb.append(expressionList.get(expressionList.size()-1).toString());
			sb.append(")");
		}
		sb.append("\n");
		return sb.toString();
	}
}
