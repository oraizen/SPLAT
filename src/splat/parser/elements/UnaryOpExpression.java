package splat.parser.elements;
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
	
    

}
