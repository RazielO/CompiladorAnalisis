package pda;

import java.util.HashMap;
import java.util.Map;

public class Rules
{
    private Map<Key, Value> ruleSet;

    public Rules(String rulesReading)
    {
        this.ruleSet = new HashMap<>();

        for (String rule : rulesReading.split("\n"))
        {
            String[] parts = rule.split(" ");

            Key key = new Key(Integer.parseInt(parts[0]), parts[1], parts[2]);
            Value value = new Value(Integer.parseInt(parts[3]), parts[4]);
            ruleSet.put(key, value);
        }
    }

    Value getNextState(Key key) throws NullPointerException
    {
        return this.ruleSet.get(key);
    }

    @Override
    public String toString()
    {
        StringBuilder string = new StringBuilder("f = {\n");

        for (Map.Entry<Key, Value> entry : this.ruleSet.entrySet())
        {
            Key key = entry.getKey();
            Value value = entry.getValue();

            string.append("\t\t  (").append(key.toString()).append(") = (").append(value.toString()).append("),\n");
        }

        string.deleteCharAt(string.length() - 2);
        string.append("\t}");

        return string.toString();
    }
}
