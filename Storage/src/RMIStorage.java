import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIStorage {

    public static void main(String[] args) {
        Registry r = null;

        try{
            r = LocateRegistry.createRegistry(2026);
        }catch(RemoteException a){
            a.printStackTrace();
        }

        try{
            Storage storage = new Storage();
            r.rebind("storage", storage);

            System.out.println("Storage ready");
        }catch(Exception e) {
            System.out.println("Storage main " + e.getMessage());
        }
    }
}
