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
    public int buildMatchingMachine(String arr[], int k){
        // Initialize all values in output function as 0.

        Arrays.fill(out, 0);


        // Initialize all values in goto function as -1.

        for(int i = 0; i < MAXS; i++)

            Arrays.fill(g[i], -1);


        // Initially, we just have the 0 state

        int states = 1;


        // Convalues for goto function, i.e., fill g[][]

        // This is same as building a Trie for arr[]

        for(int i = 0; i < k; ++i)

        {

            String word = arr[i];

            int currentState = 0;


            // Insert all characters of current

            // word in arr[]

            for(int j = 0; j < word.length(); ++j)

            {

                int ch = word.charAt(j) - 'a';


                // Allocate a new node (create a new state)

                // if a node for ch doesn't exist.

                if (g[currentState][ch] == -1)

                    g[currentState][ch] = states++;


                currentState = g[currentState][ch];

            }


            // Add current word in output function

            out[currentState] |= (1 << i);

        }


        // For all characters which don't have

        // an edge from root (or state 0) in Trie,

        // add a goto edge to state 0 itself

        for(int ch = 0; ch < MAXC; ++ch)

            if (g[0][ch] == -1)

                g[0][ch] = 0;


        // Now, let's build the failure function

        // Initialize values in fail function

        Arrays.fill(f, -1);


        // Failure function is computed in

        // breadth first order

        // using a queue

        Queue<Integer> q = new LinkedList<>();


        // Iterate over every possible input

        for(int ch = 0; ch < MAXC; ++ch)

        {



            // All nodes of depth 1 have failure

            // function value as 0. For example,

            // in above diagram we move to 0

            // from states 1 and 3.

            if (g[0][ch] != 0)

            {

                f[g[0][ch]] = 0;

                q.add(g[0][ch]);

            }

        }


        // Now queue has states 1 and 3

        while (!q.isEmpty())

        {



            // Remove the front state from queue

            int state = q.peek();

            q.remove();


            // For the removed state, find failure

            // function for all those characters

            // for which goto function is

            // not defined.

            for(int ch = 0; ch < MAXC; ++ch)

            {



                // If goto function is defined for

                // character 'ch' and 'state'

                if (g[state][ch] != -1)

                {



                    // Find failure state of removed state

                    int failure = f[state];


                    // Find the deepest node labeled by proper

                    // suffix of String from root to current

                    // state.

                    while (g[failure][ch] == -1)

                        failure = f[failure];


                    failure = g[failure][ch];

                    f[g[state][ch]] = failure;


                    // Merge output values

                    out[g[state][ch]] |= out[failure];


                    // Insert the next level node

                    // (of Trie) in Queue

                    q.add(g[state][ch]);

                }

            }

        }

        return states;
    }

    public int findNextState(int currentState, char nextInput){
        int answer = currentState;

        int ch = nextInput - 'a';


        // If goto is not defined, use

        // failure function

        while (g[answer][ch] == -1)

            answer = f[answer];


        return g[answer][ch];
    }

    public void ACSearch(String[] arr, int k, String text){
// Preprocess patterns.

        // Build machine with goto, failure

        // and output functions

        buildMatchingMachine(arr, k);


        // Initialize current state

        int currentState = 0;


        // Traverse the text through the

        // built machine to find all

        // occurrences of words in arr[]

        for(int i = 0; i < text.length(); ++i)

        {

            currentState = findNextState(currentState,

                    text.charAt(i));


            // If match not found, move to next state

            if (out[currentState] == 0)

                continue;


            // Match found, print all matching

            // words of arr[]

            // using output function.

            for(int j = 0; j < k; ++j)

            {

                if ((out[currentState] & (1 << j)) > 0)

                {

                    System.out.print("Word " +  arr[j] +

                            " appears from " +

                            (i - arr[j].length() + 1) +

                            " to " +  i + "\n");

                }

            }

        }
    }



}
