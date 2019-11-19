package syntactic.pda;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class PushDownAutomaton
{
    private final Set<Integer> finalStates;

    private final Control control;

    public PushDownAutomaton(String infoReading, String rulesReading)
    {
        Rules rules = new Rules(rulesReading);

        String[] info = infoReading.split("\n");

        int initialState = Integer.parseInt(info[3]);
        String endOfStackSymbol = info[4].trim();
        this.finalStates = new HashSet<>(
                Arrays.asList(Stream.of(info[5].split(" ")).map(Integer::valueOf).toArray(Integer[]::new)));

        this.control = new Control(initialState, rules, endOfStackSymbol);
    }

    public void getNextState(String current, String lookahead)
    {
        control.nextState(current, lookahead);
    }

    public boolean isInValidState()
    {
        return finalStates.contains(control.getCurrentState());
    }

    public int getState()
    {
        return control.getNextState();
    }

    public List<Key> possibleValues()
    {
        return this.control.possibleStates();
    }

    public List<Value> getValues(Key key)
    {
        return this.control.getValues(key);
    }
}