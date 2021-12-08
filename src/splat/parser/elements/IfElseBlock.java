package splat.parser.elements;
import splat.executor.BooleanValue;
import splat.executor.ExecutionException;
import splat.executor.ReturnFromCall;
import splat.executor.Value;
import splat.lexer.Token;
import java.util.List;
import splat.semanticanalyzer.SemanticAnalysisException;
import java.util.Map;

public class IfElseBlock extends Statement{
    private Expression expr;
    private List<Statement> IfBlockStatements;
    private List<Statement> ElseBlockStatements;

    public IfElseBlock(Token tok, Expression e, List<Statement> ifStmts,
                        List<Statement> elseStmts)
    {
        super(tok);
        expr = e;
        IfBlockStatements = ifStmts;
        ElseBlockStatements = elseStmts;
    }

    public List<Statement> getIfStatements(){
        return this.IfBlockStatements;
    }

    public List<Statement> getElseStatements(){
        return this.ElseBlockStatements;
    }

    public void analyze(Map<String, FunctionDecl> funcMap,
	                    Map<String, Type> varAndParamMap) throws SemanticAnalysisException
    {
        Type exprType = this.expr.analyzeAndGetType(funcMap, varAndParamMap);
        if ( !exprType.getType().equals("Boolean") ){
            throw new SemanticAnalysisException("expression is not a boolean type", this);
        }
        for (Statement trueStm : this.IfBlockStatements){
            trueStm.analyze(funcMap, varAndParamMap);
        }
        for (Statement falseStm : this.ElseBlockStatements){
            falseStm.analyze(funcMap, varAndParamMap);
        }
    }

    public void execute(Map<String, FunctionDecl> funcMap,
	                              Map<String, Value> varAndParamMap) 
										throws ReturnFromCall,ExecutionException
    {
        BooleanValue exprVal = (BooleanValue)this.expr.evaluate(funcMap, varAndParamMap);
        if (exprVal.getValue()){
            for (Statement stm : this.IfBlockStatements){
                stm.execute(funcMap, varAndParamMap);
            }
        }else{
            for (Statement stm : this.ElseBlockStatements){
                stm.execute(funcMap, varAndParamMap);
            }
        }
    }
}
