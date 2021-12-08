package splat.parser.elements;
import splat.lexer.Token;
import splat.semanticanalyzer.SemanticAnalysisException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import splat.executor.BooleanValue;
import splat.executor.ExecutionException;
import splat.executor.IntegerValue;
import splat.executor.ReturnFromCall;
import splat.executor.StringValue;
import splat.executor.Value;

public class FunctionCall extends Expression{
    private String funcLabel;
    private List<Expression> funcArgs;
    
    public FunctionCall(Token tok, String l,List<Expression> args)
    {
        super(tok);
        funcLabel = l;
        this.funcArgs = args;
    }

    public Type analyzeAndGetType(Map<String, FunctionDecl> funcMap,
	                                        Map<String, Type> varAndParamMap) throws SemanticAnalysisException{
        if (!funcMap.containsKey(this.funcLabel)){
            throw new SemanticAnalysisException("call to undeclared function "+this.funcLabel, this);
        }
        List<FunctionDecl.Param> params = funcMap.get(this.funcLabel).getParams();

        if (params.size() != this.funcArgs.size()){
            throw new SemanticAnalysisException("number of arguments does not match with parameters", this);
        }

        for (int i=0; i<funcArgs.size();i++){
            Type argType = funcArgs.get(i).analyzeAndGetType(funcMap, varAndParamMap);
            if( !argType.equals( params.get(i).getType() ) ){
                throw new SemanticAnalysisException("type mismatch with func paramenter", this);
            }
        }
        return new Type( funcMap.get(this.funcLabel).getReturnType() );

    }

    public Value evaluate(Map<String, FunctionDecl> funcMap,
                                 Map<String, Value> varAndParamMap) throws ExecutionException
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
                Value retVal = ex.getReturnVal();
                return retVal;
            }
        }

        throw new ExecutionException("no return value",this);

    }

}
