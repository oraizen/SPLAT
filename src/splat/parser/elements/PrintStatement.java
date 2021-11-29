package splat.parser.elements;
import splat.lexer.Token;

public class PrintStatement extends Statement{
    private Expression expr;

    public PrintStatement(Token tok, Expression e)
    {
        super(tok);
        expr = e;
    }
}
