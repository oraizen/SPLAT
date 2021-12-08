package splat.parser.elements;
import splat.lexer.Token;
import java.util.Map;

import splat.executor.BooleanValue;
import splat.executor.IntegerValue;
import splat.executor.StringValue;
import splat.executor.Value;

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
    
    public Value evaluate(Map<String, FunctionDecl> funcMap,
                          Map<String, Value> varAndParamMap){
        if (this.type.equals("String")){
            return new StringValue(this.literal_value);
        }else if(this.type.equals("Boolean")){

            return new BooleanValue(Boolean.parseBoolean(this.literal_value));
        }else{
            return new IntegerValue(Integer.parseInt(this.literal_value));
        }
    }
	
}
