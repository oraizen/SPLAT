package splat.parser.elements;
import splat.lexer.Token;
import splat.semanticanalyzer.SemanticAnalysisException;

import java.util.Map;

public class BinaryOpExpression extends Expression{
    
    private String operator;
    private Expression left;
    private Expression right;

    public BinaryOpExpression(Token tok, String op,Expression l, Expression r){
        super(tok);
        operator = op;
        left = l;
        right = r;
    }

    public Type analyzeAndGetType(Map<String, FunctionDecl> funcMap,
	                                        Map<String, Type> varAndParamMap) throws SemanticAnalysisException
    {
        String[] Bool_operators = {"and","or","=="};
        String[] Int_Bool_operators = {">","<",">=","<=","=="};
        String[] Int_Int_operators = {"-","+","*","/","%"};
        Type lt = left.analyzeAndGetType(funcMap, varAndParamMap);
        Type rt = right.analyzeAndGetType(funcMap, varAndParamMap);
        if ( !lt.equals(rt)){
            throw new SemanticAnalysisException("wrong types", this);
        }
        if (lt.getType().equals("Boolean")){

            for (String i : Bool_operators){
                if (i.equals(this.operator)){
                    return new Type("Boolean");
                }
            }
            throw new SemanticAnalysisException("wrong operator for the given operand types", this);
            
        }else if(lt.getType().equals("Integer")){
            for (String i : Int_Bool_operators){
                if (i.equals(this.operator)){
                    return new Type("Boolean");
                }
            }
            for (String i : Int_Int_operators){
                if (i.equals(this.operator)){
                    return new Type("Integer");
                }
            }
            throw new SemanticAnalysisException("wrong operator for the given operand types", this);
        }else { // String type
            if (this.operator.equals("==")){
                return new Type("Boolean");
            }else if( this.operator.equals("+") ){
                return new Type("String");
            }
            throw new SemanticAnalysisException("wrong operator for the given operand types", this);
        }
    }
	

}
