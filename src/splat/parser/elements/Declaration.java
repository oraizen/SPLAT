package splat.parser.elements;

import splat.lexer.Token;

public abstract class Declaration extends ASTElement {

	private String label;

	public Declaration(Token tok,String l) {
		super(tok);
		label = l;
	}

	public String getLabel()
	{
		return label;
	}
}
