import java.io.File;
import java.io.FileInputStream;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.LinkedList;

public class Master extends UnicastRemoteObject implements MasterInterface {
    private static ArrayList<String> Mappers = new ArrayList<>();
    private static ArrayList<String> Reducers = new ArrayList<>();

    private static MapperInterface mapperInterface;
    private static ReducerInterface reducerInterface;
    private static StorageInterface storageInterface;

    private static ObjectRegistryInterface objRegInt;

    private File file;

    protected Master() throws RemoteException {

    }

    public void setHashMaps(RMIClient.TYPECLASS mapperTypeClass, RMIClient.TYPECLASS reducerTypeClass, String registryAddress) throws RemoteException, NotBoundException, MalformedURLException {

        ObjectRegistryInterface objRegInt = (ObjectRegistryInterface) Naming.lookup(registryAddress);

        for (int i = 0 ; i < objRegInt.getNumServers(mapperTypeClass.toString()); i++)
        {
            String port = String.valueOf(2025 + i);
            Mappers.add(objRegInt.getObject(port));
        }

        for (int i = 0 ; i < objRegInt.getNumServers(reducerTypeClass.toString()); i++)
        {
            String port = String.valueOf(2030 + i);
            Reducers.add(objRegInt.getObject(port));
        }

    }

    public void ProcessData(String registryAddress) throws RemoteException, NotBoundException, MalformedURLException {
        System.out.println("Starting the process of your data");

        String mapperAddress = null;

        objRegInt = (ObjectRegistryInterface) Naming.lookup(registryAddress);

        String portStorage = objRegInt.getObject("2022");
        storageInterface = (StorageInterface) Naming.lookup(portStorage);

        ArrayList<Integer> data = storageInterface.returnDataStorage();

        System.out.println("Mapper size (" + Mappers.size() + ")");

        for(int i = 0; i < Mappers.size(); i++){
            mapperAddress = Mappers.get(i);
            mapperInterface = (MapperInterface) Naming.lookup(mapperAddress);
            mapperInterface.sendReducer(data, registryAddress);
        }

        System.out.println("Data process with success");
    }

    public void ProcessResults(String registryAddress, String path) throws RemoteException, NotBoundException, MalformedURLException {

        objRegInt = (ObjectRegistryInterface) Naming.lookup(registryAddress);
        StorageInterface storageInterface = (StorageInterface) Naming.lookup(objRegInt.getObject("2022"));

        //LinkedList<ProcessCombinationModel> combinationResults = storageInterface.getCombinationResults();

        CsvWriter csWriter = new CsvWriter();
        //group Filipe Ferreira / Emanuel Pereira
        //csWriter.SaveResourcesCombinationsProbabilities(combinationResults, "groupFFEP");

        file = new File(path);
        sendFilesToClient();

        System.out.println("You can see the results in this directory:" + path);
    }

    private void sendFilesToClient() {
        System.out.println("Send the files to the Client");
        try {
            File file_input = (File) this.file;
            FileInputStream input = new FileInputStream(file_input);
            byte[] data = new byte[1048576];

            for(int length = input.read(data); length > 0; length = input.read(data)){
                RMIClient.getComputaionalResultsFromMaster(file_input.getName(), data, length);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
