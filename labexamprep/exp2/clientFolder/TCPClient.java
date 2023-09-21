import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import java.lang.*;

class chat_swing_client extends JFrame
{
    JTextArea messageHistTA;
    JTextField fileNameTF;
    JButton uploadBTN, downloadBTN;
    JList<String> fileList;
    String[] names = new String[100];

    int portNumber;
    String dirName, serverAddress;
    int size = 9022386;
    int len; // number of files received

    Socket clientSocket;
    InputStream inFromServer;
    OutputStream outToServer;
    BufferedInputStream bis;
    PrintWriter pw;

    chat_swing_client(String dirName, String serverAddress, int portNumber)
    {
        this.dirName = dirName;
        this.serverAddress = serverAddress;
        this.portNumber = portNumber;

        setBounds(0, 0, 600, 400);
        setLayout(null);
        
        JLabel headLBL = new JLabel("CHAT CLIENT");
        headLBL.setBounds(250, 10, 100, 50);
        add(headLBL);

        loadFilesFromDirectory();

        fileNameTF = new JTextField();
        fileNameTF.setBounds(20, 300, 330, 50);
        add(fileNameTF);

        uploadBTN = new JButton("UPLOAD");
        uploadBTN.setBounds(350, 300, 100, 50);
        uploadBTN.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(ActionEvent ae)
            {
                uploadFile();
            }
        });
        add(uploadBTN);

        downloadBTN = new JButton("DOWNLOAD");
        downloadBTN.setBounds(451, 300, 110, 50);
        downloadBTN.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(ActionEvent ae)
            {
                downloadFile();
            }
        });

        add(downloadBTN);
        setVisible(true);
    }

    public void loadFilesFromDirectory()
    {
        try
        {
            clientSocket = new Socket(serverAddress, portNumber);
            inFromServer = clientSocket.getInputStream();
            pw = new PrintWriter(clientSocket.getOutputStream(), true);
            outToServer = clientSocket.getOutputStream();
            ObjectInputStream oin = new ObjectInputStream(inFromServer);

            String s = (String) oin.readObject();
            System.out.println(s);

            len = Integer.parseInt((String) oin.readObject());
            System.out.println(len);

            String[] temp_names = new String[len];

            for (int i = 0; i < len; i++)
            {
                String fileName = (String) oin.readObject();
                System.out.println(fileName);
                names[i] = fileName;
                temp_names[i] = fileName;
            }

            fileList = new JList<>(temp_names);
            fileList.addMouseListener(new java.awt.event.MouseAdapter(){
                public void mouseClicked(MouseEvent click)
                {
                    getFileName(click);
                }
            });
        }
        catch(Exception ex)
        {
            fileList = new JList<>();
        }

        fileList.setBounds(20, 80, 540, 200);
        add(fileList);
    }

    public boolean uploadFile()
    {
        String fileToUpload = fileNameTF.getText();
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        // outToServer.write(fileToUpload);
        boolean fileExists = true;
        String path = dirName + "\\" + fileToUpload;
        System.out.println("File to upload:" + fileToUpload);
        try
        {
            fis = new FileInputStream(path);
            bis = new BufferedInputStream(fis);
        }

        catch(FileNotFoundException fnfe)
        {
            JOptionPane.showMessageDialog(null, "file was not found on server!!");
            fileExists = false;
            return false;
        }

        if (fileExists)
        {
            pw.println(fileToUpload);
            try
            {
                System.out.println("Upload begins!");
                sendBytes(bis, outToServer);
                
                System.out.println("completed!");

                boolean exists = false;

                for (int i = 0; i < len; i++)
                {
                    if (names[i].equals(fileToUpload))
                    {
                        exists = true;
                        break;
                    }
                }

                if (!exists)
                {
                    names[len] = fileToUpload;
                    len++;
                }

                String[] temp_names = new String[len];
                for (int i = 0; i < len; i++)
                    temp_names[i] = names[i];
                fileList.setListData(temp_names);
            }

            catch(Exception ex)
            {
                System.out.println("Exception:" + ex.getMessage());
            }
            
            
        }
        return true;
    }

    public void downloadFile()
    {
        try
        {
            File directory = new File(dirName);

            if (!directory.exists())
                directory.mkdir();
            
            boolean complete = false;   
            byte[] data = new byte[size];

            String fileToDownload = fileNameTF.getText();
            String file = new String("*" + fileToDownload + "*");
            pw.println(file);

            ObjectInputStream oin = new ObjectInputStream(inFromServer);
            String s = (String) oin.readObject();

            if (s.equals("Success"))
            {
                File f = new File(directory, fileToDownload);
                FileOutputStream fileOut = new FileOutputStream(f);
                DataOutputStream dataOut = new DataOutputStream(fileOut);

                int c;

                while((c = inFromServer.read()) != -1)
                {
                    System.out.print("CT: " + c + " --> " + (char)c);
                    c = (c - 3) % 256;
                    if (c < 0)
                        c = c + 256;
                    System.out.print("PT: " + c + " --> " + (char)c);
                    dataOut.write(c);
                    dataOut.flush();
                }
                System.out.println("Download completed");
            }
            else
            {
                System.out.println("File not found on server!");
            }
        }
        catch(Exception ex)
        {
            System.out.println("Exception: " + ex.getMessage());
        }
    }

    public void getFileName(MouseEvent click)
    {
        // code will be executed when mouse is clicked
        if (click.getClickCount() >= 2)
            fileNameTF.setText("" + fileList.getSelectedValue());
        
    }

    public void sendBytes(BufferedInputStream in, OutputStream out) throws Exception
    {
        int c;
        while ((c = in.read()) != -1)
        {
            System.out.println(c);
            out.write((c + 3) % 256);
            out.flush();
        }
    }
}

class TCPClient
{
    public static void main(String[] args)
    {
        if (args.length == 1)
            new chat_swing_client(args[0], "127.0.0.1", 8888);
        else
            System.out.println("Please enter the client directory address!");
    }
}