import java.net.*;
import java.rmi.*;

public class Server
{
    public static void main(String[] args)
    {
        try
        {
            ServerImpl asi = new ServerImpl();
            Naming.rebind("RMIServer", asi);
            System.out.println("\nServer started...");
        }

        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}