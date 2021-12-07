package splat.parser.elements;
import splat.lexer.Token;
import java.util.List;
import java.util.Map;
import splat.semanticanalyzer.SemanticAnalysisException;

public class WhileLoop extends Statement{
    private Expression expr;
    private List<Statement> stmts;
    public WhileLoop(Token tok, Expression e, List<Statement> s){
        super(tok);
        expr = e;
        stmts = s;
    }

    public Expression getExpression()
    {
        return this.expr;
    }

    public List<Statement> getStatements()
    {
        return this.stmts;
    }

    public void analyze(Map<String, FunctionDecl> funcMap,
	                    Map<String, Type> varAndParamMap) throws SemanticAnalysisException
    {
        Type exprType = this.expr.analyzeAndGetType(funcMap, varAndParamMap);
        if ( !exprType.getType().equals("Boolean") ){
            throw new SemanticAnalysisException("Expected expression with boolean type", this);
        }
        for (Statement st : this.stmts){
            st.analyze(funcMap, varAndParamMap);
        }
    }

}
