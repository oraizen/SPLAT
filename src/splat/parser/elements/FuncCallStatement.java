package splat.parser.elements;
import splat.executor.BooleanValue;
import splat.executor.ExecutionException;
import splat.executor.IntegerValue;
import splat.executor.ReturnFromCall;
import splat.executor.StringValue;
import splat.executor.Value;
import splat.lexer.Token;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import splat.semanticanalyzer.SemanticAnalysisException;

public class FuncCallStatement extends Statement{
    private String funcLabel;
    private List<Expression> funcArgs;
    
    public FuncCallStatement(Token tok, String fLabel, List<Expression> args)
    {
        super(tok);
        this.funcLabel = fLabel;
        this.funcArgs = args;
    }

    public void analyze(Map<String, FunctionDecl> funcMap,
	                              Map<String, Type> varAndParamMap) throws SemanticAnalysisException
    {
        if (!funcMap.containsKey(this.funcLabel)){
            throw new SemanticAnalysisException("call to non-declared function statement", this);
        }
        if (!funcMap.get(this.funcLabel).getReturnType().equals("void")){
            throw new SemanticAnalysisException("call to non-void function statement", this);
        }

        List<FunctionDecl.Param> fparams = funcMap.get(this.funcLabel).getParams();

        if (fparams.size() != this.funcArgs.size()){
            throw new SemanticAnalysisException("number of arguments does not match with parameters", this);
        }
        for (int i=0; i < this.funcArgs.size(); i++){
            Type argType = this.funcArgs.get(i).analyzeAndGetType(funcMap, varAndParamMap);
            if( !argType.equals( fparams.get(i).getType() ) ){
                throw new SemanticAnalysisException("type mismatch with func paramenter", this);
            }
        }
    }

    public void execute(Map<String, FunctionDecl> funcMap,
	                              Map<String, Value> varAndParamMap) 
										throws ReturnFromCall,ExecutionException
    {
        FunctionDecl func = funcMap.get(this.funcLabel);
        // set new varAndParamMap for function scope
        Map<String,Value> funcVarAndParamMap = new HashMap<>();

        List<FunctionDecl.Param> funcParams = func.getParams();
        List<VariableDecl> funcDecls = func.getDecls();

        for (int i=0; i < funcParams.size(); i++){
            Value paramVal = this.funcArgs.get(i).evaluate(funcMap, varAndParamMap);
            funcVarAndParamMap.put(funcParams.get(i).getLabel(), paramVal);
        }

        for (int i=0; i < funcDecls.size(); i++){
            String type = funcDecls.get(i).getType().getType();
            Value varVal;
            if (type.equals("String")){
                varVal = new StringValue(new String(""));
            }else if(type.equals("Integer")){
                varVal = new IntegerValue(0);
            }else{
                varVal = new BooleanValue(false);
            }
            funcVarAndParamMap.put(funcDecls.get(i).getLabel(), varVal);
        }

        for (Statement stm : func.getStmts()){
            try{
                stm.execute(funcMap,funcVarAndParamMap);
            } catch(ReturnFromCall ex){
                return;
            }
        }

    }
}
