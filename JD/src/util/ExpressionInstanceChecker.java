package util;

import org.eclipse.jdt.core.dom.Expression;

public interface ExpressionInstanceChecker {
	public boolean instanceOf(Expression expression);
}
