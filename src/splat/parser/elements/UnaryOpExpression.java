package splat.parser.elements;
import splat.executor.BooleanValue;
import splat.executor.ExecutionException;
import splat.executor.IntegerValue;
import splat.executor.Value;
import splat.lexer.Token;
import splat.semanticanalyzer.SemanticAnalysisException;

import java.util.Map;

public class UnaryOpExpression extends Expression{
    
    private String operator;
    private Expression expr;

    public UnaryOpExpression(Token tok, String op, Expression ex)
    {
        super(tok);
        operator = op;
        expr = ex;
    }

    public Type analyzeAndGetType(Map<String, FunctionDecl> funcMap,
	                                        Map<String, Type> varAndParamMap) throws SemanticAnalysisException
    {
        Type exprType = this.expr.analyzeAndGetType(funcMap, varAndParamMap);
        if ( exprType.getType().equals("Boolean") ){
            if ( !this.operator.equals("not") ){
                throw new SemanticAnalysisException("operator mismatch in unary operation expression", this);
            }
            return exprType;
        }else if( exprType.getType().equals("Integer") ){
            if ( !this.operator.equals("-")){
                throw new SemanticAnalysisException("operator mismatch in unary operation expression", this);
            }
            return exprType;
        }else {
            throw new SemanticAnalysisException("wrong type for the unary operation expression", this);
        }
    }
	
    public Value evaluate(Map<String, FunctionDecl> funcMap,
                          Map<String, Value> varAndParamMap) throws ExecutionException{
        
        Value exprVal = this.expr.evaluate(funcMap, varAndParamMap);
        if (this.operator.equals("-")){
            return new IntegerValue( -((IntegerValue)exprVal).getValue() );
        }else{
            return new BooleanValue( !((BooleanValue)exprVal).getValue() );
        }
    }

}
