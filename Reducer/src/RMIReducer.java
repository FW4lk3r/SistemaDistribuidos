import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIReducer {

    public static void main(Integer dynamicPort) {
        Registry r = null;

        try{
            r = LocateRegistry.createRegistry(dynamicPort);
        }catch(RemoteException a){
            a.printStackTrace();
        }

        try{
            Reducer reducer = new Reducer();
            r.rebind("reducer", reducer);

            System.out.println("Reducer ready");
        }catch(Exception e) {
            System.out.println("Reducer main " + e.getMessage());
        }
    }
}
