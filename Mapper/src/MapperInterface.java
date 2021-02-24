import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public interface MapperInterface extends Remote {

    void ReceiveStorageFromMaster(ArrayList<ResourceInfo> storageData) throws RemoteException;

}
