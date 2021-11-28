package splat.parser.elements;
import splat.lexer.Token;

public class UnaryOpExpression extends Expression{
    
    private String operator;
    private Expression expr;

    public UnaryOpExpression(Token tok, String op, Expression ex)
    {
        super(tok);
        operator = op;
        expr = ex;
    }

}
