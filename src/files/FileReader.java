package files;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Clase para leer archivos
 */
public class FileReader
{
    private String filename;

    /**
     * Realiza la operación de apertura, lectura y cierre de un archivo
     *
     * @return contenido del archivo
     * @throws FileNotFoundException el archivo no existe
     */
    public String read() throws FileNotFoundException
    {
        StringBuilder reading = new StringBuilder();

        try
        {
            File file = new File(this.filename);
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine())
                reading.append(scanner.nextLine()).append("\n");

            scanner.close();
        }
        catch (FileNotFoundException e)
        {
            throw new FileNotFoundException(e.getMessage());
        }

        return reading.toString();
    }

    /**
     *
     * @param filename nombre del archivo a abrir
     */
    public void setFilename(String filename)
    {
        this.filename = filename;
    }
}
