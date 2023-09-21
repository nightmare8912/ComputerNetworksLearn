import java.net.*;
import java.rmi.*;

class Client
{
    public static void main(String[] args)
    {
        try
        {
            String name = "rmi://" + args[0] + "/RMServer";
            ServerIntf asif = (ServerIntf)Naming.lookup(name);

            double d1 = 100, d2 = 200;
            System.out.println("add: " + asif.add(d1, d2));

        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
}