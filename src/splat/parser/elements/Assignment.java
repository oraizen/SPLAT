package splat.parser.elements;
import splat.executor.ExecutionException;
import splat.executor.ReturnFromCall;
import splat.executor.Value;
import splat.lexer.Token;
import splat.semanticanalyzer.SemanticAnalysisException;

import java.util.Map;

public class Assignment extends Statement{
    private String label;
    private Expression right;

    public Assignment(Token tok, String l, Expression r)
    {
        super(tok);
        label = l;
        right = r;
    }

    // getters
    public String getLabel()
    {
        return this.label;
    }

    public Expression getRight()
    {
        return this.right;
    }

    public void analyze(Map<String, FunctionDecl> funcMap,
	                              Map<String, Type> varAndParamMap) throws SemanticAnalysisException
    {
        if (!varAndParamMap.containsKey(this.label)){
            throw new SemanticAnalysisException("assignment to non-declared variable", this);
        }
        Type exprType = right.analyzeAndGetType(funcMap, varAndParamMap);
        if ( !varAndParamMap.get(this.label).equals(exprType) ){
            throw new SemanticAnalysisException("type mismatch in assignment statement", this);
        }
    }
	
    public void execute(Map<String, FunctionDecl> funcMap,
	                              Map<String, Value> varAndParamMap) 
										throws ReturnFromCall,ExecutionException
    {
        Value exprVal = this.right.evaluate(funcMap, varAndParamMap);
        varAndParamMap.put(this.label, exprVal);
    }  


}
