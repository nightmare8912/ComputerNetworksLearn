import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

class chat_swing_client extends JFrame implements ActionListener
{
    JTextArea messageHistTA;
    JTextField messageTF;
    JButton sendBTN;

    ObjectInputStream input;
    ObjectOutputStream output;
    Socket connection;
    int port = 8888;
    String serverIP;


    chat_swing_client(String s)
    {
        setBounds(0, 0, 600, 400);
        setLayout(null);
        serverIP = s;
        
        JLabel headLBL = new JLabel("CHAT CLIENT");
        headLBL.setBounds(250, 10, 100, 50);
        add(headLBL);

        messageHistTA = new JTextArea();
        messageHistTA.setBounds(20, 80, 540, 200);
        messageHistTA.setEditable(false);
        add(messageHistTA);

        messageTF = new JTextField();
        messageTF.setBounds(20, 300, 470, 50);
        add(messageTF);

        sendBTN = new JButton("SEND");
        sendBTN.setBounds(490, 300, 70, 50);
        sendBTN.addActionListener(this);
        add(sendBTN);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent evt)
    {
        if (evt.getSource() == sendBTN)
        {
            String message = messageTF.getText();
            if (message.equals(""))
                JOptionPane.showMessageDialog(null, "Please enter a valid message!");
            else
            {
                messageHistTA.append("\n" + message);
                try
                {
                    output.writeObject(message);
                }
                catch(IOException ioE)
                {
                    ioE.printStackTrace();
                }
            }
        }
    }

    public void startRunning()
    {
        try
        {

            try
            {
                connection = new Socket(InetAddress.getByName(serverIP), port);
            }
            catch(IOException ioE2)
            {
                JOptionPane.showMessageDialog(null, "SERVER MIGHT BE DOWN");
            }

            output = new ObjectOutputStream(connection.getOutputStream());
            input = new ObjectInputStream(connection.getInputStream());
            whileChatting();
            
        }
        catch(IOException ioE)
        {
            ioE.printStackTrace();
        }
    }

    public void whileChatting()
    {
        do
        {
            try
            {
                messageHistTA.append("\n" + input.readObject());
            }
            catch(Exception e)
            {   
                e.printStackTrace();
            }
        } while(true);
    }
}

class chat_client
{
    public static void main(String[] args)
    {
        chat_swing_client client = new chat_swing_client("127.0.0.1");
        client.startRunning();
    }
}