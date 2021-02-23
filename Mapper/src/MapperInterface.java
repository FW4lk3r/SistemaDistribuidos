import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MapperInterface extends Remote {

    void FillDataTableOfFiles(int length) throws RemoteException;

    void DivideObjectsFilesFromTotalMappers() throws RemoteException;

}
