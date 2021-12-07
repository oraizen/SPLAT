package splat.parser.elements;
import splat.lexer.Token;
import splat.semanticanalyzer.SemanticAnalysisException;

import java.util.Map;

public class PrintStatement extends Statement{
    private Expression expr;

    public PrintStatement(Token tok, Expression e)
    {
        super(tok);
        expr = e;
    }

    public void analyze(Map<String, FunctionDecl> funcMap,
                        Map<String, Type> varAndParamMap) throws SemanticAnalysisException
    {
        expr.analyzeAndGetType(funcMap, varAndParamMap);
        return ;
    }
}
