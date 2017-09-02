package tcl_analyzer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.LinkedList;

public class Utils {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        LinkedList ll = new LinkedList();
        while(true){
            String[] split = br.readLine().split("\t");
            for (String s: split)
                ll.add("add(\"" + s + "\");");
            if ("default".compareTo(split[split.length-1]) == 0){
                System.out.println("PUTA");
                break;
            }
        }
        Object[] toArray = ll.toArray();
        Arrays.sort(toArray);
        System.out.println(toArray.toString());
        for ( Object s : toArray ){
            System.out.println(s);
        }
    }
}
