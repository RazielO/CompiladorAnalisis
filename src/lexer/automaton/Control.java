package lexer.automaton;

/**
 * Representa el control de un autómata, tiene la información sobre el estado
 * actual y se mueve al siguiente según el caracter de entrada. También almacena
 * una instancia de la clase Rules que representa las reglas del automata
 */
public class Control
{
    private int currentState;
    private int nextState;
    private String currentChar;

    private Rules rules;

    /**
     * Constructor de la clase Control.
     *
     * @param initialState estado inicial del autómata
     * @param rules conjunto de reglas del autómata
     */
    Control(int initialState, Rules rules)
    {
        this.currentState = initialState;
        this.rules = rules;
    }

    /**
     * Obtiene el siguiente estado del autómata tomando en cuenta el carácter
     * de entrada. Construye un objeto KeyPair con el caracter actual y el estado
     * actual, para obtener el siguiente estado, esto mediante el objeto Rules.
     *
     * @param currentChar caracter de entrada al autómata
     * @throws NullPointerException no se encontró un estado válido 'q' para '(p,a)' en δ(p,a) = q
     */
    void nextState(String currentChar) throws NullPointerException
    {
        try
        {
            this.currentState = this.nextState;
            this.currentChar = currentChar;

            KeyPair keyPair = new KeyPair(this.currentState, this.currentChar);
            this.nextState = rules.getNextState(keyPair);
        }
        catch (NullPointerException e)
        {
            throw new NullPointerException(e.getMessage());
        }
    }

    /**
     * @return estado actual del autómata
     */
    int getCurrentState()
    {
        return currentState;
    }

    /**
     *
     * @return siguiente estado del autómata
     */
    int getNextState()
    {
        return nextState;
    }

    /**
     *
     * @param currentState nuevo estado del autómata
     */
    public void setCurrentState(int currentState)
    {
        this.currentState = currentState;
    }

    /**
     *
     * @param currentChar nuevo caracter actual
     */
    public void setCurrentChar(String currentChar)
    {
        this.currentChar = currentChar;
    }
}
