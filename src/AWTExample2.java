// importing Java AWT class
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// class AWTExample2 directly creates instance of Frame class
class AWTExample2 {

    // initializing using constructor
    AWTExample2() {

        // creating a Frame
        Frame f = new Frame();

        // creating a Label
        Label l = new Label("Question");

        // creating a Button
        Button yes = new Button("Yes");
        Button no = new Button("No");
        Button enter = new Button("Enter");


        // creating a TextField
        TextField t = new TextField();

        // setting position of above components in the frame
        l.setBounds(20, 60, 240, 20);
        l.setAlignment(Label.CENTER);
        yes.setBounds(20, 100, 80, 30);
        no.setBounds(100, 100, 80, 30);
        t.setBounds(20,150, 160, 20);
        enter.setBounds(180,150,80,20);

        //add action listener to buttons
        yes.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                l.setText("Yes");
            }
        });
        no.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                l.setText("No");
            }
        });
        enter.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                l.setText(t.getText());
                if(l.getText().length() > 10)
                    l.setAlignment(Label.LEFT);
                else
                    l.setAlignment(Label.CENTER);
                t.setText("");
            }
        });

        // adding components into frame
        f.add(l);
        f.add(yes);
        f.add(no);
        f.add(t);
        f.add(enter);

        // frame size 300 width and 300 height
        f.setSize(400,300);

        // setting the title of frame
        f.setTitle("20 Questions");

        // no layout
        f.setLayout(null);

        // setting visibility of frame
        f.setVisible(true);
    }

    // main method  
    public static void main(String args[]) {

        // creating instance of Frame class
        AWTExample2 awt_obj = new AWTExample2();

    }

}