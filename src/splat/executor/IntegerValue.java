package splat.executor;

import splat.parser.elements.ASTElement;

public class IntegerValue extends Value{
    private int val;
    public IntegerValue(int i){
        this.val = i;
    }
    public int getValue(){
        return this.val;
    }
    public void setValue(int i)
    {
        this.val = i;
    }

    public Value evaluateBinary(Value other, String op, ASTElement el) throws ExecutionException{
        IntegerValue otherInt = (IntegerValue)other;
        if (op.equals(">")){
            return new BooleanValue(this.val > otherInt.getValue());
        }else if(op.equals("<")){
            return new BooleanValue(this.val < otherInt.getValue());
        }else if(op.equals(">=")){
            return new BooleanValue(this.val >= otherInt.getValue());
        }else if(op.equals("<=")){
            return new BooleanValue(this.val <= otherInt.getValue());
        }else if(op.equals("==")){
            return new BooleanValue(this.val == otherInt.getValue());
        }
        // arithmetic operations
        if (op.equals("+")){
            return new IntegerValue(this.val + otherInt.getValue());
        }else if(op.equals("-")){
            return new IntegerValue(this.val - otherInt.getValue());
        }else if(op.equals("*")){
            return new IntegerValue(this.val * otherInt.getValue());
        }else if(op.equals("/")){
            if (otherInt.getValue()==0){
                throw new ExecutionException("division by zero", el);
            }
            return new IntegerValue(this.val / otherInt.getValue());
        }else {
            return new IntegerValue(this.val % otherInt.getValue());
        }
        
    }

    public String toString(){
        return String.valueOf(this.val);
    }
}
