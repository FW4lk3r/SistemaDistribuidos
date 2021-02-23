import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public interface MapperInterface extends Remote {

    void FillDataTableOfFiles(int length, LinkedHashMap<String, ArrayList<ResourceInfo>> storageData) throws RemoteException;

    void DivideObjectsFilesFromTotalMappers(Integer length) throws RemoteException;
    void ReceiveStorageFromStorage(LinkedHashMap<String, ArrayList<ResourceInfo>> storageData) throws RemoteException;

}
