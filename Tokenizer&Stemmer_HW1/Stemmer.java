import java.io.*;
import java.util.*;

public class Stemmer {

    private char[] b;
    private int i, iend, j, k;
    private static final int INC = 50;

    public Stemmer(){
        b = new char[INC];
        i=0;
        iend =0;
    }

    public void add(char ch){
        if(i==b.length){
            char[] newb = new char[i+INC];
            for(int c=0; c<i; c++)
                newb[c] = b[c];
            b = newb;
        }
        b[i++] = ch;
    }

    public void add(char[] w, int wlen){
        if(i+wlen >= b.length){
            char[] newb = new char[i+wlen+INC];
            for(int c=0; c<i; c++)
                newb[c]  = b[c];
            b = newb;
        }
        for(int c=0;c<wlen;c++)
            b[i++] = w[c];
    }

    public String toString(){
        return new String(b,0,iend);
    }

    public int getResult(){
        return iend;
    }

    public char[] getBuffer(){
        return b;
    }

    public final boolean consonant(int i){
        switch(b[i]){
            case 'a': case 'e': case 'i': case 'o' : case'u' : return false;
            case 'y' : return (i == 0) || !consonant(i - 1);
            default: return true;
        }
    }

    private final int m(){
        int n=0;
        int i=0;
        while(true){
            if(i>j) return n;
            if(!consonant(i)) break; i++;
        }
        i++;
        while(true){
            while(true){
                if(i>j) return n;
                if(consonant(i)) break;
                i++;
            }
            i++;
            n++;
            while(true){
                if(i>j) return n;
                if(!consonant(i))    break;
                i++;
            }
            i++;
        }
    }

    private final boolean vowelCheck(){
        int i;
        for(i=0;i<=j;i++)
            if(!consonant(i))
                return true;
        return false;
    }

    private final boolean doubleconsonant(int j){
        if(j<1)
            return false;
        if(b[j] != b[j-1])
            return false;
        return consonant(j);
    }
    private final boolean cvcPattern(int i){
        if(i<2 || !consonant(i) || consonant(i-1) || !consonant(i-2))
            return false;
        else{
            final int ch = this.b[i];
            if(ch == 'w' || ch=='x' || ch=='y')
                return false;
        }
        return true;
    }

    private final boolean ends(final String s){
        final int l = s.length();
        final int o = this.k - l+1;
        if(o<0)
            return false;
        for(int i=0; i<1;i++){
            if(this.b[o+i] != s.charAt(i)){
                return false;
            }
        }
        this.j = this.k - l;
        return true;
    }

    private final void setto(String s)
    {  int l = s.length();
        int o = j+1;
        for (int i = 0; i < l; i++) b[o+i] = s.charAt(i);
        k = j+l;
    }

    private final void r(String s) { if (m() > 0) setto(s); }

    private final void step1()
    {  if (b[k] == 's')
    {  if (ends("sses")) k -= 2; else
    if (ends("ies")) setto("i"); else
    if (b[k-1] != 's') k--;
    }
        if (ends("eed")) { if (m() > 0) k--; } else
        if ((ends("ed") || ends("ing")) && vowelCheck())
        {  k = j;
            if (ends("at")) setto("ate"); else
            if (ends("bl")) setto("ble"); else
            if (ends("iz")) setto("ize"); else
            if (doubleconsonant(k))
            {  k--;
                {  int ch = b[k];
                    if (ch == 'l' || ch == 's' || ch == 'z') k++;
                }
            }
            else if (m() == 1 && cvcPattern(k)) setto("e");
        }
    }

    private final void step2() { if (ends("y") && vowelCheck()) b[k] = 'i'; }

    private final void step3() { if (k == 0) return; /* For Bug 1 */ switch (b[k-1])
    {
        case 'a': if (ends("ational")) { r("ate"); break; }
            if (ends("tional")) { r("tion"); break; }
            break;
        case 'c': if (ends("enci")) { r("ence"); break; }
            if (ends("anci")) { r("ance"); break; }
            break;
        case 'e': if (ends("izer")) { r("ize"); break; }
            break;
        case 'l': if (ends("bli")) { r("ble"); break; }
            if (ends("alli")) { r("al"); break; }
            if (ends("entli")) { r("ent"); break; }
            if (ends("eli")) { r("e"); break; }
            if (ends("ousli")) { r("ous"); break; }
            break;
        case 'o': if (ends("ization")) { r("ize"); break; }
            if (ends("ation")) { r("ate"); break; }
            if (ends("ator")) { r("ate"); break; }
            break;
        case 's': if (ends("alism")) { r("al"); break; }
            if (ends("iveness")) { r("ive"); break; }
            if (ends("fulness")) { r("ful"); break; }
            if (ends("ousness")) { r("ous"); break; }
            break;
        case 't': if (ends("aliti")) { r("al"); break; }
            if (ends("iviti")) { r("ive"); break; }
            if (ends("biliti")) { r("ble"); break; }
            break;
        case 'g': if (ends("logi")) { r("log"); break; }
    } }
    private final void step4() { switch (b[k])
    {
        case 'e': if (ends("icate")) { r("ic"); break; }
            if (ends("ative")) { r(""); break; }
            if (ends("alize")) { r("al"); break; }
            break;
        case 'i': if (ends("iciti")) { r("ic"); break; }
            break;
        case 'l': if (ends("ical")) { r("ic"); break; }
            if (ends("ful")) { r(""); break; }
            break;
        case 's': if (ends("ness")) { r(""); break; }
            break;
    } }

    private final void step5()
    {   if (k == 0) return;
        switch (b[k-1])
        {  case 'a': if (ends("al")) break; return;
            case 'c': if (ends("ance")) break;
                if (ends("ence")) break; return;
            case 'e': if (ends("er")) break; return;
            case 'i': if (ends("ic")) break; return;
            case 'l': if (ends("able")) break;
                if (ends("ible")) break; return;
            case 'n': if (ends("ant")) break;
                if (ends("ement")) break;
                if (ends("ment")) break;
                if (ends("ent")) break; return;
            case 'o': if (ends("ion") && j >= 0 && (b[j] == 's' || b[j] == 't')) break;
                if (ends("ou")) break; return;
            case 's': if (ends("ism")) break; return;
            case 't': if (ends("ate")) break;
                if (ends("iti")) break; return;
            case 'u': if (ends("ous")) break; return;
            case 'v': if (ends("ive")) break; return;
            case 'z': if (ends("ize")) break; return;
            default: return;
        }
        if (m() > 1) k = j;
    }

    private final void step6()
    {  j = k;
        if (b[k] == 'e')
        {  int a = m();
            if (a > 1 || a == 1 && !cvcPattern(k-1)) k--;
        }
        if (b[k] == 'l' && doubleconsonant(k) && m() > 1) k--;
    }
    public void stem()
    {  k = i - 1;
        if (k > 1) { step1(); step2(); step3(); step4(); step5(); step6(); }
        iend = k+1; i = 0;
    }
    public static HashMap<String, Integer> sortByValue(HashMap<String, Integer> hm)
    {
        List<Map.Entry<String, Integer>> list = new LinkedList<Map.Entry<String, Integer> >(hm.entrySet());

        Collections.sort(list, new Comparator<Map.Entry<String, Integer> >()
        {
            public int compare(Map.Entry<String, Integer> o1,  Map.Entry<String, Integer> o2)
            {
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });
        HashMap<String, Integer> temp = new LinkedHashMap<String, Integer>();
        for (Map.Entry<String, Integer> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }
    public static void main(String[] args) throws IOException
    {
        long startTime = System.currentTimeMillis();
        char[] w = new char[501];
        int no_distinct_stems=0, only_once=0,frequent_thirty=0,ct, total=0;
        Stemmer s = new Stemmer();
        HashMap<String, Integer> stems=new HashMap<>();
        List<String> frequents=new ArrayList<String>();
        BufferedWriter writer=new BufferedWriter(new FileWriter("stems.txt",true));
        try
        {
            FileInputStream in = new FileInputStream("tokens.txt");
            try
            {
                while(true)
                {  int ch = in.read();
                    if (Character.isLetter((char) ch))
                    {
                        int j = 0;
                        while(true)
                        {  ch = Character.toLowerCase((char) ch);
                            w[j] = (char) ch;
                            if (j < 500) j++;
                            ch = in.read();
                            if (!Character.isLetter((char) ch))
                            {
                                for (int c = 0; c < j; c++) s.add(w[c]);
                                s.stem();
                                {
                                    String u;
                                    u = s.toString();
                                    if (!stems.containsKey(u))
                                        stems.put(u,1);
                                    else
                                        stems.put(u,stems.get(u)+1);
                                }
                                break;
                            }
                        }
                    }
                    if (ch < 0) break;
                }
                stems=sortByValue(stems);
                ct=0;
                for (String str:stems.keySet())
                {
                    writer.append(str+"\t"+stems.get(str)+"\n");
                    no_distinct_stems+=1;
                    ct+=1;
                    if(ct<=30)
                        frequents.add(str);
                }
                writer.close();
                ct=0;
                for(int count:stems.values())
                {
                    total+=count;
                    if (count==1)
                        only_once+=1;
                    ct+=1;
                    if(ct<=30)
                        frequent_thirty+=count;
                }
                System.out.println("\n\n\t\t\t\t****STEM STATISTICS****\n");
                System.out.println("\t\tSr. No.\tInformation\t\t\t\tFrequency\n\t\t------------------------------------------------------");
                System.out.println("\t\t1.\tNo. of unique stems\t\t\t"+no_distinct_stems);
                System.out.println("\t\t2.\tNo. of stems that appear only once\t"+only_once);
                System.out.println("\t\t3.\t30 most frequent stems\t\t\t"+frequent_thirty+"\n");
                for(String item:frequents)
                    System.out.println("\t\t\t- "+item+"\t\t\t\t\t"+stems.get(item));
                System.out.printf("\t\t4.\tAverage number of stems per document\t\t\t%.4f\n",(double)total/1400);
                long stopTime = System.currentTimeMillis();
                long elapsedTime = stopTime - startTime;
                System.out.println("Total time elapsed = "+elapsedTime+"ms");
            }
            catch (IOException e)
            {
                System.out.println("error reading file");
            }
        }
        catch (FileNotFoundException e)
        {
            System.out.println("file not found");
        }
    }

}
