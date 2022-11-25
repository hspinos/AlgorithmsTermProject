import java.io.File;
import java.io.FileNotFoundException;
import java.util.Locale;
import java.util.Scanner;

public class Test {

    public static void main(String[] args) throws FileNotFoundException {

        File file = new File("input.txt");

        Scanner input = new Scanner(file);
        StringBuilder parser = new StringBuilder();

        while (input.hasNextLine()){
            parser.append(input.nextLine());
            String pattern = parser.substring(0, parser.indexOf(","));
            String text = parser.substring(parser.indexOf(",")+1, parser.length());

            Search bm = new Search(text, pattern, 1);
            //Search kmp = new Search(text, pattern, 2);
            Search ac = new Search(text, pattern, 3);

            parser.delete(0, parser.length());
        }

    }

}
