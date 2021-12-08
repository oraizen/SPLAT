package splat.parser.elements;
import splat.executor.Value;
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

    public void execute(Map<String, FunctionDecl> funcMap,
                        Map<String, Value> varAndParamMap) {
        System.out.println();
    }

}
