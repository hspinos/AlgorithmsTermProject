# AlgorithmsTermProject

The code presented in this project consists of 3 different pattern matching algorithm implementations.
The chosen three algorithms are

- Boyer Moore (a.k.a BM/bm)
- Knuth Morris Pratt (a.k.a KMP/kmp)
- Aho Corasick (a.k.a. AC/ac)

Search.java contains all of the relevant variables and methods needed to execute and measure
the performance of each algorithm implementation. A separate class ACNode.java exists to implement
the tree data structure required in the AC implementation. 

You will see that each algorithm is given its own sections for variables and methods respectively
denoted by dividers in Search.java.

    Usage Guide:
        
    In order to run pattern matching using the three algorithms, all the user
    needs to use is two files.
        - Test.java
        - input<1,2, or 3>.txt

    Inside of the input file the user should place each pattern/text pairing on an
    individual line seperated with a comma and a space as shown below:

        - <pattern>,_<text_input>

    Each new line will represent a different pattern/text pairing for which
    each algorithm will be ran on and measured. 

    Once the user has filled out the input file correctly and is satisfied with
    their test cases, they can run the code from the main method in Test.java.
    The program will prompt the user to specify which text file they wish to
    load input from.

        - Enter '1' for "input.txt"
        - Enter '2' for "input2.txt"
        - Enter '3' for "input3.txt"

    The program will then run all three pattern matching alogrithms for each 
    pattern/text pairing. After each algorithm run completion, the number of
    key comparisons made will be printed to the console. After the entire 
    input file has been processed, the total number of key comparisons
    made by each algorithm across all pattern/text pairings will be printed
    into the console as well.
