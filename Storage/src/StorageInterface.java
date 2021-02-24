import harreader.HarReaderException;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.LinkedList;

public interface StorageInterface  extends Remote {

    void ReceivedFilesClient(String filename, byte[] array, int size, int totalItems) throws IOException, HarReaderException;

    void addFile(String path, String storageAddress) throws IOException, NotBoundException;

    ArrayList<Integer> returnDataStorage() throws RemoteException;

    ArrayList<Integer> DivideResources() throws RemoteException;

    void ReceivedCombinations(ArrayList<ProcessCombinationModel> combinationStatistics) throws RemoteException;

    //LinkedList<ProcessCombinationModel> getCombinationResults();
}