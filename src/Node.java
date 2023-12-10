public class Node {
    String text;
    Node no;
    Node yes;

    public Node(String text){
        this.text = text;
        no = null;
        yes = null;
    }

    //mutator methods for nodes
    public void setText(String text){
        this.text = text;
    }
    public String getText(){
        return text;
    }
    public void setNo(Node node){
        no = node;
    }
    public Node getNo(){
        return no;
    }
    public void setYes(Node node){
        yes = node;
    }
    public Node getYes(){
        return yes;
    }

    //determines if a node is a question or not
    public boolean isQuestion(){
        if(no == null && yes == null)
            return false;
        else
            return true;
    }

    //toggles yes or no buttons and text field
    public void ynView(gameFrame game){
        game.getYes().setVisible(true);
        game.getNo().setVisible(true);
        game.getEnter().setVisible(false);
        game.getInput().setVisible(false);
    }
    public void textView(gameFrame game){
        game.getYes().setVisible(false);
        game.getNo().setVisible(false);
        game.getEnter().setVisible(true);
        game.getInput().setVisible(true);
    }

    //asks a yes or no question if the node is a question or makes a guess if there is a leaf
    public void query(gameFrame game) throws InterruptedException {
        if(this.isQuestion()) {
            game.setMessage(this.text);
            ynView(game);
            game.getYNLatch().await();
            if (game.getData().equals("GAME_YES")) {
                game.resetYNLatch();
                yes.query(game); //ask a question about the yes child if the answer is yes
            } else if (game.getData().equals("GAME_NO")) {
                game.resetYNLatch();
                no.query(game); //ask a question about the no child if the answer is no
            } else {
                throw new RuntimeException();
            }
        }
        else{ //make a guess if this is not a y/n question
            this.onQueryObject(game);
        }
    }

    //makes a guess on a leaf node
    public void onQueryObject(gameFrame game) throws InterruptedException {
        game.setMessage("Are you thinking of " + this.text + "?");
        ynView(game);
        game.getYNLatch().await();
        if(game.getData().equals("GAME_YES")) {
            game.resetYNLatch();
            game.setMessage("I win!");
        } else if (game.getData().equals("GAME_NO")) {
            game.resetYNLatch();
            updateTree(game); //if the user wins, ask them to enter what they were thinking of
        } else{
            throw new RuntimeException();
        }
    }

    //updates a tree with a new leaf node if the user wins
    private void updateTree(gameFrame game) throws InterruptedException {
        //Get the user's item
        game.setMessage("You win! What were you thinking of?");
        textView(game);
        game.getEnterLatch().await();
        String input = game.getData();
        game.resetEnterLatch();

        //Get a question to distinguish the user's item from the current guess
        game.setMessage("Please enter a question to distinguish " + this.text + " from " + input + ".");
        game.getEnterLatch().await();
        String question = game.getData();
        game.resetEnterLatch();

        //Get the answer to the previous question to assign both options to child nodes
        game.setMessage("If you were thinking of " + input + ", what would the answer to that question be?");
        ynView(game);
        game.getYNLatch().await();
        if(game.getData().equals("GAME_YES")){
            game.resetYNLatch();
            this.no = new Node(this.text);
            this.yes = new Node(input);
        } else if (game.getData().equals("GAME_NO")){
            game.resetYNLatch();
            this.yes = new Node(this.text);
            this.no = new Node(input);
        } else{
            throw new RuntimeException();
        }
        game.setMessage("Thank you. Your thing was added.");
        this.setText(question);
    }
}
