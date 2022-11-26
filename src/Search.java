import java.util.*;

public class Search {

    public static final int BM = 1;
    public static final int KMP = 2;
    public static final int AC = 3;

    //BM
    public static int NO_OF_CHARS = 256;

    //AC
    public String inputText;
    public String inputPattern;
    public List<ACNode> trie;
    public List<Integer> wordsLength;
    int size = 0;
    int root = 0;

    public int keyComparisons = 0;


    //-----------------------------Constructors-----------------------------------//
    public Search(String text, String pattern, int alg){
        this.inputText = text;
        this.inputPattern = pattern;

        switch (alg){
            case BM:
                System.out.println("----------Running with BM---------");
                BMSearch(text, pattern);
                break;
            case KMP:
                System.out.println("----------Running with KMP---------");
                KMPSearch(pattern, text);
                break;
            case AC:
                System.out.println("----------Running with AC---------");
                String[] patterns = pattern.split(" ");
                trie = new ArrayList<ACNode>();
                wordsLength = new ArrayList<Integer>();
                init();
                for (int i = 0; i < patterns.length; i++){
                    AddString(patterns[i], i);
                }
                PrepareAho();
                int countOfMatches = ProcessString(text);
                //System.out.println("AC num matches: " + countOfMatches);
                //ACSearch(patterns, patterns.length, text);
                break;
        }
    }

    private void init(){
        trie.add(new ACNode());
        size++;
    }
    //----------------------------------------------------------------------------//


    //-----------------------------Methods for Boyer Moore------------------------//
    public int max(int a, int b){
        return (a > b)? a: b;
    }

    public void badCharHeuristic(String str, int size, int[] badchar){
        for (int i = 0; i < NO_OF_CHARS; i++){
            badchar[i] = -1;
        }

        for (int i = 0; i < size; i++){
            badchar[(int) str.charAt(i)] = i;
        }
    }

    public void BMSearch(String txt, String pat){
        int m = pat.length();
        int n = txt.length();

        int badchar[] = new int[NO_OF_CHARS];

        badCharHeuristic(pat, m, badchar);

        int s = 0;

        while(s <= (n-m)){
            int j = m - 1;

            while(j >= 0 && pat.charAt(j) == txt.charAt(s+j)){
                keyComparisons++;
                j--;
            }

            if (j < 0){
                System.out.println("Patterns occur at shift = " + s);
                s += (s+m < n)? m-badchar[txt.charAt(s-m)] : 1;
            }
            else{
                s += max(1, j - badchar[txt.charAt(s+j)]);
            }
        }
    }

    //----------------------Methods for Knuth Morris Pratt------------------------//
    public void KMPSearch(String pat, String text){
        int M = pat.length();
        int N = text.length();

        // create lps[] that will hold the longest
        // prefix suffix values for pattern
        int lps[] = new int[M];
        int j = 0; // index for pat[]

        // Preprocess the pattern (calculate lps[]
        // array)
        computeLPSArray(pat, M, lps);

        int i = 0; // index for txt[]
        while ((N - i) >= (M - j)) {
            if (pat.charAt(j) == text.charAt(i)) {
                keyComparisons++;
                j++;
                i++;
            }
            if (j == M) {
                System.out.println("Found pattern "
                        + "at index " + (i - j));
                j = lps[j - 1];
            }

            // mismatch after j matches
            else if (i < N && pat.charAt(j) != text.charAt(i)) {
                keyComparisons++;
                // Do not match lps[0..lps[j-1]] characters,
                // they will match anyway
                if (j != 0)
                    j = lps[j - 1];
                else
                    i = i + 1;
            }
        }
    }

    public void computeLPSArray(String pat, int m, int[] lps){
        // length of the previous longest prefix suffix
        int len = 0;
        int i = 1;
        lps[0] = 0; // lps[0] is always 0

        // the loop calculates lps[i] for i = 1 to M-1
        while (i < m) {
            if (pat.charAt(i) == pat.charAt(len)) {
                len++;
                lps[i] = len;
                i++;
            }
            else // (pat[i] != pat[len])
            {
                // This is tricky. Consider the example.
                // AAACAAAA and i = 7. The idea is similar
                // to search step.
                if (len != 0) {
                    len = lps[len - 1];

                    // Also, note that we do not increment
                    // i here
                }
                else // if (len == 0)
                {
                    lps[i] = len;
                    i++;
                }
            }
        }
    }

    //--------------------------Methods for Aho-Corasick-------------------------//
    public void AddString(String s, int wordId){
        int curNode = root;
        for (int i = 0; i < s.length(); ++i){
            char c = s.charAt(i);

            if (!trie.get(curNode).children.containsKey(c)){
                trie.add(new ACNode());
                trie.get(size).suffixLink = -1;
                trie.get(size).parent = curNode;
                trie.get(size).parentChar = c;
                trie.get(curNode).children.put(c, size);
                size++;
            }
            curNode = (int)trie.get(curNode).children.get(c);
        }

        trie.get(curNode).leaf = true;
        trie.get(curNode).wordID = wordId;
        wordsLength.add(s.length());
    }

    public void PrepareAho(){
        Queue<Integer> nodeQueue = new LinkedList<>();
        nodeQueue.add(root);
        while (!nodeQueue.isEmpty()){
            int curNode = nodeQueue.poll();
            CalcSuffLink(curNode);

            for(Object c : trie.get(curNode).children.keySet()){
                nodeQueue.add((int)trie.get(curNode).children.get(c));
            }
        }
    }

    public void CalcSuffLink(int node){
        if (node == root) {
            trie.get(node).suffixLink = root;
            trie.get(node).endWordLink = root;
            return;
        }
        if (trie.get(node).parent == root){
            trie.get(node).suffixLink = root;
            if (trie.get(node).leaf){
                trie.get(node).endWordLink = node;
            }
            else{
                trie.get(node).endWordLink = trie.get(trie.get(node).suffixLink).endWordLink;
            }
            return;
        }
        int curBetterNode = trie.get(trie.get(node).parent).suffixLink;
        char chNode = trie.get(node).parentChar;

        while (true){
            if (trie.get(curBetterNode).children.containsKey(chNode)){
                trie.get(node).suffixLink = (int)trie.get(curBetterNode).children.get(chNode);
                break;
            }

            if (curBetterNode == root){
                trie.get(node).suffixLink = root;
                break;
            }
            curBetterNode = trie.get(curBetterNode).suffixLink;
        }

        if (trie.get(node).leaf){
            trie.get(node).endWordLink = node;
        }
        else{
            trie.get(node).endWordLink = trie.get(trie.get(node).suffixLink).endWordLink;
        }
    }

    public int ProcessString(String text){
        int currentState = root;
        int result = 0;

        for (int j = 0; j < text.length(); j++){
            while (true){
                if (trie.get(currentState).children.containsKey(text.charAt(j))){
                    keyComparisons++;
                    currentState = (int)trie.get(currentState).children.get(text.charAt(j));
                    break;
                }

                if (currentState == root){
                    break;
                }
                currentState = trie.get(currentState).suffixLink;
            }
            int checkState = currentState;

            while (true){
                checkState = trie.get(checkState).endWordLink;

                if (checkState == root) break;

                result++;
                int indexOfMatch = j + 1 - wordsLength.get(trie.get(checkState).wordID);
                System.out.println("Pattern occurs at shift = " + indexOfMatch);
                checkState = trie.get(checkState).suffixLink;
            }
        }
        return result;
    }
}
