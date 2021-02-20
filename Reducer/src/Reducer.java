import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Reducer extends UnicastRemoteObject implements ReducerInterface {

    protected Reducer() throws RemoteException {

    }

}
