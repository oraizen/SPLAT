package splat.parser.elements;
import splat.lexer.Token;
import java.util.List;

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
}
