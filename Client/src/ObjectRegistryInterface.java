import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ObjectRegistryInterface extends Remote {

    String getObject(String s) throws RemoteException;

    void addObject(String objectId, String ServerAddress) throws RemoteException;

    int getNumServers(String serverType) throws RemoteException;

}
