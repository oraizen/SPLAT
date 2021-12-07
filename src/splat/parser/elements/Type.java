package splat.parser.elements;

public class Type {
    
    private String type;
    public Type(String t)
    {
        type = t;
    }

    public String getType()
    {
        return type;
    }
    public Boolean equals(Type r)
    {
        return type.equals(r.getType());
    }
}
