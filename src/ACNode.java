import java.util.Hashtable;

public class ACNode {

    public Hashtable children;
    public boolean leaf;
    public int parent;
    public char parentChar;
    public int suffixLink;
    public int endWordLink;
    public int wordID;



    public ACNode(){
        children = new Hashtable();
        leaf = false;
        parent = -1;
        suffixLink = -1;
        wordID = -1;
        endWordLink = -1;
    }
}
