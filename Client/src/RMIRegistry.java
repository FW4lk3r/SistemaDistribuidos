import com.sun.org.apache.xpath.internal.operations.String;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIRegistry {


    public RMIRegistry() {}

    public static void main(String[] args) {
        Registry r = null;

        try {
            r = LocateRegistry.createRegistry(2023);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        try{
            ObjectRegistry registry = new ObjectRegistry();

            r.rebind("Registry", registry);

            System.out.println("Registry ready");
        }catch(Exception e) {
            System.out.println("Registry main " + e.getMessage());
        }
    }
}
