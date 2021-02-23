import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIMaster {

    public static void main(RMIClient.TYPECLASS masterTypeClass, String registryAddress) {
        Registry r = null;
        Integer port = 2024;

        try{
            r = LocateRegistry.createRegistry(port);
        }catch(RemoteException a){
            a.printStackTrace();
        }

        try{
            Master master = new Master();
            r.rebind(masterTypeClass.toString(), master);

            ObjectRegistryInterface objRegInt = (ObjectRegistryInterface) Naming.lookup(registryAddress);
            objRegInt.addObject(String.valueOf(port), "rmi://localhost:" + port + "/" + masterTypeClass);

            System.out.println(masterTypeClass.toString().toUpperCase() + " ready");
        }catch(Exception e) {
            System.out.println(masterTypeClass.toString().toUpperCase() + " main " + e.getMessage());
        }
    }

}
