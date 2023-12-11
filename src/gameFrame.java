import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.CountDownLatch;

public class gameFrame {

    private Frame f;
    private Label message;
    private TextField input;
    private String data;
    private Button enter;
    private Button yes;
    private Button no;

    private CountDownLatch EnterLatch;
    private CountDownLatch ynLatch;


    public gameFrame(){
        f = new Frame();
        data = ""; //Stores data from button press (either y/n or user input from text field)
        EnterLatch = new CountDownLatch(1);
        ynLatch = new CountDownLatch(1);
        f.addWindowListener (new WindowAdapter() {
            public void windowClosing (WindowEvent e) {
                System.exit(0);
            }
        });

        //Game message
        message = new Label("Enter a question about a thing.");

        //Yes, no, and enter buttons
        yes = new Button("Yes");
        no = new Button("No");
        enter = new Button("Enter");


        //Text field with large letters for user input
        input = new TextField();
        input.setFont(new Font("Arial", Font.PLAIN, 30));

        //layout of frame components
        message.setBounds(40, 120, 560, 40);
        message.setAlignment(Label.CENTER);
        yes.setBounds(40, 200, 160, 60);
        no.setBounds(200, 200, 160, 60);
        input.setBounds(40,300, 320, 40);
        enter.setBounds(360,300,160,40);

        //Action listeners for button press
        yes.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                setData("GAME_YES");
                ynLatch.countDown(); //countDown to trigger the continuation of the program after wait() is called (waiting for button to be pressed)
            }
        });
        no.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                setData("GAME_NO");
                ynLatch.countDown();
            }
        });
        enter.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                if(input.getText().isEmpty()){}
                else {
                    setData(input.getText());
                    input.setText("");
                    EnterLatch.countDown();
                }
            }
        });

        f.add(message);
        f.add(yes);
        f.add(no);
        f.add(input);
        f.add(enter);

        // frame size 800 width and 600 height (4:3)
        f.setSize(800,600);

        f.setTitle("Guessing Game");
        f.setLayout(null);

        // default visibility of frame and components at the start
        f.setVisible(true);
        yes.setVisible(false);
        no.setVisible(false);
    }


    //mutator methods on all gameFrame components
    public void setMessage(String m){
        message.setText(m);
    }

    public String getData(){
        return data;
    }

    public void setData(String d){
        data = d;
    }

    public Button getEnter(){
        return enter;
    }
    public TextField getInput(){
        return input;
    }

    public CountDownLatch getEnterLatch(){
        return EnterLatch;
    }
    public void resetEnterLatch(){
        EnterLatch = new CountDownLatch(1);
    }

    public CountDownLatch getYNLatch(){
        return ynLatch;
    }
    public void resetYNLatch(){
        ynLatch = new CountDownLatch(1);
    }

    public Button getYes(){
        return yes;
    }
    public Button getNo(){
        return no;
    }
}
