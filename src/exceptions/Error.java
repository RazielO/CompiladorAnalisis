package exceptions;

/**
 * Clase para almacenar el número de error y la línea en la que sucedió.
 */
public class Error
{
    private int errorCode, line;
    private String info;

    /**
     * Constructor de la clase Error.
     *
     * @param errorCode número de error
     * @param line línea en que fue encontrado
     */
    public Error(int errorCode, int line)
    {
        this.errorCode = errorCode;
        this.line = line;
        this.info = null;
    }

    public Error(int errorCode, int line, String info)
    {
        this.errorCode = errorCode;
        this.line = line;
        this.info = info;
    }

    /**
     *
     * @return número de error
     */
    int getErrorCode()
    {
        return errorCode;
    }

    /**
     *
     * @param errorCode número de error
     */
    public void setErrorCode(int errorCode)
    {
        this.errorCode = errorCode;
    }

    /**
     *
     * @return línea en que fue encontrado
     */
    int getLine()
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

    public String getInfo()
    {
        return info;
    }
}
