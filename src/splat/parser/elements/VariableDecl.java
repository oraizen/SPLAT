package splat.parser.elements;

import splat.lexer.Token;

public class VariableDecl extends Declaration {

	// Need to add some fields
	private String label;
	private String type;
	// Need to add extra arguments for setting fields in the constructor 
	public VariableDecl(Token tok, String l, String t) {
		super(tok);
		label = l;
		type = t;
	}

	// Getters?
	public String getLabel()
	{
		return this.label;
	}

	public String getType()
	{
		return this.type;
	}
	// Fix this as well
	public String toString() {
		return null;
	}
}
