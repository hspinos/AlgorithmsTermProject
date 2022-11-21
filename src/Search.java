import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class Search {

    public static final int BM = 1;
    public static final int KMP = 2;
    public static final int AC = 3;

    //BM
    public static int NO_OF_CHARS = 256;

    //AC
    public int MAXS = 500;
    public int MAXC = 26;
    public int[] out = new int[MAXS];
    public int[] f = new int[MAXS];
    public int[][] g = new int[MAXS][MAXC];

    public String inputText;
    public String inputPattern;

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
                ACSearch(patterns, patterns.length, text);
                break;
        }
    }


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
        int m = pat.length();
        int n = text.length();

        int lps[] = new int[m];
        int j = 0;

        computeLPSArray(pat, m, lps);

        int i = 0;
        while ((n - i) >= (m - i)){
            if (pat.charAt(j) == text.charAt(i)){
                j++;
                i++;
            }
            if (j == m){
                System.out.println("Found pattern at index " + (i - j));
                j = lps[j - 1];
            }
            else if (i < n && pat.charAt(j) != text.charAt(i)){
                if (j != 0){
                    j = lps[j - 1];
                }
                else {
                    i = i + 1;
                }
            }
        }
    }

    public void computeLPSArray(String pat, int m, int[] lps){
        int len = 0;
        int i = 0;
        lps[0] = 0;

        while (i < m){
            if (pat.charAt(i) == pat.charAt(len)){
                len++;
                lps[i] = len;
                i++;
            }
            else{
                if (len != 0){
                    len = lps[len - 1];
                }
                else{
                    lps[i] = len;
                    i++;
                }
            }
        }
    }

    //--------------------------Methods for Aho-Corasick-------------------------//
    public int buildMatchingMachine(String arr[], int k){
        Arrays.fill(out, 0);

        for (int i = 0; i < MAXS; i++){
            Arrays.fill(g[i], -1);
        }

        int states = 1;

        for (int i = 0; i < k; i++){
            String word = arr[i];
            int currentState = 0;

            for (int j = 0; j < word.length(); ++j){
                int ch = word.charAt(j) - 'a';

                if(g[currentState][ch] == -1){
                    g[currentState][ch] = states++;
                }

                currentState = g[currentState][ch];
            }

            out[currentState] |= (1 << i);
        }

        for(int ch = 0; ch < MAXC; ++ch){
            if (g[0][ch] == -1){
                g[0][ch] = 0;
            }
        }

        Arrays.fill(f, -1);

        Queue<Integer> q = new LinkedList<>();

        for (int ch = 0; ch < MAXC; ++ch){
            if (g[0][ch] != 0){
                f[g[0][ch]] = 0;
                q.add(g[0][ch]);
            }
        }

        while (!q.isEmpty()){
            int state = q.peek();
            q.remove();

            for (int ch = 0; ch < MAXC; ++ch){
                if (g[state][ch] != -1){
                    int failure = f[state];

                    while (g[failure][ch] == -1){
                        failure = f[failure];
                    }

                    failure = g[failure][ch];
                    f[g[state][ch]] |= out[failure];

                    q.add(g[state][ch]);
                }
            }
        }
        return states;
    }

    public int findNextState(int currentState, char nextInput){
        int answer = currentState;
        int ch = nextInput - 'a';

        while (g[answer][ch] == -1){
            answer = f[answer];
        }
        return g[answer][ch];
    }

    public void ACSearch(String[] arr, int k, String text){
        buildMatchingMachine(arr, k);

        int currentState = 0;

        for(int i = 0; i < text.length(); ++i){
            currentState = findNextState(currentState, text.charAt(i));

            if (out[currentState] == 0)
                continue;

            for(int j = 0; j < k; ++j){
                if ((out[currentState] & (1 << j)) > 0){
                    System.out.print("Word " +  arr[j] + " appears from " + (i - arr[j].length() + 1) + " to " +  i + "\n");
                }
            }
        }
    }



}
