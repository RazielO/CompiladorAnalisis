package semantic;

import exceptions.Error;
import lexer.Tag;
import lexer.Token;
import syntactic.Symbol;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class Semantic
{
    private LinkedList<Token> tokensList;
    private List<Symbol> symbolsTable;
    private Stack<Error> errorStack;

    private Tag tag;

    public Semantic(LinkedList<Token> tokensList, List<Symbol> symbolsTable)
    {
        this.tokensList = tokensList;
        this.symbolsTable = symbolsTable;
        this.errorStack = new Stack<>();
        this.tag = new Tag();
    }

    public void checkSemantics()
    {
        checkForUndefinedIDs();
        checkForMultipleDeclarations();
        checkForTypeCompatibility();
        checkForDivisionByZero();
    }

    private void checkForDivisionByZero()
    {
        for (int i = 0; i < this.tokensList.size(); i++)
        {
            if (tokenEquals(i, "DIVIDE"))
                if (tokenEquals(i + 1, "INT_VAL") && Integer.parseInt(this.tokensList.get(i + 1).getLexeme()) == 0)
                    this.errorStack.push(new Error(304, this.tokensList.get(i).getLine(), ""));
                else if (tokenEquals(i + 1, "FLOAT_VAL") && Double.parseDouble(this.tokensList.get(i + 1).getLexeme()) == 0)
                    this.errorStack.push(new Error(304, this.tokensList.get(i).getLine(), ""));
                else if (tokenEquals(i + 1, "CHAR_VAL"))
                    this.errorStack.push(new Error(305, this.tokensList.get(i).getLine(), "Can't divide by a string"));
                else if (tokenEquals(i + 1, "ID"))
                {
                    Symbol symbol = findSymbol(tokensList.get(i + 1).getLexeme());
                    assert symbol != null;
                    if (symbol.getType().equals("int") || symbol.getType().equals("float"))
                    {
                        if (Double.parseDouble(symbol.getValue()) == 0)
                            this.errorStack.push(new Error(304, this.tokensList.get(i).getLine(), ""));
                    }
                    else if (symbol.getType().equals("char"))
                        this.errorStack.push(new Error(305, this.tokensList.get(i).getLine(), "Can't divide by a string"));
                }
        }
    }

    private void checkForUndefinedIDs()
    {
        boolean startChecking = false;
        for (Token token : this.tokensList)
        {
            if (this.tag.get(token.getTag()).equals("BEGIN"))
                startChecking = true;
            if (startChecking)
                if (this.tag.get(token.getTag()).equals("ID") && !isDefined(token.getLexeme()))
                    this.errorStack.push(new Error(301, token.getLine(), token.getLexeme()));
        }
    }

    private boolean isDefined(String id)
    {
        for (Symbol symbol : this.symbolsTable)
            if (symbol.getId().equals(id) && symbol.getType() != null)
                return true;
        return false;
    }

    private void checkForMultipleDeclarations()
    {
        boolean startChecking = false;
        List<String> seen = new ArrayList<>();
        for (Token token : this.tokensList)
        {
            if (this.tag.get(token.getTag()).equals("DECLARE"))
                startChecking = true;
            else if (this.tag.get(token.getTag()).equals("BEGIN"))
                break;

            if (startChecking)
                if (this.tag.get(token.getTag()).equals("ID") && seen.contains(token.getLexeme()))
                    this.errorStack.push(new Error(302, token.getLine(), token.getLexeme()));
                else
                    seen.add(token.getLexeme());
        }
    }

    private String resultOfExpression(int start)
    {
        String first = "";

        while (!tokenEquals(start, "SEMI_COLON") && !tokenEquals(start, "PAR_CLOSE"))
        {
            Token token = tokensList.get(start);
            if (tokenEquals(start, "ID"))
            {
                Symbol symbol = findSymbol(token.getLexeme());
                if (symbol == null)
                    first = "UNDEFINED";
                else if (symbol.getValue() == null)
                {
                    this.errorStack.push(new Error(306, token.getLine(), symbol.getId()));
                    first = "UNDEFINED";
                }
                else if (first.length() == 0)
                    first = symbol.getType().toUpperCase();
                else if (!symbol.getType().toUpperCase().equals(first))
                    first = "UNDEFINED";
            }
            else if (tokenEquals(start, "CHAR_VAL") || tokenEquals(start, "INT_VAL")
                    || tokenEquals(start, "FLOAT_VAL") || tokenEquals(start, "BOOLEAN_VAL"))
            {
                if (first.length() == 0)
                    first = this.tag.get(token.getTag()).split("_")[0].toUpperCase();
                else if (!this.tag.get(token.getTag()).split("_")[0].toUpperCase().equals(first))
                    first = "UNDEFINED";
            }
            start++;
        }

        return first;
    }

    private void checkForTypeCompatibility()
    {
        for (int i = 0; i < this.tokensList.size(); i++)
        {
            String tag = this.tag.get(tokensList.get(i).getTag());
            switch (tag)
            {
                case "PLUS":
                case "TIMES":
                case "DIVIDE":
                case "MINUS":
                    Symbol a = findSymbol(this.tokensList.get(i - 1).getLexeme());
                    Symbol b = findSymbol(this.tokensList.get(i + 1).getLexeme());
                    if (b != null && a != null)
                    {
                        if (a.getType() != null && b.getType() != null && !a.getType().equals(b.getType()))
                            this.errorStack.push(new Error(303, this.tokensList.get(i).getLine(), String.format("%s and %s", a.getType(), b.getType())));
                    }
                    else if (a != null)
                    {
                        if (!String.format("%s_VAL", a.getType().toUpperCase()).equals(this.tag.get(tokensList.get(i + 1).getTag())))
                            this.errorStack.push(new Error(303, this.tokensList.get(i).getLine(), String.format("%s and %s", a.getType(), this.tag.get(tokensList.get(i + 1).getTag()).split("_")[0].toLowerCase())));
                    }
                    else if (b != null)
                    {
                        if (!String.format("%s_VAL", b.getType().toUpperCase()).equals(this.tag.get(tokensList.get(i - 1).getTag())))
                            this.errorStack.push(new Error(303, this.tokensList.get(i).getLine(), String.format("%s and %s", b.getType(), this.tag.get(tokensList.get(i - 1).getTag()).split("_")[0].toLowerCase())));
                    }
                    else if (!this.tag.get(tokensList.get(i - 1).getTag()).equals(this.tag.get(tokensList.get(i + 1).getTag())))
                        this.errorStack.push(new Error(303, this.tokensList.get(i).getLine(), String.format("%s and %s", this.tag.get(tokensList.get(i + 1).getTag()).split("_")[0].toLowerCase(), this.tag.get(tokensList.get(i + 1).getTag()).split("_")[0].toLowerCase())));
                    break;
                case "ASIG":
                    Symbol symbol = findSymbol(this.tokensList.get(i - 1).getLexeme());
                    if (symbol != null)
                    {
                        String result = resultOfExpression(i + 1);
                        if (!symbol.getType().toUpperCase().equals(result))
                            this.errorStack.push(new Error(303, this.tokensList.get(i).getLine(), String.format("%s and %s", result, symbol.getType().toUpperCase())));
                    }
                    break;
                case "WHILE":
                case "IF":
                    if (resultOfExpression(i + 2).equals("UNDEFINED"))
                        this.errorStack.push(new Error(303, tokensList.get(i).getLine(), "can't do comparatives between multiple data types"));
                    break;
            }
        }
    }

    private Symbol findSymbol(String id)
    {
        for (Symbol symbol : this.symbolsTable)
            if (symbol.getId().equals(id))
                return symbol;
        return null;
    }

    public Stack<Error> getErrorStack()
    {
        return errorStack;
    }

    private boolean tokenEquals(int i, String value)
    {
        return this.tag.get(this.tokensList.get(i).getTag()).equals(value);
    }
}
