import java.rmi.*;
import java.rmi.server.*;

class ServerImpl extends UnicastRemoteObject implements ServerIntf
{
    ServerImpl() throws RemoteException
    {

    }

    public double add(double a, double b) throws RemoteException
    {
        return (a + b);
    }
}