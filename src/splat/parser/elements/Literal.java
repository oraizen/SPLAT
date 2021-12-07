package splat.parser.elements;
import splat.lexer.Token;
import java.util.Map;

public class Literal extends Expression{
    
    String type;
    String literal_value;

    public Literal(Token tok, String t, String l) 
    {
        super(tok);
        type = t;
        literal_value = l;
    }

    public Type analyzeAndGetType(Map<String, FunctionDecl> funcMap,
	                              Map<String, Type> varAndParamMap) {
        
        return new Type(type);

    }
	
}
