package splat.lexer;

public class Token {

    private String lexeme_;
    private int line_;
    private int column_;
    public static enum Type_{
        NUMBER,
        OPERATOR,
        IDENTIFIER,
        SYMBOL,
        KEYWORD,
        StringLiteral
    }
    private Type_ tokenType_;

    public Token(String lexeme, int line, int column, Type_ t)
    {
        lexeme_ = lexeme;
        line_ = line;
        column_ = column;
        tokenType_ = t;
    }

    public void setLine (int line)
    {
        this.line_ = line;
    }

    public void setColumn(int column)
    {
        this.column_ = column;
    }

    public void setType(Type_ tokenType)
    {
        this.tokenType_ = tokenType;
    }
    public void setLexeme(String lexeme)
    {
        this.lexeme_ = lexeme;
    }
}
