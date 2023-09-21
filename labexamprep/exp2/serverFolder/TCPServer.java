import java.io.*;
import java.net.*;
import java.util.*;

public class TCPServer
{
    public static void main(String[] args) throws Exception
    {
        if (args.length == 0)
            System.out.println("Enter the server directory address");
        else
        {
            int id = 1;
            System.out.println("Server started...");
            System.out.println("Waiting for connection...");
            ServerSocket welcomeSocket = new ServerSocket(8888);

            while(true)
            {
                Socket connectionSocket = welcomeSocket.accept();
                System.out.println("Client with ID " + id + " connected from " + connectionSocket.getInetAddress().getHostName() + "...");
                Thread server = new ThreadedServer(connectionSocket, id, args[0]);
                id++;
                server.start();
            }
        }
    }
}

class ThreadedServer extends Thread
{
    int m, n, counter;
    String name, f, ch, fileData, fileName, dirName, filePath;
    Socket connectionSocket;

    public ThreadedServer(Socket connectionSocket, int counter, String dirName)
    {
        this.connectionSocket = connectionSocket;
        this.counter = counter;
        this.dirName = dirName;
    }

    public void run()
    {
        try
        {
            System.out.println("hola1!!!");
            BufferedReader in = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            InputStream inFromClient = connectionSocket.getInputStream();
            OutputStream output = connectionSocket.getOutputStream();
            PrintWriter outPw = new PrintWriter(connectionSocket.getOutputStream());
            System.out.println("hola2!!!");
            ObjectOutputStream oout = new ObjectOutputStream(output);
            oout.writeObject("Server says hi!!!");

            File ff = new File(dirName);
            ArrayList<String> names = new ArrayList<String>(Arrays.asList(ff.list()));
            int len = names.size();
            oout.writeObject(String.valueOf(names.size()));
            System.out.println("hola3!!!");
            for (String name: names)
                oout.writeObject(name);
            
            name = in.readLine();
            ch = name.substring(0, 1);
            System.out.println("hola4!!!");
            if (ch.equals("*")) // the user wants to download a file from server
            {
                System.out.println("hola5!!!");
                n = names.lastIndexOf("*");
                fileName = name.substring(1, n);
                FileInputStream file = null;
                BufferedInputStream bis = null;
                boolean fileExists = true;
                System.out.println("Request to download file: " + fileName + " received from client!");
                filePath = dirName + "\\" + fileName;
                try
                {
                    file = new FileInputStream(filePath);
                    bis = new BufferedInputStream(file);
                }
                catch(FileNotFoundException fnfe)
                {
                    fileExists = false;
                    System.out.println("file was not found: " + fnfe.getMessage());
                }
                if (fileExists)
                {
                    oout = new ObjectOutputStream(output);
                    oout.writeObject("Success");
                    // oout.writeObject("Success");
                    System.out.println("Upload begins to client");
                    sendBytes(bis, output);
                    System.out.println("Completed");
                }
                else
                {
                    oout = new ObjectOutputStream(output);
                    oout.writeObject("FileNotFound!");
                }
            }
            else // the user want to upload a file to server
            {
                try
                {
                    System.out.println("hola6!!!");
                    boolean complete = false;
                    System.out.println("Request to upload file " + name + " received! ");
                    File directory = new File(dirName);
                    if (!directory.exists())
                    {
                        directory.mkdir();
                        System.out.println("Directory was made!");
                    }

                    int size = 9022386;
                    byte[] data = new byte[size];
                    File fc = new File(directory, name);
                    FileOutputStream fileOut = new FileOutputStream(fc);
                    DataOutputStream dataOut = new DataOutputStream(fileOut);

                    while(!complete)
                    {
                        m = inFromClient.read(data, 0, data.length);
                        if (m == -1)
                        {
                            complete = true;
                            System.out.println("Completed!");
                        }
                        else
                        {
                            dataOut.write(data, 0, m);
                            dataOut.flush();
                        }
                    }
                }
                catch(Exception exc)
                {
                    System.out.println(exc.getMessage());
                }
            }
        }
        catch(Exception ex)
        {
            System.out.println(ex.getMessage());
        }
    }

    private static void sendBytes(BufferedInputStream in, OutputStream out) throws Exception
    {
        int c;
        System.out.println("Cipher text");
        while((c = in.read()) != -1)
        {
            System.out.println("PT: " + c + " - " + (char)c);
            out.write(c);
            out.flush();
            System.out.print("CT: " + c + " - " + (char)c);
        }
    }
}