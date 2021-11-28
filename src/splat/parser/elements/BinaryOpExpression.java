package splat.parser.elements;
import splat.lexer.Token;

public class BinaryOpExpression extends Expression{
    
    private String operator;
    private Expression left;
    private Expression right;

    public BinaryOpExpression(Token tok, Expression l, Expression r){
        super(tok);
        left = l;
        right = r;
    }

}
