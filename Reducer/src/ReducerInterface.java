import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Set;

public interface ReducerInterface  extends Remote {

    void processCombinationReducers(Integer fileCount) throws Exception;
    void calculateStatistics(ProcessCombinationModel combinationInfo, int fileCount) throws Exception;
    void receiveFromStorage(Set<Set<String>> data) throws RemoteException, NotBoundException, MalformedURLException;
}
