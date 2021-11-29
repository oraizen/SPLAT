package splat.parser.elements;
import splat.lexer.Token;
import java.util.List;

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
}
