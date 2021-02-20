import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Mapper extends UnicastRemoteObject implements MapperInterface {

    protected Mapper() throws RemoteException
    {

    }

}
