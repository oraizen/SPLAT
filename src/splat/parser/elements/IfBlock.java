package splat.parser.elements;
import splat.executor.BooleanValue;
import splat.executor.ExecutionException;
import splat.executor.ReturnFromCall;
import splat.executor.Value;
import splat.lexer.Token;
import splat.semanticanalyzer.SemanticAnalysisException;

import java.util.List;
import java.util.Map;

public class IfBlock extends Statement{
    private Expression expr;
    private List<Statement> ConditionStatements;

    public IfBlock(Token tok, Expression e, List<Statement> stms)
    {
        super(tok);
        expr = e;
        ConditionStatements = stms;
    }

    public Expression getExpression()
    {
        return this.expr;
    }

    public List<Statement> getStatements()
    {
        return this.ConditionStatements;
    }

    public void analyze(Map<String, FunctionDecl> funcMap,
	                              Map<String, Type> varAndParamMap) throws SemanticAnalysisException
    {
        Type exprType = this.expr.analyzeAndGetType(funcMap, varAndParamMap);
        if ( !exprType.getType().equals("Boolean") ){
            throw new SemanticAnalysisException("expression is not a boolean type", this);
        }
        for (Statement s : this.ConditionStatements){
            s.analyze(funcMap, varAndParamMap);
        }
    }

    public void execute(Map<String, FunctionDecl> funcMap,
	                              Map<String, Value> varAndParamMap) 
										throws ReturnFromCall,ExecutionException
    {
        BooleanValue exprVal = (BooleanValue)this.expr.evaluate(funcMap, varAndParamMap);
        if (exprVal.getValue()){
            for (Statement stm : this.ConditionStatements){
                stm.execute(funcMap, varAndParamMap);
            }
        }
    }
}
