import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class Driver {

    static Tree tree;
    static int counter;

    //Basic function of the game
    public static void main(String[] args) throws InterruptedException {
        //Create the game frame
        gameFrame game = new gameFrame();

        //Check if there is an existing tree saved, start the game using a save file or a new tree
        File treeFile = new File("saveTree.txt");
        if(treeFile.exists()){
            if(loadSavedGame(game)){
                try{
                    tree = loadedTree();
                } catch (FileNotFoundException e){
                    System.out.println("Error: File could not be loaded.");
                    startNewGame(game);
                }
            } else {
                startNewGame(game);
            }
        } else
            startNewGame(game);
        System.out.println("\nStarting the game.");
        tree.query(game);

        //ask the user if they want to play again and continue until they choose no.
        while(playAgain(game)){
            System.out.println();
            tree.query(game);
        }

        //When the game is closed, save the current tree into the save slot before the program ends
        try{
            tree.save();
        } catch(IOException e){
            System.out.println("Error: File could not be saved.");
        }
        System.exit(0);
    }

    //Initializes a new tree from scratch
    static void startNewGame(gameFrame game) throws InterruptedException {
        System.out.println("Starting new game.");

        //Ask the user for the root node question
        game.setMessage("Enter a yes or no question about a thing.");
        game.getYes().setVisible(false);
        game.getNo().setVisible(false);
        game.getEnter().setVisible(true);
        game.getInput().setVisible(true);

        game.getEnterLatch().await();
        String question = game.getData();
        System.out.println("Question: " + question);
        game.resetEnterLatch();

        //Ask the user for the data for the root node's yes node.
        game.setMessage("Enter a guess if the response is yes.");
        game.getEnterLatch().await();
        String yesGuess = game.getData();
        System.out.println("yesGuess: " + yesGuess);
        game.resetEnterLatch();

        //Do the same for the no node
        game.setMessage("Enter a guess if the response is no.");
        game.getEnterLatch().await();
        String noGuess = game.getData();
        System.out.println("noGuess: " + noGuess);
        game.resetEnterLatch();

        //initialize the tree with the responses
        tree = new Tree(question, yesGuess, noGuess);
    }

    //Asks the user if they want to play again
    static boolean playAgain(gameFrame game) throws InterruptedException {
        game.getYes().setVisible(false);
        game.getNo().setVisible(false);
        wait(1000);
        game.setMessage("Play again?");

        game.getYes().setVisible(true);
        game.getNo().setVisible(true);
        game.getEnter().setVisible(false);
        game.getInput().setVisible(false);

        game.getYNLatch().await();
        String input = game.getData();
        if(input.equals("GAME_YES")) {
            game.resetYNLatch();
            return true;
        } else if(input.equals("GAME_NO")) {
            game.resetYNLatch();
            return false;
        } else {
            throw new RuntimeException();
        }
    }

    //Allows calling wait(x amount of milliseconds) to pause for consecutive messages in the game
    static void wait(int timeInMilliseconds){
        try {
            Thread.sleep(timeInMilliseconds);
        }
        catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    //returns a tree loaded from the save file in the project folder
    static Tree loadedTree() throws FileNotFoundException {
        //Read the serialized tree from existing text file
        File saveTree = new File("saveTree.txt");
        Scanner treeReader = new Scanner(saveTree);
        String serializedTree = treeReader.nextLine();
        treeReader.close();
        System.out.println("Loaded tree.");

        //If the file is empty, return null (This should never be the case within the parameters of this program)
        if(serializedTree == null)
            return null;

        //Split the serialized tree string into an array of serialized nodes
        counter = 0;
        String[] serializedArray = serializedTree.split(",");

        //Assemble the tree starting at the root by de-serializing the strings
        Node root = deserialize(serializedArray);
        Tree newTree = new Tree(root.getText(), root.getYes(), root.getNo());
        counter = 0;

        //return the loaded tree
        return newTree;
    }

    //recursive method to deserialize a node and then all of its children
    static Node deserialize(String[] sTree){
        //base case for leaf node children (serialized as "#")
        if(sTree[counter].equals("#"))
            return null;

        //Set the current node as a root and build a tree with any children
        Node root = new Node(sTree[counter]);
        counter++;
        root.setYes(deserialize(sTree));
        counter++;
        root.setNo(deserialize(sTree));

        //return the current node to be appended to the tree
        return root;
    }

    //ask the user to load the saved game or wipe the save file and start from scratch
    static boolean loadSavedGame(gameFrame game) throws InterruptedException {
        game.setMessage("Existing game found. Load saved game? (selecting no will result in deletion of the saved game)");

        game.getYes().setVisible(true);
        game.getNo().setVisible(true);
        game.getEnter().setVisible(false);
        game.getInput().setVisible(false);

        game.getYNLatch().await();
        String input = game.getData();
        if(input.equals("GAME_YES")) {
            game.resetYNLatch();
            return true;
        } else if(input.equals("GAME_NO")) {
            game.resetYNLatch();
            return false;
        } else {
            throw new RuntimeException();
        }
    }


}
