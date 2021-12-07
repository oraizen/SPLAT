package splat.parser.elements;
import splat.lexer.Token;
import splat.semanticanalyzer.SemanticAnalysisException;

import java.util.List;
import java.util.Map;

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

}
