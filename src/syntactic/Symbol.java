package syntactic;

import java.util.ArrayList;
import java.util.List;

public class Symbol
{
    private String id, type, value;
    private List<Integer> lines;

    public Symbol(String id, String type, String value, int line)
    {
        this.id = id;
        this.type = type;
        this.value = value;
        this.lines = new ArrayList<>();
        this.lines.add(line);
    }



    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }

    public List<Integer> getLines()
    {
        return lines;
    }

    public void addLine(int line)
    {
        this.lines.add(line);
    }
}
