import javax.swing.*;
import java.awt.event.*;

class Swing extends JFrame implements 
{
    JTextArea oldMessageTA;
    JTextField newMessageTF;
    JButton clearBTN, sendBTN;

    Swing()
    {
        setBounds(0, 0, 600, 450);
        setLayout(null);
        setVisible(true);

        JLabel headLBL = new JLabel("SERVER");
        headLBL.setBounds(250, 40, 100, 30);
        add(headLBL);

        oldMessageTA = new JTextArea();
        oldMessageTA.setBounds(130, 100, 440, 170);
        oldMessageTA.setEditable(false);
        add(oldMessageTA);

        JLabel messageLBL = new JLabel("Message");
        messageLBL.setBounds(30, 300, 100, 30);
        add(messageLBL);

        newMessageTF = new JTextField();
        newMessageTF.setBounds(130, 300, 440, 30);
        add(newMessageTF);

        clearBTN = new JButton("CLEAR");
        clearBTN.setBounds(320, 350, 100, 30);
        add(clearBTN);

        sendBTN = new JButton("SEND");
        sendBTN.setBounds(470, 350, 100, 30);
        add(sendBTN);
    }
}

public class chat_server
{
    public static void main(String[] args)
    {
        new Swing();
    }
}