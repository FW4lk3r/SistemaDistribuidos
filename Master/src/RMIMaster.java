import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIMaster {

    public static void main(String[] args) {
        Registry r = null;

        try{
            r = LocateRegistry.createRegistry(2024);
        }catch(RemoteException a){
            a.printStackTrace();
        }

        try{
            Master master = new Master();
            r.rebind("mapper", master);

            System.out.println("Master ready");
        }catch(Exception e) {
            System.out.println("Master main " + e.getMessage());
        }
    }
}
