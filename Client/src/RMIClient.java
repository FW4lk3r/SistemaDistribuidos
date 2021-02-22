import com.sun.org.apache.xpath.internal.operations.String;

import java.io.File;
import java.io.FileInputStream;
import java.rmi.Naming;
import java.rmi.RemoteException;

public class RMIClient {

    public static void main(String[] args) throws InterruptedException {

        LaunchThread();
        java.lang.String path = ".\\Client\\files";

        try {
            ObjectRegistryInterface objRegInt = (ObjectRegistryInterface) Naming.lookup("rmi://localhost:2023/registry");

            String storageAddress = objRegInt.getObject("2022");
            String masterAddress = objRegInt.getObject("2024");

            //Get the interface of storage/master
            StorageInterface interfaceStorage = (StorageInterface) Naming.lookup(storageAddress.toString());
            MasterInterface interfaceMaster = (MasterInterface) Naming.lookup(masterAddress.toString());

            interfaceStorage.addFile(path);

        }

        catch(RemoteException e) {
            System.out.println(e.getMessage());
        }

        catch(Exception e) {
            e.printStackTrace();
        }



        Master.ProcessData();
        Master.ProcessResults();
    }

    public static void LaunchThread() throws InterruptedException {
        int numMappers = 2;
        int numReducers = 4;

        Thread thread = new Thread() {
            public void run(){
                RMIRegistry.main(new String[0]);
                RMIStorage.main(new String[0]);

                for(int i = 0; i < numMappers; i++) {
                    RMIMapper.main(new String[0]);
                }
                for(int i = 0; i < numReducers; i++) {
                    RMIReducer.main(new String[0]);
                }
                RMIMaster.main(new String[0]);
            }
        };
        thread.start();
        Thread.sleep(1000L);
    }
}
