package lexer;

/**
 * Clase que contiene la información que el analizador léxico requiere almacenar
 * de cada token que se encuentra en el código.
 */
public class Token
{
    private String type;
    private int line, tag;

    /**
     * Constructor de la clase Token
     *
     * @param tag etiqueta del token identificado
     * @param type tipo de token ('id', 'keyword')
     * @param line línea en que fue encontrado
     */
    public Token(int tag, String type, int line)
    {
        this.type = type;
        this.line = line;
        this.tag = tag;
    }


    /**
     *
     * @return tipo de token ('id', 'keyword')
     */
    public String getType()
    {
        return type;
    }

    /**
     *
     * @param type tipo de token ('id', 'keyword')
     */
    public void setType(String type)
    {
        this.type = type;
    }

    /**
     *
     * @return línea en que fue encontrado
     */
    public int getLine()
    {
        return line;
    }

    /**
     *
     * @param line línea en que fue encontrado
     */
    public void setLine(int line)
    {
        this.line = line;
    }

    /**
     *
     * @return etiqueta del token identificado
     */
    public int getTag()
    {
        return tag;
    }

    /**
     *
     * @param tag etiqueta del token identificado
     */
    public void setTag(int tag)
    {
        this.tag = tag;
    }
}
