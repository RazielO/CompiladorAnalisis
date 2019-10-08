package lexer.automaton;

import java.util.Objects;

/**
 * Clase que se usará como llave para obtener el siguiente estado en el autómata
 * en una regla de producción δ(p,a) = q, esta clase representa el par ordenado (p,a)
 * donde 'p', es el estado actual del automata; y 'a' es el caracter de entrada
 */
public class KeyPair
{
    private int state;
    private String value;

    /**
     * Constructor de la clase KeyPair
     *
     * @param state estado actual del automata (p)
     * @param value caracter de entrada en el automata (a)
     */
    KeyPair(int state, String value)
    {
        this.state = state;
        this.value = value;
    }

    /**
     * Método usado para saber si dos instancias de esta clase son iguales.
     * Necesario para ser usado como llave de un HashMap.
     *
     * @param object objeto a comparar con la instancia
     * @return son iguales o no
     */
    @Override
    public boolean equals(Object object)
    {
        if (object instanceof KeyPair)
        {
            KeyPair keyPair = (KeyPair) object;

            return keyPair.state == this.state && Objects.equals(keyPair.value, value);
        }

        return false;
    }

    /**
     * Método que obtiene el hash de cada instancia.
     * Necesario para ser usado como llave de un HashMap.
     *
     * @return hash de la representación en String de la instancia
     */
    @Override
    public int hashCode()
    {
        return (String.valueOf(this.state) + value).hashCode();
    }

    /**
     * Hace la representación en String de la instancia de la forma (qp, a),
     * donde 'p' es el número de estado actual y 'a' es el caracter de entrada
     *
     * @return representación en String de la instancia
     */
    @Override
    public String toString()
    {
        return "(q" + state + ", " + value + ")";
    }
}
