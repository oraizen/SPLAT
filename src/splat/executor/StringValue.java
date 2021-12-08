package splat.executor;

import splat.parser.elements.ASTElement;

public class StringValue extends Value{
    private String str;
    public StringValue(String s)
    {
        this.str = s;
    }
    public String getValue(){
        return this.str;
    }
    public void setValue(String s)
    {
        this.str = s;
    }
    public Value evaluateBinary(Value other,String op, ASTElement el){
        StringValue otherString = (StringValue)other;
        if (op.equals("+")){
            return new StringValue(this.str + otherString.getValue());
        }else{
            return new BooleanValue(this.str == otherString.getValue());
        }
    }
    public String toString(){
        return this.str;
    }
}

