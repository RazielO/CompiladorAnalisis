package exceptions;

import files.FileReader;

import java.io.FileNotFoundException;
import java.util.Hashtable;
import java.util.Stack;

/**
 * Clase para imprimir todos los errores encontrados
 */
public class PrintErrors
{
    private Stack<Error> errors;
    private Hashtable<Integer, String> errorCodes;

    private final String ERRORS_FILE = System.getProperty("user.dir") + "/src/exceptions/errors.txt";

    /**
     * Constructor de la lase PrintErrors, llama al método init
     */
    public PrintErrors()
    {
        init();
    }

    /**
     * Constructor de la lase PrintErrors, llama al método init
     *
     * @param errors pila de errores a imprimir
     */
    public PrintErrors(Stack<Error> errors)
    {
        this.errors = errors;
        init();
    }

    /**
     * Lee el catálogo de errores y los ingresa en una tabla para acceder a ellos
     * de forma más fácil
     */
    private void init()
    {
        FileReader fileReader = new FileReader();
        fileReader.setFilename(ERRORS_FILE);

        errorCodes = new Hashtable<>();
        try
        {
            for (String line : fileReader.read().split("\n"))
            {
                String[] aux = line.split(",");
                errorCodes.put(Integer.parseInt(aux[0].trim()), aux[1].trim());
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * @param errors pila de errores a imprimir
     */
    public void setErrors(Stack<Error> errors)
    {
        this.errors = errors;
    }

    /**
     * Vacía la pila imprimiendo el error en stderr, la línea, su tipo y el mensaje.
     */
    public void showErrors()
    {
        System.err.println("ERRORES");
        while (!errors.empty())
        {
            Error error = errors.pop();
            String errorMsg = String.format("Error %s [%d] en la línea %d. %s", errorType(error.getErrorCode()), error.getErrorCode(), error.getLine(), errorCodes.get(error.getErrorCode()));
            System.err.println(errorMsg);
        }
    }

    /**
     * Tipo de error:
     * - 100 <= errorCode < 200 -> léxico
     *
     * @param errorCode número de error
     * @return tipo de error
     */
    private String errorType(int errorCode)
    {
        if (errorCode >= 100 && errorCode < 200)
            return "léxico";
        else
            return "otro";
    }
}
