package pda;

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
            for (String token : string.split(" "))
                control.nextState(token);
            control.nextState("");

            return finalStates.contains(control.getNextState());
        }
        catch (NullPointerException e)
        {
            return false;
        }
    }
}