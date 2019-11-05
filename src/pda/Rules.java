package pda;

import java.util.*;

public class Rules
{
    private Map<Key, List<Value>> ruleSet;

    public Rules(String rulesReading)
    {
        this.ruleSet = new HashMap<>();

        for (String rule : rulesReading.split("\n"))
        {
            try
            {
                String[] parts = rule.split(" ");

                Key key = new Key(Integer.parseInt(parts[0]), parts[1], parts[2]);
                Value value = new Value(Integer.parseInt(parts[3]), Arrays.copyOfRange(parts, 4, parts.length));
                if (this.ruleSet.keySet().contains(key))
                    this.ruleSet.get(key).add(value);
                else
                {
                    this.ruleSet.put(key, new ArrayList<>());
                    this.ruleSet.get(key).add(value);
                }
            }
            catch (NumberFormatException ignored)
            {
            }
        }
    }

    List<Value> getNextState(Key key) throws NullPointerException
    {
        return this.ruleSet.get(key);
    }

    @Override
    public String toString()
    {
        StringBuilder string = new StringBuilder("f = {\n");

        for (Map.Entry<Key, List<Value>> entry : this.ruleSet.entrySet())
        {
            Key key = entry.getKey();
            List<Value> value = entry.getValue();

            string.append("\t\t  (").append(key.toString()).append(") = (").append(value.toString()).append("),\n");
        }

        string.deleteCharAt(string.length() - 2);
        string.append("\t}");

        return string.toString();
    }
}
