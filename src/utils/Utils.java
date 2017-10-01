package utils;

import utils.Grammar;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Utils {
    
    public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, Exception {
        construirGramPred();        
        testCases("Lexer");
        testCases("Parser");
//        tokenWordsGen();
    }

    public static <T> void testCases(String c) throws FileNotFoundException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, Exception {
        System.out.println("\t \t Testing: " + c);
        
        Class<?> clase = Class.forName("tcl_analyzer." + c);
        Method m = clase.getMethod("main", String[].class);
        Object inst = clase.newInstance();

        String folderName = c;
        String path = "resources/IO/" + folderName + "/";

        String[] folders = new File(path).list();
        Arrays.sort(folders);
        PrintStream outputStream = new PrintStream("resources/test/test" + folderName + ".txt");
        PrintStream standard = System.out;
        ByteArrayOutputStream stream;
        for (String folder : folders) {
            if (folder.contains(".")) {
                continue;
            }
            folder += "/";
            HashMap<String, String> inputs = new HashMap<>();
            String[] files = new File(path + folder).list();
            Arrays.sort(files);
            for (String file : files) {
                if (file.contains("in")) {
                    stream = new ByteArrayOutputStream();
                    System.setOut(new PrintStream(stream));
                    String[] args = {path + folder + file};
                    m.invoke(inst, (Object) args);
                    System.setOut(standard);
                    inputs.put(file, stream.toString());
                } else if (file.contains("out")) {
                    String number = file.replaceAll("\\D+", "");
                    BufferedReader br = new BufferedReader(new FileReader(path + folder + file));
                    String l;
                    List<String> lineas = new LinkedList<>();
                    while ((l = br.readLine()) != null) {
                        lineas.add(l);
                    }
                    List<String> outputImp = Arrays.asList(inputs.get("in" + number + ".txt").split(Character.toString((char) 13) + "\n"));

                    System.setOut(standard);
                    System.out.println(folder + file);
                    System.setOut(outputStream);
                    System.out.println(folder + file);

                    if (lineas.equals(outputImp)) {
                        System.out.println("Son idénticos");
                    } else {
                        Iterator<String> impIt = outputImp.iterator();
                        Iterator<String> outIt = lineas.iterator();
                        while (impIt.hasNext() && outIt.hasNext()) {
                            String impNext = impIt.next();
                            String outNext = outIt.next();
                            if (!impNext.equals(outNext)) {
                                System.out.println(impNext.length() + " " + outNext.length());
                                System.out.println("Diff:\n" + impNext + "\n" + outNext);
                            }
                        }
                    }
                    System.out.println();
                }
            }
        }
        System.setOut(standard);
    }

    public static void utility() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String str;
        while ((str = br.readLine()) != null && str.length() != 0) {
            for (int i = 0; i < str.length(); i++) {
                System.out.println(str.charAt(i) + " alpha " + Character.isAlphabetic(str.charAt(i)));
            }
        }
    }

    public static void tokenWordsGen() throws FileNotFoundException, IOException {
        BufferedReader br = new BufferedReader(new FileReader("resources/reservedWords.txt"));
        LinkedList<String> ll = new LinkedList<>();
        String str;
        while ((str = br.readLine()) != null) {
            String[] split = str.split("\t");
            for (String s : split) {
                ll.add("add(\"" + s + "\");");
            }
        }
        Object[] toArray = ll.toArray();
        Arrays.sort(toArray);
        for (Object s : toArray) {
            System.out.println(s);
        }
    }

    public static void construirGramPred() throws IOException {
        Grammar.main(new String[]{});
        Map<String, List<List<String>>> gramatica = Grammar.getGramatica();
        Map<String, List<Set<String>>> predicciones = Grammar.getPredicciones();

        StringBuilder g = new StringBuilder();
        g.append("    final static Map<String, List<List<String>>> GRAMMAR = new HashMap<String, List<List<String>>>() {{");
        for (String NT : gramatica.keySet()) {
            g.append("put(\"" + NT + "\", new ArrayList<List<String>>(){{");
            for (List<String> reglas : gramatica.get(NT)) {
                g.append("add(new LinkedList<String>(){{");
                for (String i : reglas) {
                    g.append("add(\"" + i + "\");");
                }
                g.append("}});");
            }
            g.append("}});");
        }
        g.append("}};");
        StringBuilder p = new StringBuilder();
        p.append("    final static Map<String, List<Set<String>>> PREDICT = new HashMap<String, List<Set<String>>>() {{");
        for (String NT : predicciones.keySet()) {
            p.append("put(\"" + NT + "\", new ArrayList<Set<String>>(){{");
            for (Set<String> reglas : predicciones.get(NT)) {
                p.append("add(new HashSet<String>(){{");
                for (String i : reglas) {
                    p.append("add(\"" + i + "\");");
                }
                p.append("}});");
            }
            p.append("}});");
        }
        p.append("}};");

        String grammarString = "final static Map<String, List<List<String>>> GRAMMAR",
                predString = "final static Map<String, List<Set<String>>> PREDICT";

        Path path = Paths.get("src/tcl_analyzer", "Parser.java");
        List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).contains(grammarString)) {
                lines.set(i++, g.toString());
                while (!lines.get(i).contains(predString)) {
                    lines.remove(i);
                }
                lines.set(i++, p.toString());
                while (!lines.get(i).equals("")) {
                    lines.remove(i);
                }
                break;
            }
        }
        Files.write(path, lines, StandardCharsets.UTF_8);
    }
}
