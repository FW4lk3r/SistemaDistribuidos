import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class Mapper extends UnicastRemoteObject implements MapperInterface {

    private static LinkedHashMap<String, ArrayList<ResourceInfo>> timeHarMap = new LinkedHashMap<>();
    private Integer port;

    protected Mapper(Integer port) throws RemoteException
    {
        this.port = port;
    }

    /**
     * @description Add to the hashmap the respective Id
     * and Object divided by the total of mappers
     * @param length
     */
    public void FillDataTableOfFiles(int length) {

        //TODO: Call the Reducer
    }


    public void ReceiveStorageFromStorage(LinkedHashMap<String, ArrayList<ResourceInfo>> storageData) {
        timeHarMap = storageData;
        System.out.println("Mapper has receive the data.");
    }
}
