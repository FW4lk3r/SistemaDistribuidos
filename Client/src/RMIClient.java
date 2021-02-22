import com.sun.org.apache.xpath.internal.operations.String;

import java.io.File;
import java.io.FileInputStream;
import java.rmi.Naming;
import java.rmi.RemoteException;

public class RMIClient {

    public static void main(String[] args) {

        File filepath = new File(".\\Client\\files");
        java.lang.String[] contents = filepath.list();

        StorageInterface storage;

        try {
            storage = (StorageInterface) Naming.lookup("rmi://localhost:2026/storage");

            for (int i = 0; i < contents.length; i++) {
                File file = new File(".\\Client\\files\\" + contents[i]);

                FileInputStream in = new FileInputStream(file);

                byte[] array = new byte[1024 * 1024];
                int size = in.read(array);
                storage.ReceivedFiles(contents[i], array, size);
                System.out.println(contents[i]);
            }
        }

        catch(RemoteException e) {
            System.out.println(e.getMessage());
        }

        catch(Exception e) {e.printStackTrace();}
    }

    public static void LaunchThread() throws InterruptedException {
        int numMappers = 2;
        int numReducers = 4;

        thread = (Thread) run() => {
            RMIRegistry.main(new String[0]);
            RMIStorage.main(new String[0]);

            for(int i = 0; i < numMappers; i++) {
                RMIMapper.main(new String[0]);
            }
            for(int i = 0; i < numReducers; i++) {
                RMIReducer.main(new String[0]);
            }
            RMIMaster.main(new String[0]);
        };
        thread.start();
        Thread.sleep(1000L);
    }
}