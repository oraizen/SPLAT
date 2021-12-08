package splat.semanticanalyzer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.List;

import splat.parser.elements.Declaration;
import splat.parser.elements.FunctionDecl;
import splat.parser.elements.IfBlock;
import splat.parser.elements.IfElseBlock;
import splat.parser.elements.NonVoidReturn;
import splat.parser.elements.ProgramAST;
import splat.parser.elements.Statement;
import splat.parser.elements.Type;
import splat.parser.elements.VariableDecl;
import splat.parser.elements.voidReturnStatement;

public class SemanticAnalyzer {

	private ProgramAST progAST;
	
	private Map<String, FunctionDecl> funcMap;
	private Map<String, Type> progVarMap;
	
	public SemanticAnalyzer(ProgramAST progAST) {
		this.progAST = progAST;
		this.funcMap = new HashMap<>();
		this.progVarMap = new HashMap<>();
	}

	public void analyze() throws SemanticAnalysisException {
		
		// Checks to make sure we don't use the same labels more than once
		// for our program functions and variables 
		checkNoDuplicateProgLabels();
		
		// This sets the maps that will be needed later when we need to
		// typecheck variable references and function calls in the 
		// program body
		setProgVarAndFuncMaps();
		
		// Perform semantic analysis on the functions
		for (FunctionDecl funcDecl : funcMap.values()) {	
			analyzeFuncDecl(funcDecl);
		}
		
		// Perform semantic analysis on the program body
		for (Statement stmt : progAST.getStmts()) {
			stmt.analyze(funcMap, progVarMap);
		}
		
	}

	private Boolean isReturn(Statement stm){
		if (stm instanceof NonVoidReturn){
			return true;
		}else if( stm instanceof IfElseBlock ){
			List<Statement> ifstms = ( (IfElseBlock)stm ).getIfStatements();
			List<Statement> elsestms = ( (IfElseBlock)stm ).getElseStatements();
			return isReturn( ifstms.get(ifstms.size()-1) ) && isReturn( elsestms.get(elsestms.size()-1) );
		}else {
			return false;
		}
	}

	private void analyzeFuncDecl(FunctionDecl funcDecl) throws SemanticAnalysisException {
		
		// Checks to make sure we don't use the same labels more than once
		// among our function parameters, local variables, and function names
		checkNoDuplicateFuncLabels(funcDecl);
		
		// Get the types of the parameters and local variables
		Map<String, Type> varAndParamMap = getVarAndParamMap(funcDecl);
		
		// Perform semantic analysis on the function body
		for (Statement stmt : funcDecl.getStmts()) {
			stmt.analyze(this.funcMap, varAndParamMap);
		}
		List<Statement> stms = funcDecl.getStmts();
		if ( !funcDecl.getReturnType().equals("void") ){
			if ( !isReturn(stms.get(stms.size()-1)) ){
				throw new SemanticAnalysisException("no return statement in function declaration", funcDecl);
			}
		}
	}
	
	
	private Map<String, Type> getVarAndParamMap(FunctionDecl funcDecl) {
		
		Map<String, Type> VarAndParamMap = new HashMap<>();

		for (VariableDecl decl : funcDecl.getDecls()) {
			
			VarAndParamMap.put(decl.getLabel().toString(), decl.getType());
		}

		for (FunctionDecl.Param param : funcDecl.getParams()){
			VarAndParamMap.put(param.getLabel(),param.getType());
		}
		VarAndParamMap.put("0result", new Type(funcDecl.getReturnType()));
		return VarAndParamMap;
	}

	private void checkNoDuplicateFuncLabels(FunctionDecl funcDecl) 
									throws SemanticAnalysisException {
		
		// FIXME: Similar to checkNoDuplicateProgLabels()

		// check the declared parameters
		Set<String> labels = new HashSet<>();

		for (FunctionDecl.Param param : funcDecl.getParams())
		{
			String label = param.getLabel();
			if( labels.contains( label ) || funcMap.containsKey( label ) ){
				throw new SemanticAnalysisException("Cannot have duplicate label '"
						+ label + "' in function",funcDecl);
			} else {
				labels.add(label);
			}
		}
		// check the declared variables
		for (Declaration decl : funcDecl.getDecls()){
			String label = decl.getLabel().toString();
			if ( labels.contains(label) || funcMap.containsKey(label) ) {
				throw new SemanticAnalysisException("Cannot have duplicate label '"
						+ label + "' in function", decl);
			} else {
				labels.add(label);
			}
		}
		
	}
	
	private void checkNoDuplicateProgLabels() throws SemanticAnalysisException {
		
		Set<String> labels = new HashSet<String>();
		
 		for (Declaration decl : progAST.getDecls()) {
 			String label = decl.getLabel().toString();
 			
			if (labels.contains(label)) {
				throw new SemanticAnalysisException("Cannot have duplicate label '"
						+ label + "' in program", decl);
			} else {
				labels.add(label);
			}
			
		}
	}
	
	private void setProgVarAndFuncMaps() {
		
		for (Declaration decl : progAST.getDecls()) {

			String label = decl.getLabel().toString();
			
			if (decl instanceof FunctionDecl) {
				FunctionDecl funcDecl = (FunctionDecl)decl;
				funcMap.put(label, funcDecl);
				
			} else if (decl instanceof VariableDecl) {
				VariableDecl varDecl = (VariableDecl)decl;
				progVarMap.put(label, varDecl.getType());
			}
		}
	}
}
