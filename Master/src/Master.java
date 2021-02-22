import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Master extends UnicastRemoteObject implements MasterInterface {

    protected Master() throws RemoteException {

    }

    public static void ProcessData() {
    }

    public static void ProcessResults() {
    }
}
