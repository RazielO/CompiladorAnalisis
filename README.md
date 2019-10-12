# Fase de análisis de un compilador

1. [Análisis léxico](#análisis-léxico)
    1. [Resultado](#resultado)
    1. [Autómatas](#autómatas)
        1. [ID](#id)
        1. [Números](#números)
        1. [Operadores relacionales](#operadores-relacionales)
        1. [Palabras reservadas](#palabras-reservadas)
        1. [Cadenas](#cadenas)
        1. [Símbolos de un carácter](#símbolos-de-un-carácter)
    1. [Diagramas de clases](#diagramas-de-clases)
        1. [Clase Automaton](#clase-automaton)
        1. [Clase Control](#clase-control)
        1. [Clase Error](#clase-error)
        1. [Clase FileReader](#clase-filereader)
        1. [Clase KeyPair](#clase-keypair)
        1. [Clase Lexer](#clase-lexer)
        1. [Clase PrintErrors](#clase-printerrors)
        1. [Clase Rules](#clase-rules)
        1. [Clase Tag](#clase-tag)
        1. [Clase Token](#clase-token)
1. [Análisis sintáctico](#análisis-sintáctico)
1. [Análisis semántico](#análisis-semántico)

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

#### ID

![id](astOO/lexer/id.png)

#### Números

![number](assets/lexer/number.png)

#### Operadores relacionales

![oprel](assets/lexer/oprel.png)

#### Palabras reservadas

![reserved](assets/lexer/reserved.png)

#### Cadenas

![string](assets/lexer/string.png)

#### Símbolos de un carácter

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
