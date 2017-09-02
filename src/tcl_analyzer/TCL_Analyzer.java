package tcl_analyzer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TCL_Analyzer {

    HashSet<String> tokenWords = new HashSet<String>() {{
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
        add("variable");
        add("vwait");
        add("while");
        add("yield");
        add("yieldto");
        add("zlib");
    }};
    Map<String, String> tokenSymbol = new HashMap<String, String>() {{
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
    
    String lexema;
    int estadoLexema = -1;
    public static void main(String[] args) {
        
        
        
    }
    public static boolean clasificar(char next){
        boolean flag = true;
        switch(estadoLexema){
            case DESCONOCIDO:
                if(Character.isDigit(next))             estadoLexema = ENTERO;
                else if(Character.isAlphabetic(next) 
                        && ALPHABET.contains(next))     estadoLexema = PALABRA;
                else if(next == '\"')                   estadoLexema = STRING;
                else if(next == '_')                    estadoLexema = ID;
                else if(CAN_ALONE_SYMBOL.contains(next))estadoLexema = CSYMBOL;
                else if(NEED_U_SYMBOL.contains(next))   estadoLexema = CNSYMBOL;
                else flag = false;
                break;
            case PALABRA:
                if(Character.isDigit(next))             estadoLexema = ID;
                else if(!Character.isAlphabetic(next)
                        && !char == '_')                flag = false;                
                break;
            case ID:
                if(!Character.isDigit(next)
                        && !Character.isAlphabetic(next)
                        && !char == '_')                flag = false;                
                break;
            case ENTERO: // AGREGAR LA OPCION DE 1e+012
                if(next == '.')                         estadoLexema = DOUBLE;
                else if(!Character.isDigit(next))       flag = false;
                break;
            case DOUBLE:
                if(!Character.isDigit(next))            flag = false;
                break;
            case STRING:
                if(next == '\"')                        estadoLexema = FIN_STRING;
                break;
            case FIN_STRING:
                flag = false;
                break;
        }
        return flag;
    }
}