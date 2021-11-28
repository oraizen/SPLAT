package splat.parser;

import java.beans.Statement;
import java.util.ArrayList;
import java.util.List;

import splat.lexer.Token;
import splat.parser.elements.*;

public class Parser {

	private String[] types={"Integer","Boolean","String"};
	private String[] return_types={"Integer","Boolean","String","void"};
	private String[] unary_operators = {"not","-"};
	private String[] binary_operators = {"and","or",">","==","<=",">=","+","-",
										"*","/","%",};
	private List<Token> tokens;
	
	public Parser(List<Token> tokens) {
		this.tokens = tokens;
	}

	/**
	 * Compares the next token to an expected value, and throws
	 * an exception if they don't match.  This removes the front-most
	 * (next) token  
	 * 
	 * @param expected value of the next token
	 * @throws ParseException if the actual token doesn't match what 
	 * 			was expected
	 */
	private void checkNext(String expected) throws ParseException {

		Token tok = tokens.remove(0);
		
		if (!tok.getValue().equals(expected)) {
			throw new ParseException("Expected '"+ expected + "', got '" 
					+ tok.getValue()+ "'.", tok);
		}
	}
	
	/**
	 * Returns a boolean indicating whether or not the next token matches
	 * the expected String value.  This does not remove the token from the
	 * token list.
	 * 
	 * @param expected value of the next token
	 * @return true iff the token value matches the expected string
	 */
	private boolean peekNext(String expected) {
		return tokens.get(0).getValue().equals(expected);
	}
	
	/**
	 * Returns a boolean indicating whether or not the token directly after
	 * the front most token matches the expected String value.  This does 
	 * not remove any tokens from the token list.
	 * 
	 * @param expected value of the token directly after the next token
	 * @return true iff the value matches the expected string
	 */
	private boolean peekTwoAhead(String expected) {
		return tokens.get(1).getValue().equals(expected);
	}
	
	
	/*
	 *  <program> ::= program <decls> begin <stmts> end ;
	 */
	public ProgramAST parse() throws ParseException {
		
		try {
			// Needed for 'program' token position info
			Token startTok = tokens.get(0);
			
			checkNext("program");
			
			List<Declaration> decls = parseDecls();
			
			checkNext("begin");
			
			List<Statement> stmts = parseStmts();
			
			checkNext("end");
			checkNext(";");
	
			return new ProgramAST(decls, stmts, startTok);
			
		// This might happen if we do a tokens.get(), and nothing is there!
		} catch (IndexOutOfBoundsException ex) {
			
			throw new ParseException("Unexpectedly reached the end of file.", -1, -1);
		}
	}
	
	/*
	 *  <decls> ::= (  <decl>  )*
	 */
	private List<Declaration> parseDecls() throws ParseException {
		
		List<Declaration> decls = new ArrayList<Declaration>();
		
		while (!peekNext("begin")) {
			Declaration decl = parseDecl();
			decls.add(decl);
		}
		
		return decls;
	}
	
	/*
	 * <decl> ::= <var-decl> | <func-decl>
	 */
	private Declaration parseDecl() throws ParseException {

		if (peekTwoAhead(":")) {
			return parseVarDecl();
		} else if (peekTwoAhead("(")) {
			return parseFuncDecl();
		} else {
			Token tok = tokens.get(0);
			throw new ParseException("Declaration expected", tok);
		}
	}
	
	/*
	 * <func-decl> ::= <label> ( <params> ) : <ret-type> is 
	 * 						<loc-var-decls> begin <stmts> end ;
	 */
	private FunctionDecl parseFuncDecl() throws ParseException {
		// TODO Auto-generated method stub
		Token tok = tokens.remove(0);
		checkLabel(tok);
		checkNext("(");
		List<FunctionDecl.Param> params = new ArrayList<>(); 
		while(!peekNext(")"))
		{
			if (params.size() > 0)
			{
				checkNext(",");
			}
			Token paramLabel = tokens.remove(0);
			checkLabel(paramLabel);
			checkNext(":");
			Token paramType = tokens.remove(0);
			checkType(paramType);
			params.add(new FunctionDecl.Param(paramType.getValue(), paramLabel.getValue()));
		}
		// remove the ")" token
		tokens.remove(0);

		checkNext(":");

		Token returnType = tokens.remove(0);
		checkReturnType(returnType);
		
		checkNext("is");
		// local variable declarations processing start here
		List<VariableDecl> funcDecls = new ArrayList<>();
		while (!peekNext("begin"))
		{
			VariableDecl decl = parseVarDecl();
			funcDecls.add(decl);
		}

		// remove the "begin" token
		checkNext("begin");



		return null;
	}

	private void checkLabel(Token label) throws ParseException
	{
		if (label.getType() != Token.Type_.IDENTIFIER)
		{
			throw new ParseException("expected label",label);
		}
	}

	private void checkReturnType(Token returnType) throws ParseException
	{
		String str = returnType.getValue();
		for (String s : this.return_types){
			if (s.equals(str)){
				return ;
			}
		}
		throw new ParseException("Expected return type, but got "+str, returnType);
	}

	private void checkType(Token parameter) throws ParseException
	{
		String paramType = parameter.getValue();
		for (String validType : this.types)
		{
			if (validType.equals(paramType)){
				return;
			}
		}

		throw new ParseException("expected type but got "+paramType, parameter);
		
	}
	/*
	 * <var-decl> ::= <label> : <type> ;
	 */
	private VariableDecl parseVarDecl() throws ParseException {
		// TODO Auto-generated method stub
		Token label = tokens.remove(0);
		checkLabel(label);
		checkNext(":");
		Token type = tokens.remove(0);
		checkType(type);
		checkNext(";");
		return new VariableDecl(label, label.getValue(), type.getValue());
	}
	
	/*
	 * <stmts> ::= (  <stmt>  )*
	 */
	private List<Statement> parseStmts() throws ParseException {
		// TODO Auto-generated method stub

		if (peekTwoAhead(":=")){
			//parse assignment
		}else if(peekNext("while")){
			// parse while loop
		}else if(peekNext("if")){
			// parse if statement
		}else if(peekTwoAhead("(")){
			//parse function call
		}else if(peekNext("print")){
			// parse print statement
		}else if(peekNext("print_line")){
			//parse print_line statement
		}else if(peekNext("return")){
			//parse return statement;
		}else {
			Token tok = tokens.get(0);
			throw new ParseException("Statement expected", tok);
		}

		return null;
	}

	private Assignment parseAssignment() throws ParseException
	{
		Token label = tokens.remove(0);
		checkLabel(label);
		checkNext(":=");
		//todo!
		Expression expr = parseExpression();
		checkNext(";");
		return new Assignment(label, label.getValue(), expr);
		//todo!
	}
	private Boolean isUnaryOp(String str){
		for (String op : this.unary_operators){
			if (op.equals(str))
			return true;
		}
		return false;
	}

	private Boolean isBinaryOp(String str){
		for (String op : this.binary_operators){
			if (op.equals(str))
			return true;
		}
		return false;
	}

	private Expression parseExpression() throws ParseException{

		if (peekNext("("))
		{
			// unary or binary operation
			Expression expr = parseOpExpression();
			return expr;

		}else if (peekTwoAhead("(")){
			// function call expression
		}
	}

	private Expression parseOpExpression() throws ParseException
	{
		// remove "(" token
		tokens.remove(0);

		Token tok = tokens.get(0);
		if (isUnaryOp(tok.getValue()))
		{
			String op = tok.getValue();
			//remove the unary operator toke
			tokens.remove(0);
			Expression expr = parseExpression();
			return new UnaryOpExpression(tok, op, expr);
		}
		Expression left = parseExpression();
		Token op = tokens.remove(0);
		if ( !isBinaryOp( op.getValue() ) ){
			throw new ParseException("expected binary operator, but got "
									+ op.getValue(), op);
		}
		Expression right = parseExpression();
		checkNext(")");
		return new BinaryOpExpression(op, left, right);
	}

	private FunctionCall parseFuncCall() throws ParseException
	{
		Token label = tokens.remove(0);
		checkLabel(label);
		checkNext("(");
		List<Expression> args = new ArrayList<>();
		while(!peekNext(")"))
		{
			if (args.size()>0)
			{
				checkNext(",");
			}
			Expression arg = parseExpression();
			args.add(arg);
		}
		// remove the ")" token
		tokens.remove(0);
		return new FunctionCall(label, args);
	}
}
