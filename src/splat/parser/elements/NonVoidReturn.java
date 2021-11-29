package splat.parser.elements;
import splat.lexer.Token;

public class NonVoidReturn extends Statement{
    private Expression expr;

    public NonVoidReturn(Token tok, Expression e)
    {
        super(tok);
        expr = e;
    }
}
