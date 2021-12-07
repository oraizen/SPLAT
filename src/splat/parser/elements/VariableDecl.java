package splat.parser.elements;

import splat.lexer.Token;

public class VariableDecl extends Declaration {

	// Need to add some fields
	private Type type;
	// Need to add extra arguments for setting fields in the constructor 
	public VariableDecl(Token tok, String l, String t) {
		super(tok,l);
		type = new Type(t);
	}

	// Getters?

	public Type getType()
	{
		return this.type;
	}
	// Fix this as well
	public String toString() {
		return "Variable declaration with label: "+super.getLabel();
	}
}
