import java.io.File;
import java.io.FileNotFoundException;
import java.util.Locale;
import java.util.Scanner;

public class Test {

    public static void main(String[] args) throws FileNotFoundException {

        File file = new File("input.txt");

        Scanner input = new Scanner(file);
        StringBuilder parser = new StringBuilder();

        int bmTotal = 0;
        int kmpTotal = 0;
        int acTotal = 0;

        while (input.hasNextLine()){
            parser.append(input.nextLine());
            String pattern = parser.substring(0, parser.indexOf(","));
            String text = parser.substring(parser.indexOf(",")+1, parser.length());

            Search bm = new Search(text, pattern, 1);
            System.out.println("Key Comparisons: " + bm.keyComparisons);
            Search kmp = new Search(text, pattern, 2);
            System.out.println("Key Comparisons: " + kmp.keyComparisons);
            Search ac = new Search(text, pattern, 3);
            System.out.println("Key Comparisons: " + ac.keyComparisons);

            bmTotal += bm.keyComparisons;
            kmpTotal += kmp.keyComparisons;
            acTotal += ac.keyComparisons;

            parser.delete(0, parser.length());
        }

        System.out.println("\nTotal Key Comparisons: ");
        System.out.println("BM: " + bmTotal);
        System.out.println("KMP Total: " + kmpTotal);
        System.out.println("AC Total: " + acTotal);

    }

}
