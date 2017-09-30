package tcl_analyzer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Parser {

    private static final Set<String> RESERVED_WORDS = new HashSet<String>() {
        {
            add("array");
            add("break");
            add("continue");
            add("default");
            add("else");
            add("elseif");
            add("exists");
            add("expr");
            add("for");
            add("gets");
            add("if");
            add("incr");
            add("proc");
            add("puts");
            add("return");
            add("set");
            add("size");
            add("stdin");
            add("switch");
            add("then");
            add("while");
            add("return");
        }
    };
    private static final Set<String> RESERVED_SYMBOLS = new HashSet<String>() {
        {
            add("{");
            add("}");
            add("$");
            add(";");
            add("[");
            add("(");
            add("]");
            add(")");
            add(">");
            add("<");
            add(">=");
            add("<=");
            add("==");
            add("eq");
            add("ne");
            add("!=");
            add("&&");
            add("||");
            add("!");
            add("+");
            add("-");
            add("*");
            add("/");
            add("%");
            add("**");
        }
    };
    private static final Set<Character> LETTERS = new HashSet<Character>() {
        {
            add('A');
            add('B');
            add('C');
            add('D');
            add('E');
            add('F');
            add('G');
            add('H');
            add('I');
            add('J');
            add('K');
            add('L');
            add('M');
            add('N');
            add('O');
            add('P');
            add('Q');
            add('R');
            add('S');
            add('T');
            add('U');
            add('V');
            add('W');
            add('X');
            add('Y');
            add('Z');
            add('a');
            add('b');
            add('c');
            add('d');
            add('e');
            add('f');
            add('g');
            add('h');
            add('i');
            add('j');
            add('k');
            add('l');
            add('m');
            add('n');
            add('o');
            add('p');
            add('q');
            add('r');
            add('s');
            add('t');
            add('u');
            add('v');
            add('w');
            add('x');
            add('y');
            add('z');
        }
    };
    private static final Set<Character> DIGITS = new HashSet<Character>() {
        {
            add('1');
            add('2');
            add('3');
            add('4');
            add('5');
            add('6');
            add('7');
            add('8');
            add('9');
            add('0');
        }
    };
    
    private static final Set<Character> ALONE_SYMBOL = new HashSet<Character>() {
        {
            add('{');
            add('}');
            add('$');
            add(';');
            add('[');
            add(']');
            add('(');
            add(')');
            add('+');
            add('-');
            add('/');
            add('%');
        }
    };
    private static final Set<Character> CAN_ALONE_SYMBOL = new HashSet<Character>() {
        {
            add('<');
            add('>');
            add('!');
            add('*');
        }
    };
    private static final Set<Character> NEED_U_SYMBOL = new HashSet<Character>() {
        {
            add('=');
            add('|');
            add('&');
        }
    };
    
    static class Token {
        private int linea;
        private int columna;
        private String lexema;

        public Token(int linea, int columna, String lexema) {
            this.linea = linea;
            this.columna = columna;
            this.lexema = lexema;
        }
        int getLinea(){
            return this.linea;
        }
        int getColumna(){
            return this.columna;
        }
        String getLexema(){
            return this.lexema;
        }
    }
    
    private static final int    DESCONOCIDO = -1,
                                PALABRA = 0,
                                ENTERO = 2,
                                DOUBLE = 3,
                                CSYMBOL = 4,
                                CNSYMBOL = 5,
                                ASYMBOL = 6,
                                FIN_STRING = 7,
                                FIN_SYMBOL = 8;

    private static int estadoLexema;
    private static boolean devolver;
    private static ArrayList<Token> tokens;
    
    public static void main(String[] args) throws FileNotFoundException, IOException {
        BufferedReader br;
        if (args.length > 0) {
            br = new BufferedReader(new FileReader(args[0]));
        } else {
            br = new BufferedReader(new InputStreamReader(System.in));
        }
        tokenizer(br);
    }
    
    
    public static void tokenizer(BufferedReader reader) throws IOException {
        String line;
        char caracter;
        String lexema;
        tokens = new ArrayList<>();
        for (int f = 0; (line = reader.readLine()) != null; f++) {
            for (int c = 0; c < line.length(); c++) {
                lexema = "";
                caracter = line.charAt(c);
                reiniciarVariables();

                int row = f+1, col = c+1;
                
                if (caracter == ' ' || caracter == '\t' || caracter == '\n') {  //Ignorar espacio/tab/salto 
                    continue;
                } else if (caracter == '#') {
                    break;                                                      // Comentario, se ignora el resto de la linea            
                }
                int validacion = validarPrimerChar(caracter);
                if(validacion == 2) {
                    while (c+1 < line.length() && line.charAt(++c) != '\"') {
                    }
                    estadoLexema = FIN_STRING;
                } else if (validacion == 0) {
                    while (c < line.length() && clasificar(caracter = line.charAt(c++), lexema)) {
                        lexema += caracter;
                        if (estadoLexema == FIN_SYMBOL) {
                            break;
                        }
                    }
                    if (devolver == true) {
                        c -= 2;
                    } else {
                        c--;
                    }
                } else {
                    lexema += caracter;
                }       
                switch (estadoLexema) {
                    case FIN_STRING:
                        lexema = "valor_string";
                        break;
                    case ENTERO:
                        lexema = "valor_entero";
                        break;
                    case DOUBLE:
                        lexema = "valor_double";
                        break;
                    default:
                        if(!RESERVED_SYMBOLS.contains(lexema) && !RESERVED_WORDS.contains(lexema)){
                            lexema = "identificador";
                        }
                        break;
                }
                Token tkn = new Token(row, col, lexema);
                tokens.add(tkn);
            }
        }
        
    }
    public static boolean clasificar(char next, String lexema) {
        boolean flag = true;
        switch (estadoLexema) {
            case DESCONOCIDO:
                if (isDigit(next)) {
                    estadoLexema = ENTERO;
                } else if (isLetter(next)) {
                    estadoLexema = PALABRA;
                } else if (CAN_ALONE_SYMBOL.contains(next)) {
                    estadoLexema = CSYMBOL;
                } else if (NEED_U_SYMBOL.contains(next)) {
                    estadoLexema = CNSYMBOL;
                } else {
                    flag = false;
                }
                break;
            case PALABRA:
                if (!isLetter(next) && next != '_') {
                    flag = false;
                    devolver = true;
                }
                break;
            case ENTERO:
                if (next == '.') {
                    estadoLexema = DOUBLE;
                } else if (!isDigit(next)) {
                    flag = false;
                    devolver = true;
                }
                break;
            case DOUBLE:
                if (!isDigit(next)) {
                    flag = false;
                    devolver = true;
                }
                break;
            case CSYMBOL:
                switch (lexema.charAt(0)) {
                    case '*':
                        if (next == '*') {
                            estadoLexema = FIN_SYMBOL;
                        } else {
                            flag = false;
                            devolver = true;
                        }
                        break;
                    default:
                        if (next == '=') {
                            estadoLexema = FIN_SYMBOL;
                        } else {
                            flag = false;
                            devolver = true;
                        }
                        break;
                }
                break;
            case CNSYMBOL:
                if (lexema.charAt(0) != next) {
                    flag = false;
                } else {
                    estadoLexema = FIN_SYMBOL;
                }
                break;
        }
        return flag;
    }
    
    private static void reiniciarVariables() {
        devolver = false;
        estadoLexema = DESCONOCIDO;
    }

    public static int validarPrimerChar(char first) {
        if (ALONE_SYMBOL.contains(first)) {
            estadoLexema = ASYMBOL;
            return 1;
        } else if (first == '\"') {                                             //Lectura String
            return 2;
        }
        return 0;
    }    
    
    private static boolean isLetter(char c) {
        return LETTERS.contains(c);
    }
    private static boolean isDigit(char c) {
        return DIGITS.contains(c);
    }
}
