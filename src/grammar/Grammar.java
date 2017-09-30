package grammar;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Grammar {

    static String primerNoTerminal;
    static Map<String, List<List<String>>> gramatica = new HashMap<>();
    static Map<String, Set<String>> primeros = new HashMap<>();
    static Map<String, Set<String>> siguientes = new HashMap<>();
    static Map<String, List<Set<String>>> predicciones = new HashMap<>();

    public static void main(String[] args) throws FileNotFoundException, IOException {
        leerGramatica();
        pred();
        
    }

    public static void leerGramatica() throws FileNotFoundException, IOException {
        BufferedReader br = new BufferedReader(new FileReader("resources/grammar/grammar.txt"));
        String line;
        while ((line = br.readLine()) != null) {
            if (line.isEmpty())
                continue;
            String[] item = line.split("\\s+");
            String noTerminal = item[0];
            if (gramatica.isEmpty())
                primerNoTerminal = noTerminal;
            if (!gramatica.containsKey(noTerminal))
                gramatica.put(noTerminal, new ArrayList<List<String>>());
            List<List<String>> producciones = gramatica.get(noTerminal);
            for (int i = 2; i < item.length; i++) {
                if (item[i] == "|" || item[i].isEmpty())
                    continue;
                List<String> regla = splitRule(item[i]);
                while (++i < item.length && item[i].charAt(0) != '|')
                    regla.addAll(splitRule(item[i]));
                producciones.add(regla);
            }
        }
    }

    private static Set<String> first(String X) {
        if (X == null || X.isEmpty())                                           // Cadenas nulas o vacías
            return new HashSet<>();
        if (!gramatica.containsKey(X) || !Character.isUpperCase(X.charAt(0)))
            return new HashSet<String>() {{                                     // FIRST(T) = {T}
                add(X);
            }};
        if (primeros.containsKey(X))
            return primeros.get(X);
        else
            primeros.put(X, new HashSet<>());

        Set<String> prim = primeros.get(X);
        for (List<String> reglasNT : gramatica.get(X)) {
            if (reglasNT.size() == 1 && reglasNT.get(0).equals("ε")) {
                prim.add("ε");                                                  // FIRST(ε) = {ε}
                continue;
            }
            boolean removed = false;
            for (String Y : reglasNT) {                                         // X ➔ Y1 Y2 Y3
                removed = addFirst(Y, prim);                            // Add FIRST(Y)-ε to FIRST(X)
                if (!removed)
                    break;
            }
            if (removed)
                prim.add("ε"); 
        }
        return prim;
    }

    private static Set<String> follow(String X) {
        if (X == null || X.isEmpty())                                           // Cadenas nulas o vacías
            return new HashSet<>();
        if (!gramatica.containsKey(X) || !Character.isUpperCase(X.charAt(0)))
            return new HashSet<String>();                                       // FOLLOW(T) = {}
        if (siguientes.containsKey(X))
            return siguientes.get(X);
        else
            siguientes.put(X, new HashSet<>());

        Set<String> sig = siguientes.get(X);
        if (X.equals(primerNoTerminal))                                         // FOLLOW(S) = {$}
            sig.add("$");                                                       // S = Start Symbol

        Map<String, List<List<String>>> reglasX = new HashMap<>();
        for (String noTerminal : gramatica.keySet())
            for (List<String> r : gramatica.get(noTerminal))
                if (r.contains(X)) {
                    if (!reglasX.containsKey(noTerminal))
                        reglasX.put(noTerminal, new LinkedList<>());
                    reglasX.get(noTerminal).add(r);
                }

        for (Map.Entry<String, List<List<String>>> entry : reglasX.entrySet())
            for (List<String> regla : entry.getValue()) {
                int pos = regla.indexOf(X);
                boolean removed = true;
                while (removed && ++pos < regla.size())                         // A ➔ α X β
                    removed = addFirst(regla.get(pos), sig);                    // Add FIRST(β)-ε to FOLLOW(X)
                if (pos == regla.size() || removed)                             // A ➔ α X
                    sig.addAll(follow(entry.getKey()));                         // Add FOLLOW(A) to FOLLOW(X)
            }
        return sig;
    }

    private static void pred() {
        for (Map.Entry<String, List<List<String>>> entry : gramatica.entrySet()){
            predicciones.put(entry.getKey(), new LinkedList<>());
            List<Set<String>> predictsX = predicciones.get(entry.getKey());
            for (List<String> regla : entry.getValue()) {
                Set<String> predict = new HashSet<>();
                int pos = -1;
                boolean removed = true;
                while (removed && ++pos < regla.size())                         // A ➔ α X β
                    removed = addFirst(regla.get(pos), predict);                    // Add FIRST(β)-ε to FOLLOW(X)
                if (removed)
                    predict.addAll(follow(entry.getKey()));
                predictsX.add(predict);
            }
        }
    }

    private static boolean addFirst(String X, Set<String> set) {
        Set<String> f = new HashSet<String>() {{ addAll(first(X)); }};
        boolean removed = f.remove("ε");
        set.addAll(f);
        return removed;
    }

    private static List<String> splitRule(String s) {
        List<String> substrings = new LinkedList<>();
        String ss = "";
        int state = 0, i = 0;
        for (int c : s.chars().toArray()) {
            i++;
            if (c == '|' && (i < s.length() && s.charAt(i) != '|')) {
                substrings.add(ss);
                ss = "";
                state = 0;
                continue;
            }
            if ((Character.isAlphabetic(c) && Character.isUpperCase(c)) || (c == '_' && state==1) || (Character.isDigit(c) && state == 1)) {
                if (state == 2) {
                    substrings.add(ss);
                    ss = "";
                }
                ss += (char) c;
                state = 1;
            } else if (!Character.isAlphabetic(c) || !Character.isUpperCase(c)) {
                if (state == 1) {
                    substrings.add(ss);
                    ss = "";
                }
                ss += (char) c;
                state = 2;
            }
        }
        substrings.add(ss);
        return substrings;
    }
}