import java.rmi.Naming;
import java.rmi.RemoteException;

public class RMIClient {

    private static String registryAddress = "rmi://localhost:2023/registry";

    enum TYPECLASS {
        storage,
        registry,
        reducer,
        master,
        mapper
    }


    public static void main(String[] args) throws InterruptedException {

        LaunchThread();
        String path = ".\\Client\\files\\";

        try {
            ObjectRegistryInterface objRegInt = (ObjectRegistryInterface) Naming.lookup(registryAddress);

            String storageAddress = objRegInt.getObject("2022");
            String masterAddress = objRegInt.getObject("2024");

            //Get the interface of storage/master
            StorageInterface interfaceStorage = (StorageInterface) Naming.lookup(storageAddress);
            MasterInterface interfaceMaster = (MasterInterface) Naming.lookup(masterAddress);

            interfaceStorage.addFile(path, storageAddress);

            //Start the process of the Data
            Master.ProcessData(registryAddress);

            //Return the results to the client
            Master.ProcessResults(registryAddress, path);

        }

        catch(RemoteException e) {
            System.out.println(e.getMessage());
        }

        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void LaunchThread() throws InterruptedException {
        int numMappers = 2;
        int numReducers = 4;

        Thread thread = new Thread() {
            public void run(){
                RMIRegistry.main(TYPECLASS.registry);
                RMIStorage.main(TYPECLASS.storage, registryAddress);

                for(int i = 0; i < numMappers; i++) {
                    RMIMapper.main(TYPECLASS.mapper, registryAddress, 2025 + i);
                }
                for(int i = 0; i < numReducers; i++) {
                    RMIReducer.main(TYPECLASS.reducer, registryAddress, 2030 + i);
                }
                RMIMaster.main(TYPECLASS.master, registryAddress);
            }
        };
        thread.start();
        Thread.sleep(1000L);
    }
}
