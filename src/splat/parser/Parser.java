package splat.parser;

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

	/*
	checks whether the token contains the valid label
	*/
	private void checkLabel(Token label) throws ParseException
	{
		if (label.getType() != Token.Type_.IDENTIFIER)
		{
			throw new ParseException("expected label",label);
		}
	}

	/*
	checks whether the token contains the valid return type
	Valid return-types: Boolean, Integer, String, void
	*/
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

	/*
	checks whether the token contains the valid type
	Valid types: Boolean, Integer, String
	*/
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
		List<Statement> stmts = new ArrayList<>();

		while (!peekNext("end") || !peekNext("else"))
		{
			if (peekTwoAhead(":=")){
				//parse assignment
				stmts.add(parseAssignment());

			}else if(peekNext("while")){
				// parse while loop
				stmts.add(parseWhileLoop());

			}else if(peekNext("if")){
				// parse if statement
				stmts.add( parseIfConditional() );

			}else if(peekTwoAhead("(")){
				//parse function call
				stmts.add(parseFuncCallStatement());

			}else if(peekNext("print")){
				// parse print statement
				// remove the "print" token
				Token tok = tokens.remove(0);
				Expression expr = parseExpression();
				checkNext(";");
				stmts.add(new PrintStatement(tok, expr));


			}else if(peekNext("print_line")){
				//parse print_line statement
				Token tok = tokens.remove(0);
				stmts.add(new PrintLineStatement(tok));
				checkNext(";");

			}else if(peekNext("return")){
				//parse return statement;
				Token tok = tokens.remove(0);
				if ( peekNext(";") )
				{
					stmts.add(new voidReturnStatement(tok));
				} else {
					Expression expr = parseExpression();
					stmts.add( new NonVoidReturn(tok, expr) );
				}
				checkNext(";");

			}else {
				Token tok = tokens.get(0);
				throw new ParseException("Statement expected", tok);
			}
		}
		return stmts;
	}

	private Statement parseFuncCallStatement()  throws ParseException {
		Token tok = tokens.remove(0);
		checkLabel(tok);
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
		checkNext(";");
		return new FuncCallStatement(tok, args);
	}

	private Statement parseIfConditional() throws ParseException
	{
		Token tok = tokens.get(0);
		checkNext("if");
		Expression expr = parseExpression();
		checkNext("then");
		List<Statement> ifStmts = parseStmts();
		if (peekNext("else")){
			// remove the "else" token
			tokens.remove(0);
			List<Statement> elseStmts = parseStmts();
			checkNext("end");
			checkNext("if");
			checkNext(";");
			return new IfElseBlock(tok, expr, ifStmts, elseStmts);
		}
		checkNext("end");
		checkNext("if");
		checkNext(";");
		return new IfBlock(tok, expr, ifStmts);
	}

	private WhileLoop parseWhileLoop() throws ParseException
	{
		Token whileLoopToken = tokens.get(0);
		checkNext("while");
		Expression expr = parseExpression();
		checkNext("do");
		List<Statement> loopStmts = parseStmts();
		checkNext("end");
		checkNext("while");
		checkNext(";");
		return new WhileLoop(whileLoopToken, expr, loopStmts); 
	}

	private Assignment parseAssignment() throws ParseException
	{
		Token label = tokens.remove(0);
		checkLabel(label);
		checkNext(":=");
		
		Expression expr = parseExpression();
		checkNext(";");
		return new Assignment(label, label.getValue(), expr);
		
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
	private Boolean isLiteral(Token tok) {
		Token.Type_ type = tok.getType();
		if (type == Token.Type_.NUMBER || type == Token.Type_.StringLiteral
				|| tok.getValue()=="true" || tok.getValue()=="false"){
					return true;
		}
		return false;
	}
	private Literal parseLiteral(){
		Token tok = tokens.remove(0);
		if (tok.getType() == Token.Type_.NUMBER){
			return new Literal(tok, "Integer", tok.getValue());
		}else if(tok.getType() == Token.Type_.StringLiteral){
			return new Literal(tok,"String", tok.getValue());
		}else{
			return new Literal(tok,"Boolean",tok.getValue());
		}
	}

	private Expression parseExpression() throws ParseException{

		if (peekNext("("))
		{
			// unary or binary operation
			return parseOpExpression();

		}else if ( peekTwoAhead("(") ){
			// function call expression
			return parseFuncCall();

		}else if(tokens.get(0).getType() == Token.Type_.IDENTIFIER){
			// single variable call expression
			return new Variable(tokens.remove(0));

		}else if (isLiteral(tokens.get(0))){
			// literal expression
			return parseLiteral();

		}else {
			throw new ParseException("expected expression, but got "
										+tokens.get(0).getValue(), tokens.get(0));
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
