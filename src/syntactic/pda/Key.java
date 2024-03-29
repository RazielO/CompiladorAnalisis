package syntactic.pda;

import java.util.Objects;

public class Key
{
    private int state;
    private String value, stackValue;

    Key(int state, String value, String stackValue)
    {
        this.state = state;
        this.value = value;
        this.stackValue = stackValue;
    }

    public int getState()
    {
        return state;
    }

    public String getValue()
    {
        return value;
    }

    public String getStackValue()
    {
        return stackValue;
    }

    @Override
    public boolean equals(Object object)
    {
        if (object instanceof Key)
        {
            Key key = (Key) object;

            return key.state == this.state
                    && Objects.equals(key.stackValue, this.stackValue)
                    && Objects.equals(key.value, this.value);
        }

        return false;
    }

    @Override
    public int hashCode()
    {
        return (String.valueOf(this.state) + value + stackValue).hashCode();
    }

    @Override
    public String toString()
    {
            return String.format("q%d, %3s, %3s", this.state, this.value, this.stackValue);
    }
}
