package splat.parser.elements;
import splat.executor.ExecutionException;
import splat.executor.ReturnFromCall;
import splat.executor.Value;
import splat.lexer.Token;
import splat.semanticanalyzer.SemanticAnalysisException;
import java.util.Map;

public class NonVoidReturn extends Statement{
    private Expression expr;

    public NonVoidReturn(Token tok, Expression e)
    {
        super(tok);
        expr = e;
    }

    public void analyze(Map<String, FunctionDecl> funcMap,
	                              Map<String, Type> varAndParamMap) throws SemanticAnalysisException
    {
        if ( !varAndParamMap.get("0result").equals( this.expr.analyzeAndGetType(funcMap, varAndParamMap) ) )
        {
            throw new SemanticAnalysisException("return type mismatch", this);
        }
    }

    public void execute(Map<String, FunctionDecl> funcMap,
	                              Map<String, Value> varAndParamMap) 
										throws ReturnFromCall,ExecutionException
    {
        Value exprVal = this.expr.evaluate(funcMap, varAndParamMap);
        throw new ReturnFromCall(exprVal);
    }
}
