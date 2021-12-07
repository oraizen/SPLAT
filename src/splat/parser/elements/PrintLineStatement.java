package splat.parser.elements;
import splat.lexer.Token;
import java.util.Map;

public class PrintLineStatement extends Statement{

    public PrintLineStatement(Token tok)
    {
        super(tok);
    }

    public void analyze(Map<String, FunctionDecl> funcMap,
                        Map<String, Type> varAndParamMap)
    {
        //
    }

}
