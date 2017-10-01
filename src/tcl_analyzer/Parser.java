package tcl_analyzer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;

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
            add("case");
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

    final static Map<String, List<List<String>>> GRAMMAR = new HashMap<String, List<List<String>>>() {{put("ELSEIF", new ArrayList<List<String>>(){{add(new LinkedList<String>(){{add("INICIO_ELSEIF");add("CUERPO_INST");add("}");add("ELSEIF");}});add(new LinkedList<String>(){{add("ELSE");}});}});put("CASE_LOOP", new ArrayList<List<String>>(){{add(new LinkedList<String>(){{add("INICIO_CASE");add("CUERPO_LOOP");add("}");add("CASE2_LOOP");}});}});put("CUERPO_LOOP_FUNC", new ArrayList<List<String>>(){{add(new LinkedList<String>(){{add("BREAK");add("CUERPO_LOOP_FUNC");}});add(new LinkedList<String>(){{add("CONTINUE");add("CUERPO_LOOP_FUNC");}});add(new LinkedList<String>(){{add("DECLARACION");add("CUERPO_LOOP_FUNC");}});add(new LinkedList<String>(){{add("GETS");add(";");add("CUERPO_LOOP_FUNC");}});add(new LinkedList<String>(){{add("PUTS");add("CUERPO_LOOP_FUNC");}});add(new LinkedList<String>(){{add("RETURN_LOOP");add("CUERPO_LOOP_FUNC");}});add(new LinkedList<String>(){{add("IF_LOOP_FUNC");add("CUERPO_LOOP_FUNC");}});add(new LinkedList<String>(){{add("SWITCH_LOOP_FUNC");add("CUERPO_LOOP_FUNC");}});add(new LinkedList<String>(){{add("FOR_FUNCION");add("CUERPO_LOOP_FUNC");}});add(new LinkedList<String>(){{add("WHILE_FUNCION");add("CUERPO_LOOP_FUNC");}});add(new LinkedList<String>(){{add("AGRUP_INST");add(";");add("CUERPO_LOOP_FUNC");}});add(new LinkedList<String>(){{add("ε");}});}});put("INICIO_ELSE", new ArrayList<List<String>>(){{add(new LinkedList<String>(){{add("else");add("{");}});}});put("EXPRESION", new ArrayList<List<String>>(){{add(new LinkedList<String>(){{add("TERM");add("AUX_EXPR");}});add(new LinkedList<String>(){{add("OP_UNARIO");add("EXPRESION");}});add(new LinkedList<String>(){{add("(");add("EXPRESION");add(")");add("AUX_EXPR");}});}});put("CUERPO_LOOP", new ArrayList<List<String>>(){{add(new LinkedList<String>(){{add("DECLARACION");add("CUERPO_LOOP");}});add(new LinkedList<String>(){{add("FOR");add("CUERPO_LOOP");}});add(new LinkedList<String>(){{add("GETS");add(";");add("CUERPO_LOOP");}});add(new LinkedList<String>(){{add("IF_LOOP");add("CUERPO_LOOP");}});add(new LinkedList<String>(){{add("PUTS");add("CUERPO_LOOP");}});add(new LinkedList<String>(){{add("WHILE");add("CUERPO_LOOP");}});add(new LinkedList<String>(){{add("SWITCH_LOOP");add("CUERPO_LOOP");}});add(new LinkedList<String>(){{add("BREAK");add("CUERPO_LOOP");}});add(new LinkedList<String>(){{add("CONTINUE");add("CUERPO_LOOP");}});add(new LinkedList<String>(){{add("AGRUP_INST");add(";");add("CUERPO_LOOP");}});add(new LinkedList<String>(){{add("ε");}});}});put("SWITCH_FUNCION", new ArrayList<List<String>>(){{add(new LinkedList<String>(){{add("INICIO_SWITCH");add("CASE_FUNCION");add("}");}});}});put("INICIO_CASE", new ArrayList<List<String>>(){{add(new LinkedList<String>(){{add("case");add("valor_entero");add("{");}});}});put("AUX_EXPR", new ArrayList<List<String>>(){{add(new LinkedList<String>(){{add("OP_BINARIO");add("EXPRESION");}});add(new LinkedList<String>(){{add("ε");}});}});put("FOR_FUNCION", new ArrayList<List<String>>(){{add(new LinkedList<String>(){{add("INICIO_FOR");add("CUERPO_LOOP_FUNC");add("}");}});}});put("INICIO_FOR", new ArrayList<List<String>>(){{add(new LinkedList<String>(){{add("for");add("{");add("set");add("identificador");add("ASIG_FOR");add("}");add("{");add("EXPRESION");add("}");add("{");add("incr");add("identificador");add("INCREMENTO");add("}");add("{");}});}});put("BREAK", new ArrayList<List<String>>(){{add(new LinkedList<String>(){{add("break");add(";");}});}});put("WHILE_FUNCION", new ArrayList<List<String>>(){{add(new LinkedList<String>(){{add("INICIO_WHILE");add("CUERPO_LOOP_FUNC");add("}");}});}});put("CASE2_LOOP", new ArrayList<List<String>>(){{add(new LinkedList<String>(){{add("INICIO_CASE");add("CUERPO_LOOP");add("}");add("CASE2_LOOP");}});add(new LinkedList<String>(){{add("DEFAULT_LOOP");}});}});put("VAL_INDICE", new ArrayList<List<String>>(){{add(new LinkedList<String>(){{add("VALOR");}});add(new LinkedList<String>(){{add("AGRUP");}});}});put("ELSE", new ArrayList<List<String>>(){{add(new LinkedList<String>(){{add("INICIO_ELSE");add("CUERPO_INST");add("}");}});add(new LinkedList<String>(){{add("ε");}});}});put("INICIO", new ArrayList<List<String>>(){{add(new LinkedList<String>(){{add("DECLARACION_FUNCION");add("MODULO_PPAL");}});}});put("ELSE_FUNCION", new ArrayList<List<String>>(){{add(new LinkedList<String>(){{add("INICIO_ELSE");add("CUERPO_FUNCION");add("}");}});add(new LinkedList<String>(){{add("ε");}});}});put("ELSE_LOOP_FUNC", new ArrayList<List<String>>(){{add(new LinkedList<String>(){{add("INICIO_ELSE");add("CUERPO_LOOP_FUNC");add("}");}});add(new LinkedList<String>(){{add("ε");}});}});put("IF", new ArrayList<List<String>>(){{add(new LinkedList<String>(){{add("INICIO_IF");add("CUERPO_INST");add("}");add("ELSEIF");}});}});put("DEFAULT_LOOP", new ArrayList<List<String>>(){{add(new LinkedList<String>(){{add("INICIO_DEFAULT");add("CUERPO_LOOP");add("}");}});add(new LinkedList<String>(){{add("ε");}});}});put("IF_FUNCION", new ArrayList<List<String>>(){{add(new LinkedList<String>(){{add("INICIO_IF");add("CUERPO_FUNCION");add("}");add("ELSEIF_FUNCION");}});}});put("R2", new ArrayList<List<String>>(){{add(new LinkedList<String>(){{add("ASIGNACION");}});add(new LinkedList<String>(){{add("ε");}});}});put("INDICE_TERM", new ArrayList<List<String>>(){{add(new LinkedList<String>(){{add("(");add("VALOR_INDICE");add(")");}});add(new LinkedList<String>(){{add("ε");}});}});put("INICIO_SWITCH", new ArrayList<List<String>>(){{add(new LinkedList<String>(){{add("switch");add("$");add("identificador");add("INDICE_SWITCH");add("{");}});}});put("ARGS_FUNCION", new ArrayList<List<String>>(){{add(new LinkedList<String>(){{add("{");add("identificador");add("}");add("ARGS_FUNCION");}});add(new LinkedList<String>(){{add("ε");}});}});put("FOR", new ArrayList<List<String>>(){{add(new LinkedList<String>(){{add("INICIO_FOR");add("CUERPO_LOOP");add("}");}});}});put("AUX_AGRUP_INST", new ArrayList<List<String>>(){{add(new LinkedList<String>(){{add("EXPR");add("]");}});add(new LinkedList<String>(){{add("identificador");add("PARAM_FUNC");add("]");}});add(new LinkedList<String>(){{add("GETS");add("]");}});add(new LinkedList<String>(){{add("array");add("AUX_ARRAY");}});}});put("DEFAULT_FUNCION", new ArrayList<List<String>>(){{add(new LinkedList<String>(){{add("INICIO_DEFAULT");add("CUERPO_FUNCION");add("}");}});add(new LinkedList<String>(){{add("ε");}});}});put("OP_UNARIO", new ArrayList<List<String>>(){{add(new LinkedList<String>(){{add("-");}});add(new LinkedList<String>(){{add("!");}});}});put("CASE", new ArrayList<List<String>>(){{add(new LinkedList<String>(){{add("INICIO_CASE");add("CUERPO_INST");add("}");add("CASE2");}});}});put("RETURN_LOOP", new ArrayList<List<String>>(){{add(new LinkedList<String>(){{add("return");add("R2");add(";");}});}});put("INDICE", new ArrayList<List<String>>(){{add(new LinkedList<String>(){{add("(");add("VAL_INDICE");add(")");}});add(new LinkedList<String>(){{add("ε");}});}});put("SWITCH_LOOP", new ArrayList<List<String>>(){{add(new LinkedList<String>(){{add("INICIO_SWITCH");add("CASE_LOOP");add("}");}});}});put("AGRUP", new ArrayList<List<String>>(){{add(new LinkedList<String>(){{add("[");add("AUX_AGRUP");}});}});put("AUX_AGRUP", new ArrayList<List<String>>(){{add(new LinkedList<String>(){{add("EXPR");add("]");}});add(new LinkedList<String>(){{add("identificador");add("PARAM_FUNC");add("]");}});add(new LinkedList<String>(){{add("GETS");add("]");}});add(new LinkedList<String>(){{add("array");add("AUX_ARRAY");}});}});put("CASE_LOOP_FUNC", new ArrayList<List<String>>(){{add(new LinkedList<String>(){{add("INICIO_CASE");add("CUERPO_LOOP_FUNC");add("}");add("CASE2_LOOP_FUNC");}});}});put("INDICE_ASIG", new ArrayList<List<String>>(){{add(new LinkedList<String>(){{add("(");add("VAL_INDICE");add(")");}});add(new LinkedList<String>(){{add("ε");}});}});put("MODULO_PPAL", new ArrayList<List<String>>(){{add(new LinkedList<String>(){{add("DECLARACION");add("MODULO_PPAL");}});add(new LinkedList<String>(){{add("FOR");add("MODULO_PPAL");}});add(new LinkedList<String>(){{add("GETS");add(";");add("MODULO_PPAL");}});add(new LinkedList<String>(){{add("IF");add("MODULO_PPAL");}});add(new LinkedList<String>(){{add("PUTS");add("MODULO_PPAL");}});add(new LinkedList<String>(){{add("WHILE");add("MODULO_PPAL");}});add(new LinkedList<String>(){{add("SWITCH");add("MODULO_PPAL");}});add(new LinkedList<String>(){{add("AGRUP_INST");add(";");add("MODULO_PPAL");}});add(new LinkedList<String>(){{add("ε");}});}});put("INDICE_FOR", new ArrayList<List<String>>(){{add(new LinkedList<String>(){{add("(");add("VAL_INDICE");add(")");}});add(new LinkedList<String>(){{add("ε");}});}});put("SWITCH", new ArrayList<List<String>>(){{add(new LinkedList<String>(){{add("INICIO_SWITCH");add("CASE");add("}");}});}});put("VALOR_PARAM", new ArrayList<List<String>>(){{add(new LinkedList<String>(){{add("VALOR");}});add(new LinkedList<String>(){{add("$");add("identificador");add("INDICE_PARAM");}});add(new LinkedList<String>(){{add("AGRUP");}});}});put("CASE2_LOOP_FUNC", new ArrayList<List<String>>(){{add(new LinkedList<String>(){{add("INICIO_CASE");add("CUERPO_LOOP_FUNC");add("}");add("CASE2_LOOP_FUNC");}});add(new LinkedList<String>(){{add("DEFAULT_LOOP_FUNC");}});}});put("AUX_ARRAY", new ArrayList<List<String>>(){{add(new LinkedList<String>(){{add("size");add("identificador");add("]");}});add(new LinkedList<String>(){{add("exists");add("identificador");add("]");}});}});put("INDICE_PARAM", new ArrayList<List<String>>(){{add(new LinkedList<String>(){{add("(");add("VAL_INDICE");add(")");}});add(new LinkedList<String>(){{add("ε");}});}});put("INDICE_SWITCH", new ArrayList<List<String>>(){{add(new LinkedList<String>(){{add("(");add("VAL_INDICE");add(")");}});add(new LinkedList<String>(){{add("ε");}});}});put("INICIO_ELSEIF", new ArrayList<List<String>>(){{add(new LinkedList<String>(){{add("elseif");add("{");add("EXPRESION");add("}");add("then");add("{");}});}});put("ASIG_FOR", new ArrayList<List<String>>(){{add(new LinkedList<String>(){{add("valor_entero");}});add(new LinkedList<String>(){{add("$");add("identificador");add("INDICE_FOR");}});add(new LinkedList<String>(){{add("EXPR");}});}});put("CASE2_FUNCION", new ArrayList<List<String>>(){{add(new LinkedList<String>(){{add("INICIO_CASE");add("CUERPO_FUNCION");add("}");add("CASE2_FUNCION");}});add(new LinkedList<String>(){{add("DEFAULT_FUNCION");}});}});put("TERM", new ArrayList<List<String>>(){{add(new LinkedList<String>(){{add("$");add("identificador");add("INDICE_TERM");}});add(new LinkedList<String>(){{add("AGRUP");}});add(new LinkedList<String>(){{add("VALOR");}});}});put("CASE2", new ArrayList<List<String>>(){{add(new LinkedList<String>(){{add("INICIO_CASE");add("CUERPO_INST");add("}");add("CASE2");}});add(new LinkedList<String>(){{add("DEFAULT");}});}});put("INICIO_WHILE", new ArrayList<List<String>>(){{add(new LinkedList<String>(){{add("while");add("{");add("EXPRESION");add("}");add("{");}});}});put("ELSEIF_LOOP_FUNC", new ArrayList<List<String>>(){{add(new LinkedList<String>(){{add("INICIO_ELSEIF");add("CUERPO_LOOP_FUNC");add("}");add("ELSEIF_LOOP_FUNC");}});add(new LinkedList<String>(){{add("ELSE_LOOP_FUNC");}});}});put("RETURN", new ArrayList<List<String>>(){{add(new LinkedList<String>(){{add("return");add("R2");add(";");}});}});put("OP_BINARIO", new ArrayList<List<String>>(){{add(new LinkedList<String>(){{add(">");}});add(new LinkedList<String>(){{add("<");}});add(new LinkedList<String>(){{add(">=");}});add(new LinkedList<String>(){{add("<=");}});add(new LinkedList<String>(){{add("eq");}});add(new LinkedList<String>(){{add("==");}});add(new LinkedList<String>(){{add("ne");}});add(new LinkedList<String>(){{add("!=");}});add(new LinkedList<String>(){{add("&&");}});add(new LinkedList<String>(){{add("||");}});add(new LinkedList<String>(){{add("+");}});add(new LinkedList<String>(){{add("-");}});add(new LinkedList<String>(){{add("*");}});add(new LinkedList<String>(){{add("/");}});add(new LinkedList<String>(){{add("%");}});add(new LinkedList<String>(){{add("**");}});}});put("EXPR", new ArrayList<List<String>>(){{add(new LinkedList<String>(){{add("expr");add("{");add("EXPRESION");add("}");}});}});put("SWITCH_LOOP_FUNC", new ArrayList<List<String>>(){{add(new LinkedList<String>(){{add("INICIO_SWITCH");add("CASE_LOOP_FUNC");add("}");}});}});put("CASE_FUNCION", new ArrayList<List<String>>(){{add(new LinkedList<String>(){{add("INICIO_CASE");add("CUERPO_FUNCION");add("}");add("CASE2_FUNCION");}});}});put("PUTS", new ArrayList<List<String>>(){{add(new LinkedList<String>(){{add("puts");add("ASIGNACION");add(";");}});}});put("DEFAULT_LOOP_FUNC", new ArrayList<List<String>>(){{add(new LinkedList<String>(){{add("INICIO_DEFAULT");add("CUERPO_LOOP_FUNC");add("}");}});add(new LinkedList<String>(){{add("ε");}});}});put("ELSEIF_LOOP", new ArrayList<List<String>>(){{add(new LinkedList<String>(){{add("INICIO_ELSEIF");add("CUERPO_LOOP");add("}");add("ELSEIF_LOOP");}});add(new LinkedList<String>(){{add("ELSE_LOOP");}});}});put("ELSE_LOOP", new ArrayList<List<String>>(){{add(new LinkedList<String>(){{add("INICIO_ELSE");add("CUERPO_LOOP");add("}");}});add(new LinkedList<String>(){{add("ε");}});}});put("DECLARACION", new ArrayList<List<String>>(){{add(new LinkedList<String>(){{add("set");add("identificador");add("INDICE_ASIG");add("ASIGNACION");add(";");}});}});put("CUERPO_INST", new ArrayList<List<String>>(){{add(new LinkedList<String>(){{add("DECLARACION");add("CUERPO_INST");}});add(new LinkedList<String>(){{add("FOR");add("CUERPO_INST");}});add(new LinkedList<String>(){{add("GETS");add(";");add("CUERPO_INST");}});add(new LinkedList<String>(){{add("IF");add("CUERPO_INST");}});add(new LinkedList<String>(){{add("PUTS");add("CUERPO_INST");}});add(new LinkedList<String>(){{add("WHILE");add("CUERPO_INST");}});add(new LinkedList<String>(){{add("SWITCH");add("CUERPO_INST");}});add(new LinkedList<String>(){{add("AGRUP_INST");add(";");add("CUERPO_INST");}});add(new LinkedList<String>(){{add("ε");}});}});put("INICIO_DEFAULT", new ArrayList<List<String>>(){{add(new LinkedList<String>(){{add("default");add("{");}});}});put("AGRUP_INST", new ArrayList<List<String>>(){{add(new LinkedList<String>(){{add("[");add("AUX_AGRUP_INST");}});}});put("CONTINUE", new ArrayList<List<String>>(){{add(new LinkedList<String>(){{add("continue");add(";");}});}});put("INICIO_IF", new ArrayList<List<String>>(){{add(new LinkedList<String>(){{add("if");add("{");add("EXPRESION");add("}");add("then");add("{");}});}});put("INCREMENTO", new ArrayList<List<String>>(){{add(new LinkedList<String>(){{add("valor_entero");}});add(new LinkedList<String>(){{add("ε");}});}});put("IF_LOOP_FUNC", new ArrayList<List<String>>(){{add(new LinkedList<String>(){{add("INICIO_IF");add("CUERPO_LOOP_FUNC");add("}");add("ELSEIF_LOOP_FUNC");}});}});put("ELSEIF_FUNCION", new ArrayList<List<String>>(){{add(new LinkedList<String>(){{add("INICIO_ELSEIF");add("CUERPO_FUNCION");add("}");add("ELSEIF_FUNCION");}});add(new LinkedList<String>(){{add("ELSE_FUNCION");}});}});put("CUERPO_FUNCION", new ArrayList<List<String>>(){{add(new LinkedList<String>(){{add("DECLARACION");add("CUERPO_FUNCION");}});add(new LinkedList<String>(){{add("PUTS");add("CUERPO_FUNCION");}});add(new LinkedList<String>(){{add("GETS");add(";");add("CUERPO_FUNCION");}});add(new LinkedList<String>(){{add("IF_FUNCION");add("CUERPO_FUNCION");}});add(new LinkedList<String>(){{add("FOR_FUNCION");add("CUERPO_FUNCION");}});add(new LinkedList<String>(){{add("WHILE_FUNCION");add("CUERPO_FUNCION");}});add(new LinkedList<String>(){{add("SWITCH_FUNCION");add("CUERPO_FUNCION");}});add(new LinkedList<String>(){{add("RETURN");add("CUERPO_FUNCION");}});add(new LinkedList<String>(){{add("AGRUP_INST");add(";");add("CUERPO_FUNCION");}});add(new LinkedList<String>(){{add("ε");}});}});put("AUX_PARAM", new ArrayList<List<String>>(){{add(new LinkedList<String>(){{add("VALOR_PARAM");add("}");add("PARAM_FUNC");}});add(new LinkedList<String>(){{add("EXPR");add("}");add("PARAM_FUNC");}});}});put("WHILE", new ArrayList<List<String>>(){{add(new LinkedList<String>(){{add("INICIO_WHILE");add("CUERPO_LOOP");add("}");}});}});put("ASIGNACION", new ArrayList<List<String>>(){{add(new LinkedList<String>(){{add("VALOR");}});add(new LinkedList<String>(){{add("$");add("identificador");add("INDICE");}});add(new LinkedList<String>(){{add("AGRUP");}});}});put("GETS", new ArrayList<List<String>>(){{add(new LinkedList<String>(){{add("gets");add("stdin");}});}});put("DEFAULT", new ArrayList<List<String>>(){{add(new LinkedList<String>(){{add("INICIO_DEFAULT");add("CUERPO_INST");add("}");}});add(new LinkedList<String>(){{add("ε");}});}});put("IF_LOOP", new ArrayList<List<String>>(){{add(new LinkedList<String>(){{add("INICIO_IF");add("CUERPO_LOOP");add("}");add("ELSEIF_LOOP");}});}});put("VALOR", new ArrayList<List<String>>(){{add(new LinkedList<String>(){{add("valor_string");}});add(new LinkedList<String>(){{add("valor_entero");}});add(new LinkedList<String>(){{add("valor_double");}});}});put("DECLARACION_FUNCION", new ArrayList<List<String>>(){{add(new LinkedList<String>(){{add("proc");add("identificador");add("{");add("ARGS_FUNCION");add("}");add("{");add("CUERPO_FUNCION");add("}");add("DECLARACION_FUNCION");}});add(new LinkedList<String>(){{add("ε");}});}});put("PARAM_FUNC", new ArrayList<List<String>>(){{add(new LinkedList<String>(){{add("{");add("AUX_PARAM");}});add(new LinkedList<String>(){{add("ε");}});}});}};
    final static Map<String, List<Set<String>>> PREDICT = new HashMap<String, List<Set<String>>>() {{put("ELSEIF", new ArrayList<Set<String>>(){{add(new HashSet<String>(){{add("elseif");}});add(new HashSet<String>(){{add("puts");add("set");add("else");add("for");add("[");add("while");add("gets");add("if");add("EOF");add("}");add("switch");}});}});put("CASE_LOOP", new ArrayList<Set<String>>(){{add(new HashSet<String>(){{add("case");}});}});put("CUERPO_LOOP_FUNC", new ArrayList<Set<String>>(){{add(new HashSet<String>(){{add("break");}});add(new HashSet<String>(){{add("continue");}});add(new HashSet<String>(){{add("set");}});add(new HashSet<String>(){{add("gets");}});add(new HashSet<String>(){{add("puts");}});add(new HashSet<String>(){{add("return");}});add(new HashSet<String>(){{add("if");}});add(new HashSet<String>(){{add("switch");}});add(new HashSet<String>(){{add("for");}});add(new HashSet<String>(){{add("while");}});add(new HashSet<String>(){{add("[");}});add(new HashSet<String>(){{add("}");}});}});put("INICIO_ELSE", new ArrayList<Set<String>>(){{add(new HashSet<String>(){{add("else");}});}});put("EXPRESION", new ArrayList<Set<String>>(){{add(new HashSet<String>(){{add("valor_string");add("valor_double");add("$");add("valor_entero");add("[");}});add(new HashSet<String>(){{add("!");add("-");}});add(new HashSet<String>(){{add("(");}});}});put("CUERPO_LOOP", new ArrayList<Set<String>>(){{add(new HashSet<String>(){{add("set");}});add(new HashSet<String>(){{add("for");}});add(new HashSet<String>(){{add("gets");}});add(new HashSet<String>(){{add("if");}});add(new HashSet<String>(){{add("puts");}});add(new HashSet<String>(){{add("while");}});add(new HashSet<String>(){{add("switch");}});add(new HashSet<String>(){{add("break");}});add(new HashSet<String>(){{add("continue");}});add(new HashSet<String>(){{add("[");}});add(new HashSet<String>(){{add("}");}});}});put("SWITCH_FUNCION", new ArrayList<Set<String>>(){{add(new HashSet<String>(){{add("switch");}});}});put("INICIO_CASE", new ArrayList<Set<String>>(){{add(new HashSet<String>(){{add("case");}});}});put("AUX_EXPR", new ArrayList<Set<String>>(){{add(new HashSet<String>(){{add("==");add("&&");add("||");add("**");add("<=");add("%");add("*");add("+");add("eq");add("-");add("/");add("ne");add("<");add("!=");add(">");add(">=");}});add(new HashSet<String>(){{add(")");add("}");}});}});put("FOR_FUNCION", new ArrayList<Set<String>>(){{add(new HashSet<String>(){{add("for");}});}});put("INICIO_FOR", new ArrayList<Set<String>>(){{add(new HashSet<String>(){{add("for");}});}});put("BREAK", new ArrayList<Set<String>>(){{add(new HashSet<String>(){{add("break");}});}});put("WHILE_FUNCION", new ArrayList<Set<String>>(){{add(new HashSet<String>(){{add("while");}});}});put("CASE2_LOOP", new ArrayList<Set<String>>(){{add(new HashSet<String>(){{add("case");}});add(new HashSet<String>(){{add("default");add("}");}});}});put("VAL_INDICE", new ArrayList<Set<String>>(){{add(new HashSet<String>(){{add("valor_string");add("valor_double");add("valor_entero");}});add(new HashSet<String>(){{add("[");}});}});put("ELSE", new ArrayList<Set<String>>(){{add(new HashSet<String>(){{add("else");}});add(new HashSet<String>(){{add("puts");add("set");add("for");add("[");add("while");add("gets");add("if");add("EOF");add("}");add("switch");}});}});put("INICIO", new ArrayList<Set<String>>(){{add(new HashSet<String>(){{add("puts");add("proc");add("set");add("for");add("[");add("while");add("gets");add("if");add("EOF");add("switch");}});}});put("ELSE_FUNCION", new ArrayList<Set<String>>(){{add(new HashSet<String>(){{add("else");}});add(new HashSet<String>(){{add("puts");add("set");add("for");add("[");add("while");add("gets");add("if");add("}");add("return");add("switch");}});}});put("ELSE_LOOP_FUNC", new ArrayList<Set<String>>(){{add(new HashSet<String>(){{add("else");}});add(new HashSet<String>(){{add("puts");add("set");add("break");add("continue");add("for");add("[");add("while");add("gets");add("if");add("}");add("return");add("switch");}});}});put("IF", new ArrayList<Set<String>>(){{add(new HashSet<String>(){{add("if");}});}});put("DEFAULT_LOOP", new ArrayList<Set<String>>(){{add(new HashSet<String>(){{add("default");}});add(new HashSet<String>(){{add("}");}});}});put("IF_FUNCION", new ArrayList<Set<String>>(){{add(new HashSet<String>(){{add("if");}});}});put("R2", new ArrayList<Set<String>>(){{add(new HashSet<String>(){{add("valor_string");add("valor_double");add("$");add("valor_entero");add("[");}});add(new HashSet<String>(){{add(";");}});}});put("INDICE_TERM", new ArrayList<Set<String>>(){{add(new HashSet<String>(){{add("(");}});add(new HashSet<String>(){{add("==");add("&&");add("||");add("**");add("<=");add("%");add(")");add("*");add("+");add("eq");add("-");add("/");add("ne");add("<");add("!=");add("}");add(">");add(">=");}});}});put("INICIO_SWITCH", new ArrayList<Set<String>>(){{add(new HashSet<String>(){{add("switch");}});}});put("ARGS_FUNCION", new ArrayList<Set<String>>(){{add(new HashSet<String>(){{add("{");}});add(new HashSet<String>(){{add("}");}});}});put("FOR", new ArrayList<Set<String>>(){{add(new HashSet<String>(){{add("for");}});}});put("AUX_AGRUP_INST", new ArrayList<Set<String>>(){{add(new HashSet<String>(){{add("expr");}});add(new HashSet<String>(){{add("identificador");}});add(new HashSet<String>(){{add("gets");}});add(new HashSet<String>(){{add("array");}});}});put("DEFAULT_FUNCION", new ArrayList<Set<String>>(){{add(new HashSet<String>(){{add("default");}});add(new HashSet<String>(){{add("}");}});}});put("OP_UNARIO", new ArrayList<Set<String>>(){{add(new HashSet<String>(){{add("-");}});add(new HashSet<String>(){{add("!");}});}});put("CASE", new ArrayList<Set<String>>(){{add(new HashSet<String>(){{add("case");}});}});put("RETURN_LOOP", new ArrayList<Set<String>>(){{add(new HashSet<String>(){{add("return");}});}});put("INDICE", new ArrayList<Set<String>>(){{add(new HashSet<String>(){{add("(");}});add(new HashSet<String>(){{add(";");}});}});put("SWITCH_LOOP", new ArrayList<Set<String>>(){{add(new HashSet<String>(){{add("switch");}});}});put("AGRUP", new ArrayList<Set<String>>(){{add(new HashSet<String>(){{add("[");}});}});put("AUX_AGRUP", new ArrayList<Set<String>>(){{add(new HashSet<String>(){{add("expr");}});add(new HashSet<String>(){{add("identificador");}});add(new HashSet<String>(){{add("gets");}});add(new HashSet<String>(){{add("array");}});}});put("CASE_LOOP_FUNC", new ArrayList<Set<String>>(){{add(new HashSet<String>(){{add("case");}});}});put("INDICE_ASIG", new ArrayList<Set<String>>(){{add(new HashSet<String>(){{add("(");}});add(new HashSet<String>(){{add("valor_string");add("valor_double");add("$");add("valor_entero");add("[");}});}});put("MODULO_PPAL", new ArrayList<Set<String>>(){{add(new HashSet<String>(){{add("set");}});add(new HashSet<String>(){{add("for");}});add(new HashSet<String>(){{add("gets");}});add(new HashSet<String>(){{add("if");}});add(new HashSet<String>(){{add("puts");}});add(new HashSet<String>(){{add("while");}});add(new HashSet<String>(){{add("switch");}});add(new HashSet<String>(){{add("[");}});add(new HashSet<String>(){{add("EOF");}});}});put("INDICE_FOR", new ArrayList<Set<String>>(){{add(new HashSet<String>(){{add("(");}});add(new HashSet<String>(){{add("}");}});}});put("SWITCH", new ArrayList<Set<String>>(){{add(new HashSet<String>(){{add("switch");}});}});put("VALOR_PARAM", new ArrayList<Set<String>>(){{add(new HashSet<String>(){{add("valor_string");add("valor_double");add("valor_entero");}});add(new HashSet<String>(){{add("$");}});add(new HashSet<String>(){{add("[");}});}});put("CASE2_LOOP_FUNC", new ArrayList<Set<String>>(){{add(new HashSet<String>(){{add("case");}});add(new HashSet<String>(){{add("default");add("}");}});}});put("AUX_ARRAY", new ArrayList<Set<String>>(){{add(new HashSet<String>(){{add("size");}});add(new HashSet<String>(){{add("exists");}});}});put("INDICE_PARAM", new ArrayList<Set<String>>(){{add(new HashSet<String>(){{add("(");}});add(new HashSet<String>(){{add("}");}});}});put("INDICE_SWITCH", new ArrayList<Set<String>>(){{add(new HashSet<String>(){{add("(");}});add(new HashSet<String>(){{add("{");}});}});put("INICIO_ELSEIF", new ArrayList<Set<String>>(){{add(new HashSet<String>(){{add("elseif");}});}});put("ASIG_FOR", new ArrayList<Set<String>>(){{add(new HashSet<String>(){{add("valor_entero");}});add(new HashSet<String>(){{add("$");}});add(new HashSet<String>(){{add("expr");}});}});put("CASE2_FUNCION", new ArrayList<Set<String>>(){{add(new HashSet<String>(){{add("case");}});add(new HashSet<String>(){{add("default");add("}");}});}});put("TERM", new ArrayList<Set<String>>(){{add(new HashSet<String>(){{add("$");}});add(new HashSet<String>(){{add("[");}});add(new HashSet<String>(){{add("valor_string");add("valor_double");add("valor_entero");}});}});put("CASE2", new ArrayList<Set<String>>(){{add(new HashSet<String>(){{add("case");}});add(new HashSet<String>(){{add("default");add("}");}});}});put("INICIO_WHILE", new ArrayList<Set<String>>(){{add(new HashSet<String>(){{add("while");}});}});put("ELSEIF_LOOP_FUNC", new ArrayList<Set<String>>(){{add(new HashSet<String>(){{add("elseif");}});add(new HashSet<String>(){{add("set");add("break");add("for");add("while");add("gets");add("switch");add("puts");add("else");add("continue");add("[");add("if");add("}");add("return");}});}});put("RETURN", new ArrayList<Set<String>>(){{add(new HashSet<String>(){{add("return");}});}});put("OP_BINARIO", new ArrayList<Set<String>>(){{add(new HashSet<String>(){{add(">");}});add(new HashSet<String>(){{add("<");}});add(new HashSet<String>(){{add(">=");}});add(new HashSet<String>(){{add("<=");}});add(new HashSet<String>(){{add("eq");}});add(new HashSet<String>(){{add("==");}});add(new HashSet<String>(){{add("ne");}});add(new HashSet<String>(){{add("!=");}});add(new HashSet<String>(){{add("&&");}});add(new HashSet<String>(){{add("||");}});add(new HashSet<String>(){{add("+");}});add(new HashSet<String>(){{add("-");}});add(new HashSet<String>(){{add("*");}});add(new HashSet<String>(){{add("/");}});add(new HashSet<String>(){{add("%");}});add(new HashSet<String>(){{add("**");}});}});put("EXPR", new ArrayList<Set<String>>(){{add(new HashSet<String>(){{add("expr");}});}});put("SWITCH_LOOP_FUNC", new ArrayList<Set<String>>(){{add(new HashSet<String>(){{add("switch");}});}});put("CASE_FUNCION", new ArrayList<Set<String>>(){{add(new HashSet<String>(){{add("case");}});}});put("PUTS", new ArrayList<Set<String>>(){{add(new HashSet<String>(){{add("puts");}});}});put("DEFAULT_LOOP_FUNC", new ArrayList<Set<String>>(){{add(new HashSet<String>(){{add("default");}});add(new HashSet<String>(){{add("}");}});}});put("ELSEIF_LOOP", new ArrayList<Set<String>>(){{add(new HashSet<String>(){{add("elseif");}});add(new HashSet<String>(){{add("puts");add("set");add("break");add("else");add("continue");add("for");add("[");add("while");add("gets");add("if");add("}");add("switch");}});}});put("ELSE_LOOP", new ArrayList<Set<String>>(){{add(new HashSet<String>(){{add("else");}});add(new HashSet<String>(){{add("puts");add("set");add("break");add("continue");add("for");add("[");add("while");add("gets");add("if");add("}");add("switch");}});}});put("DECLARACION", new ArrayList<Set<String>>(){{add(new HashSet<String>(){{add("set");}});}});put("CUERPO_INST", new ArrayList<Set<String>>(){{add(new HashSet<String>(){{add("set");}});add(new HashSet<String>(){{add("for");}});add(new HashSet<String>(){{add("gets");}});add(new HashSet<String>(){{add("if");}});add(new HashSet<String>(){{add("puts");}});add(new HashSet<String>(){{add("while");}});add(new HashSet<String>(){{add("switch");}});add(new HashSet<String>(){{add("[");}});add(new HashSet<String>(){{add("}");}});}});put("INICIO_DEFAULT", new ArrayList<Set<String>>(){{add(new HashSet<String>(){{add("default");}});}});put("AGRUP_INST", new ArrayList<Set<String>>(){{add(new HashSet<String>(){{add("[");}});}});put("CONTINUE", new ArrayList<Set<String>>(){{add(new HashSet<String>(){{add("continue");}});}});put("INICIO_IF", new ArrayList<Set<String>>(){{add(new HashSet<String>(){{add("if");}});}});put("INCREMENTO", new ArrayList<Set<String>>(){{add(new HashSet<String>(){{add("valor_entero");}});add(new HashSet<String>(){{add("}");}});}});put("IF_LOOP_FUNC", new ArrayList<Set<String>>(){{add(new HashSet<String>(){{add("if");}});}});put("ELSEIF_FUNCION", new ArrayList<Set<String>>(){{add(new HashSet<String>(){{add("elseif");}});add(new HashSet<String>(){{add("puts");add("set");add("else");add("for");add("[");add("while");add("gets");add("if");add("}");add("return");add("switch");}});}});put("CUERPO_FUNCION", new ArrayList<Set<String>>(){{add(new HashSet<String>(){{add("set");}});add(new HashSet<String>(){{add("puts");}});add(new HashSet<String>(){{add("gets");}});add(new HashSet<String>(){{add("if");}});add(new HashSet<String>(){{add("for");}});add(new HashSet<String>(){{add("while");}});add(new HashSet<String>(){{add("switch");}});add(new HashSet<String>(){{add("return");}});add(new HashSet<String>(){{add("[");}});add(new HashSet<String>(){{add("}");}});}});put("AUX_PARAM", new ArrayList<Set<String>>(){{add(new HashSet<String>(){{add("valor_string");add("valor_double");add("$");add("valor_entero");add("[");}});add(new HashSet<String>(){{add("expr");}});}});put("WHILE", new ArrayList<Set<String>>(){{add(new HashSet<String>(){{add("while");}});}});put("ASIGNACION", new ArrayList<Set<String>>(){{add(new HashSet<String>(){{add("valor_string");add("valor_double");add("valor_entero");}});add(new HashSet<String>(){{add("$");}});add(new HashSet<String>(){{add("[");}});}});put("GETS", new ArrayList<Set<String>>(){{add(new HashSet<String>(){{add("gets");}});}});put("DEFAULT", new ArrayList<Set<String>>(){{add(new HashSet<String>(){{add("default");}});add(new HashSet<String>(){{add("}");}});}});put("IF_LOOP", new ArrayList<Set<String>>(){{add(new HashSet<String>(){{add("if");}});}});put("VALOR", new ArrayList<Set<String>>(){{add(new HashSet<String>(){{add("valor_string");}});add(new HashSet<String>(){{add("valor_entero");}});add(new HashSet<String>(){{add("valor_double");}});}});put("DECLARACION_FUNCION", new ArrayList<Set<String>>(){{add(new HashSet<String>(){{add("proc");}});add(new HashSet<String>(){{add("puts");add("set");add("for");add("[");add("while");add("gets");add("if");add("EOF");add("switch");}});}});put("PARAM_FUNC", new ArrayList<Set<String>>(){{add(new HashSet<String>(){{add("{");}});add(new HashSet<String>(){{add("]");}});}});}};

    static class Token {

        private int linea;
        private int columna;
        private String lexema;
        private String valor;

        public Token(int linea, int columna, String lexema) {
            this.linea = linea;
            this.columna = columna;
            this.lexema = lexema;
        }

        public Token(int linea, int columna, String lexema, String valor) {
            this.linea = linea;
            this.columna = columna;
            this.lexema = lexema;
            this.valor = valor;
        }

        int getLinea() {
            return this.linea;
        }

        int getColumna() {
            return this.columna;
        }

        String getLexema() {
            return this.lexema;
        }
        
        String getValor() {
            if (valor != null)
                return this.valor;
            return this.lexema;
        }
    }

    private static final int DESCONOCIDO = -1,
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
    private static boolean error;
    private static Queue<Token> tokens;

    public static void main(String[] args) throws FileNotFoundException, IOException {
        BufferedReader br;
        
//        args = new String[]{"resources/IO/Parser/in.txt"};
        if (args.length > 0)
            br = new BufferedReader(new FileReader(args[0]));
        else
            br = new BufferedReader(new InputStreamReader(System.in));
        tokenizer(br);
        parser();
    }

    public static void parser() {
        error = false;
        Stack<String> stk = new Stack<>();
        
        stk.push("EOF");
        stk.push("INICIO");
        while (!tokens.isEmpty() && !error) {
            Token next = tokens.peek();
            String nt = stk.peek();
//            System.out.println("------------------- " + next.getLexema() + " -----------------------");
//            System.out.println(stk);
//            System.out.println(nt);
            if (next.getLexema().equals(nt)) {
                tokens.poll();
                stk.pop();
            } else {
                int idx = predictIndex(nt, next.getLexema());
                if (idx == -1) {
                    error = true;
                } else {
                    List<String> rule = GRAMMAR.get(nt).get(idx);
                    stk.pop();
                    if (rule.size() != 1 || !rule.get(0).equals("ε")) {
                        ListIterator<String> it = rule.listIterator(rule.size());
                        while (it.hasPrevious())
                            stk.push(it.previous());
                    }
                }
            }
        }
        if (error) {
            Set<String> predError = new TreeSet<>();
            if (!PREDICT.containsKey(stk.peek()))
                predError.add(stk.peek());
            else {
                List<Set<String>> pred = PREDICT.get(stk.peek());
                for (Set<String> p : pred)
                    predError.addAll(p);
            }
            printError(predError, tokens.peek());
        } else {
            System.out.println("El analisis sintactico ha finalizado correctamente.");
        }
    }
    
    public static void printError(Set<String> predError, Token err){
        StringBuilder result = new StringBuilder("");
        result.append("<").append(err.getLinea()).append(",").append(err.getColumna());
        result.append("> Error sintactico se encontro: ").append("'").append(err.getValor()).append("'; se esperaba: ");
        Iterator<String> it = predError.iterator();
        while(it.hasNext()){
            result.append("'").append(it.next()).append("'");
            if(it.hasNext())
                result.append(", ");
            else result.append(".");
        }
        System.out.println(result.toString());
    }

    public static int predictIndex(String nt, String token) {
        if (!PREDICT.containsKey(nt))
            return -1;
        List<Set<String>> pred = PREDICT.get(nt);
        for (int i = 0; i < pred.size(); i++) {
            if (pred.get(i).contains(token)) {
                return i;
            }
        }
        return -1;
    }

    public static void tokenizer(BufferedReader reader) throws IOException {
        String line;
        char caracter;
        String lexema;
        tokens = new LinkedList<>();
        int f = 0, c = 0;
        for (f = 0; (line = reader.readLine()) != null; f++) {
            for (c = 0; c < line.length(); c++) {
                lexema = "";
                caracter = line.charAt(c);
                reiniciarVariables();

                int row = f + 1, col = c + 1;

                if (caracter == ' ' || caracter == '\t' || caracter == '\n') {  //Ignorar espacio/tab/salto 
                    continue;
                } else if (caracter == '#') {
                    break;                                                      // Comentario, se ignora el resto de la linea            
                }
                int validacion = validarPrimerChar(caracter);
                if (validacion == 2) {
                    while (c + 1 < line.length() && line.charAt(++c) != '\"')
                        lexema += line.charAt(c);
                    estadoLexema = FIN_STRING;
                } else if (validacion == 0) {
                    while (c < line.length() && clasificar(caracter = line.charAt(c++), lexema)) {
                        lexema += caracter;
                        if (estadoLexema == FIN_SYMBOL)
                            break;
                    }
                    if (devolver == true)
                        c -= 2;
                    else
                        c--;
                } else
                    lexema += caracter;
                String valor = null;
                switch (estadoLexema) {
                    case FIN_STRING:
                        valor = "\"" + lexema + "\"";
                        lexema = "valor_string";
                        break;
                    case ENTERO:
                        valor = lexema;
                        lexema = "valor_entero";
                        break;
                    case DOUBLE:
                        valor = lexema;
                        lexema = "valor_double";
                        break;
                    default:
                        if (!RESERVED_SYMBOLS.contains(lexema) && !RESERVED_WORDS.contains(lexema)) {
                            valor = lexema;
                            lexema = "identificador";
                        }
                        break;
                }
                tokens.add(new Token(row, col, lexema, valor));
            }
        }
        tokens.add(new Token(f + 1, c + 1, "EOF"));
    }

    public static boolean clasificar(char next, String lexema) {
        boolean flag = true;
        switch (estadoLexema) {
            case DESCONOCIDO:
                if (isDigit(next)) {
                    estadoLexema = ENTERO;
                } else if (isLetter(next) || next == '_') {
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
                if (!isLetter(next) && next != '_' && !isDigit(next)) {
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