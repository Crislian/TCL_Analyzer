package tcl_analyzer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TCL_Analyzer {

    private static final int DESCONOCIDO = -1,
            PALABRA = 0,
            ID = 1,
            ENTERO = 2,
            MAYBE_DOUBLE = 9,
            DOUBLE = 3,
            CSYMBOL = 4,
            CNSYMBOL = 5,
            ASYMBOL = 6,
            FIN_STRING = 7,
            FIN_SYMBOL = 8;

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

    private static final Set<Character> LETTERS = new HashSet<Character>() {
        {
            add('%');
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
    private static final Set<Character> ALPHABET = new HashSet<Character>() {
        {
            add('#');
            add('{');
            add('}');
            add('$');
            add(';');
            add('.');
            add('[');
            add(']');
            add('(');
            add(')');
            add('>');
            add('<');
            add('!');
            add('=');
            add('&');
            add('|');
            add('+');
            add('-');
            add('*');
            add('/');
            add('"');
            add('_');
            add('%');
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
        }
    };
    private static final Map<String, String> RESERVED_SYMBOLS = new HashMap<String, String>() {
        {
            put("{", "token_llave_izq");
            put("}", "token_llave_der");
            put("$", "token_dollar");
            put(";", "token_pyc");
            put("[", "token_cor_izq");
            put("]", "token_cor_der");
            put("(", "token_par_izq");
            put(")", "token_par_der");
            put(">", "token_mayor");
            put("<", "token_menor");
            put(">=", "token_mayor_igual");
            put("<=", "token_menor_igual");
            put("eq", "token_igual_str");
            put("==", "token_igual_num");
            put("ne", "token_diff_str");
            put("!=", "token_diff_num");
            put("&&", "token_and");
            put("||", "token_or");
            put("!", "token_not");
            put("+", "token_mas");
            put("-", "token_menos");
            put("*", "token_mul");
            put("/", "token_div");
            put("%", "token_mod");
            put("**", "token_pot");
        }
    };

    private static int estadoLexema = DESCONOCIDO;
    private static String lexema;
    private static boolean error, devolver;

    public static void main(String[] args) throws FileNotFoundException, IOException {
        BufferedReader br;
        if (args.length > 0) {
            br = new BufferedReader(new FileReader(args[0]));
        } else {
            br = new BufferedReader(new InputStreamReader(System.in));
        }
        ejecutar(br);
    }

    public static void ejecutar(BufferedReader reader) throws IOException {
        String line;
        char caracter;
        error = false;
        for (int f = 0; (line = reader.readLine()) != null; f++) {
            for (int c = 0; c < line.length() && !error; c++) {
                int cnt = c;
                caracter = line.charAt(c);
                StringBuilder output = new StringBuilder("<");
                reiniciarVariables();
                if (caracter == ' ' || caracter == '\t' || caracter == '\n') {  //Ignorar espacio/tab/salto 
                    continue;
                } else if (caracter == '#') {
                    break;                                                      // Comentario, se ignora el resto de la linea            
                }
                int validacion = validarPrimerChar(caracter);
                if (validacion == 2) {
                    while (line != null && (caracter = line.charAt(++c)) != '\"') {
                        lexema += caracter;
                        if (c + 1 == line.length()) {
                            line = reader.readLine();
                            f++;
                            c = -1;
                        }
                    }
                    if (line == null) {
                        error = true;
                    } else {
                        estadoLexema = FIN_STRING;
                    }
                } else if (validacion == 0) {
                    while (c < line.length() && clasificar(caracter = line.charAt(c++))) {
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
                }
                if (estadoLexema == MAYBE_DOUBLE) {
                    output.append("token_integer,").append(lexema.substring(0, lexema.length() - 1));
                    if (!error) {
                        System.out.println(output.toString() + "," + (f + 1) + "," + (cnt + 1) + ">");
                    }
                    printError(f, c);
                    break;
                }
                if (error == true) {
                    printError(f, cnt);
                    break;
                }
                switch (estadoLexema) {
                    case CNSYMBOL:
                        error = true;
                        printError(f, cnt);
                        break;
                    case CSYMBOL:
                    case FIN_SYMBOL:
                    case ASYMBOL:
                        output.append(RESERVED_SYMBOLS.get(lexema));
                        break;
                    case ENTERO:
                        output.append("token_integer,").append(lexema);
                        break;
                    case DOUBLE:
                        output.append("token_double,").append(lexema);
                        break;
                    case FIN_STRING:
                        output.append("token_string,").append(lexema);
                        break;
                    case PALABRA:
                        if (RESERVED_WORDS.contains(lexema)) {
                            output.append(lexema);
                        } else if (RESERVED_SYMBOLS.containsKey(lexema)) {
                            output.append(RESERVED_SYMBOLS.get(lexema));
                        } else {
                            output.append("id,").append(lexema);
                        }
                        break;
                    case ID:
                        output.append("id,").append(lexema);
                        break;
                }
                output.append(",").append(f + 1).append(",").append(cnt + 1).append(">");
                if (!error) {
                    System.out.println(output.toString());
                }
            }
        }
    }

    public static boolean clasificar(char next) {
        boolean flag = true;
        switch (estadoLexema) {
            case DESCONOCIDO:
                if (isDigit(next)) {
                    estadoLexema = ENTERO;
                } else if (isLetter(next)) {
                    estadoLexema = PALABRA;
                } else if (next == '_') {
                    estadoLexema = ID;
                } else if (CAN_ALONE_SYMBOL.contains(next)) {
                    estadoLexema = CSYMBOL;
                } else if (NEED_U_SYMBOL.contains(next)) {
                    estadoLexema = CNSYMBOL;
                } else {
                    flag = false;
                }
                break;
            case PALABRA:
                if (isDigit(next)) {
                    estadoLexema = ID;
                } else if (!isLetter(next) && next != '_') {
                    flag = false;
                    devolver = true;
                }
                break;
            case ID:
                if (!isDigit(next) && !isLetter(next) && next != '_') {
                    flag = false;
                    devolver = true;
                }
                break;
            case ENTERO:
                if (next == '.') {
                    estadoLexema = MAYBE_DOUBLE;
                } else if (!isDigit(next)) {
                    flag = false;
                    devolver = true;
                }
                break;
            case MAYBE_DOUBLE:
                if (isDigit(next)) {
                    estadoLexema = DOUBLE;
                } else {
                    error = true;
                }
                break;
            case DOUBLE:
                if (!isDigit(next)) {
                    if (lexema.charAt(lexema.length() - 1) == '.') {
                        error = true;
                    }
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
                    error = true;
                    flag = false;
                } else {
                    estadoLexema = FIN_SYMBOL;
                }

                break;
        }
        return flag;
    }

    private static void printError(int f, int c) {
        System.out.println(">>> Error lexico (linea: " + (f + 1) + ", posicion: " + (c + 1) + ")");
    }

    private static void reiniciarVariables() {
        lexema = "";
        error = false;
        devolver = false;
        estadoLexema = DESCONOCIDO;
    }

    public static int validarPrimerChar(char first) {
        if (first == '.' || !ALPHABET.contains(first)) {
            error = true;                                                       //Error porque no existe en el alfabeto
            return 1;
        } else if (ALONE_SYMBOL.contains(first)) {
            lexema += first;
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
