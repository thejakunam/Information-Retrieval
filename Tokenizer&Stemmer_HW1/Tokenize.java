import java.nio.Buffer;
import java.util.*;
import java.io.*;
import java.util.regex.*;
import java.lang.*;

public class Tokenize {
    public static File[] getFiles(String path){
        File[] files = new File(path).listFiles();
        return files;
    }

    public static String readFile(String path) throws Exception{
        Scanner s = new Scanner(new File(path));

        String txt = s.useDelimiter("\\A").next();
        s.close();

        txt = txt.replaceAll("\\<.*?"," ");
        txt = txt.replaceAll("\\d"," ");
        txt = txt.replaceAll("[+^:,?;=%#&~`$!@*_)(}/{\\.]"," ");
        txt = txt.replaceAll("\\'s", "");
        txt = txt.replaceAll("\\'", "");
        txt = txt.replaceAll("-"," ");
        txt = txt.replaceAll("\\s+", " ");
        txt = txt.trim().toLowerCase();
        return txt;
    }

    public static boolean isAlphabet(String s){
        char[] chars = s.toCharArray();
        for(char c:chars)
            if(!Character.isLetter(c))
                return false;
        return true;
    }

    public static HashMap<String,Integer> updateCount(String s, HashMap<String,Integer> tokenfreq){
        if(isAlphabet(s)){
            if(!tokenfreq.containsKey(s))
                tokenfreq.put(s,1);
            else
                tokenfreq.put(s,tokenfreq.getOrDefault(s,0)+1);
        }
        return tokenfreq;
    }

    public static HashMap<String,Integer> getToken(HashMap<String,String> map ){
        HashMap<String,Integer> tokenfreq = new HashMap<>();
        List<String> init_tokens = new ArrayList<String>();
        for(String key:map.keySet()){
            for(String str:map.get(key).split(" "))
                init_tokens.add(str);
        }
        for(String t:init_tokens)
            tokenfreq = updateCount(t,tokenfreq);

        return tokenfreq;
    }

    public static HashMap<String, Integer> sort(HashMap<String,Integer> map){
        List<Map.Entry<String,Integer>> list = new LinkedList<Map.Entry<String, Integer>>(map.entrySet());

        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });
        HashMap<String,Integer> res = new LinkedHashMap<String,Integer>();
        for(Map.Entry<String, Integer> e: list){
            res.put(e.getKey(),e.getValue());
        }
        return res;
    }

    public static void main(String args[]) throws Exception{
        long start = System.currentTimeMillis();
        String path = args[0];
        File[] files = getFiles(path);

        BufferedWriter writer1 = new BufferedWriter(new FileWriter("tokens.txt",true));
        BufferedWriter writer2 = new BufferedWriter(new FileWriter("token_counts.txt",true));

        int n_docs = 0;

        List<String> frequents = new ArrayList<String>();
        HashMap<String, String> map = new HashMap<>();

        for(File file:files){
            n_docs+=1;
            String fname = file.toString();
            String txt = readFile(fname);
            map.put(file.getName(),txt.toLowerCase());
        }

        HashMap<String,Integer> tokens = sort(getToken(map));

        int total = 0, unique = 0, onlyonce = 0, tokensthirty=0,ct=0;

        for(int count: tokens.values()){
            total+=count;
            if(count==1)
                onlyonce+=1;
            ct+=1;
            if(ct<=30)
                tokensthirty+=count;
        }
        ct = 0;

        for(String key:tokens.keySet()){
            for(int i=0;i<tokens.get(key);i++) {
                writer1.append(key + "\n");
            }
            writer2.append(key+"\t"+tokens.get(key)+"\n");
            unique+=1;
            ct+=1;
            if(ct<=30)
                frequents.add(key);
        }

        writer1.close();
        writer2.close();

        System.out.println("\n\n\t\t\t\t****TOKEN STATISTICS****\n");
        System.out.println("\t\tSr. No.\tInformation\t\t\t\tFrequency\n\t\t------------------------------------------------------");
        System.out.println("\t\t1.\tNo. of tokens\t\t\t\t"+total);
        System.out.println("\t\t2.\tNo. of unique tokens\t\t\t"+unique);
        System.out.println("\t\t3.\tNo. of tokens that appear only once\t"+onlyonce);
        System.out.println("\t\t4.\t30 most frequent tokens\t\t\t"+tokensthirty+"\n");
        for(String item:frequents)
            System.out.println("\t\t\t- "+item+"\t\t\t\t\t"+tokens.get(item));
        System.out.print("\n\t\t5.\tAverage word tokens per document\t");
        System.out.printf("%.4f",(double)total/1400);
        System.out.println();
        long stop = System.currentTimeMillis();
        long elapsedTime = stop - start;
        System.out.println("Total time elapsed = "+elapsedTime+"ms");
    }
}
