# Fase de análisis de un compilador

## Análisis léxico

### Autómatas

Para la implementación del analizador léxico, se hizo uso de autómatas, 6 en total, estos identifican:

1. **ID**

![id](assets/id.png)

2. **Números (enteros y flotantes)**

![number](assets/number.png)

3. **Operadores relacionales (<, >, ==, !=, <=, >=)**

![oprel](assets/oprel.png)

4. **Palabras reservadas**

![reserved](assets/reserved.png)

5. **Cadenas**

![string](assets/string.png)

6. **Símbolos de un carácter**

![unary](assets/unary.png)

### Diagramas de clases

#### Clase Automaton 

![automaton](assets/uml/Automaton.png)


#### Clase Control 

![control](assets/uml/Control.png)


#### Clase Error 

![error](assets/uml/Error.png)


#### Clase FileReader 

![filereader](assets/uml/FileReader.png)


#### Clase KeyPair 

![keypair](assets/uml/KeyPair.png)


#### Clase Lexer 

![lexer](assets/uml/Lexer.png)


#### Clase PrintErrors 

![printerrors](assets/uml/PrintErrors.png)


#### Clase Rules 

![rules](assets/uml/Rules.png)


#### Clase Tag 

![tag](assets/uml/Tag.png)


#### Clase Token 

![token](assets/uml/Token.png)


## Análisis sintáctico

## Análisis semántico
