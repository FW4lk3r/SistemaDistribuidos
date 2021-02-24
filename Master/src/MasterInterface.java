import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MasterInterface extends Remote {

    void setHashMaps(RMIClient.TYPECLASS mapperTypeClass, RMIClient.TYPECLASS reducerTypeClass, String registryAddress) throws RemoteException, NotBoundException, MalformedURLException;
    void ProcessData(String registryAddress) throws RemoteException, NotBoundException, MalformedURLException;
    void ProcessResults(String registryAddress, String path) throws RemoteException, NotBoundException, MalformedURLException;
}
