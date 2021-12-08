package splat.parser.elements;
import splat.executor.ExecutionException;
import splat.lexer.Token;
import splat.semanticanalyzer.SemanticAnalysisException;

import java.util.Map;

import splat.executor.Value;

public class Variable extends Expression{
    
    private String label;

    public Variable(Token tok)
    {
        super(tok);
        label = tok.getValue();
    }

    public Type analyzeAndGetType(Map<String, FunctionDecl> funcMap,
	                              Map<String, Type> varAndParamMap) throws SemanticAnalysisException
	{
        if ( !varAndParamMap.containsKey( this.label ) ){
            throw new SemanticAnalysisException("reference to undeclared variable", this);
        }
        return varAndParamMap.get(this.label);
    }

    public Value evaluate(Map<String, FunctionDecl> funcMap,
                          Map<String, Value> varAndParamMap) throws ExecutionException
    {
        return varAndParamMap.get(this.label);
    }
    

}
