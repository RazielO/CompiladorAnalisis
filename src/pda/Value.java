package pda;

public class Value
{
    private int state;
    private String stack;

    public Value(int state, String stack)
    {
        this.state = state;
        this.stack = stack;
    }

    public int getState()
    {
        return state;
    }

    public void setState(int state)
    {
        this.state = state;
    }

    public String getStack()
    {
        return stack;
    }

    public void setStack(String stack)
    {
        this.stack = stack;
    }

    @Override
    public String toString()
    {
        return String.format("q%d, %s", this.state, this.stack);
    }
}
