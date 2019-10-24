package pda;

import java.util.Stack;

public class Control
{
    private int currentState;
    private int nextState;
    private String currentChar;
    private String endOfStackSymbol;

    private Rules rules;
    private Stack<String> stack;

    public Control(int initialState, Rules rules, String endOfStackSymbol)
    {
        this.currentState = initialState;
        this.nextState = this.currentState;
        this.rules = rules;
        this.endOfStackSymbol = endOfStackSymbol;

        this.stack = new Stack<>();
        this.stack.push(endOfStackSymbol);
    }

    public void nextState(String currentChar) throws NullPointerException
    {
        this.currentState = this.nextState;
        this.currentChar = currentChar.equals("") ? "λ" : currentChar;
        String stackChar = this.stack.pop();
        if (stackChar.equals(this.endOfStackSymbol) && this.stack.size() == 0)
            this.stack.push(this.endOfStackSymbol);
        Key key = new Key(this.currentState, this.currentChar, stackChar);
        //System.out.printf("Key: '%s'\n", key.toString());
        Value value = this.rules.getNextState(key);
        for (String val : value.getStack().split(""))
            if (!val.equals("λ"))
                this.stack.push(val);
        this.nextState = value.getState();
    }

    public int getNextState()
    {
        return nextState;
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