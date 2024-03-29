package syntactic.pda;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Control
{
    private int currentState;
    private int nextState;
    private String currentChar;
    private String endOfStackSymbol;

    private Rules rules;
    private Stack<String> stack;

    private String prevStackChar;
    private String stackChar;

    public Control(int initialState, Rules rules, String endOfStackSymbol)
    {
        this.currentState = initialState;
        this.nextState = this.currentState;
        this.rules = rules;
        this.endOfStackSymbol = endOfStackSymbol;

        this.stack = new Stack<>();
        this.stack.push(endOfStackSymbol);
    }

    void nextState(String currentChar, String lookahead) throws NullPointerException
    {
        this.prevStackChar = this.currentChar;
        this.currentState = this.nextState;
        this.currentChar = currentChar.equals("") ? "λ" : currentChar;
        this.prevStackChar = this.stackChar;
        this.stackChar = this.stack.pop();
        if (stackChar.equals(this.endOfStackSymbol) && this.stack.size() == 0)
            this.stack.push(this.endOfStackSymbol);
        Key key = new Key(this.currentState, this.currentChar, stackChar);

        try
        {
            List<Value> values = this.rules.getNextState(key);
            if (values.size() == 1)
            {
                Value value = values.get(0);
                for (String aux : value.getStack())
                    if (!aux.equals("λ"))
                        this.stack.push(aux);
                this.nextState = value.getState();
            }
            else
            {
                for (Value value : values)
                {
                    String nextStack = value.getStack()[0];
                    Key auxKey = new Key(2, lookahead, nextStack);
                    List<Value> auxValue = this.rules.getNextState(auxKey);
                    if (auxValue != null)
                    {
                        for (String aux : value.getStack())
                            if (!aux.equals("λ"))
                                this.stack.push(aux);
                        this.nextState = value.getState();
                    }
                }
            }
        }
        catch (NullPointerException e)
        {
            throw new NullPointerException();
        }
    }

    List<Key> possibleStates()
    {
        List<Key> keys = new ArrayList<>();
        for (Key key : this.rules.getRuleSet().keySet())
            if (key.getState() == 2 && key.getStackValue().equals(this.stackChar))
                keys.add(key);
        return keys;
    }

    int getNextState()
    {
        return nextState;
    }

    List<Value> getValues(Key key)
    {
        return this.rules.getNextState(key);
    }

    public int getCurrentState()
    {
        return currentState;
    }

    @Override
    public String toString()
    {
        return "Control{" +
                "currentState=" + currentState +
                ", nextState=" + nextState +
                ", currentChar='" + currentChar + '\'' +
                ", endOfStackSymbol='" + endOfStackSymbol + '\'' +
                ", rules=" + rules +
                ", stack=" + stack +
                '}';
    }
}