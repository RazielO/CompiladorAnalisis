package syntactic;

import exceptions.Error;
import files.FileReader;
import lexer.Tag;
import lexer.Token;
import syntactic.pda.PushDownAutomaton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import static java.util.stream.Collectors.toList;

public class Syntactic
{
    private PushDownAutomaton pda;
    private List<Symbol> symbolsTable;

    private String previous;

    private Stack<Error> errorStack;
    private Tag tag;

    public Syntactic() throws IOException
    {
        initAutomaton();
        this.symbolsTable = new ArrayList<>();
        this.errorStack = new Stack<>();
        this.tag = new Tag();
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

    public boolean validString(LinkedList<Token> tokens) throws NullPointerException, IOException
    {
        if (tokens != null)
        {
            initAutomaton();
            for (int i = 0; i < tokens.size(); i++)
                try
                {
                    if (i != 0)
                        this.previous = String.valueOf(tokens.get(i - 1).getTag());
                    else
                        this.previous = String.valueOf(tokens.get(i).getTag());

                    String current = String.valueOf(tokens.get(i).getTag());
                    String lookahead = i + 2 > tokens.size() ? "" : String.valueOf(tokens.get(i + 1).getTag());
                    if (tokens.get(i).getTag() == -1) // Lambda
                        current = tokens.get(i).getLexeme();
                    if ((i + 2) <= tokens.size() && tokens.get(i + 1).getTag() == -1) // Lambda
                        lookahead = tokens.get(i).getLexeme();

                    pda.getNextState(current, lookahead);

                    if (current.equals("224"))
                        addSymbolToTable(tokens, i);
                }
                catch (NullPointerException e)
                {
                    // Error
                    this.errorStack.push(new Error(200, tokens.get(i).getLine(), this.tag.get(tokens.get(i).getTag())));
                    tokens = panicMode(tokens);
                    return validString(tokens);
                }
            try
            {
                pda.getNextState("", "");
            }
            catch (Exception e)
            {
                return false;
            }
            return pda.isInValidState();
        }
        return false;
    }

    private LinkedList<Token> panicMode(LinkedList<Token> tokens)
    {
        int i = 0;
        while (tokens.size() > 0)
        {
            try
            {
                this.pda.getNextState(String.valueOf(tokens.get(i).getTag()), String.valueOf(tokens.get(i + 1).getTag()));
                i++;
                return tokens;
            }
            catch (Exception ignored)
            {
            }
            tokens.remove(i);
        }
        return null;
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
                        symbol.setValue(tokens.get(i + 2).getLexeme());
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

    public Stack<Error> getErrorStack()
    {
        return errorStack;
    }

    private String getAlphaNumericString()
    {

        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        StringBuilder sb = new StringBuilder(10);

        for (int i = 0; i < 10; i++)
        {
            int index = (int) (AlphaNumericString.length() * Math.random());
            sb.append(AlphaNumericString.charAt(index));
        }

        return sb.toString();
    }
}
