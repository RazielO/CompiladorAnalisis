package syntactic.pda;

import java.util.Arrays;

public class Value
{
    private int state;
    private String[] stack;

    Value(int state, String[] stack)
    {
        this.state = state;
        this.stack = stack;
    }

    public int getState()
    {
        return state;
    }

    public String[] getStack()
    {
        return stack;
    }

    @Override
    public String toString()
    {
        return String.format("q%d, %10s", this.state, Arrays.toString(this.stack));
    }
}
