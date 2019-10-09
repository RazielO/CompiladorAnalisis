package main;

import lexer.Token;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * Clase auxiliar para imprimir una tabla en consola
 */
class PrintTable
{
    private Hashtable<String, Token> symbols;

    private List<String> id, type, line, tag;
    private List<List<String>> lists;

    /**
     * Constructor de la clase PrintTable. Convierte los campos de la tabla de símbolos
     * en una lista de listas.
     *
     * @param symbols tabla de símbolos a imprimir
     */
    PrintTable(Hashtable<String, Token> symbols)
    {
        this.symbols = symbols;

        this.id = new ArrayList<>();
        this.type = new ArrayList<>();
        this.line = new ArrayList<>();
        this.tag = new ArrayList<>();

        id.add(0, "ID");
        type.add(0, "TYPE");
        line.add(0, "LINE");
        tag.add(0, "TAG");

        symbols.forEach((key, value) ->
        {
            id.add(key);
            type.add(value.getType());
            line.add(String.valueOf(value.getLine()));
            tag.add(String.valueOf(value.getTag()));
        });

        lists = new ArrayList<>();
        lists.add(id);
        lists.add(type);
        lists.add(line);
        lists.add(tag);
    }

    /**
     * Constructor de la clase PrintTable.
     *
     * @param lists lista de listas a imprimir
     */
    PrintTable(List<List<String>> lists)
    {
        this.lists = lists;
    }

    /**
     * Imprime la lista de listas en forma tabular
     */
    void print()
    {
        int size = 0;
        StringBuilder format = new StringBuilder();
        int i = 1;
        String[] args = new String[lists.size()];

        for (List<String> list : lists)
        {
            format.append("| %").append(i).append("$").append(maxLength(list)).append("s ");
            i++;
            size += maxLength(list);
        }
        format.append("|");
        size += (lists.size() * 2) + (lists.size() - 1);

        for (i = 0; i < lists.get(0).size(); i++)
        {
            if (i == 1)
                System.out.println("|" + new String(new char[size]).replace("\0", "-") + "|");
            for (int j = 0; j < args.length; j++)
                args[j] = lists.get(j).get(i);
            System.out.println(String.format(format.toString(), (Object[]) args));
        }
    }

    /**
     * Obtiene la longitud de la cadena más larga en una lista para formatear la tabla
     *
     * @param list lista de donde se obtendrá la longitud
     * @return longitud de la cadena más larga
     */
    private int maxLength(List<String> list)
    {
        return list.stream().map(String::length).max(Integer::compareTo).get();
    }
}
