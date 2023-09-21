import java.rmi.*;

public interface ServerIntf extends Remote
{
    int i = 0;
    double add(double a, double b) throws RemoteException;
}