package splat.parser.elements;
import splat.lexer.Token;

public class Variable extends Expression{
    
    private String label;

    public Variable(Token tok)
    {
        super(tok);
    }

}
