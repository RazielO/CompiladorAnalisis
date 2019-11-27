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

    private boolean isDefined(String id)
    {
        for (Symbol symbol : this.symbolsTable)
            if (symbol.getId().equals(id) && symbol.getType() != null)
                return true;
        return false;
    }

    public Stack<Error> getErrorStack()
    {
        return errorStack;
    }
}
