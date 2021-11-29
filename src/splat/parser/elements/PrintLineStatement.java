package splat.parser.elements;
import splat.lexer.Token;

public class PrintLineStatement extends Statement{
    private Expression expr;

    public PrintLineStatement(Token tok)
    {
        super(tok);
    }
}
