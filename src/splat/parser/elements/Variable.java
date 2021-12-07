package splat.parser.elements;
import splat.lexer.Token;
import splat.semanticanalyzer.SemanticAnalysisException;

import java.util.Map;

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

}
