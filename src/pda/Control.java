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

    void nextState(String currentChar) throws NullPointerException
    {
        this.currentState = this.nextState;
        this.currentChar = currentChar.equals("") ? "λ" : currentChar;
        String stackChar = this.stack.pop();
        if (stackChar.equals(this.endOfStackSymbol) && this.stack.size() == 0)
            this.stack.push(this.endOfStackSymbol);
        Key key = new Key(this.currentState, this.currentChar, stackChar);
        Value value = this.rules.getNextState(key);
        if (!value.getStack().equals("λ"))
            this.stack.push(value.getStack());
        this.nextState = value.getState();
        System.out.printf("Current State: '%s', Current Char: '%s', Stack: '%s', Next State: '%s'\n", this.currentState, currentChar, this.stack.toString(), this.nextState);
    }

    int getNextState()
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