# Fase de análisis de un compilador

## Análisis léxico

### Resultado

En un código simple, como el siguiente:

```
{
// Comentario
declare
    int X;
begin
    x = 10;
    fizz = "Fizz";
	print(fizz);
}
```

El resultado es:

![result](assets/lexer/result.png)


### Autómatas

Para la implementación del analizador léxico, se hizo uso de autómatas, 6 en total, estos identifican:

1. **ID**
![id](assets/lexer/id.png)

2. **Números (enteros y flotantes)**
![number](assets/lexer/number.png)

3. **Operadores relacionales (<, >, ==, !=, <=, >=)**
![oprel](assets/lexer/oprel.png)

4. **Palabras reservadas**
![reserved](assets/lexer/reserved.png)

5. **Cadenas**
![string](assets/lexer/string.png)

6. **Símbolos de un carácter**
![unary](assets/lexer/unary.png)

### Diagramas de clases

#### Clase Automaton 

![automaton](assets/lexer/uml/Automaton.png)


#### Clase Control 

![control](assets/lexer/uml/Control.png)


#### Clase Error 

![error](assets/lexer/uml/Error.png)


#### Clase FileReader 

![filereader](assets/lexer/uml/FileReader.png)


#### Clase KeyPair 

![keypair](assets/lexer/uml/KeyPair.png)


#### Clase Lexer 

![lexer](assets/lexer/uml/Lexer.png)


#### Clase PrintErrors 

![printerrors](assets/lexer/uml/PrintErrors.png)


#### Clase Rules 

![rules](assets/lexer/uml/Rules.png)


#### Clase Tag 

![tag](assets/lexer/uml/Tag.png)


#### Clase Token 

![token](assets/lexer/uml/Token.png)


## Análisis sintáctico

## Análisis semántico