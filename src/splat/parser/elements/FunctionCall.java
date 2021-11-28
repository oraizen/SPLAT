package splat.parser.elements;
import splat.lexer.Token;
import java.util.List;

public class FunctionCall extends Expression{
    private String funcLabel;
    private List<Expression> funcArgs;
    
    public FunctionCall(Token tok, List<Expression> args)
    {
        super(tok);
        this.funcArgs = args;
    }
}
