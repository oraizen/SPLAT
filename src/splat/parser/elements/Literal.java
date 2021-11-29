package splat.parser.elements;
import splat.lexer.Token;

public class Literal extends Expression{
    
    String type;
    String literal_value;

    public Literal(Token tok, String t, String l) 
    {
        super(tok);
        type = t;
        literal_value = l;
    }
}
