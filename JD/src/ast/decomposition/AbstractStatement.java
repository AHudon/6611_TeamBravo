package ast.decomposition;

import java.util.List;

import ast.ASTInformation;
import ast.ASTInformationGenerator;

import org.eclipse.jdt.core.dom.Statement;

public abstract class AbstractStatement extends AbstractMethodFragment {

	private ASTInformation statement;
	private StatementType type;
	
    public AbstractStatement(Statement statement, StatementType type, AbstractMethodFragment parent) {
    	super(parent);
    	this.type = type;
    	this.statement = ASTInformationGenerator.generateASTInformation(statement);
    }

    public Statement getStatement() {
    	return (Statement)this.statement.recoverASTNode();
    }

	public StatementType getType() {
		return type;
	}

	public abstract List<String> stringRepresentation();
}
