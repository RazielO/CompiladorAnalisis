package syntactic;

import files.FileReader;
import syntactic.pda.PushDownAutomaton;
import lexer.Token;

import java.io.IOException;
import java.util.*;

import static java.util.stream.Collectors.toList;

public class Syntactic
{
    private PushDownAutomaton pda;
    private List<Symbol> symbolsTable;

    public Syntactic() throws IOException
    {
        initAutomaton();
        this.symbolsTable = new ArrayList<>();
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

    public boolean validString(LinkedList<Token> tokens)
    {
        for (int i = 0; i < tokens.size(); i++)
            try
            {
                String current = String.valueOf(tokens.get(i).getTag());
                String lookahead = i + 2 > tokens.size() ? "" : String.valueOf(tokens.get(i + 1).getTag());
                if (tokens.get(i).getTag() == -1) // Lambda
                    current = tokens.get(i).getLexeme();
                if ((i + 2) <= tokens.size() && tokens.get(i + 1).getTag() == -1) // Lambda
                    lookahead = tokens.get(i).getLexeme();


                if (current.equals("224"))
                    addSymbolToTable(tokens, i);

                pda.getNextState(current, lookahead);
            }
            catch (NullPointerException e)
            {
                // Error
                System.err.println(tokens.get(i - 1).getLexeme());
            }
        pda.getNextState("", "");

        System.out.println(pda.getState());
        return pda.isInValidState();
    }

    private void addSymbolToTable(LinkedList<Token> tokens, int i)
    {
//        <id> = <expr>;
//        <tipo> <id>;
        try
        {
            Symbol symbol = null;
            if (tokens.get(i - 1).getTag() == 220 ||
                    tokens.get(i - 1).getTag() == 221 ||
                    tokens.get(i - 1).getTag() == 222 ||
                    tokens.get(i - 1).getTag() == 223)
                symbol = new Symbol(tokens.get(i).getLexeme(), tokens.get(i - 1).getLexeme(), null, tokens.get(i).getLine());
            else if (tokens.get(i + 1).getTag() == 231 && (
                    tokens.get(i + 2).getTag() == 225 ||
                            tokens.get(i + 2).getTag() == 217 ||
                            tokens.get(i + 2).getTag() == 218 ||
                            tokens.get(i + 2).getTag() == 226 ||
                            tokens.get(i + 2).getTag() == 227
            ))
            {
                symbol = new Symbol(tokens.get(i).getLexeme(), null, tokens.get(i + 2).getLexeme(), tokens.get(i).getLine());
                for (Symbol aSymbolsTable : this.symbolsTable)
                    if (aSymbolsTable.getId().equals(tokens.get(i).getLexeme()))
                    {
                        symbol = aSymbolsTable;
                        symbol.addLine(tokens.get(i).getLine());
                    }
            }
            else
            {
                for (Symbol aSymbolsTable : this.symbolsTable)
                    if (aSymbolsTable.getId().equals(tokens.get(i).getLexeme()))
                    {
                        symbol = aSymbolsTable;
                        symbol.addLine(tokens.get(i).getLine());
                    }
            }

            if (symbol != null)
            {
                Symbol finalSymbol = symbol;
                this.symbolsTable = this.symbolsTable
                        .stream()
                        .filter(item -> !item.getId().equals(finalSymbol.getId()))
                        .collect(toList());

                this.symbolsTable.add(symbol);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public List<Symbol> getSymbolsTable()
    {
        return symbolsTable;
    }
}
