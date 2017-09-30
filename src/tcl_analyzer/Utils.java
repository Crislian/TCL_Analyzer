package tcl_analyzer;

import grammar.Grammar;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Utils {

    public final static int LEXER_CASE = 0x00;
    public final static int PARSER_CASE = 0x01;
//    public final static int LEXER_CASE = 0X10;
//    public final static int LEXER_CASE = 0;
    
    public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, Exception {
        testCases(PARSER_CASE);
//        tokenWordsGen();
//        crearConstructores();
    }

    public static final String NOMBRE_METODO = "main";
    
    public static void testCases(int caso) throws FileNotFoundException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, Exception {
        
        
        
//         HUEVADAS! 
//        T instancia = clase.newInstance();
//        
//        Method method = clase.getMethod("main", String[].class);
//        System.out.println(method.getParameterTypes());
//        for(Method m: clase.getMethods()){
//            if (m.getName().equals(NOMBRE_METODO)) {
//                for(Parameter param : m.getParameters()){
//                    System.out.println(param.getParameterizedType());
//                }
//                m.invoke(instancia, args);
//            }
//        }
        

String folderName = null;        

        switch(caso){
            case LEXER_CASE:
                folderName = "Lexer";
                break;
            case PARSER_CASE:
                folderName = "Parser";
                break;
        }

        String path = "resources/IO/" + folderName + "/";

        String[] folders = new File(path).list();
        Arrays.sort(folders);
        PrintStream outputStream = new PrintStream("resources/test" + folderName + ".txt");
        PrintStream standard = System.out;
        ByteArrayOutputStream stream;
        for (String folder : folders) {
            folder += "/";
            HashMap<String, String> inputs = new HashMap<>();
            String[] files = new File(path + folder).list();
            Arrays.sort(files);
            for (String file : files) {
                if (file.contains("in")) {
                    stream = new ByteArrayOutputStream();
                    System.setOut(new PrintStream(stream));
                    String[] args = {path + folder + file};
                    
                    switch(caso){
                        case LEXER_CASE:
                            Lexer.main(args);
                            break;
                        case PARSER_CASE:
                            Parser.main(args);
                            break;
//                        case LEXER_CASE:
//                            Lexer.main(args);
//                            break;
                            default:
                                throw new Exception("Pinche cabro mira las putas parametrizaciones!");
                    }
                    
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
                                System.out.println("Diff: " + impNext + " " + outNext);
                            }
                        }
                    }
                    System.out.println();
                }
            }
        }
    }

    public static void utility() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String str;
//        LinkedList ll = new LinkedList();
        while ((str = br.readLine()) != null && str.length() != 0) {
            for (int i = 0; i < str.length(); i++) //                System.out.println("add(\'" + str.charAt(i) + "\');");
            {
                System.out.println(str.charAt(i) + " alpha " + Character.isAlphabetic(str.charAt(i)));
            }
        }
//            }
//        }
//        for ( int i = 65 ; i <= 89 ; i++) {
//            System.out.println("add(\"" + (char) i  + "\');");
//        }
//        for ( int i = 97 ; i <= 122 ; i++) {
//            System.out.println("add(\"" + (char) i  + "\");");
//        }
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
//        ll.clear();
        Object[] toArray = ll.toArray();
        Arrays.sort(toArray);
        for (Object s : toArray) {
            System.out.println(s);
        }
    }
    
    public static void crearConstructores() throws IOException {
        Grammar.main(new String[]{});
        Map<String, List<List<String>>> gramatica = Grammar.getGramatica();
        Map<String, List<Set<String>>> predicciones = Grammar.getPredicciones();
        
        System.setOut(new PrintStream(new PrintStream("resources/Grammar.txt")));
        
        StringBuilder g = new StringBuilder();
        g.append("final static Map<String, List<List<String>>> GRAMMAR = new HashMap<String, List<List<String>>>() {{");
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
        System.out.print(g.toString());
        
        System.setOut(new PrintStream(new PrintStream("resources/Predict.txt")));
        
        StringBuilder p = new StringBuilder();
        p.append("final static Map<String, List<Set<String>>> PREDICT = new HashMap<String, List<Set<String>>>() {{");
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
        System.out.print(p.toString());
    }
}
