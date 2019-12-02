package lexer;

import java.util.Hashtable;

/**
 * Clase que se toma como referencia para los identificadores
 */
public class Tag
{
    private Hashtable<Integer, String> tags;

    /**
     * Constructor de la clase Tag. Inicializa la tabla de etiquetas y agrega los números
     * de tokens válidos
     */
    public Tag()
    {
        this.tags = new Hashtable<>();

        this.tags.put(-1, "LAMBDA");
        this.tags.put(200, "DECLARE");
        this.tags.put(201, "BEGIN");
        this.tags.put(203, "PRINT");
        this.tags.put(204, "READ");
        this.tags.put(205, "IF");
        this.tags.put(206, "ELSE");
        this.tags.put(207, "WHILE");
        this.tags.put(208, "DO");
        this.tags.put(209, "OR");
        this.tags.put(210, "AND");
        this.tags.put(211, "LT");
        this.tags.put(212, "GT");
        this.tags.put(213, "LE");
        this.tags.put(214, "GE");
        this.tags.put(215, "EQ");
        this.tags.put(216, "NE");
        this.tags.put(217, "BOOLEAN_VAL");
//        this.tags.put(218, "FALSE");
        this.tags.put(219, "BREAK");
        this.tags.put(220, "INT_TYPE");
        this.tags.put(221, "FLOAT_TYPE");
        this.tags.put(222, "BOOLEAN_TYPE");
        this.tags.put(223, "CHAR_TYPE");
        this.tags.put(224, "ID");
        this.tags.put(225, "CHAR_VAL");
        this.tags.put(226, "INT_VAL");
        this.tags.put(227, "FLOAT_VAL");
        this.tags.put(228, "PAR_OPEN");
        this.tags.put(229, "PAR_CLOSE");
        this.tags.put(230, "PLUS");
        this.tags.put(231, "ASIG");
        this.tags.put(232, "SEMI_COLON");
        this.tags.put(233, "CURLY_OPEN");
        this.tags.put(234, "CURLY_CLOSE");
        this.tags.put(235, "TIMES");
        this.tags.put(236, "DIVIDE");
        this.tags.put(237, "MINUS");
    }

    /**
     * Agregar a la tabla de etiquetas
     *
     * @param key número de token
     * @param value valor con el que identificamos al token
     */
    public void add(int key, String value)
    {
        this.tags.put(key, value);
    }

    /**
     * Obtener el valor de identificación del token según su identificador
     *
     * @param key número de token
     * @return valor con el que identificamos al token
     */
    public String get(int key)
    {
        return this.tags.get(key);
    }
}
