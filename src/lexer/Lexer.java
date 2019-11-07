package lexer;

import exceptions.Error;
import files.FileReader;
import lexer.automaton.Automaton;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Hashtable;
import java.util.Stack;

/**
 * Clase que realiza el análisis léxico
 */
public class Lexer
{
    private static int line = 1;
    private Hashtable<String, Token> symbols = new Hashtable<>();

    private static String code;

    private static BufferedReader bufferedReader;

    private char current = ' ';
    private char lookahead = ' ';
    private String lexeme;

    private boolean endOfCode = false;

    private Automaton id_automaton, number_automaton, reserved_automaton, string_automaton, unary_automaton,
            oprel_automaton;

    private Stack<Error> errorStack;

    /**
     * Constructor de la clase Lexer.
     *
     * @param code lectura del archivo de código
     * @throws FileNotFoundException excepción necesaria para hacer uso del BufferedReader
     */
    public Lexer(String code) throws IOException
    {
        Lexer.code = code;
        InputStream inputStream = new ByteArrayInputStream(Lexer.code.getBytes(Charset.forName("UTF-8")));
        bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        errorStack = new Stack<>();

        initAutomata();
    }

    /**
     * Inicializa todos los automatas leyendo los archivos de reglas y de información.
     *
     * @throws IOException los archivos de reglas o de información no existen
     */
    private void initAutomata() throws IOException
    {
        String folder = System.getProperty("user.dir") + "/src/lexer/automaton/";
        String[] definitions = {"info/", "rules/"};
        String[] fileEnding = {"_info.txt", "_rules.txt"};
        String[] names = {"id", "number", "reserved", "string", "unary", "oprel"};
        Automaton[] automata = {id_automaton, number_automaton, reserved_automaton, string_automaton,
                unary_automaton, oprel_automaton};

        FileReader infoFileReader = new FileReader();
        FileReader rulesFileReader = new FileReader();

        for (int i = 0; i < automata.length; i++)
        {
            infoFileReader.setFilename(folder + definitions[0] + names[i] + fileEnding[0]);
            rulesFileReader.setFilename(folder + definitions[1] + names[i] + fileEnding[1]);

            automata[i] = new Automaton(rulesFileReader.read(), infoFileReader.read());
        }

        id_automaton = automata[0];
        number_automaton = automata[1];
        reserved_automaton = automata[2];
        string_automaton = automata[3];
        unary_automaton = automata[4];
        oprel_automaton = automata[5];
    }

    /**
     * Lee el siguiente caracter del código hasta que sea el final
     *
     * @throws IOException no se puede leer el siguiente caracter
     */
    private void readChar() throws IOException
    {
        current = lookahead;
        lookahead = (char) bufferedReader.read();
        if (current == '\uFFFF')
            this.endOfCode = true;
    }

    /**
     * Método principal. Lee caracter por caracter hasta encontrar el final de código o
     * hasta encontrar un delimitador, la lectura se ingresa en todos los automatas, y en caso de
     * que pertenezca a alguno, regresa su número de token; de lo contrario, ingresa un nuevo
     * error a la pila de errores.
     *
     * @return número de token leído
     * @throws IOException no se puede leer el siguiente caracter
     */
    public int scanNextToken() throws IOException
    {
        if (!endOfCode)
        {
            id_automaton.restart();
            number_automaton.restart();
            reserved_automaton.restart();
            string_automaton.restart();
            unary_automaton.restart();
            oprel_automaton.restart();

            for (; ; readChar())
                if (current == '\n')
                    line += 1;
                else if (isNotDelimiter())
                    break;

            StringBuilder builder = new StringBuilder();
            do
            {
                if (current == '/' && lookahead == '/')
                {
                    while (current != '\n' && !isEndOfCode())
                        readChar();
                    break;
                }
                builder.append(current);
                if (isOpRel() || oprel_automaton.stringInTheLanguage(String.valueOf(current)))
                {
                    readChar();
                    if (oprel_automaton.stringInTheLanguage(builder.toString() + String.valueOf(current)))
                    {
                        builder.append(current);
                        readChar();
                    }
                    oprel_automaton.restart();
                    break;
                }
                else if (isUnary())
                {
                    readChar();
                    break;
                }
                readChar();
            } while (isNotDelimiter() && !endOfCode);

            oprel_automaton.restart();
            lexeme = builder.toString();

            if (reserved_automaton.stringInTheLanguage(lexeme))
            {
                int tokenTag = reserved_automaton.getTokenTag();
                symbols.put(lexeme, new Token(tokenTag, "keyword", 0));
                return tokenTag;
            }
            else if (id_automaton.stringInTheLanguage(lexeme))
            {
                int tokenTag = id_automaton.getTokenTag();
                symbols.put(lexeme, new Token(tokenTag, "id", line));
                return tokenTag;
            }
            else if (number_automaton.stringInTheLanguage(lexeme))
                return number_automaton.getTokenTag();
            else if (string_automaton.stringInTheLanguage(lexeme))
                return string_automaton.getTokenTag();
            else if (unary_automaton.stringInTheLanguage(lexeme))
                return unary_automaton.getTokenTag();
            else if (oprel_automaton.stringInTheLanguage(lexeme))
                return oprel_automaton.getTokenTag();
            else if (lexeme.equals(""))
                return -1;
            else
                errorStack.push(new Error(100, line));
        }
        return -1;
    }

    /**
     * El caracter actual es un delimitador explícito (espacio, tabulador o salto de línea)
     *
     * @return el caracter actual es delimitador
     */
    private boolean isNotDelimiter()
    {
        return current != ' ' && current != '\t' && current != '\n';
    }

    /**
     * El caracter actual o el siguiente es un caracter único que sirve como delimitador
     *
     * @return el caracter actual o el siguiente es un caracter único que sirve como delimitador
     */
    private boolean isUnary()
    {
        boolean val = unary_automaton.stringInTheLanguage(String.valueOf(lookahead));
        unary_automaton.restart();
        val = val || unary_automaton.stringInTheLanguage(String.valueOf(current));
        unary_automaton.restart();
        return val;
    }

    /**
     * El caracter actual o los 2 siguientes son un operador relacional
     *
     * @return el caracter actual o los 2 siguientes son un operador relacional
     */
    private boolean isOpRel()
    {
        boolean val = oprel_automaton.stringInTheLanguage(String.valueOf(lookahead)) || lookahead == '=' || lookahead == '!';
        oprel_automaton.restart();
        return val;
    }

    /**
     * @return se leyó todo el código
     */
    public boolean isEndOfCode()
    {
        return endOfCode;
    }

    /**
     * @return tabla de símbolos encontrados
     */
    public Hashtable<String, Token> getSymbols()
    {
        return symbols;
    }

    /**
     * @return pila de errores léxicos
     */
    public Stack<Error> getErrorStack()
    {
        return errorStack;
    }

    /**
     * @return lexema que se acaba de leer
     */
    public String getLexeme()
    {
        return lexeme;
    }

    /**
     * Obtiene la cadena completa de tokens obtenidos en el análisis léxico
     *
     * @return cadena de tokens obtenidos en el análisis léxico
     * @throws IOException no se puede leer el siguiente caracter
     */
    public String getResult() throws IOException
    {
        StringBuilder builder = new StringBuilder();
        while (!isEndOfCode())
        {
            int token = this.scanNextToken();
            if (token != -1)
                builder.append(token).append(" ");
        }
        return builder.toString().trim();
    }
}