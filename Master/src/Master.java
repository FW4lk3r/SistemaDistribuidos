import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Master extends UnicastRemoteObject implements MasterInterface {

    protected Master() throws RemoteException {

    }

}
