package splat.executor;

import splat.parser.elements.ASTElement;

public class BooleanValue extends Value{
    private Boolean bool;
    public BooleanValue(Boolean b)
    {
        this.bool = b;
    }
    public Boolean getValue()
    {
        return this.bool;
    }
    public void setValue(Boolean b){
        this.bool = b;
    }
    public Value evaluateBinary(Value other, String op, ASTElement el){
        BooleanValue otherBool = (BooleanValue) other;
        if (op.equals("and")){
            return new BooleanValue(this.bool && otherBool.getValue());
        }else if(op.equals("or")){
            return new BooleanValue(this.bool || otherBool.getValue());
        }else {
            return new BooleanValue(this.bool == otherBool.getValue());
        }
    }
    public String toString(){
        return String.valueOf(this.bool);
    }
}
