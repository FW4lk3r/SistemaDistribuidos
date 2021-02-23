import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIStorage {

    public static void main(RMIClient.TYPECLASS strorageTypeClass, String registryAddress) {
        Registry r = null;
        Integer port = 2022;

        try{
            r = LocateRegistry.createRegistry(port);
        }catch(RemoteException a){
            a.printStackTrace();
        }

        try{
            Storage storage = new Storage();
            r.rebind(strorageTypeClass.toString(), storage);

            ObjectRegistryInterface objRegInt = (ObjectRegistryInterface) Naming.lookup(registryAddress);
            objRegInt.addObject(String.valueOf(port), "rmi://localhost:" + port + "/" + strorageTypeClass);

            System.out.println(strorageTypeClass.toString().toUpperCase() + " ready");
        }catch(Exception e) {
            System.out.println(strorageTypeClass.toString().toUpperCase() + " main " + e.getMessage());
        }
    }
}
