package splat.parser.elements;
import splat.lexer.Token;
import java.util.Map;
import splat.semanticanalyzer.SemanticAnalysisException;

public class voidReturnStatement extends Statement {
    public voidReturnStatement(Token tok)
    {
        super(tok);
    }

    public void analyze(Map<String, FunctionDecl> funcMap,
	                    Map<String, Type> varAndParamMap) throws SemanticAnalysisException
    {
        if (!varAndParamMap.containsKey("0result")){
            throw new SemanticAnalysisException("calling return in global scope", this);
        }

        if ( !varAndParamMap.get("0result").getType().equals("void") )
        {
            throw new SemanticAnalysisException("expected void return statement", this);
        }
    }

}
