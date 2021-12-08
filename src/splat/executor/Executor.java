package splat.executor;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

import splat.parser.elements.FunctionDecl;
import splat.parser.elements.ProgramAST;
import splat.parser.elements.Statement;
import splat.parser.elements.Declaration;
import splat.parser.elements.VariableDecl;
import splat.parser.elements.Type;

public class Executor {

	private ProgramAST progAST;
	
	private Map<String, FunctionDecl> funcMap;
	private Map<String, Value> progVarMap;
	
	public Executor(ProgramAST progAST) {
		this.progAST = progAST;
		this.funcMap = new HashMap<>();
		this.progVarMap = new HashMap<>();
	}

	public void runProgram() throws ExecutionException {

		// This sets the maps that will be needed for executing function 
		// calls and storing the values of the program variables
		setMaps();
		
		try {
			
			// Go through and execute each of the statements
			for (Statement stmt : progAST.getStmts()) {
				stmt.execute(funcMap, progVarMap);
			}
			
		// We should never have to catch this exception here, since the
		// main program body cannot have returns
		} catch (ReturnFromCall ex) {
			System.out.println("Internal error!!! The main program body "
					+ "cannot have a return statement -- this should have "
					+ "been caught during semantic analysis!");
			
			throw new ExecutionException("Internal error -- fix your "
					+ "semantic analyzer!", -1, -1);
		}
	}
	
	private void setMaps() {
		// TODO: Use setMaps() from SemanticAnalyzer as a guide
		for (Declaration decl : this.progAST.getDecls()) {

			String label = decl.getLabel().toString();
			
			if (decl instanceof FunctionDecl) {
				FunctionDecl funcDecl = (FunctionDecl)decl;
				this.funcMap.put(label, funcDecl);
				
			} else if (decl instanceof VariableDecl) {
				VariableDecl varDecl = (VariableDecl)decl;
				Type varType = varDecl.getType();
				Value varValue;
				// initialization part
				if (varType.getType().equals("Boolean")){
					varValue = new BooleanValue(false);
				}else if(varType.getType().equals("Integer")){
					varValue = new IntegerValue( 0 );
				}else{
					varValue = new StringValue(new String(""));
				}
				this.progVarMap.put(label, varValue);
			}
		}
	}

}
