package syntactic;

import files.FileReader;
import syntactic.pda.PushDownAutomaton;

import java.io.IOException;

public class Syntactic
{
    private PushDownAutomaton pda;

    public Syntactic() throws IOException
    {
        initAutomaton();
    }

    private void initAutomaton() throws IOException
    {
        String info = System.getProperty("user.dir") + "/src/syntactic/pda/files/info.txt";
        String rules = System.getProperty("user.dir") + "/src/syntactic/pda/files/rules.txt";

        FileReader reader = new FileReader();
        reader.setFilename(info);
        info = reader.read();
        reader.setFilename(rules);
        rules = reader.read();

        pda = new PushDownAutomaton(info, rules);
    }

    public boolean validString(String lexerResult)
    {
        String[] tokens = lexerResult.split(" ");

        for (int i = 0; i < tokens.length; i++)
            pda.getNextState(tokens[i], i + 2 > tokens.length ? "" : tokens[i + 1]);
        pda.getNextState("", "");

        return pda.isInValidState();
    }
}
