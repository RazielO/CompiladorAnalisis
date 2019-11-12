package syntactic.pda;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

public class PushDownAutomaton
{
    private Set<Integer> states, finalStates;
    private Integer initialState;
    private Set<String> alphabet, stackSymbols;
    private String endOfStackSymbol;

    private Rules rules;
    private Control control;

    private String stackValue;
    private String value;

    public PushDownAutomaton(String infoReading, String rulesReading)
    {
        this.rules = new Rules(rulesReading);

        String[] info = infoReading.split("\n");

        this.alphabet = new HashSet<>(Arrays.asList(info[0].split(" ")));
        this.stackSymbols = new HashSet<>(Arrays.asList(info[1].split(" ")));
        this.states = new HashSet<>(
                Arrays.asList(Stream.of(info[2].split(" ")).map(Integer::valueOf).toArray(Integer[]::new)));
        this.initialState = Integer.parseInt(info[3]);
        this.endOfStackSymbol = info[4].trim();
        this.finalStates = new HashSet<>(
                Arrays.asList(Stream.of(info[5].split(" ")).map(Integer::valueOf).toArray(Integer[]::new)));

        this.control = new Control(this.initialState, this.rules, this.endOfStackSymbol);
    }

    public boolean validString(String string)
    {
        try
        {
            String[] parts = string.split(" ");
            for (int i = 0; i < parts.length; i++)
                getNextState(parts[i], i + 2 > parts.length ? "" : parts[i + 1]);
            getNextState("", "");

            return finalStates.contains(control.getNextState());
        }
        catch (NullPointerException e)
        {
            return false;
        }
    }

    public void getNextState(String current, String lookahead)
    {
        setStackValue(control.peekStackTopSymbol());
        setValue(control.getCurrentChar());
        control.nextState(current, lookahead);
    }

    public String getStackValue()
    {
        return stackValue;
    }

    public void setStackValue(String stackValue)
    {
        this.stackValue = stackValue;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }

    public boolean isInValidState()
    {
        return finalStates.contains(control.getNextState());
    }

    public int getState()
    {
        return control.getNextState();
    }
}