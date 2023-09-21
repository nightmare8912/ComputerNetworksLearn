import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

class chat_swing_server extends JFrame implements ActionListener
{
    JTextArea messageHistTA;
    JTextField messageTF;
    JButton sendBTN;

    ObjectOutputStream output;
    ObjectInputStream input;
    Socket connection;
    ServerSocket server;
    int totalClients = 50;
    int port = 8888;

    chat_swing_server()
    {
        setBounds(0, 0, 600, 400);
        setLayout(null);
        
        JLabel headLBL = new JLabel("CHAT SERVER");
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

    public void startRunning()
    {
        try
        {
            server = new ServerSocket(port, totalClients);
            while(true)
            {
                try
                {
                    connection = server.accept();
                    output = new ObjectOutputStream(connection.getOutputStream());
                    input = new ObjectInputStream(connection.getInputStream());
                    whileChatting();
                }
                catch(EOFException eofE)
                {

                }
            }
        }
        catch(IOException ioE)
        {
            ioE.printStackTrace();
        }
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
               try
               {
                    messageHistTA.append("\n" + message);
                    output.writeObject(message);
                    output.flush();
               }
               catch(IOException ioE)
               {
                    ioE.printStackTrace();
               }
            }
        }
    }

    public void whileChatting() throws IOException
    {
        String message = "";
        do
        {
            try
            {
                message = "" + input.readObject();
                messageHistTA.append("\n\t\t" + message);
            }
            catch(ClassNotFoundException cfe)
            {

            }
        }while(true);
    }
}

class chat_server
{
    public static void main(String[] args)
    {
        chat_swing_server server = new chat_swing_server();
        server.startRunning();
    }
}