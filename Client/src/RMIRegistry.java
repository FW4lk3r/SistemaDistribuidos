import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIRegistry {


    public RMIRegistry() {}

    public static void main(RMIClient.TYPECLASS registryTypeClass) {
        Registry r = null;
        Integer port = 2023;

        try {
            r = LocateRegistry.createRegistry(port);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        try{
            ObjectRegistry registry = new ObjectRegistry();

            r.rebind(registryTypeClass.toString(), registry);

            System.out.println(registryTypeClass.toString().toUpperCase() + " ready");
        }catch(Exception e) {
            System.out.println(registryTypeClass.toString().toUpperCase() + " main " + e.getMessage());
        }
    }
}
