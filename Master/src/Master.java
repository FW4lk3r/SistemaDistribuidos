import java.io.File;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;
import java.util.TreeMap;

public class Master extends UnicastRemoteObject implements MasterInterface {
    static Map<Integer, String> hashMapMappers = new TreeMap<>();
    private static MapperInterface mapperInterface;
    private static ObjectRegistryInterface objRegInt;
    private static File file;

    protected Master() throws RemoteException {
        //TODO: set here the hashmaps
        // hashMapMappers = hashmap;
    }

    public static void ProcessData(String registryAddress) throws RemoteException, NotBoundException, MalformedURLException {
        System.out.println("Starting the process of your data");

        String mapperAddress = null;

        objRegInt = (ObjectRegistryInterface) Naming.lookup(registryAddress);

        int length = objRegInt.getLength();

        System.out.println("Starting the process of your data");
        for(int i = 0; i < hashMapMappers.size(); i++){
            mapperAddress = objRegInt.getObject(hashMapMappers.get(i));
            mapperInterface = (MapperInterface) Naming.lookup(mapperAddress);
            mapperInterface.FillDataTableOfFiles(length);
        }

        System.out.println("Data process with success");
    }

    public static void ProcessResults(String registryAddress, String path) throws RemoteException, NotBoundException, MalformedURLException {

        objRegInt = (ObjectRegistryInterface) Naming.lookup(registryAddress);
        StorageInterface storageInt = (StorageInterface) Naming.lookup(objRegInt.getObject("2022"));

        //Todo: End this
        //LinkedList<ProcessCombinationModel> combinationResults = storageInt.getCombinationResults();

        file = new File(path);
        sendFilesToClient();

        System.out.println("You can see the results in this directory:" + path);
    }

    private static void sendFilesToClient() {
        System.out.println("Send the files to the Client");
    }
}
