import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Set;

public interface MapperInterface extends Remote {

    void ReceiveStorageFromMaster(ArrayList<ResourceInfo> storageData) throws RemoteException;
    Set<Set<ResourceInfo>> combinations(int len, int fileCount) throws  RemoteException;

    void sendReducer(ArrayList<Integer> data, String registryAddress) throws  RemoteException, NotBoundException, MalformedURLException;
}
