package files;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
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
     */
    public String read()
    {
        StringBuilder reading = new StringBuilder();

        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(filename);
        Scanner scanner = new Scanner(inputStream);

        while (scanner.hasNextLine())
            reading.append(scanner.nextLine()).append("\n");

        scanner.close();

        return reading.toString();
    }

    /**
     * @param filename nombre del archivo a abrir
     */
    public void setFilename(String filename)
    {
        this.filename = filename;
    }
}
