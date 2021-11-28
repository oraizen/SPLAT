package splat.parser.elements;
import splat.lexer.Token;

public class Assignment extends Statement{
    private String label;
    private Expression right;

    public Assignment(Token tok, String l, Expression r)
    {
        super(tok);
        label = l;
        right = r;
    }

    // getters
    public String getLabel()
    {
        return this.label;
    }

    public Expression getRight()
    {
        return this.right;
    }

}
