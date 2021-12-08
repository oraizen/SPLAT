package splat.executor;

import splat.parser.elements.ASTElement;

public abstract class Value {
	// TODO: Implement me!
	public abstract Value evaluateBinary(Value other,String op, ASTElement el) throws ExecutionException;
	public abstract String toString();
}
