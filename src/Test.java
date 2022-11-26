import java.io.File;
import java.io.FileNotFoundException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Test {

    public static void main(String[] args) throws FileNotFoundException {

        Scanner user = new Scanner(System.in);
        System.out.print("Enter the input text file number you wish to use (1, 2, 3): ");

        String fileName = "";

        try{
            switch(user.nextInt()){
                case 1:
                    fileName = "input.txt";
                    System.out.println("input.txt selected ... running pattern matching\n");
                    break;
                case 2:
                    fileName = "input2.txt";
                    System.out.println("input2.txt selected ... running pattern matching\n");
                    break;
                case 3:
                    fileName = "input3.txt";
                    System.out.println("input3.txt selected ... running pattern matching\n");
                    break;
                default:
                    fileName = "input.txt";
                    System.out.println("Invalid input. Default of input.txt selected ... running pattern matching\n");
                    break;
            }
        }catch (InputMismatchException ex){
            System.out.println("Invalid input. Default of input.txt selected ... running pattern matching");
        }


        File file = new File(fileName);

        Scanner input = new Scanner(file);
        StringBuilder parser = new StringBuilder();

        int bmTotal = 0;
        int kmpTotal = 0;
        int acTotal = 0;

        while (input.hasNextLine()){
            parser.append(input.nextLine());
            String pattern = parser.substring(0, parser.indexOf(","));
            String text = parser.substring(parser.indexOf(",")+1, parser.length());

            System.out.println("Pattern: " + pattern + "\nText: " + text);

            Search bm = new Search(text, pattern, 1);
            System.out.println("Key Comparisons: " + bm.keyComparisons);
            Search kmp = new Search(text, pattern, 2);
            System.out.println("Key Comparisons: " + kmp.keyComparisons);
            Search ac = new Search(text, pattern, 3);
            System.out.println("Key Comparisons: " + ac.keyComparisons + "\n\n");

            bmTotal += bm.keyComparisons;
            kmpTotal += kmp.keyComparisons;
            acTotal += ac.keyComparisons;

            parser.delete(0, parser.length());
        }

        System.out.println("\nTotal Key Comparisons for " + fileName + ": ");
        System.out.println("BM: " + bmTotal);
        System.out.println("KMP Total: " + kmpTotal);
        System.out.println("AC Total: " + acTotal);

    }

}
