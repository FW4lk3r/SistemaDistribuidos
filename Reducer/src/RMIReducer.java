import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIReducer {

    public static void main(RMIClient.TYPECLASS reducerTypeClass, String registryAddress, Integer dynamicPort) {
        Registry r = null;

        try{
            r = LocateRegistry.createRegistry(dynamicPort);
        }catch(RemoteException a){
            a.printStackTrace();
        }

        try{
            Reducer reducer = new Reducer();
            r.rebind(reducerTypeClass.toString(), reducer);

            ObjectRegistryInterface objRegInt = (ObjectRegistryInterface) Naming.lookup(registryAddress);
            objRegInt.addObject(String.valueOf(dynamicPort), "rmi://localhost:" + dynamicPort + "/" + reducerTypeClass);

            System.out.println(reducerTypeClass.toString().toUpperCase() + " ready");
        }catch(Exception e) {
            System.out.println(reducerTypeClass.toString().toUpperCase() + " main " + e.getMessage());
        }
    }
}
