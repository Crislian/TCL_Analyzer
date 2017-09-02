package tcl_analyzer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.LinkedList;

public class Utils {
    public static void main(String[] args) throws IOException {
//        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//        String str;
//        LinkedList ll = new LinkedList();
//        while((str=br.readLine()) != null && str.length() != 0){
//            for ( int i = 0 ; i < str.length() ; i++ ){
//                System.out.println("add(\'" + str.charAt(i) + "\');");
//                System.out.println(str.charAt(i) + " alpha " + Character.isAlphabetic(str.charAt(i)));
//            }
//        }
//        for ( int i = 65 ; i <= 89 ; i++) {
//            System.out.println("add(\"" + (char) i  + "\');");
//        }
//        for ( int i = 97 ; i <= 122 ; i++) {
//            System.out.println("add(\"" + (char) i  + "\");");
//        }
        tokenWordsGen();
    }
    
    
    
    public static void tokenWordsGen() throws FileNotFoundException, IOException {
        BufferedReader br = new BufferedReader(new FileReader("resources/tokenWords.txt"));
        LinkedList ll = new LinkedList();
        String str;
        while((str=br.readLine())!=null){
            String[] split = str.split("\t");
            for (String s: split)
                ll.add("add(\"" + s + "\");");
            if ("default".compareTo(split[split.length-1]) == 0)
                break;
        }
        ll.clear();
        Object[] toArray = ll.toArray();
        Arrays.sort(toArray);
        for ( Object s : toArray ){
            System.out.println(s);
        }
    }
}
