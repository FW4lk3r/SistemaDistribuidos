import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Registry extends UnicastRemoteObject implements RMIRegistryInterface {

    protected Registry() throws RemoteException {

    }
}