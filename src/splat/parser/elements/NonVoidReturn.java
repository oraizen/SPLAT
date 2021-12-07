package splat.parser.elements;
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
}
