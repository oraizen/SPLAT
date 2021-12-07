package splat.parser.elements;
import splat.lexer.Token;
import java.util.List;
import java.util.Map;
import splat.semanticanalyzer.SemanticAnalysisException;

public class FuncCallStatement extends Statement{
    private String funcLabel;
    private List<Expression> funcArgs;
    
    public FuncCallStatement(Token tok, String fLabel, List<Expression> args)
    {
        super(tok);
        this.funcLabel = fLabel;
        this.funcArgs = args;
    }

    public void analyze(Map<String, FunctionDecl> funcMap,
	                              Map<String, Type> varAndParamMap) throws SemanticAnalysisException
    {
        if (!funcMap.containsKey(this.funcLabel)){
            throw new SemanticAnalysisException("call to non-declared function statement", this);
        }
        if (!funcMap.get(this.funcLabel).getReturnType().equals("void")){
            throw new SemanticAnalysisException("call to non-void function statement", this);
        }

        List<FunctionDecl.Param> fparams = funcMap.get(this.funcLabel).getParams();

        if (fparams.size() != this.funcArgs.size()){
            throw new SemanticAnalysisException("number of arguments does not match with parameters", this);
        }
        for (int i=0; i < this.funcArgs.size(); i++){
            Type argType = this.funcArgs.get(i).analyzeAndGetType(funcMap, varAndParamMap);
            if( !argType.equals( fparams.get(i).getType() ) ){
                throw new SemanticAnalysisException("type mismatch with func paramenter", this);
            }
        }
    }
}
