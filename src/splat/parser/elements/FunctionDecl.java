package splat.parser.elements;
import java.util.List;
import java.util.ArrayList;

import splat.lexer.Token;
import splat.parser.Parser;

public class FunctionDecl extends Declaration {

	// Need to add some fields

	public static class Param {
		public String param_type;
		public String label_;
		public Param(String type, String label){
			label_ = label;
			param_type = type;
		}
	}

	private String label_;
	private String returnType_;
	private List<Param> params_;
	private List<Statement> stmts;
	
	// Need to add extra arguments for setting fields in the constructor 
	public FunctionDecl(Token tok, String label,
						String returnType,List<Param> params,
						List<Statement> s) {
		super(tok);
		this.label_ = label;
		this.returnType_ = returnType;
		this.params_ = params;
		this.stmts = s;
	}

	public String getLabel(){
		return this.label_;
	}
	public String getReturnType()
	{
		return this.returnType_;
	}
	
	// Fix this as well
	public String toString() {
		return "Function Declaration with label "+this.label_
		+ " ,return type: "+this.returnType_;
	}
}
