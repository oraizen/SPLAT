package splat.parser.elements;
import java.util.List;
import java.util.ArrayList;

import splat.lexer.Token;
import splat.parser.Parser;

public class FunctionDecl extends Declaration {

	// Need to add some fields

	public static class Param {

		private Type param_type;
		private String label_;

		public Param(String type, String label){
			label_ = label;
			param_type = new Type(type);
		}
		public String getLabel(){
			return label_;
		}
		public Type getType(){
			return param_type;
		}
	}

	private String returnType_;
	private List<Param> params_;
	private List<VariableDecl> decls;
	private List<Statement> stmts;
	
	// Need to add extra arguments for setting fields in the constructor 
	public FunctionDecl(Token tok, String label,
						String returnType,List<Param> params,
						List<VariableDecl> d,
						List<Statement> s) {
		super(tok,label);
		this.returnType_ = returnType;
		this.params_ = params;
		this.decls = d;
		this.stmts = s;
	}

	public String getReturnType()
	{
		return this.returnType_;
	}
	
	public List<Param> getParams()
	{
		return this.params_;
	}

	public List<VariableDecl> getDecls()
	{
		return this.decls;
	}

	public List<Statement> getStmts()
	{
		return stmts;
	}

	// Fix this as well
	public String toString() {
		return "Function Declaration with label "+super.getLabel()
		+ " ,return type: "+this.returnType_;
	}
}
