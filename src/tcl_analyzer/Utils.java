package tcl_analyzer;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Utils {

    public static void main(String[] args) throws FileNotFoundException, IOException {
        testCases();
//        tokenWordsGen();
    }

    public static void testCases() throws FileNotFoundException, IOException {
        ByteArrayOutputStream stream;
        PrintStream standard = System.out;
        PrintStream outputStream = new PrintStream("resources/test.txt");
        HashMap<String, String> inputs = new HashMap<>();
        String[] files = new File("resources/IO").list();
        Arrays.sort(files);
        for (String file : files) {
            if (file.contains("in")) {
                stream = new ByteArrayOutputStream();
                System.setOut(new PrintStream(stream));
                String[] args = {"resources/IO/" + file};
                TCL_Analyzer.main(args);
                inputs.put(file, stream.toString());
            } else {
                String number = file.replaceAll("\\D+", "");
                System.setOut(standard);
                System.out.println(number);
                System.setOut(outputStream);
                System.out.println(number);

                BufferedReader br = new BufferedReader(new FileReader("resources/IO/" + file));
                String l;
                List<String> lineas = new LinkedList<>();
                while ((l = br.readLine()) != null) {
                    lineas.add(l);
                }
                List<String> outputImp = Arrays.asList(inputs.get("in" + number + ".txt").split(Character.toString((char)13) + "\n"));
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
}
