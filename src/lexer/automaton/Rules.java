package lexer.automaton;

import java.util.HashMap;
import java.util.Map;

/**
 * Clase que representa las reglas de producción de un automata en la forma
 * δ(p,a) = q. Estas reglas son almacenadas en un HashMap donde la llave es una
 * instancia de la clase KeyPair que representa el par ordenado '(p,a)';y el valor
 * es un entero que representa 'q'.
 */
public class Rules
{
    private Map<KeyPair, Integer> ruleSet;

    /**
     * Constructor de la clase Rules. Separa las reglas por el salto de línea y posteriormente
     * por el espacio, con esto podremos crear el objeto KeyPair e ingresarlo con su respectivo
     * valor. Cada línea de 'rulesReading' tiene el formato 'p q a'.
     *
     * @param rulesReading lectura del archivo de reglas de cada automata
     */
    Rules(String rulesReading)
    {
        this.ruleSet = new HashMap<>();

        for (String rule : rulesReading.split("\n"))
        {
            String[] parts = rule.split(" ");

            KeyPair keyPair = new KeyPair(Integer.parseInt(parts[0].trim()), parts[1].trim());
            ruleSet.put(keyPair, Integer.parseInt(parts[2].trim()));
        }
    }

    /**
     * Obtiene el siguiente estado del automata según el estado actual y el
     * caracter de entrada.
     *
     * @param keyPair par ordenado del estado actual y el caracter de entrada
     * @return estado siguiente del automata
     * @throws NullPointerException no se encontró una regla con ese par ordenado
     */
    int getNextState(KeyPair keyPair) throws NullPointerException
    {
        return this.ruleSet.get(keyPair);
    }

    /**
     * Construye la representación en String de la instancia, incluye todas las reglas de producción
     * en la forma δ(p,a) = q
     *
     * @return representación en String de la instancia
     */
    @Override
    public String toString()
    {
        StringBuilder string = new StringBuilder("δ = {\n");

        for (Map.Entry<KeyPair, Integer> entry : this.ruleSet.entrySet())
        {
            KeyPair key = entry.getKey();
            Integer value = entry.getValue();

            string.append("\t\t  δ").append(key.toString()).append(" = q").append(value).append(",\n");
        }

        string.deleteCharAt(string.length() - 2);
        string.append("\t}");

        return string.toString();
    }
}
