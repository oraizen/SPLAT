package splat.parser.elements;
import splat.lexer.Token;
import java.util.List;

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
}
