package splat.parser.elements;
import splat.lexer.Token;
import java.util.List;

public class FuncCallStatement extends Statement{
    private String funcLabel;
    private List<Expression> funcArgs;
    
    public FuncCallStatement(Token tok, List<Expression> args)
    {
        super(tok);
        this.funcArgs = args;
    }
}
