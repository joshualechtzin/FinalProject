import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Tree {
    Node root;

    //Constructor for a new game (No child nodes on the root, only their future data)
    public Tree(String question, String yesGuess, String noGuess){
        this.root = new Node(question);
        this.root.setYes(new Node(yesGuess));
        this.root.setNo(new Node(noGuess));
    }

    //Constructor for a saved tree (root already has children)
    public Tree(String question, Node yesNode, Node noNode){
        this.root = new Node(question);
        this.root.setYes(yesNode);
        this.root.setNo(noNode);
    }

    //Initial query call from the main method on the root of the tree (from user input at the start)
    public void query(gameFrame game) throws InterruptedException {
        root.query(game);
    }


    //saves a tree in a file in the project folder
    public void save() throws IOException{
        //serializes the tree into a string
        String thisTree = this.serialize();

        //save the serialized tree string into a text file
        File saveTree = new File("saveTree.txt");
        if (saveTree.createNewFile())
            System.out.println("File created: " + saveTree.getName());
        else
            System.out.println("File already exists.");

        FileWriter treeWriter = new FileWriter("saveTree.txt");
        treeWriter.write(thisTree);
        treeWriter.close();
        System.out.println("File saved.");

    }

    //Serialize a tree into a string using pre-order traversal (root-down, left-to-right)
    public String serialize(){
        //if the tree is empty, the string is empty
        if(root == null){
            return null;
        }

        //Add the root to a stack
        Stack<Node> stack = new Stack<Node>();
        stack.push(root);

        List<String> list = new ArrayList<String>();
        while(!stack.isEmpty()){
            Node thisNode = stack.pop(); //get the next node from the stack, remove it from the stack now that it is counted

            if(thisNode == null) //If this node is a leaf, assign it "#"
                list.add("#");
            else{
                list.add(thisNode.getText()); //add the current node's data to the list to be concatenated into a string
                stack.push(thisNode.getNo()); //add the no node child (left node) to the stack
                stack.push(thisNode.getYes()); //add the yes node child (right node) to the stack
            }
        }

        //return the serialized tree string, with nodes separated by commas
        return String.join(",", list);
    }
}
