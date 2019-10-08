package lexer.automaton;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Clase que simula un autómata finito determinista. En una definición formal
 * M = < Q, Σ ,δ, q0, F >, esta clase mantiene toda esa información:
 *  - Estados válidos
 *  - Estados finales
 *  - Reglas de producción
 *  - Alfabeto
 *  - Funciones de transición
 *
 * Además tiene una instancia de la clase Control para obtener y moverlo al siguiente
 * estado.
 */
public class Automaton
{
    private Set<Integer> states, finalStates;
    private Integer initialState;
    private Rules rules;
    private Set<String> alphabet;

    private Control control;

    /**
     * Constructor de la clase Automaton. Inicializa todos los valores de los estados válidos,
     * estados finales, reglas de producción, alfabeto y funciones de transición.
     *
     * @param rulesReading lectura del archivo de reglas respectivo a cada automata
     * @param infoReading lectura del archivo de información respectivo a cada automata
     */
    public Automaton(String rulesReading, String infoReading)
    {
        this.rules = new Rules(rulesReading);

        String[] lines = infoReading.split("\n");

        this.states = new HashSet<>(
                Arrays.asList(Stream.of(lines[0].split(" ")).map(Integer::valueOf).toArray(Integer[]::new)));
        this.alphabet = new HashSet<>(Arrays.asList(lines[1].split(" ")));
        this.initialState = Integer.valueOf(lines[2]);
        this.finalStates = new HashSet<>(
                Arrays.asList(Stream.of(lines[3].split(" ")).map(Integer::valueOf).toArray(Integer[]::new)));

        this.control = new Control(initialState, rules);
    }

    /**
     * Hace una iteración sobre los caracteres de una cadena, y va moviendo el control al estado
     * correspondiente, si al finalizar, se encuentra en un estado final, la cadena es válida; si
     * al final, se encuentra en un estado no final, o no existe una regla válida, la cadena
     * no es válida.
     *
     * @param string cadena a comprobar
     * @return se encuentra o no en el lenguaje
     */
    public boolean stringInTheLanguage(String string)
    {
        try
        {
            for (String letter : string.split(""))
                control.nextState(letter);

            return finalStates.contains(control.getNextState());
        }
        catch (NullPointerException e)
        {
            return false;
        }
    }

    /**
     *
     * @return estado del autómata. Usado como identificador numérico del token
     */
    public int getTokenTag()
    {
        return control.getNextState();
    }

    /**
     * Hace un reinicio del autómata.
     */
    public void restart()
    {
        this.control = new Control(initialState, rules);
    }

    /**
     * Hace una representación en String del autómata, en la forma M = < Q, Σ ,δ, q0, F >
     *
     * @return representación en String del autómata
     */
    @Override
    public String toString()
    {
        String string = "M = <\n";

        string += "\tQ = " + this.states.toString() + ",\n";
        string += "\tΣ = " + this.alphabet.toString() + ",\n";
        string += "\t" + this.rules.toString() + ",\n";
        string += "\tq0 = q" + this.initialState + ",\n";
        string += "\tF = " + this.finalStates.toString() + "\n";

        return string + ">";
    }
}
