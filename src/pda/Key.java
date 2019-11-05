package pda;

import lexer.Tag;

import java.util.Objects;

public class Key
{
    private int state;
    private String value, stackValue;

    //TODO: Remove tags
    Tag tags = new Tag();

    Key(int state, String value, String stackValue)
    {
        this.state = state;
        this.value = value;
        this.stackValue = stackValue;
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
//        if (!this.value.equals("λ"))
//            return String.format("q%d, %12s, %5s", this.state, this.tags.get(Integer.parseInt(this.value)), this.stackValue);
//        else
            return String.format("q%d, %3s, %3s", this.state, this.value, this.stackValue);
    }
}
