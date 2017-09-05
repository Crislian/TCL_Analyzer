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

    private static boolean ERROR, devolver;
    private static final int DESCONOCIDO = -1,
                             PALABRA = 0,
                             ID = 1,
                             ENTERO = 2,
                             DOUBLE = 3,
                             CSYMBOL = 4,
                             CNSYMBOL = 5,
                             ASYMBOL = 6,
                             FIN_STRING = 7,
                             FIN_SYMBOL = 8;
    
    private static final Set<Character> ALONE_SYMBOL = new HashSet<Character>() {{
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
    }};
    private static final Set<Character> CAN_ALONE_SYMBOL = new HashSet<Character>() {{
        add('<');
        add('>');
        add('!');
        add('*');
    }};    
    private static final Set<Character> NEED_U_SYMBOL = new HashSet<Character>() {{
        add('=');
        add('|');
        add('&');
    }};
    
    private static final Set<Character> LETTERS = new HashSet<Character>() {{
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
    }};
    private static final Set<Character> DIGITS = new HashSet<Character>() {{
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
    }};
    private static final Set<Character> ALPHABET = new HashSet<Character>() {{
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
    }};
    
    private static final Set<String> RESERVED_WORDS = new HashSet<String>() {{
        add("Tcl");
        add("after");
        add("append");
        add("apply");
        add("argc");
        add("argv");
        add("argv0");
        add("array");
        add("auto_execok");
        add("auto_import");
        add("auto_load");
        add("auto_load_index");
        add("auto_mkindex");
        add("auto_path");
        add("auto_qualify");
        add("auto_reset");
        add("bgerror");
        add("binary");
        add("break");
        add("catch");
        add("cd");
        add("chan");
        add("clock");
        add("close");
        add("concat");
        add("continue");
        add("coroutine");
        add("dde");
        add("default");
        add("dict");
        add("else");
        add("elseif");
        add("encoding");
        add("env");
        add("eof");
        add("error");
        add("errorCode");
        add("errorInfo");
        add("eval");
        add("exec");
        add("exit");
        add("expr");
        add("fblocked");
        add("fconfigure");
        add("fcopy");
        add("file");
        add("fileevent");
        add("filename");
        add("flush");
        add("for");
        add("foreach");
        add("format");
        add("gets");
        add("glob");
        add("global");
        add("history");
        add("http");
        add("if");
        add("incr");
        add("info");
        add("interp");
        add("join");
        add("lappend");
        add("lassign");
        add("lindex");
        add("linsert");
        add("list");
        add("llength");
        add("lmap");
        add("load");
        add("lrange");
        add("lrepeat");
        add("lreplace");
        add("lreverse");
        add("lsearch");
        add("lset");
        add("lsort");
        add("mathfunc");
        add("mathop");
        add("memory");
        add("msgcat");
        add("my");
        add("namespace");
        add("next");
        add("nextto");
        add("oo::class");
        add("oo::copy");
        add("oo::define");
        add("oo::objdefine");
        add("oo::object");
        add("open");
        add("package");
        add("parray");
        add("pid");
        add("pkg::create");
        add("pkg_mk");
        add("pkg_mkIndex");
        add("platform");
        add("platform::shell");
        add("proc");
        add("puts");
        add("pwd");
        add("re_syntax");
        add("read");
        add("refchan");
        add("regexp");
        add("registry");
        add("regsub");
        add("rename");
        add("resource");
        add("return");
        add("safe");
        add("scan");
        add("seek");
        add("self");
        add("set");
        add("socket");
        add("source");
        add("split");
        add("string");
        add("subst");
        add("subst");
        add("switch");
        add("tailcall");
        add("tcl::prefix");
        add("tclLog  ");
        add("tcl_endOfWord");
        add("tcl_findLibrary");
        add("tcl_interactive");
        add("tcl_library");
        add("tcl_nonwordchars");
        add("tcl_patchLevel");
        add("tcl_pkgPath");
        add("tcl_platform");
        add("tcl_precision");
        add("tcl_rcFileName");
        add("tcl_startOfNextWord");
        add("tcl_startOfPreviousWord");
        add("tcl_traceCompile");
        add("tcl_traceExec");
        add("tcl_version");
        add("tcl_wordBreakAfter");
        add("tcl_wordBreakBefore");
        add("tcl_wordchars");
        add("tcltest");
        add("tell");
        add("then");
        add("throw");
        add("time");
        add("tm");
        add("trace");
        add("transchan");
        add("try");
        add("unknown");
        add("unload");
        add("unset");
        add("update");
        add("uplevel");
        add("upvar");
        add("vwait");
        add("while");
        add("yield");
        add("yieldto");
        add("zlib");
    }};
    private static final Map<String, String> RESERVED_SYMBOLS = new HashMap<String, String>() {{
        put("{" , "token_llave_izq");
        put("}" , "token_llave_der");
        put("$" , "token_dollar");
        put(";" , "token_pyc");
        put("[" , "token_cor_izq");
        put("]" , "token_cor_der");
        put("(" , "token_par_izq");
        put(")" , "token_par_der");
        put(">" , "token_mayor");
        put("<" , "token_menor");
        put(">=" , "token_mayor_igual");
        put("<=" , "token_menor_igual");
        put("eq" , "token_igual_str");
        put("==" , "token_igual_num");
        put("ne" , "token_diff_str");
        put("!=" , "token_diff_num");
        put("&&" , "token_and");
        put("||" , "token_or");
        put("!" , "token_not");
        put("+" , "token_mas");
        put("-" , "token_menos");
        put("*" , "token_mul");
        put("/" , "token_div");
        put("%" , "token_mod");
        put("**" , "token_pot");
    }};

    private static int estadoLexema = DESCONOCIDO;
    private static String lexema;
    
    public static void main(String[] args) throws FileNotFoundException, IOException {
        BufferedReader br;
        try  {
            br = new BufferedReader(new FileReader("resources/IO/in0.txt"));
        } catch (FileNotFoundException e) {
            br = new BufferedReader(new InputStreamReader(System.in));
        }
        String line;
        char caracter;
        for(int f = 0; (line=br.readLine())!=null; f++){
            for (int c = 0; c < line.length() && !ERROR; c++){
                int cnt = c;
                StringBuilder output = new StringBuilder("<");
                lexema = "";
                ERROR = false;
                devolver = false;
                estadoLexema = DESCONOCIDO;
                caracter = line.charAt(c);
                if (caracter == ' ' || caracter == '\t' || caracter == '\n')    //Ignorar espacio/tab/salto 
                    continue;
                else if(caracter == '#') break;    // Comentario, se ignora el resto de la linea            
                else if(caracter == '.' || !ALPHABET.contains(caracter)) ERROR = true; //Error porque no existe en el alfabeto
                else if(ALONE_SYMBOL.contains(caracter)) {
                    lexema += caracter;
                    estadoLexema = ASYMBOL;
                } else if (caracter == '\"'){                                          //Lectura String
                    while(line != null && (caracter = line.charAt(++c)) != '\"'){
                        lexema += caracter;
                        if(c+1 == line.length()){
                            line = br.readLine();
                            f++;
                            c = -1;
                        }
                    }
                    if (line == null) ERROR = true;
                    else estadoLexema = FIN_STRING;
                } else {
                    while (c < line.length() && clasificar(caracter=line.charAt(c++))){                        
                        lexema += caracter;
                        if(estadoLexema == FIN_SYMBOL) break;
                    }
                    if(devolver == true) c -= 2;
                    else c--;
                }
                
                if (ERROR == true){
                    printError(f, cnt);
                    break;
                }
                switch (estadoLexema) {
                    case CNSYMBOL:
                        if(NEED_U_SYMBOL.contains(lexema)){
                            output.append(RESERVED_SYMBOLS.get(lexema));
                        } else {
                            ERROR = true;
                            printError(f, cnt);
                        }
                        break;
                    case CSYMBOL:
                    case FIN_SYMBOL:
                    case ASYMBOL:
                        output.append(RESERVED_SYMBOLS.get(lexema));
                        break;
                    case ENTERO:
                        output.append("token_integer,");
                        output.append(lexema);
                        break;
                    case DOUBLE:
                        output.append("token_double,");
                        output.append(lexema);
                        break;
                    case FIN_STRING:
                        output.append("token_string,");
                        output.append(lexema);
                        break;
                    case PALABRA:
                        if(RESERVED_WORDS.contains(lexema)) output.append(lexema);
                        else if(RESERVED_SYMBOLS.containsKey(lexema)) output.append(RESERVED_SYMBOLS.get(lexema));
                        else{
                            output.append("id,");
                            output.append(lexema);
                        }
                        break;
                    case ID:
                        output.append("id,");
                        output.append(lexema);
                        break;                    
                }
                output.append(",");
                output.append(f+1);
                output.append(",");
                output.append(cnt+1);
                output.append(">");
                if(!ERROR) System.out.println(output.toString());
            }
        }
    }

    public static boolean clasificar(char next){
        boolean flag = true;
        switch(estadoLexema){
            case DESCONOCIDO:
                if(isDigit(next))             estadoLexema = ENTERO;
                else if(isLetter(next))                 estadoLexema = PALABRA;
                else if(next == '_')                    estadoLexema = ID;
                else if(CAN_ALONE_SYMBOL.contains(next))estadoLexema = CSYMBOL;
                else if(NEED_U_SYMBOL.contains(next))   estadoLexema = CNSYMBOL;
                else flag = false; 
                break;
            case PALABRA:
                if(isDigit(next))             estadoLexema = ID;
                else if(!isLetter(next) && next != '_')  {
                    flag = false;
                    devolver = true;
                }
                break;
            case ID:
                if(!isDigit(next) && !isLetter(next) && next != '_'){
                    flag = false;
                    devolver = true;
                }
                break;
            case ENTERO: // TOCA MEJORAR CUANDO PASA A DOUBLE PORQUE SI ENTRA LA CADENA 10. DEBE ACEPTAR EL LEXEMA '10' Y ERROR EN .
                if(next == '.')                         estadoLexema = DOUBLE;
                else if(!isDigit(next)){
                    flag = false;
                    devolver = true;
                }
                break;
            case DOUBLE:
                if(!isDigit(next)){
                    if(lexema.charAt(lexema.length()-1) == '.')
                        ERROR = true;
                    flag = false;
                    devolver = true;
                }
                break;
            case CSYMBOL:
                switch(lexema.charAt(0)) {
                    case '*':
                        if(next == '*') estadoLexema = FIN_SYMBOL; 
                        else {
                            flag = false;
                            devolver = true;
                        }
                        break;
                    default:
                        if(next == '=') estadoLexema = FIN_SYMBOL;
                        else {
                            flag = false;
                            devolver = true;
                        }
                        break;
                }
                break;
            case CNSYMBOL:
                switch(lexema.charAt(0)) {
                    case '|':
                        if(next != '|'){
                            ERROR = true;
                            flag = false;
                        } else estadoLexema = FIN_SYMBOL;
                        break;
                    case '&':
                        if(next != '&'){
                            ERROR = true;
                            flag = false;
                        } else estadoLexema = FIN_SYMBOL;
                        break;
                    case '=':
                        if(next != '='){
                            ERROR = true;
                            flag = false;
                        } else estadoLexema = FIN_SYMBOL;
                        break;
                }
                break;
        }
        return flag;
    }
    
    private static void printError(int f, int c){
        System.out.println(">>> Error lexico (linea: " + (f+1) +", posicion:" + (c+1) + ")");
    }
    
    private static boolean isLetter(char c){
        return LETTERS.contains(c);
    }
    private static boolean isDigit(char c){
        return DIGITS.contains(c);
    }
}


